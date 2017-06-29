package com.javangarda.fantacalcio.user.application.gateway.command;

import com.javangarda.fantacalcio.commons.validation.RepositoryFieldUnique;
import lombok.Value;
import org.hibernate.validator.constraints.Email;

@Value(staticConstructor = "of")
public class ChangeEmailCommand {

    private String oldEmail;
    @RepositoryFieldUnique(query = "SELECT COUNT(*) FROM users WHERE email = ?", message = "validation.email.exists")
    @Email
    private String newEmail;
}
