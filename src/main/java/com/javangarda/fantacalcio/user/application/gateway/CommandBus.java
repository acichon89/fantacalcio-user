package com.javangarda.fantacalcio.user.application.gateway;

import com.javangarda.fantacalcio.user.application.gateway.command.*;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface CommandBus {
    @Gateway(requestChannel = "registerCommandChannel")
    void registerUser(RegisterUserCommand registerUserCommand);
    @Gateway(requestChannel = "confirmEmailCommandChannel")
    void confirmEmail(ConfirmEmailCommand confirmEmailCommand);
    @Gateway(requestChannel = "startChangingEmailProcedureCommandChannel")
    void startChangingEmailProcedure(StartChangingEmailProcedureCommand startChangingEmailProcedureCommand);
    @Gateway(requestChannel = "startResettingPasswordProcedureCommandChannel")
    void startResetPasswordProcedure(StartResettingPasswordProcedureCommand startResettingPasswordProcedureCommand);
    @Gateway(requestChannel = "resetPasswordCommandChannel")
    void resetPassword(ResetPasswordCommand resetPasswordCommand);
}
