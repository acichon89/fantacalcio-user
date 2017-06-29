package com.javangarda.fantacalcio.user.application.gateway;

import com.javangarda.fantacalcio.user.application.gateway.command.ChangeEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface CommandBus {
    @Gateway(requestChannel = "registerCommandChannel")
    void registerUser(RegisterUserCommand registerUserCommand);
    @Gateway(requestChannel = "confirmUserCommandChannel")
    void confirmUserEmail(String email);
    @Gateway(requestChannel = "changeEmailCommandChannel")
    void startChangingEmailProcedure(ChangeEmailCommand changeEmailCommand);
}
