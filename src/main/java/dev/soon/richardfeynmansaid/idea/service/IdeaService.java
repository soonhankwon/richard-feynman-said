package dev.soon.richardfeynmansaid.idea.service;

import dev.soon.richardfeynmansaid.idea.controller.dto.IdeaEditReqDto;
import dev.soon.richardfeynmansaid.idea.controller.dto.IdeaSaveReqDto;
import dev.soon.richardfeynmansaid.idea.controller.dto.IdeaSubmitReqDto;
import dev.soon.richardfeynmansaid.idea.domain.Idea;
import dev.soon.richardfeynmansaid.idea.repository.IdeaRepository;
import dev.soon.richardfeynmansaid.security.SecurityUser;
import dev.soon.richardfeynmansaid.user.domain.User;
import dev.soon.richardfeynmansaid.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Idea> findAllIdeasByUser(SecurityUser securityUser) {
        User user = userRepository.findUserByEmail(securityUser.getUsername()).orElseThrow();
        return ideaRepository.findAllByUser(user);
    }

    public Idea findIdeaById(Long ideaId) {
        return ideaRepository.findById(ideaId).orElseThrow();
    }

    @Transactional
    public void saveIdea(SecurityUser securityUser, IdeaSaveReqDto dto) {
        User user = userRepository.findUserByEmail(securityUser.getUsername()).orElseThrow();
        Idea idea = new Idea(dto, user);
        ideaRepository.save(idea);
        log.info("save idea={}", idea);
    }

    public void submitIdea(IdeaSubmitReqDto dto) {
        log.info("AI submit={}", dto);
        // use external api
        // return result
    }

    @Transactional
    public void editIdea(Long ideaId, IdeaEditReqDto dto) {
        Idea idea = ideaRepository.findById(ideaId).orElseThrow();
        idea.edit(dto);
        log.info("edit idea={}", idea);
    }

    @Transactional
    public void deleteIdea(Long ideaId) {
        Idea idea = ideaRepository.findById(ideaId).orElseThrow();
        ideaRepository.delete(idea);
        log.info("delete idea={}", idea);
    }
}
