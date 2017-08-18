package com.javangarda.fantacalcio.user.application.internal.saga;


import com.javangarda.fantacalcio.user.application.gateway.command.*;

public interface CommandHandler {
    void handle(RegisterUserCommand command);
    void handle(ConfirmEmailCommand command);
    void handle(StartChangingEmailProcedureCommand command);
    void handle(StartResettingPasswordProcedureCommand command);
    void handle(ResetPasswordCommand command);
}
