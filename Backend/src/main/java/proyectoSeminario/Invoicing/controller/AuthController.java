package proyectoSeminario.Invoicing.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proyectoSeminario.Invoicing.dto.LoginRequest;
import proyectoSeminario.Invoicing.dto.RegisterRequest;
import proyectoSeminario.Invoicing.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("v1/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Received Login Request");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        logger.info("User authenticated successfully! setting authentication context");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.info("Authentication Context set successfully!");
        return ResponseEntity.ok(Map.of("message", "Successful login!"));
    }
}