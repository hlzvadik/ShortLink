package dev.goltsov.shortlinksgenerator.repository;

import dev.goltsov.shortlinksgenerator.Links;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinksRepository extends JpaRepository<Links, Long> {
    Links findByShortLink(String shortLink);
    Links findByLongLink(String longLink);

    boolean existsByShortLink(String candidate);
}
