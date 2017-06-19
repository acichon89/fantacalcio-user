package com.javangarda.fantacalcio.user.infrastructure.port.adapter.messaging;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface Events {

    @Output
    MessageChannel activationMailChannel();

    String ACCOUNT_CREATED_INPUT = "accountCreatedChannel";

    @Input("dsadas")
    SubscribableChannel confirmUserChannel();
}
