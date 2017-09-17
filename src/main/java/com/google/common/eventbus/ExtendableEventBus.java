package com.google.common.eventbus;

import java.util.concurrent.Executor;

public class ExtendableEventBus extends EventBus {

    public ExtendableEventBus(String identifier, Executor executor, Dispatcher dispatcher) {
        super(identifier, executor, dispatcher, LoggingHandler.INSTANCE);
    }
}
