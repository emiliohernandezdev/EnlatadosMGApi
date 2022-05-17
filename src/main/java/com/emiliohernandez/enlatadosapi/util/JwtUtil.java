/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.emiliohernandez.enlatadosapi.util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
/**
 *
 * @author emilio.hernandez
 */
public class JwtUtil {
    
    public JwtUtil(){
    }
    public String secret = "asdfSFS34wfsdfsdfSDSD32dfsddDDerQSNCK34SOWEK5354fdgdf4";

    public Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret), 
                            SignatureAlgorithm.HS256.getJcaName());
    
    
    public String createToken(String id, String name){
        Instant now = Instant.now();
        String jwtToken = Jwts.builder()
        .claim("id", id)
        .claim("name", name)
        .setSubject(id)
        .setId(UUID.randomUUID().toString())
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plus(3, ChronoUnit.DAYS)))
        .signWith(hmacKey)
        .compact();
        
        return jwtToken;
    }

    public Object getInfoToken(String token){
        String[] parts = token.split("\\.");
        String b64Payload = parts[1];
        String jsonString = new String(Base64.decodeBase64(b64payload), "UTF-8");
        return new Object();
    }
}
