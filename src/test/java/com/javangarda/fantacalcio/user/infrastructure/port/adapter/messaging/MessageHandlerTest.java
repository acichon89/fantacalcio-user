package com.javangarda.fantacalcio.user.infrastructure.port.adapter.messaging;

import com.javangarda.fantacalcio.user.application.gateway.CommandBus;
import com.javangarda.fantacalcio.user.application.gateway.command.ConfirmEmailCommand;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class MessageHandlerTest {

    private CommandBus commandBus;

    private MessageHandler messageHandler;

    @Before
    public void init() {
        commandBus = Mockito.mock(CommandBus.class);
        messageHandler = new MessageHandler(commandBus);
    }

    @Test
    public void should_handle_event_and_send_to_bus() {
        //given:
        ConfirmEmailCommand confirmEmailCommand = ConfirmEmailCommand.of("john@doe.com");
        //when:
        messageHandler.handleAccountCreatedEvent(confirmEmailCommand);
        //then:
        ArgumentCaptor<ConfirmEmailCommand> commandAC = ArgumentCaptor.forClass(ConfirmEmailCommand.class);
        Mockito.verify(commandBus).confirmUserEmail(commandAC.capture());
        assertEquals(commandAC.getValue().getEmail(), confirmEmailCommand.getEmail());
    }
}