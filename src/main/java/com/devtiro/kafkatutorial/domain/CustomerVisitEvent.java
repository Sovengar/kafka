package com.devtiro.kafkatutorial.domain;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
public record CustomerVisitEvent (String customerId, LocalDateTime dateTime){}
