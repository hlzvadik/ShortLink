package dev.goltsov.shortlinksgenerator.security;

import dev.goltsov.shortlinksgenerator.User;
import dev.goltsov.shortlinksgenerator.dto.JwtTokens;
import dev.goltsov.shortlinksgenerator.service.UserService;
import io.jsonwebtoken.Jwt;
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

    public JwtTokens registration(String username, String password) {
        if (userService.existsByUsername(username)) {
            throw new IncorrectLoginOrPassword("Incorrect login or password");
        }
        User newUser = new User(username, PasswordUtils.hashPassword(password));
        newUser = userService.newUser(newUser);
        return new JwtTokens(
                jwtService.generateAccessToken(username),
                jwtService.generateRefreshToken(username)
        );
    }

    public JwtTokens authentication(String username, String password) {
        if (!userService.existsByUsername(username)) {
            throw new IncorrectLoginOrPassword("Incorrect login or password");
        }
        User user = userService.getUserByUsername(username);
        if (!PasswordUtils.checkPassword(password, user.getPassword())) {
            throw new IncorrectLoginOrPassword("Incorrect login or password");
        }
        return new JwtTokens(
                jwtService.generateAccessToken(username),
                jwtService.generateRefreshToken(username)
        );
    }
}
