package com.javangarda.fantacalcio.user.application.internal.impl;

import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.internal.AccessTokenGenerator;
import com.javangarda.fantacalcio.user.application.internal.UserFactory;
import com.javangarda.fantacalcio.user.application.internal.storage.model.User;
import lombok.AllArgsConstructor;

import java.util.Locale;
import java.util.UUID;

@AllArgsConstructor
public class SimpleUserFactory implements UserFactory {

    private final AccessTokenGenerator accessTokenGenerator;
    private Locale defaultEmailLocale;

    @Override
    public User create(RegisterUserCommand registerUserCommand) {
        User user = new User(generateId());
        user.register(registerUserCommand.getFullName(), registerUserCommand.getEmail(),
                accessTokenGenerator.createConfirmEmailToken(), defaultEmailLocale);
        return user;
    }

    protected String generateId() {
        return UUID.randomUUID().toString();
    }
}
