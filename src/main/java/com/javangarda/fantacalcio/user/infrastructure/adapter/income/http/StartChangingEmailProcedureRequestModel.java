package com.javangarda.fantacalcio.user.infrastructure.adapter.income.http;

import com.javangarda.fantacalcio.commons.validation.CurrentUser;
import com.javangarda.fantacalcio.commons.validation.RepositoryFieldUnique;
import org.hibernate.validator.constraints.Email;

public class StartChangingEmailProcedureRequestModel {

    @CurrentUser(message = "validation.changeemail.notcurrentuser")
    public String oldEmail;
    @RepositoryFieldUnique(query = "SELECT COUNT(*) FROM users WHERE email = ?", message = "validation.email.exists")
    @Email
    public String newEmail;
}
