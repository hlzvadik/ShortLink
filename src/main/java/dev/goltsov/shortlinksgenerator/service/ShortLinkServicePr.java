package dev.goltsov.shortlinksgenerator.service;

import dev.goltsov.shortlinksgenerator.Links;
import dev.goltsov.shortlinksgenerator.repository.LinksRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;

@Service
@Slf4j
public class ShortLinkServicePr {

    private final LinksRepository linksRepository;

    public ShortLinkServicePr(LinksRepository linksRepository) {
        this.linksRepository = linksRepository;
    }

    @Transactional
    public String getShortLinkPr(String longLink) {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        Links links = linksRepository.findByLongLink(longLink);

        if (links == null) {
            links = new Links();
            links.setLongLink(longLink);
            links.setShortLink(generateUniqueCode(longLink));
            links.setCreatedAt(now);
            log.info("Created new record: {}", links.toString());
        } else if (now.isAfter(links.getCreatedAt().plusMinutes(10))) {
            links.setCreatedAt(now);
            log.info("Refactor time for record: {}", links.toString());
        } else {
            log.info("Found good record: {}", links.toString());
        }

        linksRepository.save(links);
        return links.getShortLink();
    }

    private String generateUniqueCode(String longLink) {
        String input = longLink;
        while (true) {
            String encoded = sha256Base64(input);
            for (int s = 0; s + 8 <= encoded.length(); s += 8) {
                String candidate = encoded.substring(s, s + 8);
                if (!linksRepository.existsByShortLink(candidate)) {
                    return candidate;
                }
            }
            input = input + encoded;
        }
    }

    private String sha256Base64(String input) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256")
                    .digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
