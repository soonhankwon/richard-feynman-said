package dev.soon.richardfeynmansaid.idea.repository;

import dev.soon.richardfeynmansaid.idea.domain.Idea;
import dev.soon.richardfeynmansaid.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
    List<Idea> findAllByUser(User user);
}
