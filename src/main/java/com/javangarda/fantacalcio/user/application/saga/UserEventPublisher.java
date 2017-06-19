package com.javangarda.fantacalcio.user.application.saga;

import com.javangarda.fantacalcio.user.application.data.event.UserRegisteredEvent;

public interface UserEventPublisher {

    void publishUserRegistered(UserRegisteredEvent event);
}
