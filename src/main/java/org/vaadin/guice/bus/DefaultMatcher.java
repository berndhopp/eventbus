package org.vaadin.guice.bus;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Binding;
import com.google.inject.matcher.AbstractMatcher;

import static java.util.Arrays.stream;

class DefaultMatcher extends AbstractMatcher<Binding<?>> {
    @Override
    public boolean matches(Binding<?> binding) {
        final Class<?> rawType = binding.getKey().getTypeLiteral().getRawType();

        return stream(rawType.getDeclaredMethods())
                .anyMatch(method -> method.isAnnotationPresent(Subscribe.class));
    }
}
