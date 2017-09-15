package org.vaadin.guice.bus;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import com.vaadin.guice.annotation.UIScope;
import com.vaadin.guice.annotation.VaadinSessionScope;
import com.vaadin.guice.annotation.ViewScope;

import static com.google.common.base.Preconditions.checkNotNull;

class EventBusModule extends AbstractModule {

    private final Class<? extends GlobalEventBus> defaultGlobalEventBusClass;
    private final Class<? extends SessionEventBus> defaultSessionEventBusClass;
    private final Class<? extends UIEventBus> defaultUIEventBusClass;
    private final Class<? extends ViewEventBus> defaultViewEventBusClass;

    private final Provider<Injector> injectorProvider;
    private final EnableEventBus annotation;

    @SuppressWarnings("unused, unchecked")
    EventBusModule(Provider<Injector> injectorProvider, EnableEventBus annotation) throws ClassNotFoundException {
        this.annotation = checkNotNull(annotation);
        this.injectorProvider = checkNotNull(injectorProvider);
        defaultGlobalEventBusClass = (Class<? extends GlobalEventBus>) Class.forName("com.google.common.eventbus.GlobalEventBusImpl");
        defaultSessionEventBusClass= (Class<? extends SessionEventBus>) Class.forName("com.google.common.eventbus.SessionEventBusImpl");
        defaultUIEventBusClass = (Class<? extends UIEventBus>) Class.forName("com.google.common.eventbus.UIEventBusImpl");
        defaultViewEventBusClass = (Class<? extends ViewEventBus>) Class.forName("com.google.common.eventbus.ViewEventBusImpl");
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

        Class<? extends GlobalEventBus> globalEventBusClass = GlobalEventBus.class.equals(annotation.globalEventBus()) ? defaultGlobalEventBusClass : annotation.globalEventBus();
        Class<? extends SessionEventBus> sessionEventBusClass = SessionEventBus.class.equals(annotation.sessionEventBus()) ? defaultSessionEventBusClass : annotation.sessionEventBus();
        Class<? extends UIEventBus> uiEventBusClass = UIEventBus.class.equals(annotation.uiEventBus()) ? defaultUIEventBusClass : annotation.uiEventBus();
        Class<? extends ViewEventBus> viewEventBusClass = ViewEventBus.class.equals(annotation.viewEventBus()) ? defaultViewEventBusClass : annotation.viewEventBus();

        bind(GlobalEventBus.class).to(globalEventBusClass).in(Singleton.class);
        bind(SessionEventBus.class).to(sessionEventBusClass).in(VaadinSessionScope.class);
        bind(UIEventBus.class).to(uiEventBusClass).in(UIScope.class);
        bind(ViewEventBus.class).to(viewEventBusClass).in(ViewScope.class);
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
