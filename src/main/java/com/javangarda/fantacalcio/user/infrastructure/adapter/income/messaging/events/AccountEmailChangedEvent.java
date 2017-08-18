package com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging.events;

import lombok.Value;

@Value(staticConstructor = "of")
public class AccountEmailChangedEvent {
    private String oldEmail;
    private String newEmail;
    private String confirmToken;
}
