package tech.antoniosgarbi.gestorpeixaria.security.jwt;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;
import tech.antoniosgarbi.gestorpeixaria.security.services.UserDetailsImpl;

import java.time.LocalDate;
import java.util.Date;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${personal.security.jwtSecret}")
  private String jwtSecret;

  @Value("${personal.security.jwtExpirationMs}")
  private int jwtExpirationMs;

  @Value("${personal.security.jwtRefreshExpirationMs}")
  private Long refreshTokenDurationMs;

  @Value("${personal.security.nova-senha}")
  private String senhaGeradaNoBoot;

  public String generateAccessToken(UserDetailsImpl userPrincipal) {
    return generateTokenFromUsername(userPrincipal.getUsername());
  }
  public String generateAccessToken(String username) {
    return generateTokenFromUsername(username);
  }

  public String generateTokenFromUsername(String username) {
    return Jwts.builder().setSubject(username).setIssuedAt(new Date())
            .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)).signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact();
  }

  public String generateRefreshTokenFromUsername(String username) {
    Date momentoAtual = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(momentoAtual)
                .setExpiration(new Date(momentoAtual.getTime() + refreshTokenDurationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }

  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  @Bean()
  public void geradorDeSenha() {
    Argon2PasswordEncoder argon = new Argon2PasswordEncoder();
    String senhaEncripitada = argon.encode(this.senhaGeradaNoBoot);
    logger.info(String.format("Nova Senha gerada! %n%nfonte: %s%ngerado: %s%n", senhaGeradaNoBoot, senhaEncripitada));
  }
}
