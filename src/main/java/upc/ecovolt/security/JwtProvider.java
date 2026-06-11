package upc.ecovolt.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import upc.ecovolt.entity.Option;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    // REGLA SENIOR: La clave debe tener al menos 64 caracteres y NO tener caracteres especiales raros.
    private String secret = "EstaEsUnaClaveSecretaMuyLargaParaEcovolt2024SinGuionesBajosParaQueNoFalle";
    private int expiration = 36000;

    // Convertimos el String a Key de forma segura
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Authentication authentication) {
        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        claims.put("idUser", principal.getIdUser());
        claims.put("fullName", principal.getFullName());
        claims.put("login", principal.getLogin());

        // Solo guardamos nombres de roles y rutas (Strings simples)
        List<String> roles = principal.getAuthorities().stream()
                .map(auth -> auth.getAuthority()).collect(Collectors.toList());
        claims.put("roles", roles);

        List<String> rutas = principal.getOpciones().stream()
                .map(Option::getRoute).collect(Collectors.toList());
        claims.put("options", rutas);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(principal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String getNombreUsuarioFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("Error en el Token: {}", e.getMessage());
        }
        return false;
    }
}