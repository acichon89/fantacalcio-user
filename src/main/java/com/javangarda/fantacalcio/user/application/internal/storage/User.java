package com.javangarda.fantacalcio.user.application.internal.storage;

import com.javangarda.fantacalcio.commons.entities.VersionedDefaultEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Table(name = "users")
@ToString
public class User extends VersionedDefaultEntity <String> {

    @Getter
    private String email;
    @Getter
    private String tmpEmail;
    @Getter @Setter
    private String fullName;
    @Getter
    @Column(name="confirm_email_token")
    private String confirmEmailToken;
    @Getter
    @Setter
    private String resetPasswordToken;
    @Getter
    private Locale emailLocale;

    public User() {super();};

    public User(String id){
        super(id);
    }

    public void register(String fullName, String email, String confirmationToken, Locale emailLocale){
        this.fullName=fullName;
        this.emailLocale=emailLocale;
        assignEmailToBeConfirmed(email, confirmationToken);
    }

    public void confirmEmail() {
        this.email=this.tmpEmail;
        this.tmpEmail=null;
        this.confirmEmailToken=null;
    }

    public void assignEmailToBeConfirmed(String email, String confirmEmailToken){
        this.tmpEmail=email;
        this.confirmEmailToken=confirmEmailToken;
    }

    public boolean hasConfirmationEmailToken(String token){
        return this.confirmEmailToken == null ? token == null : this.confirmEmailToken.equals(token);
    }
}
