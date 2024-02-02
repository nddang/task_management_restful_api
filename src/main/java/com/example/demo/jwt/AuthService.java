package com.example.demo.jwt;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtGenerator jwtGenerator;
    private final PasswordEncoder passwordEncoder;

    public String tokenlogin(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,
                        password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userService.findByEmail(email);
        if(user !=null){
            var token = jwtGenerator.generateToken(email);
           return token;
        }
        return null;
    }

    public String tokenRegister(User registerUser) {
        User userResponse = userService.findByEmail(registerUser.getEmail());
        if (userResponse!=null) {
            return null;
        }
        registerUser.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        userService.registerUser(registerUser);
        var token = jwtGenerator.generateToken(registerUser.getEmail());

        return token;
    }
}

