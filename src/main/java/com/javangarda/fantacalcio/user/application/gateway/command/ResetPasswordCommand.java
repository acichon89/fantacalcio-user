package com.javangarda.fantacalcio.user.application.gateway.command;

import com.javangarda.fantacalcio.commons.validation.RepositoryFieldExists;
import lombok.Value;
import org.hibernate.validator.constraints.NotBlank;

@Value(staticConstructor = "of")
public class ResetPasswordCommand {
    @NotBlank
    @RepositoryFieldExists(query = "SELECT COUNT(id) FROM users WHERE email = ?", message = "user.validation.email.notexists")
    private String email;
}
