package com.javangarda.fantacalcio.user.infrastructure.port.adapter.messaging;

import com.javangarda.fantacalcio.user.application.internal.saga.UserAttemptedToChangeEmailEvent;
import com.javangarda.fantacalcio.user.application.internal.saga.UserRegisteredEvent;
import com.javangarda.fantacalcio.user.application.internal.saga.UserEventPublisher;
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

    @Override
    public void publishUserAttemptedToChangeEmail(UserAttemptedToChangeEmailEvent event) {
        Message<UserAttemptedToChangeEmailEvent> message = MessageBuilder.withPayload(event).build();
        events.changeEmailChannel().send(message);
    }

}
