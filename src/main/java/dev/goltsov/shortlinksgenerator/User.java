package dev.goltsov.shortlinksgenerator;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Links> linksList;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void addLinks(Links links) {
        linksList.add(links);
        links.setUser(this);
    }

    public void removeLinks(Links links) {
        linksList.remove(links);
        links.setUser(null);
    }
}
