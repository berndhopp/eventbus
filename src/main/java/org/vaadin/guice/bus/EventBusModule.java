package org.vaadin.guice.bus;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.vaadin.guice.annotation.UIScope;
import com.vaadin.guice.annotation.VaadinSessionScope;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventBusModule extends AbstractModule {

    private final Provider<Injector> injectorProvider;

    @SuppressWarnings("unused, unchecked")
    public EventBusModule(Provider<Injector> injectorProvider) {
        this.injectorProvider = checkNotNull(injectorProvider);
    }

    @Override
    protected void configure() {
        bindListener(new GlobalMatcher(), new Registrator(GlobalEventBus.class));
        bindListener(new SessionMatcher(), new Registrator(SessionEventBus.class));
        bindListener(new UIMatcher(), new Registrator(UIEventBus.class));

        bind(GlobalEventBus.class).to(GlobalEventBusImpl.class).in(Singleton.class);
        bind(SessionEventBus.class).to(SessionEventBusImpl.class).in(VaadinSessionScope.class);
        bind(UIEventBus.class).to(UIEventBusImpl.class).in(UIScope.class);
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
