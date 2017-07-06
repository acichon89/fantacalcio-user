package com.javangarda.fantacalcio.user.application.internal.saga;


import com.javangarda.fantacalcio.user.application.gateway.command.ChangeEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.ConfirmEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.ResetPasswordCommand;

public interface CommandHandler {
    void handle(ChangeEmailCommand command);
    void handle(RegisterUserCommand command);
    void handle(ConfirmEmailCommand command);
    void handle(ResetPasswordCommand command);
}
