package org.vaadin.guice.bus;

import com.google.common.eventbus.EventBus;

import com.vaadin.guice.annotation.UIScope;

@UIScope
@SuppressWarnings("unused")
public final class UIEventBus extends EventBus implements org.vaadin.guice.bus.EventBus {

    UIEventBus(){
    }
}
