package com.wheels2spin.enduser.activity;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.wheels2spin.enduser.R;
import com.wheels2spin.enduser.parse.Address;
import com.wheels2spin.enduser.parse.Bike;
import com.wheels2spin.enduser.parse.Booking;

import org.w3c.dom.Text;

public class ReviewBookingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_booking);

        ParseQuery<Booking> query = Booking.getQuery();
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<Booking>() {
            @Override
            public void done(Booking booking, ParseException e) {
                Bike bike = booking.getBike();
                ParseUser owner = booking.getServiceOwner();
                owner.add("bookings", booking);


            }
        });


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ReviewBookingFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_review_booking, menu);
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

    public static class PendingBookingConfirmationFragment extends Fragment {
        public PendingBookingConfirmationFragment() {

        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_review_booking, container, false);

            return v;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ReviewBookingFragment extends Fragment {

        private ParseImageView bikeImageView;
        private TextView bikeNameField;
        private Button pickUpDateButton;
        private Button dropOffDateButton;
        private Button pickUpLocationButton;
        private TextView rentField;
        private TextView rentAmountField;
        private TextView convenienceFeeAmountField;
        private TextView totalAmountField;

        public ReviewBookingFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_review_booking, container, false);

            bikeImageView = (ParseImageView) v.findViewById(R.id.bike_image);
            bikeNameField = (TextView) v.findViewById(R.id.bike_name);
            pickUpDateButton = (Button) v.findViewById(R.id.pick_up_date);
            dropOffDateButton = (Button) v.findViewById(R.id.drop_off_date);
            pickUpLocationButton = (Button) v.findViewById(R.id.pick_up_location);
            rentField = (TextView) v.findViewById(R.id.rent);
            rentAmountField = (TextView) v.findViewById(R.id.rent_amount);
            convenienceFeeAmountField = (TextView) v.findViewById(R.id.convenience_fee_amount);
            totalAmountField = (TextView) v.findViewById(R.id.total_amount);



            return v;
        }
    }
}
