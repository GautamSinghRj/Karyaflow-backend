package com.example.karyaflow.security.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HexFormat;
import java.util.function.Function;


@Component
public class JwtUtility {
    private static final Logger logger= (Logger) LoggerFactory.getLogger(JwtUtility.class);
    @org.springframework.beans.factory.annotation.Value("${jwt.secret}")
    private String jwtSecret;
    @org.springframework.beans.factory.annotation.Value("${jwt.expiration}")
    private long jwtExpirationMs;

    public  String tokenGenerator(UserDetails userDetails){//UserDetails object represents authenticated user
        // UserDetails.getUsername() returns email
        return createToken(userDetails.getUsername());
    }

    public String createToken(String email) {
        return Jwts
                .builder()
                .setSubject(email)
                .setIssuedAt(new Date(new Date().getTime()))
                .setExpiration(new Date(new Date().getTime()+jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }
    public boolean validateToken(String authToken,UserDetails userDetails){
        return (extractEmail(authToken).equals(userDetails.getUsername()) && !isTokenExpired(authToken));
        //if the username from the token and the username from userDetails is same and the token is not expired
        // then the token is valid
    }

    public String extractEmail(String authToken) {
        String subject = extractClaims(authToken, Claims::getSubject);
        if (subject == null) {
            logger.error("Failed to extract subject from JWT");
        }
            return subject;//extract username from the claims
    }

    private <T> T extractClaims(String authToken, Function<Claims,T> claimsResolver) {
       final Claims claims=extractAllClaims(authToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String authToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(authToken)
                .getBody();
        //extract the current claim and return it after parsing and signing
    }

    private boolean isTokenExpired(String authToken) {
        return extractExpiration(authToken).before(new Date(new Date().getTime()));
        //extract username from the claims and check whether
        // it is before the time or not
    }

    public Date extractExpiration(String authToken) {
        Date time = extractClaims(authToken, Claims::getExpiration);
        if (time == null) {
            logger.error("Failed to extract expiration time from JWT");
        }
        return time;//extract expiration from the claims
    }

    //Hash the token in redis in memory database for blacklisting
    //SHA-256 creates a fixed-length unique fingerprint of the token, keeping blacklist storage compact and secure.
    public String hashToken(String token){
        try
        {
            MessageDigest digest=MessageDigest.getInstance("SHA-256");//Creates a cryptographic hash function object that uses the SHA-256 algorithm
            byte[] hash=digest.digest(token.getBytes(StandardCharsets.UTF_8));//converts the token into bytes and applies the algo
            return HexFormat.of().formatHex(hash);//Converts the raw byte array into a human-readable hexadecimal string
        }
        catch (NoSuchAlgorithmException e){
            throw new RuntimeException("Error hashing token",e);
        }
    }
}
