package com.javangarda.fantacalcio.user.application.gateway;

import com.javangarda.fantacalcio.user.application.gateway.command.ChangeEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.ConfirmEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.ResetPasswordCommand;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface CommandBus {
    @Gateway(requestChannel = "registerCommandChannel")
    void registerUser(RegisterUserCommand registerUserCommand);
    @Gateway(requestChannel = "confirmUserCommandChannel")
    void confirmUserEmail(ConfirmEmailCommand confirmEmailCommand);
    @Gateway(requestChannel = "changeEmailCommandChannel")
    void startChangingEmailProcedure(ChangeEmailCommand changeEmailCommand);
    @Gateway(requestChannel = "resetPasswordCommandChannel")
    void startResetPasswordProcedure(ResetPasswordCommand resetPasswordCommand);
}
