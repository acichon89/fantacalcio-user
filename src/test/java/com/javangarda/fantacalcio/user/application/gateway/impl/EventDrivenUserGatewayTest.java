package com.javangarda.fantacalcio.user.application.gateway.impl;

import com.javangarda.fantacalcio.user.application.data.command.RegisterUserCommand;
import com.javangarda.fantacalcio.user.application.data.dto.UserDTO;
import com.javangarda.fantacalcio.user.application.data.event.UserRegisteredEvent;
import com.javangarda.fantacalcio.user.application.internal.UserService;
import com.javangarda.fantacalcio.user.application.saga.UserEventPublisher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Locale;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class EventDrivenUserGatewayTest {

    private UserService userService;
    private UserEventPublisher userEventPublisher;

    private EventDrivenUserGateway eventDrivenUserGateway;

    @Before
    public void init(){
        userService = mock(UserService.class);
        userEventPublisher = mock(UserEventPublisher.class);

        eventDrivenUserGateway = new EventDrivenUserGateway(userService, userEventPublisher);
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
        eventDrivenUserGateway.registerUser(registerUserCommand);
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

    @Test
    public void should_delegate_to_service_while_confirming(){
        //given:
        String email = "john@doe.com";
        //when:
        eventDrivenUserGateway.confirmUserEmail(email);
        //then:
        verify(userService).confirmUserEmail(email);
    }

    @Test
    public void should_return_from_service(){
        //given:
        String token = "abc";
        when(userService.getByConfirmationToken(eq(token))).thenReturn(Optional.empty());
        //when:
        Optional<UserDTO> userValue = eventDrivenUserGateway.getByConfirmationToken(token);
        //then:
        assertThat(userValue).isEmpty();
    }
}