package org.vaadin.guice.bus;

import com.google.inject.AbstractModule;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.spi.ProvisionListener;

import org.reflections.Reflections;

import java.util.Set;

class EventBusModule extends AbstractModule {

    private final Reflections reflections;
    private final Provider<Injector> injectorProvider;

    EventBusModule(Reflections reflections, Provider<Injector> injectorProvider){
        this.reflections = reflections;
        this.injectorProvider = injectorProvider;
    }

    @Override
    protected void configure() {
        final Set<Class<?>> typesToRegister = reflections.getTypesAnnotatedWith(RegisterOn.class);

        typesToRegister.forEach(typeToRegister -> {
            final Class<? extends EventBus> value = typeToRegister.getAnnotation(RegisterOn.class).value();

            bindListener(new AbstractMatcher<Binding<?>>() {
                @Override
                public boolean matches(Binding<?> binding) {
                    return typeToRegister.equals(binding.getKey().getTypeLiteral().getRawType());
                }
            }, new ProvisionListener() {
                @Override
                public <T> void onProvision(ProvisionInvocation<T> provisionInvocation) {
                    injectorProvider.get().getInstance(value).register(provisionInvocation.provision());
                }
            });
        });
    }
}
