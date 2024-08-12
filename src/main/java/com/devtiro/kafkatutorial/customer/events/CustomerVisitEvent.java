package com.devtiro.kafkatutorial.customer.events;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CustomerVisitEvent (String customerId, LocalDateTime dateTime){}
