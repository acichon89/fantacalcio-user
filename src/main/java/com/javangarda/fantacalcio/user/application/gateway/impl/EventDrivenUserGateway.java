package com.javangarda.fantacalcio.user.application.gateway.impl;

import com.javangarda.fantacalcio.user.application.data.event.UserRegisteredEvent;
import com.javangarda.fantacalcio.user.application.data.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.data.dto.UserDTO;
import com.javangarda.fantacalcio.user.application.gateway.UserGateway;
import com.javangarda.fantacalcio.user.application.internal.UserService;
import com.javangarda.fantacalcio.user.application.saga.UserEventPublisher;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@AllArgsConstructor
@Slf4j
public class EventDrivenUserGateway implements UserGateway {

    private final UserService userService;
    private final UserEventPublisher userEventPublisher;

    @Override
    public void registerUser(RegisterUserCommand registerUserCommand) {
        UserDTO storedUser = userService.saveUser(registerUserCommand);
        UserRegisteredEvent event = create(storedUser);
        userEventPublisher.publishUserRegistered(event);
    }

    @Override
    public void confirmUserEmail(String email) {
        userService.confirmUserEmail(email);
    }

    @Override
    public Optional<UserDTO> getByConfirmationToken(String confirmationToken) {
        return userService.getByConfirmationToken(confirmationToken);
    }

    private UserRegisteredEvent create(UserDTO storedUser) {
        return UserRegisteredEvent.of(storedUser.getId(), storedUser.getFullName(),storedUser.getUnConfirmedEmail(),
                storedUser.getConfirmationToken(), storedUser.getEmailLocale());
    }

}
