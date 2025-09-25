package com.example.karyaflow.security.filter;

import com.example.karyaflow.security.utility.JwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //jwtauthenticationfilter just checks each request before giving access to any endpoint of the api
    private final JwtUtility jwtUtility;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtAuthenticationFilter(JwtUtility jwtUtility, UserDetailsService userDetailsService, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String clientToken=request.getHeader("Authorization");
        String email=null;
        String jwtToken=null;//token from the user from frontend
        if(clientToken!=null && clientToken.startsWith("Bearer ")){
            jwtToken=clientToken.substring(7);
            //Immediately denies requests with blacklisted tokens, preventing access
            String tokenHash =  jwtUtility.hashToken(jwtToken);
            Boolean blacklisted =redisTemplate.hasKey(tokenHash);
            if (blacklisted)
            {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Token is invalid.Please log again");
                return;
            }
            try {
                email=jwtUtility.extractEmail(jwtToken);
            }
            catch (Exception e)
            {
                logger.error("Unable to get username from the token",e);
            }
        }
        /*SecurityContextHolder holds the current security context that contains the current Authentication object
         which in turn contains username,roles,credentials*/
        if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails=this.userDetailsService.loadUserByUsername(email);
            if(jwtUtility.validateToken(jwtToken,userDetails)){
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=new
                        UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }
}
