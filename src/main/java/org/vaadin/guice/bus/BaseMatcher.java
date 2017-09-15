package org.vaadin.guice.bus;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Binding;
import com.google.inject.matcher.AbstractMatcher;

import org.vaadin.guice.bus.api.Event;

import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;

abstract class BaseMatcher extends AbstractMatcher<Binding<?>> {

    private final Class<? extends Event> eventClass;

    BaseMatcher(Class<? extends Event> eventClass) {
        this.eventClass = eventClass;
    }

    @Override
    public boolean matches(Binding<?> binding) {
        final Class<?> rawType = binding.getKey().getTypeLiteral().getRawType();

        for (Method method : rawType.getDeclaredMethods()) {
            if(method.isAnnotationPresent(Subscribe.class)){
                final Class<?>[] parameterTypes = method.getParameterTypes();

                checkArgument(parameterTypes.length == 1);

                final Class<?> parameterType = parameterTypes[0];

                if(eventClass.isAssignableFrom(parameterType)){
                    return true;
                }
            }
        }

        return false;
    }
}
