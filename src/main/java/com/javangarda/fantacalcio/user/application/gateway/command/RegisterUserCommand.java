package com.javangarda.fantacalcio.user.application.gateway.command;

import com.javangarda.fantacalcio.commons.validation.RepositoryFieldUnique;
import lombok.Value;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Value(staticConstructor = "of")
public class RegisterUserCommand {
    @NotBlank
    @Email
    @Size(min = 3, max = 50)
    @RepositoryFieldUnique(query = "SELECT COUNT(*) FROM users WHERE email = ?", message = "validation.email.alreadyregistered")
    private String email;
    @NotBlank
    @Size(min = 3, max = 50)
    private String fullName;
}
