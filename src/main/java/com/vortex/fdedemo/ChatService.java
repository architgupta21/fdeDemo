package com.vortex.fdedemo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SummarizeService {

    private ChatClient chatClient;

    private List<Message> history = new ArrayList<>();

    private final String SYSTEM_PROMPT = """
                You are a customer-support executive for
                our food delivery application called Tomato.
                
                Your job is to identify the customer's main
                problem and urgency. Answer them related to there
                query in 1 line.
                
                Respond to customer professionally.
                If user is furious or angry or have any issue
                use words like I understand your concern, or I am
                sorry you have go through this and so on. Then
                solve customer query and give a response.
                
                Do not respond to any other message which is not 
                related to ordering food query, refund query, 
                order tracking status query or company policy
                query.If user ask any other question that is not 
                 related to our services then tell that this is 
                beyond my capability. Just dont answer any other
                question that is not related to our services.
                 Below is the customer query.
                """;


    public SummarizeService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String chat(String message) {

        history.add(new UserMessage(message));

        String output = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .call()
                .content();

        history.add(new AssistantMessage(output));

        return output;
    }
}
