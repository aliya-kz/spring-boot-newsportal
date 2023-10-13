package org.zhumagulova.springbootnewsportal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zhumagulova.springbootnewsportal.api.model.AuthenticationRequest;
import org.zhumagulova.springbootnewsportal.api.model.RegistrationRequest;
import org.zhumagulova.springbootnewsportal.exception.PasswordIncorrectException;
import org.zhumagulova.springbootnewsportal.exception.UserNotFoundException;
import org.zhumagulova.springbootnewsportal.api.model.response.AuthenticationResponse;
import org.zhumagulova.springbootnewsportal.entity.User;
import org.zhumagulova.springbootnewsportal.security.jwt.JwtTokenProvider;
import org.zhumagulova.springbootnewsportal.service.UserService;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Api("Authentication controller")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;

    private final UserService userService;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthRestController(AuthenticationManager authenticationManager, UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    @ApiOperation("Log in by email and password")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = userService.findUserByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("User does not exist"));

            String token = jwtTokenProvider.createToken(request.getEmail(), user.getRoles());

            return new AuthenticationResponse(request.getEmail(), token);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }

    @PostMapping("/logout")
    @ApiOperation("Log out")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }


    @PostMapping("/signup")
    @ApiOperation("Register new user")
    public void signup(@RequestBody @Valid RegistrationRequest request) throws PasswordIncorrectException {
        if (!request.getConfirmPassword().equals(request.getPassword())) {
            throw new PasswordIncorrectException("Passwords do not match");
        }
        User user = request.toUser();
        userService.save(user);
    }
}
