package com.javangarda.fantacalcio.user.application.data.dto;

import lombok.Data;

import java.util.Locale;

@Data
public class UserDTO {

    private String id;
    private String fullName;
    private String confirmedEmail;
    private String unConfirmedEmail;
    private String confirmationToken;
    private String resetPasswordToken;
    private Locale emailLocale;
}
