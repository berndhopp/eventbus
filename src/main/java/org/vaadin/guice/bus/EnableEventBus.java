package org.vaadin.guice.bus;

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
     * the default implementation of a GlobalEventBus can be overwritten here
     */
    Class<? extends GlobalEventBus> global() default GlobalEventBus.class;
}
