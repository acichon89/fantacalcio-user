package com.javangarda.fantacalcio.user.application.internal;


public interface AccessTokenGenerator {
    String createConfirmEmailToken();

    String createResetPasswordToken();
}
