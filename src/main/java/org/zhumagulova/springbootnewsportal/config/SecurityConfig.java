package org.zhumagulova.springbootnewsportal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.zhumagulova.springbootnewsportal.security.jwt.JwtConfigurer;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    private final JwtConfigurer jwtConfigurer;

    private static final String END_POINT_REST = "/api/**";
    private static final String USER_END_POINT = "/news/**";
    private static final String ADMIN_END_POINT = "/admin/**";
    private static final String ADMIN = "ADMIN";

    @Autowired
    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/registration").permitAll()
                .antMatchers(USER_END_POINT).permitAll()
                .antMatchers(ADMIN_END_POINT).hasAuthority(ADMIN)
                .antMatchers(HttpMethod.GET, END_POINT_REST).permitAll()
                .antMatchers(HttpMethod.DELETE, END_POINT_REST).hasAuthority(ADMIN)
                .antMatchers(HttpMethod.POST, END_POINT_REST).hasAuthority(ADMIN)
                .antMatchers(HttpMethod.PATCH, END_POINT_REST).hasAuthority(ADMIN)
                .antMatchers(HttpMethod.PUT, END_POINT_REST).hasAuthority(ADMIN)
                .antMatchers("/auth/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/")
                .and()
                .apply(jwtConfigurer);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
