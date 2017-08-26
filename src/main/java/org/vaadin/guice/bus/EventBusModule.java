package org.vaadin.guice.bus;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.vaadin.guice.annotation.UIScope;
import com.vaadin.guice.annotation.VaadinSessionScope;

import static com.google.common.base.Preconditions.checkNotNull;

class EventBusModule extends AbstractModule {

    private final Provider<Injector> injectorProvider;
    private final EnableEventBus annotation;

    @SuppressWarnings("unused")
    EventBusModule(Provider<Injector> injectorProvider, EnableEventBus annotation) {
        this.annotation = checkNotNull(annotation);
        this.injectorProvider = checkNotNull(injectorProvider);
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends GlobalEventBus> getDefaultImplementationClass() {
        try {
            return (Class<? extends GlobalEventBus>) Class.forName("com.google.common.eventbus.GlobalEventBusImpl");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void configure() {

        if (annotation.enableAutoRegistration()) {
            try {
                bindListener(annotation.globalRegistrationMatcher().newInstance(), new ProvisionListener(GlobalEventBus.class));
                bindListener(annotation.sessionRegistrationMatcher().newInstance(), new ProvisionListener(SessionEventBus.class));
                bindListener(annotation.uiRegistrationMatcher().newInstance(), new ProvisionListener(UIEventBus.class));
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        Class<? extends GlobalEventBus> globalEventBusClass;

        if (!GlobalEventBus.class.equals(annotation.globalEventBus())) {
            globalEventBusClass = annotation.globalEventBus();
        } else {
            globalEventBusClass = getDefaultImplementationClass();
        }

        bind(GlobalEventBus.class).to(globalEventBusClass).in(Singleton.class);

        bind(SessionEventBus.class).to(annotation.sessionEventBus()).in(VaadinSessionScope.class);

        bind(UIEventBus.class).to(annotation.uiEventBus()).in(UIScope.class);
    }

    private class ProvisionListener implements com.google.inject.spi.ProvisionListener {

        private final Class<? extends EventBus> busClass;

        private ProvisionListener(Class<? extends EventBus> busClass) {
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
