package com.wheels2spin.enduser.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wheels2spin.enduser.R;
import com.wheels2spin.enduser.enums.Duration;
import com.wheels2spin.enduser.fragments.AdditionalOptionsFragment;
import com.wheels2spin.enduser.fragments.CalendarFragment;
import com.wheels2spin.enduser.fragments.RentDurationFragment;
import com.wheels2spin.enduser.parse.Booking;
import com.wheels2spin.enduser.utils.DateFormatter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DatePickerActivity extends Activity implements
        CalendarFragment.CalendarEventListener,
        RentDurationFragment.RentDurationEventListener,
        AdditionalOptionsFragment.AddtionalOptionEventListener {

    private static final String TAG = "DatePickerActivity";
    private ActionBar actionBar;
    private String action;
    private Booking booking;
    private GregorianCalendar pickUpDate;
    private GregorianCalendar dropOffDate;
    private static final int SELECT_LOCATION_REQUEST_CODE = 1;
    private static final int SELECT_BIKE_MODEL_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        TextView actionHintTextView = (TextView) findViewById(R.id.action_hint);
        action = getString(R.string.select_pick_up_date);
        actionHintTextView.setText(action);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Fragment fragment = CalendarFragment.newInstance(Calendar.getInstance().getTimeInMillis());
            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date_picker, menu);
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

    private void setPickUpDate(int year, int month, int dayOfMonth) {
        pickUpDate = new GregorianCalendar(year, month, dayOfMonth);
        dropOffDate = pickUpDate;
    }

    private void setDropOffDate(int year, int month, int dayOfMonth) {
        dropOffDate = new GregorianCalendar(year, month, dayOfMonth);
    }

    private Date getDate(int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        return c.getTime();
    }

    public void onCalendarDateSet(int year, int month, int dayOfMonth) {
        if (action.equals(getString(R.string.select_pick_up_date))) {
            setPickUpDate(year, month, dayOfMonth);
            Button pickUpDateButton = (Button) findViewById(R.id.pick_up_date);
            String pickUpDateText = getString(R.string.pick_up_date);
            String formattedPickUpDate = DateFormatter.getFormattedDate(year, month, dayOfMonth);
            pickUpDateButton.setText(pickUpDateText + formattedPickUpDate);
            pickUpDateButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);

            action = getString(R.string.duration_of_rent);
            TextView actionHintTextView = (TextView) findViewById(R.id.action_hint);
            actionHintTextView.setText(action);

            // set start date in the booking object
            booking = new Booking();
            booking.setStartDate(getDate(year, month, dayOfMonth));

            FragmentManager manager = getFragmentManager();
            Fragment fragment = RentDurationFragment.newInstance();
            manager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        else if (action.equals(getString(R.string.select_drop_off_date))) {
            setDropOffDate(year, month, dayOfMonth);

            Button dropOffDateButton = (Button) findViewById(R.id.drop_off_date);
            String dropOffDateText = getString(R.string.drop_off_date);
            String formattedDropOffDate = DateFormatter.getFormattedDate(dropOffDate);
            dropOffDateButton.setText(dropOffDateText + formattedDropOffDate);
            dropOffDateButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);

            // set end date in the booking object
            booking.setEndDate(getDate(year, month, dayOfMonth));
            booking.pinInBackground("booking");

            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, AdditionalOptionsFragment.newInstance())
                    .addToBackStack(null)
                    .commit();

        }
    }

    public void onDurationSelected(Duration duration) {
        Button dropOffDateButton = (Button) findViewById(R.id.drop_off_date);
        String dropOffDateText = getString(R.string.drop_off_date);
        dropOffDate = pickUpDate;
        switch (duration) {
            case ONE_DAY: dropOffDate.add(GregorianCalendar.DAY_OF_MONTH, 1);
                break;
            case ONE_WEEK: dropOffDate.add(GregorianCalendar.WEEK_OF_MONTH, 1);
                break;
            case ONE_MONTH: dropOffDate.add(GregorianCalendar.MONTH, 1);
                break;
            default:
        }

        // set end date in the booking object
        booking.setEndDate(dropOffDate.getTime());
        booking.pinInBackground("booking");

        String formattedDropOffDate = DateFormatter.getFormattedDate(dropOffDate);
        dropOffDateButton.setText(dropOffDateText + formattedDropOffDate);
        dropOffDateButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.calendar, 0, 0, 0);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, AdditionalOptionsFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    public void onSelectDropOffDate() {
        action = getString(R.string.select_drop_off_date);
        FragmentManager manager = getFragmentManager();
        Fragment fragment = CalendarFragment.newInstance(pickUpDate.getTimeInMillis());
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void onSelectLocation() {
        Intent selectLocation = new Intent(this, LocationPickerActivity.class);
        startActivityForResult(selectLocation, SELECT_LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SELECT_LOCATION_REQUEST_CODE:
                break;
            case SELECT_BIKE_MODEL_REQUEST_CODE:
                break;
        }
    }

    public void onSelectBikeModel() {

    }

    public void onSearch() {

    }
}
