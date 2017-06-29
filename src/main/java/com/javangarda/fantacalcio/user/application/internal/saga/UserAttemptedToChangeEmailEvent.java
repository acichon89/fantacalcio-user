package com.javangarda.fantacalcio.user.application.internal.saga;

import lombok.Value;

import java.util.Locale;

@Value(staticConstructor = "of")
public class UserAttemptedToChangeEmailEvent {
    private String id;
    private String email;
    private String newEmail;
    private String confirmationToken;
    private Locale emailLocale;
}
