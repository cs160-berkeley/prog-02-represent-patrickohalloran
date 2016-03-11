package com.patrickohalloran.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by Patrick O'Halloran
 */
public class WatchListenerService extends WearableListenerService {
    // In PhoneToWatchService, we passed in a path, either "/FRED" or "/LEXY"
    // These paths serve to differentiate different phone-to-watch messages
    private static final String DATA_FEED = "/CONGRESS_DATA";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
         Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());
        //use the 'path' field in sendmessage to differentiate use cases
        //(here, fred vs lexy)

        if( messageEvent.getPath().equalsIgnoreCase( DATA_FEED ) ) {
            String data = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, MainViewWatchActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //you need to add this flag since you're starting a new activity from a service
            intent.putExtra("CONGRESS_DATA", data);
            System.out.println(data);
            Log.d("T", "about to start watch MainActivity with CONGRESS_DATA: " + data);
            startActivity(intent);
        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}