package com.example.karyaflow.controller;

import com.example.karyaflow.entity.User;
import com.example.karyaflow.repo.UserRepo;
import com.example.karyaflow.requestbody.AuthentaticationDetails.*;
import com.example.karyaflow.security.utility.JwtUtility;
import com.example.karyaflow.service.EmailService;
import com.example.karyaflow.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager manager;
    private final UserService userService;
    private final JwtUtility jwtUtility;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailService emailService;

    public AuthenticationController(AuthenticationManager manager, UserService userService, JwtUtility jwtUtility, RedisTemplate<String, String> redisTemplate, EmailService emailService) {
        this.manager = manager;
        this.userService = userService;
        this.jwtUtility = jwtUtility;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;

    }

    @PostMapping("/addNewUser")
    public ResponseEntity<ApiResponse<RegistrationResponse>> createNewUser(@RequestBody RegistrationDetails registrationDetails) throws URISyntaxException, IOException {
        User user=userService.createUser(registrationDetails);
        String token=jwtUtility.createToken(user.getEmail());
        URI location = URI.create("/auth/user/" + registrationDetails.getEmail());
        emailService.sendMailWithAttachment(
                registrationDetails.getEmail(),
                "🎉 Welcome to KaryaFlow – Let’s get things done!",
                """
                <html>
                  <body style="margin:0; padding:0; font-family: Arial, sans-serif; background-color:#f7f7f7;">
                    <table width="100%" cellpadding="0" cellspacing="0" border="0" style="background-color:#f7f7f7;">
                      <tr>
                        <td align="center">
                          <table width="600" cellpadding="20" cellspacing="0" border="0" style="background-color:#ffffff; margin:20px; border-radius:8px; box-shadow:0 2px 5px rgba(0,0,0,0.1);">
                            
                            <!-- Banner Image -->
                            <tr>
                              <td align="center" style="padding:0;">
                                <img src="https://images.businessnewsdaily.com/app/uploads/2022/04/04074130/teamwork_g-stockstudio_getty.jpg" 
                                     alt="Welcome to KaryaFlow" 
                                     style="display:block; border-radius:8px 8px 0 0; max-width:600px; width:100%;">
                              </td>
                            </tr>
            
                            <!-- Title -->
                            <tr>
                              <td>
                                <h2 style="color:#4CAF50; margin-bottom:10px;">Welcome to <b>KaryaFlow</b> 🚀</h2>
                                <p style="color:#555; font-size:15px; line-height:1.6;">
                                  Hi <b>User</b>,<br><br>
                                  We’re excited to have you on board! KaryaFlow is designed to help you 
                                  manage projects, track tasks, and collaborate effortlessly — all in one place.
                                </p>
                              </td>
                            </tr>
            
                            <!-- Steps -->
                            <tr>
                              <td>
                                <h3 style="color:#333;">Here’s what you can do next:</h3>
                                <ul style="padding-left:20px; color:#555; font-size:15px; line-height:1.8;">
                                  <li>✅ Create your first project and start adding tasks</li>
                                  <li>✅ Invite your teammates and collaborate in real-time</li>
                                  <li>✅ Track progress and stay on top of deadlines</li>
                                </ul>
                              </td>
                            </tr>
            
                            <!-- Call to Action -->
                            <tr>
                              <td align="center">
                                <a href="https://karyaflow.com/login"
                                   style="background-color:#4CAF50; color:#ffffff; padding:12px 24px; 
                                          text-decoration:none; border-radius:5px; font-weight:bold; display:inline-block;">
                                  Get Started →
                                </a>
                              </td>
                            </tr>
            
                            <!-- Footer -->
                            <tr>
                              <td style="font-size:12px; color:#999; text-align:center; padding-top:20px;">
                                Cheers,<br>The KaryaFlow Team
                              </td>
                            </tr>
                          </table>
                        </td>
                      </tr>
                    </table>
                  </body>
                </html>
                """
        );
        return ResponseEntity.created(location)
                .body(new ApiResponse<>(true,"Registration Successful",
                        new RegistrationResponse(token,registrationDetails.getUsername(),user.getImage())));
    }
    @PostMapping("/validateUser")
    public ResponseEntity<ApiResponse<LoginResponse>> validateUser(@RequestBody LoginDetails loginDetails) {
        Authentication authentication = manager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDetails.getEmail(), loginDetails.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findByEmail(loginDetails.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found in the database" + loginDetails.getEmail()));
        String token = jwtUtility.createToken(user.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(true, "Login Successful",
                    new LoginResponse(token, user.getUsername(),user.getImage())));
    }
    @PostMapping("/logOut")
    public ResponseEntity<ApiResponse<String>> logOutUser(HttpServletRequest request)
    {
        String authHeader= request.getHeader("Authorization");
        if (authHeader==null || !authHeader.startsWith("Bearer "))
        {
            return ResponseEntity.badRequest().body(new ApiResponse<>(false,"No Jwt Token found ",null));
        }
        String token=authHeader.substring(7);
        //extracts the expiration and finds remaining time if not 0 then it hashes the token
        //and set it as a key such that whenever trying to log with invalid token it fails
        long expiration=jwtUtility.extractExpiration(token).getTime();
        long now= System.currentTimeMillis();
        long ttl=expiration-now;
        if(ttl>0)
        {
            String tokenHash=jwtUtility.hashToken(token);
            redisTemplate.opsForValue().set(tokenHash,"Blacklisted",ttl, TimeUnit.MILLISECONDS);
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponse<>(true, "Logout successful", null));
    }
    @GetMapping("/listUsers")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers(){
        return userService.getUsers();
    }
    @GetMapping("/listUsernames")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getUsernames(){
        return userService.getUsernames();
    }
}
