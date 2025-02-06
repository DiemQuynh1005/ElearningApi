package com.edu.project_edu.utils;

import java.util.*;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.edu.project_edu.entities.Account;

@Component
public class JwtUtil {
  public static final String SECRET_KEY = "EPRJ4_EDU";

  public String generateToken(Account user) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("id", user.getId());
    claims.put("name", user.getName());
    claims.put("role", user.getRole());
    return createToken(claims, user.getEmail());
  }

  private String createToken(Map<String, Object> claims, String email) {
    Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY.getBytes());
    return JWT.create()
        .withSubject(email)
        .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000 * 24))
        .withClaim("userInfo", claims)
        .sign(algorithm);
  }
}
