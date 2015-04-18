package com.wheels2spin.enduser;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.wheels2spin.enduser.parse.Address;
import com.wheels2spin.enduser.parse.Bike;
import com.wheels2spin.enduser.parse.Booking;
import com.wheels2spin.enduser.parse.Rent;

/**
 * Created by Abhinav Goel on 3/29/2015.
 */
public class Wheels2Spin extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Bike.class);
        ParseObject.registerSubclass(Rent.class);
        ParseObject.registerSubclass(Booking.class);
        ParseObject.registerSubclass(Address.class);
        // Required - Initialize the Parse SDK
        Parse.initialize(this, getString(R.string.parse_app_id),
                getString(R.string.parse_client_key));

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

    }
}
