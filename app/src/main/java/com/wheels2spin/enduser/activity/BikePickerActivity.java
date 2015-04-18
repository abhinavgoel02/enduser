package com.wheels2spin.enduser.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.wheels2spin.enduser.R;
import com.wheels2spin.enduser.fragments.AutoCompleteListView;
import com.wheels2spin.enduser.parse.Address;

import java.util.ArrayList;
import java.util.List;

public class BikePickerActivity extends Activity implements AutoCompleteListView.AutoCompleteListViewEventListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseQuery<ParseObject> bikeParseQuery = ParseQuery.getQuery("Bike");
        bikeParseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> bikes, ParseException e) {

                ArrayList<String> bikeList = new ArrayList<String>();
                for (ParseObject bike: bikes) {
                    bikeList.add(bike.getString("name"));
                }

                String [] bike = new String[bikeList.size()];
                if (savedInstanceState == null) {
                    Fragment fragment = AutoCompleteListView.newInstance(getString(R.string.enter_bike_model), bikeList.toArray(bike));
                    getFragmentManager().beginTransaction()
                            .add(R.id.bike_picker, fragment)
                            .commit();
                }
            }
        });

        setContentView(R.layout.activity_bike_picker);
    }

    @Override
    public void onDone(ArrayList<String> selectedBikes) {
        Intent data = new Intent();
        data.putStringArrayListExtra(getString(R.string.selected_bikes), selectedBikes);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bike_picker, menu);
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
