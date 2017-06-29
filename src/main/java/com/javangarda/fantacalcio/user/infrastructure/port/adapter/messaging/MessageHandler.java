package com.javangarda.fantacalcio.user.infrastructure.port.adapter.messaging;

import com.javangarda.fantacalcio.user.application.gateway.CommandBus;
import lombok.AllArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;

@AllArgsConstructor
public class MessageHandler {

    private CommandBus commandBus;

    @ServiceActivator(inputChannel = Events.ACCOUNT_CREATED_INPUT)
    public void handleAccountCreatedEvent(String email){
        commandBus.confirmUserEmail(email);
    }
}
