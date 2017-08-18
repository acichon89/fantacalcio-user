package com.javangarda.fantacalcio.user.infrastructure.adapter.income.http;


import com.javangarda.fantacalcio.commons.validation.RepositoryFieldUnique;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

public class RegisterRequestModel {
    @NotBlank
    @Email
    @Size(min = 3, max = 50)
    @RepositoryFieldUnique(query = "SELECT COUNT(*) FROM users WHERE email = ?", message = "validation.email.alreadyregistered")
    public String email;
    @NotBlank
    @Size(min = 3, max = 50)
    public String fullName;
}
