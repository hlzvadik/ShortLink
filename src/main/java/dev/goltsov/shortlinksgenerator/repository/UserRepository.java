package dev.goltsov.shortlinksgenerator.repository;

import dev.goltsov.shortlinksgenerator.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public boolean existsByUsername(String username);
    public User findUsersByUsername(String username);
}
