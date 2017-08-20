package org.vaadin.guice.bus;

import com.google.common.eventbus.Subscribe;
import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.matcher.AbstractMatcher;

import com.vaadin.guice.annotation.UIScope;
import com.vaadin.guice.annotation.VaadinSessionScope;

import static java.util.Arrays.stream;

class EventBusModule extends AbstractModule {

    private final Provider<Injector> injectorProvider;
    private final Class<? extends GlobalEventBus> globalEventBusClass;

    @SuppressWarnings("unused")
    EventBusModule(Provider<Injector> injectorProvider, EnableEventBus annotation) {
        this.injectorProvider = injectorProvider;

        if (!GlobalEventBus.class.equals(annotation.global())) {
            this.globalEventBusClass = annotation.global();
        } else {
            this.globalEventBusClass = getDefaultImplementationClass();
        }
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
        bindListener(new Matcher(), new ProvisionListener());

        bind(GlobalEventBus.class).to(globalEventBusClass).in(Singleton.class);
        bind(SessionEventBus.class).to(SessionEventBusImpl.class).in(VaadinSessionScope.class);
        bind(UIEventBus.class).to(UIEventBusImpl.class).in(UIScope.class);
    }

    private static class Matcher extends AbstractMatcher<Binding<?>> {
        @Override
        public boolean matches(Binding<?> binding) {
            final Class<?> rawType = binding.getKey().getTypeLiteral().getRawType();

            return stream(rawType.getDeclaredMethods())
                    .anyMatch(method -> method.isAnnotationPresent(Subscribe.class));
        }
    }

    private class ProvisionListener implements com.google.inject.spi.ProvisionListener {
        @Override
        public <T> void onProvision(ProvisionInvocation<T> provisionInvocation) {
            final T t = provisionInvocation.provision();

            final Injector injector = injectorProvider.get();

            injector.getInstance(GlobalEventBus.class).register(t);
            injector.getInstance(SessionEventBus.class).register(t);
            injector.getInstance(UIEventBus.class).register(t);
        }
    }
}
