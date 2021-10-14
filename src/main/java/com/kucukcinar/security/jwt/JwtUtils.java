package com.kucukcinar.security.jwt;

import java.util.Date;

import com.kucukcinar.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.*;

/**
 * The type Jwt utils.
 */
@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private String jwtSecret = "YKSecretKey";

    private int jwtExpirationMs = 86400000;


    /**
     * Generate jwt token string.
     *
     * @param authentication the authentication
     * @return the string
     */
    public String generateJwtToken(Authentication authentication) {

        User userPrincipal = (User) authentication.getPrincipal();
        logger.info(jwtSecret);
        logger.info(String.valueOf(jwtExpirationMs));
        return Jwts.builder().setSubject((userPrincipal.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    /**
     * Gets user name from jwt token.
     *
     * @param token the token
     * @return the user name from jwt token
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Validate jwt token boolean.
     *
     * @param authToken the auth token
     * @return the boolean
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
