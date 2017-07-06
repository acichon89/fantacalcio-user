package com.javangarda.fantacalcio.user.application.internal.saga;

public interface UserEventPublisher {

    void publishUserRegistered(UserRegisteredEvent event);
    void publishUserAttemptedToChangeEmail(UserAttemptedToChangeEmailEvent event);
    void publishUserForgotPassword(UserForgotPasswordEvent event);
}
