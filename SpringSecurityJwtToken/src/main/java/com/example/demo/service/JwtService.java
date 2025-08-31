package com.example.demo.service;

import java.security.Key;
import java.util.Base64.Decoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	
	public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
	
    public String generateToken(String userName) {
	 Map<String,Object> claims=new HashMap<>();
	return createToken(claims,userName); 
    }

 private String createToken(Map<String, Object> claims, String userName) {
	return Jwts.builder()
			.setClaims(claims)
			.setSubject(userName)
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis()+1000*60*30))
			.signWith((Key) getSignKey(), SignatureAlgorithm.HS256).compact();
          }

    private Object getSignKey() {
	byte[] keyBytes = Decoders.BASE64.decode(SECRET);
	return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET)   // use your secret key here
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
	

}
