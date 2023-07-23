package com.rdb.eventbus;

/**
 * Created by DB on 2017/8/29.
 */

public abstract class EventSubscriber {

    private boolean inThread;

    public EventSubscriber(boolean inThread) {
        this.inThread = inThread;
    }

    protected abstract void onEvent(Event event);

    public final boolean isInThread() {
        return inThread;
    }
}
