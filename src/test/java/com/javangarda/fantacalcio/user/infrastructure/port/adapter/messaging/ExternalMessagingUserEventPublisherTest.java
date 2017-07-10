package com.javangarda.fantacalcio.user.infrastructure.port.adapter.messaging;

import com.javangarda.fantacalcio.user.application.internal.saga.UserAttemptedToChangeEmailEvent;
import com.javangarda.fantacalcio.user.application.internal.saga.UserForgotPasswordEvent;
import com.javangarda.fantacalcio.user.application.internal.saga.UserRegisteredEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.util.Locale;

import static org.junit.Assert.*;


public class ExternalMessagingUserEventPublisherTest {

    private Events events;

    private ExternalMessagingUserEventPublisher externalMessagingUserEventPublisher;

    @Before
    public void init(){
        events = Mockito.mock(Events.class);
        externalMessagingUserEventPublisher = new ExternalMessagingUserEventPublisher(events);
    }

    @Test
    public void should_send_event_register(){
        //given:
        MessageChannel fakeChannel = Mockito.mock(MessageChannel.class);
        Mockito.when(events.activationMailChannel()).thenReturn(fakeChannel);
        UserRegisteredEvent event = UserRegisteredEvent.of(
                "123",
                "john doe",
                "john@doe.com",
                "token-123",
                Locale.ENGLISH
        );
        //when:
        externalMessagingUserEventPublisher.publishUserRegistered(event);
        //then:
        Mockito.verify(events).activationMailChannel();
        Class<Message<UserRegisteredEvent>> clazz = (Class<Message<UserRegisteredEvent>>)(Class)Message.class;
        ArgumentCaptor<Message<UserRegisteredEvent>> messageAC = ArgumentCaptor.forClass(clazz);
        Mockito.verify(fakeChannel).send(messageAC.capture());
        assertEquals(messageAC.getValue().getPayload().getEmail(), "john@doe.com");
        assertEquals(messageAC.getValue().getPayload().getId(), "123");
    }

    @Test
    public void should_send_event_atempt_to_change_email(){
        //given:
        MessageChannel fakeChannel = Mockito.mock(MessageChannel.class);
        Mockito.when(events.changeEmailChannel()).thenReturn(fakeChannel);
        UserAttemptedToChangeEmailEvent event = UserAttemptedToChangeEmailEvent.of(
                "124",
                "john@doe.com",
                "johndoe@newdomain.com",
                "token222",
                Locale.ENGLISH
        );
        //when:
        externalMessagingUserEventPublisher.publishUserAttemptedToChangeEmail(event);
        //then:
        Mockito.verify(events).changeEmailChannel();
        Class<Message<UserAttemptedToChangeEmailEvent>> clazz = (Class<Message<UserAttemptedToChangeEmailEvent>>)(Class)Message.class;
        ArgumentCaptor<Message<UserAttemptedToChangeEmailEvent>> messageAC = ArgumentCaptor.forClass(clazz);
        Mockito.verify(fakeChannel).send(messageAC.capture());
        assertEquals(messageAC.getValue().getPayload().getNewEmail(), "johndoe@newdomain.com");
        assertEquals(messageAC.getValue().getPayload().getConfirmationToken(), "token222");
    }

    @Test
    public void should_send_event_user_forgot_password(){
        //given:
        MessageChannel fakeChannel = Mockito.mock(MessageChannel.class);
        Mockito.when(events.userResetPasswordChannel()).thenReturn(fakeChannel);
        UserForgotPasswordEvent event = UserForgotPasswordEvent.of(
                "John Smith",
                "john@smith.com",
                "rest-token-123",
                Locale.ENGLISH
        );
        //when:
        externalMessagingUserEventPublisher.publishUserForgotPassword(event);
        //then:
        Mockito.verify(events).userResetPasswordChannel();
        Class<Message<UserForgotPasswordEvent>> clazz = (Class<Message<UserForgotPasswordEvent>>)(Class)Message.class;
        ArgumentCaptor<Message<UserForgotPasswordEvent>> messageAC = ArgumentCaptor.forClass(clazz);
        Mockito.verify(fakeChannel).send(messageAC.capture());
        assertEquals(messageAC.getValue().getPayload().getFullName(), "John Smith");
        assertEquals(messageAC.getValue().getPayload().getResetPasswordToken(), "rest-token-123");
    }
}