package com.javangarda.fantacalcio.user.application.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends Repository<User, String> {

    @Query("SELECT COUNT(u) FROM User u WHERE u.confirmEmailToken=:token")
    int countUserWithConfirmEmailToken(@Param("token") String token);

    @Query("SELECT COUNT(u) FROM User u WHERE u.resetPasswordToken=:token")
    int countUserWithResetPasswordToken(@Param("token") String token);

    Optional<User> findOne(String id);

    User save(User u);

    @Query("SELECT u FROM User u WHERE u.confirmEmailToken =:token")
    Optional<User> findByConfirmEmailToken(@Param("token") String token);

    @Query("SELECT u FROM User u WHERE u.tmpEmail =:email")
    Optional<User> findByTmpEmail(String email);
}
