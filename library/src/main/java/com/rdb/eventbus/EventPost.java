package com.rdb.eventbus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DB on 2017/8/29.
 */

class EventPost {

    private final static List<EventPost> postPool = new ArrayList<>();
    Event event;
    EventSubscriber subscriber;

    private EventPost(Event event, EventSubscriber subscriber) {
        this.event = event;
        this.subscriber = subscriber;
    }

    static EventPost obtainPost(EventSubscriber subscriber, Event event) {
        synchronized (postPool) {
            int size = postPool.size();
            if (size > 0) {
                EventPost post = postPool.remove(0);
                post.event = event;
                post.subscriber = subscriber;
                return post;
            }
        }
        return new EventPost(event, subscriber);
    }

    static void releasePost(EventPost post) {
        post.event = null;
        post.subscriber = null;
        synchronized (postPool) {
            if (postPool.size() < 10000) {
                postPool.add(post);
            }
        }
    }
}
