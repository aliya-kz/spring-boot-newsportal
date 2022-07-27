package org.zhumagulova.springbootnewsportal.security.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.zhumagulova.springbootnewsportal.model.Role;
import org.zhumagulova.springbootnewsportal.service.UserDetailsServiceImpl;

import javax.annotation.PostConstruct;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.validityInMilliSeconds}")
    private long validityInMilliSeconds;

    @Value("${jwt.header}")
    private String authorizationHeader;

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final PasswordEncoder passwordEncoder;

    private Key signingKey;

    @Autowired
    public JwtTokenProvider(UserDetailsServiceImpl appUserDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsServiceImpl = appUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    protected void init() {
        signingKey = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    public String createToken(String username, Set<Role> roles) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", getRoleNames(roles));

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliSeconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(signingKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer_")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    private Set<String> getRoleNames(Set<Role> userRoles) {
        Set<String> result = new HashSet<>();

        userRoles.forEach(role -> {
            result.add(role.getName());
        });

        return result;
    }
}