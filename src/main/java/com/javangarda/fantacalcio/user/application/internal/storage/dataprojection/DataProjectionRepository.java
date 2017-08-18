package com.javangarda.fantacalcio.user.application.internal.storage.dataprojection;

import java.util.Optional;

public interface DataProjectionRepository {

    Optional<UserVerificationDataProjection> findByTmpEmailAndVerificationEmailToken(String tmpEmail, String token);

    Optional<UserVerificationDataProjection> findByEmailAndResetPasswordToken(String email, String resetPasswordToken);
}
