package com.vortex.fdedemo;

import com.vortex.fdedemo.aitools.WebsiteTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WebsiteBuilderService {

    private final ChatClient chatClient;
    private final WebsiteTools websiteTools;

    private final List<Message> history = new ArrayList<>();

    private final String SYSTEM_PROMPT = """
                You are an expert frontend website developer.
                Your job is to create complete static websites using the available tools.
                Follow these instructions:
                1. Create a separate directory for every website.
                2. Create index.html
                3. Create style.css
                4. Create script.js when javascript is useful
                5. Build modern, beautiful and responsive websites.
                6. Use only HTML, CSS and vanilla Javascript
                7. Do not just return website code in your responce. Actually create website using given tools.
                8. After creating the website, list the project files.
                9. Read important files again if needed and fix obvious problems.
                10. Finish only when the complete website is created.
                """;

    public WebsiteBuilderService(ChatClient.Builder builder,
                                 WebsiteTools websiteTools) {
        this.chatClient = builder.build();
        this.websiteTools = websiteTools;
    }

    public String generate(String message) {

        history.add(new UserMessage(message));

        String output = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .tools(websiteTools)
                .call()
                .content();

        history.add(new AssistantMessage(output));

        return output;
    }
}