package com.javangarda.fantacalcio.user.application.gateway.command;

import lombok.Value;

@Value(staticConstructor = "of")
public class ConfirmEmailCommand {

    private String email;
}
