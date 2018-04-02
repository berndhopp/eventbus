package org.vaadin.guice.bus;

import com.google.common.eventbus.EventBus;

import com.vaadin.guice.annotation.VaadinSessionScope;

@VaadinSessionScope
@SuppressWarnings("unused")
public final class SessionEventBus extends EventBus implements org.vaadin.guice.bus.EventBus {
    SessionEventBus(){
    }
}
