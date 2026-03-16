package com.example.demo.Services;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Entities.JWTToken;
import com.example.demo.Entities.User;
import com.example.demo.Repositories.JWTTokenRepository;
import com.example.demo.Repositories.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.nio.charset.StandardCharsets;

@Service
public class AuthService {

	private final Key SIGNING_KEY;
private final UserRepository userrepo;
private final JWTTokenRepository jwttokenrepo;
private final BCryptPasswordEncoder passwordencoder;

@Autowired
public AuthService(UserRepository userrepo, JWTTokenRepository jwttokenrepo,@Value("${jwt.secret}") String jwtSecret) {
	this.userrepo = userrepo;
	this.jwttokenrepo = jwttokenrepo;
	this.passwordencoder = new BCryptPasswordEncoder();
	
	
	if(jwtSecret.getBytes(StandardCharsets.UTF_8).length < 64) {
		throw new IllegalArgumentException("JWT_SECRET in application.properties must be at least 64 bytes long for HS512");
	} 
	this.SIGNING_KEY = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
} 
public User authenticate(String username, String password) {
	User user = userrepo.findByUsername(username).orElseThrow(() -> new RuntimeException("Invalid username"));
	if(!passwordencoder.matches(password, user.getPassword())) {
		throw new RuntimeException("Invalid Password");
	}
	return user;
}
public String generateToken(User user) {
	String token;
	LocalDateTime now = LocalDateTime.now();
	JWTToken existingToken = jwttokenrepo.findByUserId(user.getUserId());
	
	if(existingToken != null && now.isBefore(existingToken.getExpiresAt())) {
		token = existingToken.getToken();
	} else {
		token = generateNewToken(user);
		if(existingToken != null) {
			jwttokenrepo.delete(existingToken);
		} saveToken(user, token);
	}
	return token;
}
private String generateNewToken(User user) {
	return Jwts.builder()
			.setSubject(user.getUsername())
			.claim("role", user.getRole().name())
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + 3600000))
			.signWith(SIGNING_KEY,SignatureAlgorithm.HS512)
			.compact();
}
public void saveToken(User user, String token) {
	JWTToken jwttoken = new JWTToken(user, token, LocalDateTime.now().plusHours(1));
	jwttokenrepo.save(jwttoken);
}

public void logout(User user) {
	jwttokenrepo.deleteByUserId(user.getUserId());
}
public boolean validateToken(String token) {
    try {
    	System.err.println("Token Validation...");
        Jwts.parserBuilder()
            .setSigningKey(SIGNING_KEY)
            .build()
            .parseClaimsJws(token);
        
        Optional<JWTToken> jwtToken = jwttokenrepo.findByToken(token);
        if(jwtToken.isPresent()) {
        	System.err.println("Token Expiry: " + jwtToken.get().getExpiresAt());
        	System.err.println("Current Time: " + LocalDateTime.now());
        	return jwtToken.get().getExpiresAt().isAfter(LocalDateTime.now());
        }
        return false;
    } catch (Exception e) {
    	System.err.println("Token Validation Failed: " + e.getMessage());
        return false; 

    }
}

public String extractUsername(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(SIGNING_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
}

}

