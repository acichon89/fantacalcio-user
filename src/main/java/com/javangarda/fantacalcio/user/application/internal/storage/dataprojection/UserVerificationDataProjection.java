package com.javangarda.fantacalcio.user.application.internal.storage.dataprojection;

import lombok.Value;

import java.util.Optional;

@Value(staticConstructor = "of")
public class UserVerificationDataProjection {
    private Optional<String> email;
    private Optional<String> tmpEmail;
    private Optional<String> emailVerificationToken;
    private Optional<String> resetPasswordToken;
    private String fullName;
}
