package dev.goltsov.shortlinksgenerator.security;

import dev.goltsov.shortlinksgenerator.User;
import dev.goltsov.shortlinksgenerator.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    public Map<String, String> registration(String username, String password) {
        if (userService.existsByUsername(username)) {
            throw new IncorrectLoginOrPassword("Incorrect login or password");
        }
        User newUser = new User(username, PasswordUtils.hashPassword(password));
        newUser = userService.newUser(newUser);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtService.generateAccessToken(username));
        tokens.put("refreshToken", jwtService.generateRefreshToken(username));
        return tokens;
    }

    public Map<String, String> authentication(String username, String password) {
        if (!userService.existsByUsername(username)) {
            throw new IncorrectLoginOrPassword("Incorrect login or password");
        }
        User user = userService.getUserByUsername(username);
        if (!PasswordUtils.checkPassword(password, user.getPassword())) {
            throw new IncorrectLoginOrPassword("Incorrect login or password");
        }
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", jwtService.generateAccessToken(username));
        tokens.put("refreshToken", jwtService.generateRefreshToken(username));
        return tokens;
    }
}
