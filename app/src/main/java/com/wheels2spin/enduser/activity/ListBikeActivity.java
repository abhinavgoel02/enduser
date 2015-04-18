package com.wheels2spin.enduser.activity;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.wheels2spin.enduser.R;
import com.wheels2spin.enduser.adapters.BikeAdapter;
import com.wheels2spin.enduser.parse.Bike;
import com.wheels2spin.enduser.parse.Booking;


public class ListBikeActivity extends ListActivity {

    private ParseQueryAdapter<Bike> bikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent reviewBookingIntent = new Intent(this, ReviewBookingActivity.class);
        bikeAdapter = new BikeAdapter(this);
        getListView().setClickable(false);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Bike selectedBike = bikeAdapter.getItem(position);
                ParseQuery<Booking> query = Booking.getQuery();
                query.fromLocalDatastore();
                query.getFirstInBackground(new GetCallback<Booking>() {
                    @Override
                    public void done(Booking booking, ParseException e) {
                        booking.setServiceOwner(selectedBike.getOwner());
                        booking.setBike(selectedBike);
                        booking.setState("pending_confirmation");
                        startActivity(reviewBookingIntent);
                    }
                });
            }
        });



        setListAdapter(bikeAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_pick_up_date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
