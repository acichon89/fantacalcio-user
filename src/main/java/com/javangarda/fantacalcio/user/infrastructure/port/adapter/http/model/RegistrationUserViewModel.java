package com.javangarda.fantacalcio.user.infrastructure.port.adapter.http.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class RegistrationUserViewModel {
    private boolean success;
    private String email;

}
