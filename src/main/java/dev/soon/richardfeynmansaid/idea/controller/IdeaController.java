package dev.soon.richardfeynmansaid.idea.controller;

import dev.soon.richardfeynmansaid.idea.controller.dto.IdeaEditReqDto;
import dev.soon.richardfeynmansaid.idea.controller.dto.IdeaSaveReqDto;
import dev.soon.richardfeynmansaid.idea.controller.dto.IdeaSubmitReqDto;
import dev.soon.richardfeynmansaid.idea.domain.Idea;
import dev.soon.richardfeynmansaid.idea.service.IdeaService;
import dev.soon.richardfeynmansaid.security.SecurityUser;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/ideas")
public class IdeaController {

    private final IdeaService ideaService;
    private final ChatgptService chatgptService;

    @Value("${gpt.protocol.topic}")
    public String topicProtocol;

    @Value("${gpt.protocol.description}")
    public String descriptionProtocol;

    @GetMapping
    public String ideas(@AuthenticationPrincipal SecurityUser securityUser, Model model) {
        List<Idea> ideas = ideaService.findAllIdeasByUser(securityUser);
        model.addAttribute("ideas", ideas);
        return "ideas/ideas";
    }

    @GetMapping("/{ideaId}")
    public String idea(@PathVariable Long ideaId, Model model) {
        Idea idea = ideaService.findIdeaById(ideaId);
        model.addAttribute("idea", idea);
        return "ideas/idea";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("idea", new Idea());
        return "ideas/addForm";
    }

    @PostMapping("/add")
    public String addIdea(@AuthenticationPrincipal SecurityUser securityUser,
                          @ModelAttribute("idea") IdeaSaveReqDto dto) {
        ideaService.saveIdea(securityUser, dto);
        return "redirect:/ideas";
    }

    @GetMapping("/{ideaId}/edit")
    public String editForm(@PathVariable Long ideaId, Model model) {
        Idea idea = ideaService.findIdeaById(ideaId);
        model.addAttribute("idea", idea);
        return "ideas/editForm";
    }

    @PostMapping("/{ideaId}/edit")
    public String editIdea(@PathVariable Long ideaId, @ModelAttribute("idea") IdeaEditReqDto dto) {
        ideaService.editIdea(ideaId, dto);
        return "redirect:/ideas/{ideaId}";
    }

    @GetMapping("/{ideaId}/submit")
    public String submitForm(@PathVariable Long ideaId, Model model) {
        Idea idea = ideaService.findIdeaById(ideaId);
        model.addAttribute("idea", idea);
        return "ideas/submitForm";
    }

    @PostMapping("/{ideaId}/submit")
    public String submitIdea(@PathVariable Long ideaId,
                             @ModelAttribute("idea") IdeaSubmitReqDto dto) {
        String result = chatgptService.sendMessage(topicProtocol + " " + dto.description() + " " + descriptionProtocol);
        ideaService.saveFeedback(ideaId, result);
        return "redirect:/ideas/{ideaId}/feedback";
    }

    @GetMapping("/{ideaId}/feedback")
    public String feedback(@PathVariable Long ideaId, Model model) {
        Idea idea = ideaService.findIdeaById(ideaId);
        model.addAttribute("idea", idea);
        return "/feedbacks/feedback";
    }

    @PostMapping("/{ideaId}/delete")
    public String deleteIdea(@PathVariable Long ideaId) {
        ideaService.deleteIdea(ideaId);
        return "redirect:/ideas";
    }
}
