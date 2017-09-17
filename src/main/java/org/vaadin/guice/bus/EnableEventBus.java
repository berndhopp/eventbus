package org.vaadin.guice.bus;

import com.google.inject.Binding;
import com.google.inject.matcher.Matcher;
import com.google.inject.spi.ProvisionListener;

import com.vaadin.guice.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * adding this annotation to a {@link com.vaadin.guice.server.GuiceVaadinServlet} will cause every
 * object that is created by guice and has at least one method with a {@link
 * com.google.common.eventbus.Subscribe}-annotation to be {@link com.google.common.eventbus.EventBus#register(Object)}ed
 * with the {@link GlobalEventBus}, {@link SessionEventBus} and {@link UIEventBus}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(EventBusModule.class)
public @interface EnableEventBus {

    /**
     * the default implementation of a GlobalEventBus can be overwritten here, if a distributed
     * eventbus is to be used. The default-implementation will take care of dispatching to the
     * correct thread and preventing memory-leaks that would occur if components are registered at
     * the bus but never unregistered
     *
     * @see com.vaadin.ui.UI#access(Runnable)
     */
    Class<? extends GlobalEventBus> globalEventBus() default GlobalEventBusImpl.class;

    /**
     * the implementation of a {@link SessionEventBus}. If not overwritten, it defaults to a
     * guava-{@link com.google.common.eventbus.EventBus}.
     */
    Class<? extends SessionEventBus> sessionEventBus() default SessionEventBusImpl.class;

    /**
     * the implementation of a {@link UIEventBus}. If not overwritten, it defaults to a guava-{@link
     * com.google.common.eventbus.EventBus}.
     */
    Class<? extends UIEventBus> uiEventBus() default UIEventBusImpl.class;

    /**
     * the implementation of a {@link ViewEventBus}. If not overwritten, it defaults to a guava-{@link
     * com.google.common.eventbus.EventBus}.
     */
    Class<? extends ViewEventBus> viewEventBus() default ViewEventBusImpl.class;

    /**
     * the {@link Matcher} for bindings where instances should be registered on the {@link
     * GlobalEventBus}
     *
     * @see com.google.inject.AbstractModule#bindListener(Matcher, ProvisionListener...)
     */
    Class<? extends Matcher<Binding<?>>> globalRegistrationMatcher() default GlobalMatcher.class;

    /**
     * the {@link Matcher} for bindings where instances should be registered on the {@link
     * SessionEventBus}
     *
     * @see com.google.inject.AbstractModule#bindListener(Matcher, ProvisionListener...)
     */
    Class<? extends Matcher<Binding<?>>> sessionRegistrationMatcher() default SessionMatcher.class;

    /**
     * the {@link Matcher} for bindings where instances should be registered on the {@link
     * UIEventBus}
     *
     * @see com.google.inject.AbstractModule#bindListener(Matcher, ProvisionListener...)
     */
    Class<? extends Matcher<Binding<?>>> uiRegistrationMatcher() default UIMatcher.class;

    /**
     * the {@link Matcher} for bindings where instances should be registered on the {@link
     * ViewEventBus}
     *
     * @see com.google.inject.AbstractModule#bindListener(Matcher, ProvisionListener...)
     */
    Class<? extends Matcher<Binding<?>>> viewRegistrationMatcher() default ViewMatcher.class;
}
