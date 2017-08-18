package com.javangarda.fantacalcio.user.infrastructure.adapter.outcome.persistence;

import com.javangarda.fantacalcio.user.application.internal.storage.dataprojection.DataProjectionRepository;
import com.javangarda.fantacalcio.user.application.internal.storage.dataprojection.UserVerificationDataProjection;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Optional;

@AllArgsConstructor
public class JdbcDataProjectionRepository implements DataProjectionRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<UserVerificationDataProjection> findByTmpEmailAndVerificationEmailToken(String tmpEmail, String token) {
        String query = "SELECT tmp_email, email, confirm_email_token, reset_password_token, full_name FROM users WHERE tmp_email = ? and confirm_email_token = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, new Object[]{tmpEmail, token}, rowMapper()));
    }

    @Override
    public Optional<UserVerificationDataProjection> findByEmailAndResetPasswordToken(String email, String resetPasswordToken) {
        String query = "SELECT tmp_email, email, confirm_email_token, reset_password_token, full_name FROM users WHERE email = ? and reset_password_token = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(query, new Object[]{email, resetPasswordToken}, rowMapper()));
    }

    private RowMapper<UserVerificationDataProjection> rowMapper() {
        return (rs, rowNum) -> {
            String tmpEmailVal = rs.getString("tmp_email");
            String email = rs.getString("email");
            String confirmEmailToken = rs.getString("confirm_email_token");
            String resetPasswordToken = rs.getString("reset_password_token");
            String fullName = rs.getString("full_name");
            return UserVerificationDataProjection.of(Optional.ofNullable(email),
                    Optional.ofNullable(tmpEmailVal), Optional.ofNullable(confirmEmailToken), Optional.ofNullable(resetPasswordToken), fullName);
        };
    }
}
