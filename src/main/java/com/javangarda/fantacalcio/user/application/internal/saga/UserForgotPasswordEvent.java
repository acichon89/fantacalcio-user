package com.javangarda.fantacalcio.user.application.internal.saga;


import lombok.Value;

import java.util.Locale;

@Value(staticConstructor = "of")
public class UserForgotPasswordEvent {
    private String fullName;
    private String email;
    private String resetPasswordToken;
    private Locale emailLocale;
}
