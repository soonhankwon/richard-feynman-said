package dev.soon.richardfeynmansaid.idea.domain;

import dev.soon.richardfeynmansaid.idea.controller.dto.IdeaEditReqDto;
import dev.soon.richardfeynmansaid.idea.controller.dto.IdeaSaveReqDto;
import dev.soon.richardfeynmansaid.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@NoArgsConstructor
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private String description;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Idea(IdeaSaveReqDto dto, User user) {
        this.topic = dto.topic();
        this.description = dto.description();
        this.createdAt = LocalDateTime.now();
        this.user = user;
    }

    public void edit(IdeaEditReqDto dto) {
        this.topic = dto.topic();
        this.description = dto.description();
    }
}
