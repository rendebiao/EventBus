package com.rdb.eventbus;

import android.os.Bundle;

/**
 * Created by DB on 2017/8/29.
 */

public class Event {

    private String action;
    private Bundle bundle;

    public Event(String action, Bundle bundle) {
        this.action = action;
        this.bundle = bundle;
    }

    public String getAction() {
        return action;
    }

    public Bundle getBundle() {
        return bundle;
    }
}
