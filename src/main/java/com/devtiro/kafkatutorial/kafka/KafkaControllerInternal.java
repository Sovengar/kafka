package com.devtiro.kafkatutorial.kafka;

import com.devtiro.kafkatutorial.customer.application.CustomerEventsService;
import com.devtiro.kafkatutorial.customer.events.CustomerVisitEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class KafkaControllerInternal {
    private final CustomerEventsService customerEventsService;
    private final ObjectMapper objectMapper;

    @GetMapping("/send")
    public void sendMessage(@RequestParam("message") String message) throws JsonProcessingException {
        final CustomerVisitEvent event = CustomerVisitEvent.builder()
                .customerId(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now())
                .build();

        final String payload = objectMapper.writeValueAsString(event);

        customerEventsService.publish(payload);
    }
}
