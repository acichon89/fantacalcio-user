package com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging.events;

import lombok.Value;

@Value(staticConstructor = "of")
public class AccountCreatedEvent {
    private String email;
    private String registrationToken;
}
