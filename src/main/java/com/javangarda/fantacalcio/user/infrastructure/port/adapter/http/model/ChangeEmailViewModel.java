package com.javangarda.fantacalcio.user.infrastructure.port.adapter.http.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class ChangeEmailViewModel {
    private boolean success;
    private String newEmail;

}
