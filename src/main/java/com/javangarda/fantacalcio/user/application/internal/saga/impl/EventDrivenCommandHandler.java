package com.javangarda.fantacalcio.user.application.internal.saga.impl;


import com.javangarda.fantacalcio.user.application.gateway.command.*;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.saga.*;
import com.javangarda.fantacalcio.user.application.internal.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.handler.annotation.Payload;

@AllArgsConstructor
@Slf4j
public class EventDrivenCommandHandler implements CommandHandler {

    private final UserService userService;
    private final UserEventPublisher userEventPublisher;

    @Override
    @ServiceActivator(inputChannel = "registerCommandChannel")
    public void handle(RegisterUserCommand command) {
        UserDTO storedUser = userService.saveUser(command);
        UserRegisteredEvent event = UserRegisteredEvent.of(storedUser.getFullName(),storedUser.getUnConfirmedEmail(),
                storedUser.getConfirmationToken(), storedUser.getEmailLocale());
        userEventPublisher.publishUserRegistered(event);
    }

    @Override
    @ServiceActivator(inputChannel = "startChangingEmailProcedureCommandChannel")
    public void handle(StartChangingEmailProcedureCommand command) {
        UserDTO user = userService.storeTmpEmail(command);
        UserAttemptedToChangeEmailEvent event = UserAttemptedToChangeEmailEvent.of(
                user.getId(), user.getConfirmedEmail(), user.getUnConfirmedEmail(), user.getConfirmationToken(), user.getEmailLocale()
        );
        userEventPublisher.publishUserAttemptedToChangeEmail(event);
    }

    @Override
    @ServiceActivator(inputChannel = "confirmEmailCommandChannel")
    public void handle(ConfirmEmailCommand command) {
        userService.confirmUserEmail(command.getConfirmationToken(), command.getEmail());
    }

    @Override
    @ServiceActivator(inputChannel = "startResettingPasswordProcedureCommandChannel")
    public void handle(StartResettingPasswordProcedureCommand command) {
        UserDTO userDTO = userService.assignResetPasswordToken(command.getEmail());
        UserForgotPasswordEvent event = UserForgotPasswordEvent.of(userDTO.getFullName(), userDTO.getConfirmedEmail(), userDTO.getResetPasswordToken(), userDTO.getEmailLocale());
        userEventPublisher.publishUserForgotPassword(event);
    }

    @Override
    @ServiceActivator(inputChannel = "resetPasswordCommandChannel")
    public void handle(ResetPasswordCommand command) {
        if(command.isClearResetPasswordToken()){
            userService.removeResetPasswordToken(command.getEmail());
        }
    }
}
