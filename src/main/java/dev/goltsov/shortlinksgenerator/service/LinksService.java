package dev.goltsov.shortlinksgenerator.service;

import dev.goltsov.shortlinksgenerator.Links;
import dev.goltsov.shortlinksgenerator.User;
import dev.goltsov.shortlinksgenerator.exception.LinkExpiredException;
import dev.goltsov.shortlinksgenerator.exception.LinkNotFoundException;
import dev.goltsov.shortlinksgenerator.repository.LinksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.Semaphore;

@Service
@Slf4j
public class LinksService {

    private final LinksRepository linksRepository;

    private static Semaphore nowLoad = new Semaphore(1000);
    private final LinksServicePr shortLinkServicePr;

    public LinksService(LinksRepository linksRepository, LinksServicePr shortLinkServicePr) {
        this.linksRepository = linksRepository;
        this.shortLinkServicePr = shortLinkServicePr;
    }

    public String getShortLink(String longLink, User user) {
        try {
            nowLoad.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            return shortLinkServicePr.getShortLinkPr(longLink, user);
        } finally {
            nowLoad.release();
        }
    }

    public String getLongLink(String shortLink) {
        Links links = linksRepository.findByShortLink(shortLink);
        if (links == null) {
            log.info("Not found shortLink: {}", shortLink);
            throw new LinkNotFoundException("No such short link");
        }
        if (LocalDateTime.now(ZoneOffset.UTC).isAfter(links.getCreatedAt().plusMinutes(10))) {
            log.info("Link {} is expired", shortLink);
            throw new LinkExpiredException("Link " + shortLink + " is expired");
        }
        log.info("For {} found {}", shortLink, links.getLongLink());
        return links.getLongLink();
    }

}
