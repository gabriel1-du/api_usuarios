package com.example.api_usuarios.Login;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // DEBE ser identico al jwt.secret del API Gateway para que pueda validar el token.
    private final String SECRET = "clave_super_segura_de_mas_de_64_bytes_de_longitud_esto_es_necesario_para_HS512";
    private final long EXPIRATION_TIME = 86400000; // 24 horas

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT con los MISMOS claims que usaba el API Gateway:
     * - subject: username (correo electronico)
     * - username: correo electronico
     * - permiso_admin: Boolean
     * - userId: Integer (el gateway lo extrae como Integer.class)
     */
    public String generateToken(String username, Boolean permisoAdmin, Integer userId) {
        return Jwts.builder()
                .setSubject(username)
                .claim("username", username)
                .claim("permiso_admin", permisoAdmin)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
