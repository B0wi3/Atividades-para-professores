package com.atividades.bowie.api.controller;

import com.atividades.bowie.api.model.LoginBody;
import com.atividades.bowie.api.model.RegistrationBody;
import com.atividades.bowie.exception.IncorrectPasswordException;
import com.atividades.bowie.exception.UserAlreadyExistsException;
import com.atividades.bowie.exception.UsernameNotFoundException;
import com.atividades.bowie.model.LocalUser;
import com.atividades.bowie.service.TokenBlacklistService;
import com.atividades.bowie.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private UserService userService;
    private TokenBlacklistService tokenBlacklistService;

    public AuthenticationController(UserService userService, TokenBlacklistService tokenBlacklistService) {
        this.userService = userService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/register")
    public ResponseEntity<LocalUser> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
        System.out.println("Requisição recebida: " + registrationBody);
        try {
            LocalUser user = userService.registerUser(registrationBody);
            return ResponseEntity.ok(user);
        } catch (UserAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginBody loginBody) {
        System.out.println("Requisição recebida: " + loginBody);
        try {
            String token = userService.loginUser(loginBody);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (IncorrectPasswordException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring("Bearer".length());
        tokenBlacklistService.addToBlacklist(token);
        return ResponseEntity.ok("Logout realizado");
    }
}
