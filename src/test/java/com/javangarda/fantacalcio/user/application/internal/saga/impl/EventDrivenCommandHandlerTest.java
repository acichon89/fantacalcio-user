package com.javangarda.fantacalcio.user.application.internal.saga.impl;

import com.javangarda.fantacalcio.user.application.gateway.command.ChangeEmailCommand;
import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.UserService;
import com.javangarda.fantacalcio.user.application.internal.saga.UserAttemptedToChangeEmailEvent;
import com.javangarda.fantacalcio.user.application.internal.saga.UserEventPublisher;
import com.javangarda.fantacalcio.user.application.internal.saga.UserRegisteredEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class EventDrivenCommandHandlerTest {

    private UserService userService;
    private UserEventPublisher userEventPublisher;

    private EventDrivenCommandHandler eventDrivenCommandHandler;

    @Before
    public void init(){
        userService = mock(UserService.class);
        userEventPublisher = mock(UserEventPublisher.class);

        eventDrivenCommandHandler = new EventDrivenCommandHandler(userService, userEventPublisher);
    }

    @Test
    public void should_send_event_after_register() {
        //given:
        RegisterUserCommand registerUserCommand = RegisterUserCommand.of("johndoe@newdomain.com", "John Doe");
        UserDTO userDTO = dummyUser();
        when(userService.saveUser(eq(registerUserCommand))).thenReturn(userDTO);
        //when:
        eventDrivenCommandHandler.handle(registerUserCommand);
        //then:
        verify(userService).saveUser(registerUserCommand);
        ArgumentCaptor<UserRegisteredEvent> argumentCaptorUserEvent = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(userEventPublisher).publishUserRegistered(argumentCaptorUserEvent.capture());
        UserRegisteredEvent userRegisteredEvent = argumentCaptorUserEvent.getValue();
        assertThat(userRegisteredEvent.getEmail()).isEqualTo("johndoe@newdomain.com");
        assertThat(userRegisteredEvent.getConfirmationToken()).isEqualTo("abbba");
        assertThat(userRegisteredEvent.getEmailLocale()).isEqualTo(Locale.ENGLISH);
        assertThat(userRegisteredEvent.getFullName()).isEqualTo("John Doe");
    }

    private UserDTO dummyUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId("vdfvdfvdf-v-sdvdf");
        userDTO.setFullName("John Doe");
        userDTO.setConfirmedEmail("john@doe.com");
        userDTO.setUnConfirmedEmail("johndoe@newdomain.com");
        userDTO.setEmailLocale(Locale.ENGLISH);
        userDTO.setConfirmationToken("abbba");
        return userDTO;
    }

    @Test
    public void should_send_event_after_change_email() {
        //given:
        ChangeEmailCommand command = ChangeEmailCommand.of("john@doe.com", "johndoe@newdomain.com");
        when(userService.storeTmpEmail(eq(command))).thenReturn(dummyUser());
        //when:
        eventDrivenCommandHandler.handle(command);
        //then:
        ArgumentCaptor<UserAttemptedToChangeEmailEvent> eventAC = ArgumentCaptor.forClass(UserAttemptedToChangeEmailEvent.class);
        verify(userEventPublisher).publishUserAttemptedToChangeEmail(eventAC.capture());
        assertThat(eventAC.getValue().getId()).isEqualTo(dummyUser().getId());
        assertThat(eventAC.getValue().getNewEmail()).isEqualTo("johndoe@newdomain.com");
    }
}