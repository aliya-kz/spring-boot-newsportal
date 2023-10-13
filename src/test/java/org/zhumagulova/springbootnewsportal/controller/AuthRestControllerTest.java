package org.zhumagulova.springbootnewsportal.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.zhumagulova.springbootnewsportal.api.model.AuthenticationRequest;
import org.zhumagulova.springbootnewsportal.entity.Role;
import org.zhumagulova.springbootnewsportal.entity.User;
import org.zhumagulova.springbootnewsportal.security.jwt.JwtTokenProvider;
import org.zhumagulova.springbootnewsportal.service.UserService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class AuthRestControllerTest {

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @Autowired
    WebApplicationContext context;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }
    @Test
    void authenticate() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("email@email.com");
        request.setPassword("password");

        User user = new User();

        Set<Role> roles = new HashSet<>();
        Role role = new Role ();
        role.setName("ADMIN");
        roles.add(role);
        user.setRoles(roles);

        String token = "some_short_token";

        when(userService.findUserByEmail(request.getEmail())).thenReturn(Optional.of(new User()));
        when(jwtTokenProvider.createToken(request.getEmail(), user.getRoles())).thenReturn(token);

        mockMvc.perform(post("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.email").value(request.getEmail()))
                .andDo(print());
    }


    @Test
    void logout_success() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }
}