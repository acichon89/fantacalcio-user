package com.javangarda.fantacalcio.user.infrastructure.port.adapter.messaging;

import com.javangarda.fantacalcio.user.application.data.event.UserRegisteredEvent;
import com.javangarda.fantacalcio.user.application.saga.UserEventPublisher;
import lombok.AllArgsConstructor;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

@AllArgsConstructor
public class ExternalMessagingUserEventPublisher implements UserEventPublisher {

    private Events events;

    @Override
    public void publishUserRegistered(UserRegisteredEvent event) {
        Message<UserRegisteredEvent> message = MessageBuilder.withPayload(event).build();
        events.activationMailChannel().send(message);
    }
}
