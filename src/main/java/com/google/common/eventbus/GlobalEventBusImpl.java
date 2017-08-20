package com.google.common.eventbus;

import com.google.common.util.concurrent.MoreExecutors;

import com.vaadin.server.SessionDestroyEvent;
import com.vaadin.server.SessionDestroyListener;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import org.vaadin.guice.bus.GlobalEventBus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unused")
class GlobalEventBusImpl extends EventBus implements GlobalEventBus, SessionDestroyListener {

    private static final Dispatcher DISPATCHER = new Dispatcher() {
        void dispatch(final Object event, Iterator<Subscriber> subscribers) {
            while (subscribers.hasNext()) {
                final Subscriber subscriber = subscribers.next();
                if (subscriber.target instanceof Component) {
                    UI ui = ((Component) subscriber.target).getUI();
                    if (ui != null) {
                        ui.access(() -> subscriber.dispatchEvent(event));
                    } else {
                        subscriber.dispatchEvent(event);
                    }
                } else {
                    subscriber.dispatchEvent(event);
                }
            }

        }
    };

    private final Map<VaadinSession, Set<Object>> registeredObjectsBySession = new HashMap<>();

    GlobalEventBusImpl() {
        super("default", MoreExecutors.directExecutor(), DISPATCHER, LoggingHandler.INSTANCE);
        VaadinService.getCurrent().addSessionDestroyListener(this);
    }

    @Override
    public void register(Object object) {
        checkNotNull(object);

        VaadinSession vaadinSession = checkNotNull(VaadinSession.getCurrent());

        registeredObjectsBySession
                .computeIfAbsent(vaadinSession, s -> new HashSet<>())
                .add(object);

        super.register(object);
    }

    @Override
    public void unregister(Object object) {
        checkNotNull(object);

        VaadinSession vaadinSession = checkNotNull(VaadinSession.getCurrent());

        Set<Object> objects = registeredObjectsBySession.get(vaadinSession);

        if (objects != null) {
            objects.remove(object);
        }

        super.unregister(object);
    }

    @Override
    public void sessionDestroy(SessionDestroyEvent e) {
        Set<Object> registeredObjects = registeredObjectsBySession.remove(e.getSession());

        if (registeredObjects != null) {
            registeredObjects.forEach(super::unregister);
        }
    }
}
