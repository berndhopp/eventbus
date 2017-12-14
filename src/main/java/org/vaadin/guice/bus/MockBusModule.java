package org.vaadin.guice.bus;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class MockBusModule extends AbstractModule{
    @Override
    protected void configure() {
        bind(GlobalEventBus.class).to(GlobalEventBusImpl.class).in(Singleton.class);
        bind(UIEventBus.class).to(UIEventBusImpl.class).in(Singleton.class);
        bind(SessionEventBus.class).to(SessionEventBusImpl.class).in(Singleton.class);
    }
}
