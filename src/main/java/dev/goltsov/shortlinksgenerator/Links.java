package dev.goltsov.shortlinksgenerator;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "links")
public class Links {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String longLink;

    @Column(nullable = false, unique = true)
    private String shortLink;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Links(String longLink, String shortLink, LocalDateTime createdAt) {
        this.longLink = longLink;
        this.shortLink = shortLink;
        this.createdAt = createdAt;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
