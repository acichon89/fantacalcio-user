package com.javangarda.fantacalcio.user.application.internal;


import com.javangarda.fantacalcio.user.application.data.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.storage.User;

public interface UserFactory {
    User create(RegisterUserCommand registerUserCommand);
}
