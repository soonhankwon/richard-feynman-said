package dev.soon.richardfeynmansaid.idea.domain;

import lombok.Getter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Embeddable
public class Feedback {

    @Enumerated(EnumType.STRING)
    private Grade grade;
    private String feed;

    public Feedback() {
        this.grade = Grade.BEFORE_EVALUATION;
        this.feed = null;
    }
}
