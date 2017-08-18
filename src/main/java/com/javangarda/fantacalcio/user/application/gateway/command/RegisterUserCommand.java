package com.javangarda.fantacalcio.user.application.gateway.command;

import lombok.Value;

@Value(staticConstructor = "of")
public class RegisterUserCommand {
    private String email;
    private String fullName;
}
