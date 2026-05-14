package upc.ecovolt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    // REGLA DE SEGURIDAD: Para HS512 la clave DEBE tener al menos 64 caracteres.
    // He quitado los "_" para evitar cualquier conflicto de decoding.
    private String secret = "EstaEsUnaClaveSecretaMuyLargaParaEcovolt2024UpcSi705SistemasSeguro";
    private int expiration = 36000;

    // Convertimos el String en una Key válida para JJWT 0.11+
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Authentication authentication) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("idUser", principal.getIdUser());
        claims.put("fullName", principal.getFullName());
        claims.put("login", principal.getLogin());

        List<String> roles = principal.getAuthorities().stream()
                .map(auth -> auth.getAuthority()).collect(Collectors.toList());
        claims.put("roles", roles);
        claims.put("options", principal.getOpciones());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(principal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // Usamos la Key procesada
                .compact();
    }

    public String getNombreUsuarioFromToken(String token) {
        // En JJWT 0.11+ se usa parserBuilder()
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
        } catch (Exception e) {
            logger.error("Token de Ecovolt inválido: " + e.getMessage());
        }
        return false;
    }
}