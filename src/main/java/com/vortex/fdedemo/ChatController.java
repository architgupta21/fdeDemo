package com.vortex.fdedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SummarizeController {

    @Autowired
    private SummarizeService summarizeService;

    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        return summarizeService.chat(message);
    }
}
