package dev.goltsov.shortlinksgenerator.security;

import dev.goltsov.shortlinksgenerator.dto.AccessToken;
import dev.goltsov.shortlinksgenerator.dto.AuthRequest;
import dev.goltsov.shortlinksgenerator.dto.JwtTokens;
import dev.goltsov.shortlinksgenerator.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthService authService, JwtService jwtService, UserService userService) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/authentication")
    public ResponseEntity<JwtTokens> authentication(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authentication(authRequest.username(), authRequest.password()));
    }

    @PostMapping("/registration")
    public ResponseEntity<JwtTokens> registration(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.registration(authRequest.username(), authRequest.password()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AccessToken> refresh(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).build();
        }
        String refreshToken = authHeader.substring(7);

        if (!jwtService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(401).build();
        }
        String username = jwtService.extractUsernameFromRefreshToken(refreshToken);
        if (username == null || !userService.existsByUsername(username)) {
            return ResponseEntity.status(401).build();
        }
        String newAccessToken = jwtService.generateAccessToken(username);
        return ResponseEntity.ok(new AccessToken(newAccessToken));
    }
}
