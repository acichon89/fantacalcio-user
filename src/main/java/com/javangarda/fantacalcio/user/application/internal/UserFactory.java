package com.javangarda.fantacalcio.user.application.internal;


import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.internal.storage.model.User;

public interface UserFactory {
    User create(RegisterUserCommand registerUserCommand);
}
