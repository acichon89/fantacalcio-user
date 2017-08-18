package com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Events {

    @Output
    MessageChannel activationMailChannel();

    @Output
    MessageChannel changeEmailChannel();

    @Output
    MessageChannel userResetPasswordChannel();

    String ACCOUNT_CREATED_INPUT = "accountCreatedChannel";
    @Input(ACCOUNT_CREATED_INPUT)
    SubscribableChannel confirmUserChannel();

    String ACCOUNT_PASSWORD_CHANGED_INPUT = "accountPasswordChangedChannel";
    @Input(ACCOUNT_PASSWORD_CHANGED_INPUT)
    SubscribableChannel accountPasswordChangedChannel();

    String ACCOUNT_EMAIL_CHANGED_INPUT = "accountEmailChangedChannel";
    @Input(ACCOUNT_EMAIL_CHANGED_INPUT)
    SubscribableChannel accountEmailChangedChannel();
}
