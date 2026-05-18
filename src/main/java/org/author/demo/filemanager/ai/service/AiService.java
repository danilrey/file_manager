package org.author.demo.filemanager.ai.service;

import org.author.demo.filemanager.ai.dto.AiGeneratedResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

@Service
public class AiService {
    private static final String SYSTEM_PROMPT = """
            You are an assistant that must return ONLY valid JSON.
            
            The JSON must have exactly these fields:
            {
              "title": "short informative title",
              "response": "full answer text"
            }
            
            Rules:
            - Return raw JSON only.
            - Do not use markdown.
            - Do not add code fences.
            - Do not add any extra text before or after the JSON.
            - Do not explain anything outside the JSON.
            - The title must be short, clear, and informative.
            - The response must contain the complete answer to the user's request.
            - If the user asks for code, put the code inside the "response" field as plain text.
            - Keep the JSON valid and parseable.""";
    protected static final String RESPONSE = "response";
    protected static final String TITLE = "title";

    private final ChatClient chatClient;
    private final PromptContextBuilder promptContextBuilder;
    private final ObjectMapper objectMapper;

    public AiService(ChatClient chatClient, PromptContextBuilder promptContextBuilder, ObjectMapper objectMapper) {
        this.chatClient = chatClient;
        this.promptContextBuilder = promptContextBuilder;
        this.objectMapper = objectMapper;
    }

    public AiGeneratedResponse generate(String prompt, List<UUID> sourceFileIds) {
        String fullPrompt = promptContextBuilder.buildPromptWithContext(prompt, sourceFileIds);
        String rawJson = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(fullPrompt)
                .call()
                .content();

        return parseJson(rawJson);
    }

    private AiGeneratedResponse parseJson(String json) {
        JsonNode root = objectMapper.readTree(json);
        String title = root.get(TITLE).stringValue();
        String responseText = root.get(RESPONSE).stringValue();

        return new AiGeneratedResponse(title, responseText);
    }
}
