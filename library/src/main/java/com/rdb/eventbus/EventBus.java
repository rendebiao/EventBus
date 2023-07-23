package com.rdb.eventbus;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by DB on 2017/8/29.
 */

public class EventBus {

    private static EventBus eventBus;
    private EventRunnable mainRunnable = new EventRunnable(false);
    private EventRunnable threadRunnable = new EventRunnable(true);
    private Map<String, ArrayList<EventSubscriber>> subscriberMap = new ConcurrentHashMap<>();

    public static EventBus getDefault() {
        if (eventBus == null) {
            eventBus = new EventBus();
        }
        return eventBus;
    }

    public void register(String action, EventSubscriber subscriber) {
        if (!TextUtils.isEmpty(action) && subscriber != null) {
            unregister(subscriber);
            ArrayList<EventSubscriber> list = subscriberMap.get(action);
            if (list == null) {
                list = new ArrayList<>();
                subscriberMap.put(action, list);
            }
            list.add(subscriber);
        }
    }

    public void unregister(EventSubscriber subscriber) {
        Iterator<ArrayList<EventSubscriber>> iterator = subscriberMap.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().remove(subscriber);
        }
    }

    public void post(Event event) {
        if (event != null && !TextUtils.isEmpty(event.getAction())) {
            ArrayList<EventSubscriber> subscribers = subscriberMap.get(event.getAction());
            if (subscribers != null && subscribers.size() > 0) {
                for (EventSubscriber subscriber : subscribers) {
                    if (subscriber.isInThread()) {
                        threadRunnable.enqueue(event, subscriber);
                    } else {
                        mainRunnable.enqueue(event, subscriber);
                    }
                }
            }
        }
    }
}
