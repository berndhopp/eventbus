package org.vaadin.guice.bus.api;

import com.vaadin.server.VaadinSession;

import java.util.function.Predicate;

public interface GlobalEvent extends Event {

    default Predicate<VaadinSession> sessionPredicate(){
        return null;
    }

    default boolean cancelAfterDispatch(){
        return false;
    }
}
