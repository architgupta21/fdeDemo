package com.vortex.fdedemo;

import com.vortex.fdedemo.aitools.CalculatorTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;

    private final CalculatorTool calculatorTool;

    private List<Message> history = new ArrayList<>();

    private final String SYSTEM_PROMPT = """
                You are a helpful AI assistant with access to external tools.
                Follow these instructions:
                1. For arithmetic calculations always use the calculator tool.
                2. After receiving tool results, explain the answer naturally.
                """;

    public ChatService(ChatClient.Builder builder, CalculatorTool calculatorTool) {
        this.calculatorTool = calculatorTool;
        this.chatClient = builder.build();
    }

    public String chat(String message) {

        history.add(new UserMessage(message));

        String output = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .tools(calculatorTool)
                .call()
                .content();

        history.add(new AssistantMessage(output));

        return output;
    }
}