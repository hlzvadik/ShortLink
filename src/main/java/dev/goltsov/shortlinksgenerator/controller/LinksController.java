package dev.goltsov.shortlinksgenerator.controller;

import dev.goltsov.shortlinksgenerator.dto.LongLink;
import dev.goltsov.shortlinksgenerator.dto.ShortLink;
import dev.goltsov.shortlinksgenerator.service.ShortLinkService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class LinksController {
    private final ShortLinkService shortLinkService;

    public LinksController(ShortLinkService shortLinkService) {
        this.shortLinkService = shortLinkService;
    }

    @PostMapping
    public ResponseEntity<ShortLink> create(@Valid @RequestBody LongLink longLink) {
        return ResponseEntity.ok(new ShortLink(shortLinkService.getShortLink(longLink.longLink())));
    }

    @GetMapping
    public ResponseEntity<LongLink> getLong(@Valid @RequestBody ShortLink shortLink) {
        return ResponseEntity.ok(new LongLink(shortLinkService.getLongLink(shortLink.shortLink())));
    }

    @GetMapping("/{link}")
    public ResponseEntity<Void> redirect(@PathVariable String link) {
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(shortLinkService.getLongLink(link)))
                .build();
    }
}
