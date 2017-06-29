package com.javangarda.fantacalcio.user.application.internal.saga;


import lombok.Value;

import java.util.Locale;

@Value(staticConstructor = "of")
public class UserRegisteredEvent {
    private String id;
    private String fullName;
    private String email;
    private String confirmationToken;
    private Locale emailLocale;
}
