package com.javangarda.fantacalcio.user.application.internal.saga.impl;


import com.javangarda.fantacalcio.user.application.gateway.command.ConfirmEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.ResetPasswordCommand;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.saga.*;
import com.javangarda.fantacalcio.user.application.gateway.command.ChangeEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.internal.UserService;
import lombok.AllArgsConstructor;
import org.springframework.integration.annotation.ServiceActivator;

@AllArgsConstructor
public class EventDrivenCommandHandler implements CommandHandler {

    private final UserService userService;
    private final UserEventPublisher userEventPublisher;

    @Override
    @ServiceActivator(inputChannel = "changeEmailCommandChannel")
    public void handle(ChangeEmailCommand command) {
        UserDTO user = userService.storeTmpEmail(command);
        UserAttemptedToChangeEmailEvent event = UserAttemptedToChangeEmailEvent.of(
                user.getId(), user.getConfirmedEmail(), user.getUnConfirmedEmail(), user.getConfirmationToken(), user.getEmailLocale()
        );
        userEventPublisher.publishUserAttemptedToChangeEmail(event);
    }

    @Override
    @ServiceActivator(inputChannel = "registerCommandChannel")
    public void handle(RegisterUserCommand command) {
        UserDTO storedUser = userService.saveUser(command);
        UserRegisteredEvent event = UserRegisteredEvent.of(storedUser.getId(), storedUser.getFullName(),storedUser.getUnConfirmedEmail(),
                storedUser.getConfirmationToken(), storedUser.getEmailLocale());
        userEventPublisher.publishUserRegistered(event);
    }

    @Override
    @ServiceActivator(inputChannel = "confirmUserCommandChannel")
    public void handle(ConfirmEmailCommand command) {
        userService.confirmUserEmail(command.getEmail());
    }

    @Override
    public void handle(ResetPasswordCommand command) {
        UserDTO userDTO = userService.assignResetPasswordToken(command.getEmail());
        UserForgotPasswordEvent event = UserForgotPasswordEvent.of(userDTO.getFullName(), userDTO.getConfirmedEmail(), userDTO.getResetPasswordToken(), userDTO.getEmailLocale());
        userEventPublisher.publishUserForgotPassword(event);
    }
}
