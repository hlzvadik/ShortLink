package dev.goltsov.shortlinksgenerator.controller;

import dev.goltsov.shortlinksgenerator.Links;
import dev.goltsov.shortlinksgenerator.User;
import dev.goltsov.shortlinksgenerator.dto.LongLink;
import dev.goltsov.shortlinksgenerator.dto.ShortLink;
import dev.goltsov.shortlinksgenerator.security.JwtService;
import dev.goltsov.shortlinksgenerator.service.LinksService;
import dev.goltsov.shortlinksgenerator.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/links")
public class LinksController {
    private final LinksService shortLinkService;
    private final JwtService jwtService;
    private final UserService userService;

    public LinksController(LinksService shortLinkService, JwtService jwtService, UserService userService) {
        this.shortLinkService = shortLinkService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<ShortLink> create(@Valid @RequestBody LongLink longLink, @RequestHeader("Authorization") String authHeader) {
        if (isValidAccessToken(authHeader)) return ResponseEntity.status(401).build();
        User user = getUserFromAccessToken(authHeader);
        return ResponseEntity.ok(new ShortLink(shortLinkService.getShortLink(longLink.longLink(), user)));
    }

    @GetMapping
    public ResponseEntity<LongLink> getLong(@Valid @RequestBody ShortLink shortLink, @RequestHeader("Authorization") String authHeader) {
        if (isValidAccessToken(authHeader)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(new LongLink(shortLinkService.getLongLink(shortLink.shortLink())));
    }

    @GetMapping("/{link}")
    public ResponseEntity<Void> redirect(@PathVariable String link) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(shortLinkService.getLongLink(link)))
                .build();
    }

    private boolean isValidAccessToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return true;
        }
        String accessToken = authHeader.substring(7);

        String username = jwtService.extractUsername(accessToken);
        if (username == null || !userService.existsByUsername(username)) {
            return true;
        }
        if (!jwtService.validateToken(accessToken, username)) {
            return true;
        }
        return false;
    }

    private User getUserFromAccessToken(@RequestHeader("Authorization") String authHeader) {
        String accessToken = authHeader.substring(7);

        String username = jwtService.extractUsername(accessToken);
        return userService.getUserByUsername(username);
    }
}
