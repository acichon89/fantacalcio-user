package com.javangarda.fantacalcio.user.application.gateway;

import com.javangarda.fantacalcio.user.application.internal.storage.dataprojection.UserVerificationDataProjection;

import java.util.Optional;

public interface QueryFacade {
    Optional<UserVerificationDataProjection> findByTmpEmailAndVerificationEmailToken(String tmpEmail, String verificationEmailToken);
    Optional<UserVerificationDataProjection> findByEmailAndResetPasswordToken(String email, String resetPasswordToken);
}
