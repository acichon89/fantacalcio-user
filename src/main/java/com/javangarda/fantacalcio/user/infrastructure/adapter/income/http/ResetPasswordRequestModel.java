package com.javangarda.fantacalcio.user.infrastructure.adapter.income.http;


import com.javangarda.fantacalcio.commons.validation.RepositoryFieldExists;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class ResetPasswordRequestModel {

    @NotBlank
    @Email
    @RepositoryFieldExists(query = "SELECT COUNT(id) FROM users WHERE email=?", message = "validation.email.notfound")
    public String email;
}
