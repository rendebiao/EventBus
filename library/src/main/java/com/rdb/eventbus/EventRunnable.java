package com.rdb.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by DB on 2017/8/29.
 */

class EventRunnable implements Runnable {

    private final boolean inThread;
    private boolean running;
    private Handler mainHandler;
    private ExecutorService executor;
    private LinkedList<EventPost> eventPostQueue = new LinkedList<>();

    EventRunnable(boolean inThread) {
        this.inThread = inThread;
        if (inThread) {
            executor = Executors.newSingleThreadExecutor();
        } else {
            mainHandler = new Handler(Looper.getMainLooper());
        }
    }

    void enqueue(Event event, EventSubscriber subscriber) {
        synchronized (this) {
            eventPostQueue.add(EventPost.obtainPost(subscriber, event));
            if (!running) {
                running = true;
                if (inThread) {
                    executor.submit(this);
                } else {
                    mainHandler.post(this);
                }
            }
        }
    }

    @Override
    public void run() {
        long waitTime = 0;
        try {
            EventPost post;
            while (running) {
                synchronized (this) {
                    post = eventPostQueue.poll();
                }
                if (post == null) {
                    if (inThread) {
                        Thread.sleep(1000);
                        waitTime += 1000;
                        if (waitTime > 2000) {
                            running = false;
                        }
                    } else {
                        running = false;
                    }
                } else {
                    try {
                        post.subscriber.onEvent(post.event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        EventPost.releasePost(post);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            running = false;
        }
    }
}
