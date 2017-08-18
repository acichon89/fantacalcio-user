package com.javangarda.fantacalcio.user.application.internal.storage;

import com.javangarda.fantacalcio.user.application.internal.storage.model.User;
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

    @Query("SELECT u FROM User u WHERE u.confirmEmailToken =:token and u.tmpEmail=:email")
    Optional<User> findByTmpEmailAndToken(@Param("token") String token, @Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.tmpEmail =:email")
    Optional<User> findByTmpEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.email =:email")
    Optional<User> findByEmail(@Param("email") String email);
}
