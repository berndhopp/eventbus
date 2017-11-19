package org.vaadin.guice.bus;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provider;

import static com.google.common.base.Preconditions.checkNotNull;

class EventBusModule extends AbstractModule {

    private final Provider<Injector> injectorProvider;
    private final EnableEventBus annotation;

    @SuppressWarnings("unused, unchecked")
    EventBusModule(Provider<Injector> injectorProvider, EnableEventBus annotation) throws ClassNotFoundException {
        this.annotation = checkNotNull(annotation);
        this.injectorProvider = checkNotNull(injectorProvider);
    }

    @Override
    protected void configure() {
        try {
            bindListener(annotation.globalRegistrationMatcher().newInstance(), new Registrator(GlobalEventBus.class));
            bindListener(annotation.sessionRegistrationMatcher().newInstance(), new Registrator(SessionEventBus.class));
            bindListener(annotation.uiRegistrationMatcher().newInstance(), new Registrator(UIEventBus.class));
            bindListener(annotation.viewRegistrationMatcher().newInstance(), new Registrator(ViewEventBus.class));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        bind(GlobalEventBus.class).to(annotation.globalEventBus());
        bind(SessionEventBus.class).to(annotation.sessionEventBus());
        bind(UIEventBus.class).to(annotation.uiEventBus());
        bind(ViewEventBus.class).to(annotation.viewEventBus());
    }

    private class Registrator implements com.google.inject.spi.ProvisionListener {

        private final Class<? extends EventBus> busClass;

        private Registrator(Class<? extends EventBus> busClass) {
            this.busClass = busClass;
        }

        @Override
        public <T> void onProvision(ProvisionInvocation<T> provisionInvocation) {
            final T t = provisionInvocation.provision();

            final Injector injector = injectorProvider.get();

            injector.getInstance(busClass).register(t);
        }
    }
}
