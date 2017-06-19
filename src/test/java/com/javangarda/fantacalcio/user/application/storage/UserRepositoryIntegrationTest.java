package com.javangarda.fantacalcio.user.application.storage;

import org.flywaydb.core.Flyway;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = Context.class)
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    private User user;

    @Before
    public void init(){
        user = new User("123");
        user.register("John Doe", "john@doe.com", "token", Locale.ENGLISH);
    }

    @After
    public void tearDown(){
        entityManager.remove(user);
        user = null;
    }

    @Test
    public void findByConfirmEmailTokenTest(){
        //given:
        entityManager.persist(user);
        //when:
        Optional<User> userOpt = userRepository.findByConfirmEmailToken("token");
        Optional<User> userOpt2 = userRepository.findByConfirmEmailToken("token2222222");
        //then:
        assertTrue(userOpt.isPresent());
        userOpt.ifPresent(u -> assertEquals("123", u.getId()));
        assertFalse(userOpt2.isPresent());
    }

    @Test
    public void countUserWithConfirmEmailTokenTest(){
        //given:
        entityManager.persist(user);
        //when:
        int count = userRepository.countUserWithConfirmEmailToken("token");
        int count2 = userRepository.countUserWithConfirmEmailToken("token2222222");
        //then:
        assertEquals(1, count);
        assertEquals(0, count2);
    }

    @Test
    public void countUserWithResetPasswordTokenTest(){
        //given:
        user.setResetPasswordToken("resetToken");
        entityManager.persist(user);
        //when:
        int count = userRepository.countUserWithResetPasswordToken("resetToken");
        int count2 = userRepository.countUserWithResetPasswordToken("token2222222");
        //then:
        assertEquals(1, count);
        assertEquals(0, count2);
    }
}
@Configuration
@EntityScan("com.javangarda.fantacalcio.user.application.storage")
class Context {
    @Bean
    public Flyway flyway(){
        return Mockito.mock(Flyway.class);
    }
}