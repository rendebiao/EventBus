package com.rdb.eventbus.demo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.rdb.eventbus.Event;
import com.rdb.eventbus.EventBus;
import com.rdb.eventbus.EventSubscriber;

public class MainActivity extends AppCompatActivity {

    private EventSubscriber mainSubscriber = new EventSubscriber(false) {
        @Override
        protected void onEvent(Event event) {
            Log.e("MainActivity", "onEvent " + isInThread() + " " + event.getAction());
        }
    };

    private EventSubscriber threadSubscriber = new EventSubscriber(true) {
        @Override
        protected void onEvent(Event event) {
            Log.e("MainActivity", "onEvent " + isInThread() + " " + event.getAction());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View mainView = findViewById(R.id.mainView);
        View threadView = findViewById(R.id.threadView);
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Event("onClick main", null));
            }
        });
        threadView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new Event("onClick thread", null));
            }
        });
        EventBus.getDefault().register("onClick main", mainSubscriber);
        EventBus.getDefault().register("onClick thread", threadSubscriber);
    }

}
