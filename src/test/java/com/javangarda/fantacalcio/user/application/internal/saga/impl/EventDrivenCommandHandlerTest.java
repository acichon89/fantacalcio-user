package com.javangarda.fantacalcio.user.application.internal.saga.impl;

import com.javangarda.fantacalcio.user.application.gateway.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.gateway.data.UserDTO;
import com.javangarda.fantacalcio.user.application.internal.UserService;
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
        RegisterUserCommand registerUserCommand = RegisterUserCommand.of("john@doe.com", "John Doe");
        UserDTO userDTO = new UserDTO();
        userDTO.setId("vdfvdfvdf-v-sdvdf");
        userDTO.setFullName("John Doe");
        userDTO.setUnConfirmedEmail("john@doe.com");
        userDTO.setEmailLocale(Locale.ENGLISH);
        userDTO.setConfirmationToken("abbba");
        when(userService.saveUser(eq(registerUserCommand))).thenReturn(userDTO);
        //when:
        eventDrivenCommandHandler.handle(registerUserCommand);
        //then:
        verify(userService).saveUser(registerUserCommand);
        ArgumentCaptor<UserRegisteredEvent> argumentCaptorUserEvent = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(userEventPublisher).publishUserRegistered(argumentCaptorUserEvent.capture());
        UserRegisteredEvent userRegisteredEvent = argumentCaptorUserEvent.getValue();
        assertThat(userRegisteredEvent.getEmail()).isEqualTo("john@doe.com");
        assertThat(userRegisteredEvent.getConfirmationToken()).isEqualTo("abbba");
        assertThat(userRegisteredEvent.getEmailLocale()).isEqualTo(Locale.ENGLISH);
        assertThat(userRegisteredEvent.getFullName()).isEqualTo("John Doe");
    }
}