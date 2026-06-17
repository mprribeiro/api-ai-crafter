package com.mprribeiro.app_ai_crafter.service.impl;

import com.mprribeiro.app_ai_crafter.security.AuthUtil;
import com.mprribeiro.app_ai_crafter.service.AIGenerativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class AiGenerativeServiceImpl implements AIGenerativeService {

    private final ChatClient chatClient;
    private final AuthUtil authUtil;

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public Flux<String> streamResponse(final String message, final Long projectId) {
        final var userId = authUtil.getCurrentUserId();
        createChatSessionIfNotExists(projectId, userId);

        return chatClient.prompt(message).stream().content();
    }

    private void createChatSessionIfNotExists(final Long projectId,
                                              final Long userId) {

    }
}
