package com.vortex.fdedemo;

import com.vortex.fdedemo.aitools.CalculatorTool;
import com.vortex.fdedemo.aitools.CurrencyExchangeTool;
import com.vortex.fdedemo.aitools.WeatherTool;
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
    private WeatherTool weatherTool;
    private CurrencyExchangeTool currencyExchangeTool;

    private List<Message> history = new ArrayList<>();

    private final String SYSTEM_PROMPT = """
                You are a helpful AI assistant with access to external tools.
                Follow these instructions:
                1. For arithmetic calculations always use the calculator tool.
                2. For current weather, always use Weather Tool.
                3. For currency conversion, always use currency exchange tool.
                4. Always use calculator tool for arithmetic calculations.
                5. You can use multiple tools when solving a multi step request.
                6. After receiving tool results, explain the answer naturally.
                7. Never invent current weather or exchange rate information.
                """;

    public ChatService(ChatClient.Builder builder,
                       CalculatorTool calculatorTool,
                       WeatherTool weatherTool,
                       CurrencyExchangeTool currencyExchangeTool) {
        this.calculatorTool = calculatorTool;
        this.weatherTool = weatherTool;
        this.chatClient = builder.build();
        this.currencyExchangeTool = currencyExchangeTool;
    }

    public String chat(String message) {

        history.add(new UserMessage(message));

        String output = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .messages(history)
                .tools(calculatorTool, weatherTool, currencyExchangeTool)
                .call()
                .content();

        history.add(new AssistantMessage(output));

        return output;
    }
}