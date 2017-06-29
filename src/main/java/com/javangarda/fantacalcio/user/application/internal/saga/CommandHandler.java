package com.javangarda.fantacalcio.user.application.internal.saga;


import com.javangarda.fantacalcio.user.application.gateway.command.ChangeEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;

public interface CommandHandler {
    void handle(ChangeEmailCommand command);
    void handle(RegisterUserCommand command);
}
