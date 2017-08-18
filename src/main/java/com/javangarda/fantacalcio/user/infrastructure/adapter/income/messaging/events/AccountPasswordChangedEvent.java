package com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging.events;

import lombok.Value;

@Value(staticConstructor = "of")
public class AccountPasswordChangedEvent {
    private String email;
    private boolean withReset;
}
