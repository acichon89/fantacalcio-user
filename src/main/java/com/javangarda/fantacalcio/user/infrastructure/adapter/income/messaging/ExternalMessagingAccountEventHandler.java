package com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging;

import com.javangarda.fantacalcio.user.application.gateway.CommandBus;
import com.javangarda.fantacalcio.user.application.gateway.command.ConfirmEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.ResetPasswordCommand;
import com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging.events.AccountCreatedEvent;
import com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging.events.AccountEmailChangedEvent;
import com.javangarda.fantacalcio.user.infrastructure.adapter.income.messaging.events.AccountPasswordChangedEvent;
import lombok.AllArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;

@AllArgsConstructor
public class ExternalMessagingAccountEventHandler {

    private CommandBus commandBus;

    @ServiceActivator(inputChannel = Events.ACCOUNT_CREATED_INPUT)
    public void handle(AccountCreatedEvent event){
        commandBus.confirmEmail(ConfirmEmailCommand.of(event.getEmail(), event.getRegistrationToken()));
    }

    @ServiceActivator(inputChannel = Events.ACCOUNT_PASSWORD_CHANGED_INPUT)
    public void handle(AccountPasswordChangedEvent event){
        commandBus.resetPassword(ResetPasswordCommand.of(event.getEmail(), event.isWithReset()));
    }

    @ServiceActivator(inputChannel = Events.ACCOUNT_EMAIL_CHANGED_INPUT)
    public void handle(AccountEmailChangedEvent event){
        commandBus.confirmEmail(ConfirmEmailCommand.of(event.getNewEmail(), event.getConfirmToken()));
    }
}
