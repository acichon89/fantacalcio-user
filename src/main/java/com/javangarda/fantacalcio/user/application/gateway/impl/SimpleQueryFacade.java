package com.javangarda.fantacalcio.user.application.gateway.impl;

import com.javangarda.fantacalcio.user.application.gateway.QueryFacade;
import com.javangarda.fantacalcio.user.application.internal.storage.dataprojection.DataProjectionRepository;
import com.javangarda.fantacalcio.user.application.internal.storage.dataprojection.UserVerificationDataProjection;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class SimpleQueryFacade implements QueryFacade {

    private DataProjectionRepository dataProjectionRepository;

    @Override
    public Optional<UserVerificationDataProjection> findByTmpEmailAndVerificationEmailToken(String tmpEmail, String verificationEmailToken) {
        return dataProjectionRepository.findByTmpEmailAndVerificationEmailToken(tmpEmail, verificationEmailToken);
    }

    @Override
    public Optional<UserVerificationDataProjection> findByEmailAndResetPasswordToken(String email, String resetPasswordToken) {
        return dataProjectionRepository.findByEmailAndResetPasswordToken(email, resetPasswordToken);
    }
}
