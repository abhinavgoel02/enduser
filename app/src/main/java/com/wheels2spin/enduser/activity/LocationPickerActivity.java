package com.wheels2spin.enduser.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationServices;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.wheels2spin.enduser.R;
import com.wheels2spin.enduser.fragments.AutoCompleteListView;
import com.wheels2spin.enduser.fragments.CalendarFragment;
import com.wheels2spin.enduser.parse.Address;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocationPickerActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        AutoCompleteListView.AutoCompleteListViewEventListener {

    private GoogleApiClient mGoogleApiClient;
    private Location currentLocation;

    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    public Location getLocation() {
        return currentLocation;
    }

    @Override
    public void onDone(ArrayList<String> selectedLocations) {
        Intent data = new Intent();
        data.putStringArrayListExtra(getString(R.string.selected_locations), selectedLocations);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        buildGoogleApiClient();

        setContentView(R.layout.activity_location_picker);

        ParseQuery<Address> addressParseQuery = ParseQuery.getQuery("Address");
        addressParseQuery.findInBackground(new FindCallback<Address>() {
            @Override
            public void done(List<Address> addresses, ParseException e) {
                ArrayList<String> areaList = new ArrayList<String>();
                for (Address address: addresses) {
                    System.out.print(address.getArea());
                    areaList.add(address.getArea());
                }

                String [] area = new String[areaList.size()];
                if (savedInstanceState == null) {
                    Fragment fragment = AutoCompleteListView.newInstance(getString(R.string.enter_location), areaList.toArray(area));
                    getFragmentManager().beginTransaction()
                            .add(R.id.location_picker, fragment)
                            .commit();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location_picker, menu);
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

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {  // more about this later
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        CharSequence toastText = "Connected";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(this, toastText, duration).show();
        currentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        CharSequence toastText = "Suspended";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(this, toastText, duration).show();
        // The connection has been interrupted.
        // Disable any UI components that depend on Google APIs
        // until onConnected() is called.
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        CharSequence toastText = "Failed";
        int duration = Toast.LENGTH_SHORT;
        Toast.makeText(this, toastText, duration).show();

        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

            /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
            // Create a fragment for the error dialog
            ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
            // Pass the error that should be displayed
            Bundle args = new Bundle();
            args.putInt(DIALOG_ERROR, errorCode);
            dialogFragment.setArguments(args);
            dialogFragment.show(getFragmentManager(), "errordialog");
        }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
            mResolvingError = false;
        }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
            public ErrorDialogFragment() { }

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Get the error code and retrieve the appropriate dialog
                int errorCode = this.getArguments().getInt(DIALOG_ERROR);
                return GooglePlayServicesUtil.getErrorDialog(errorCode,
                        this.getActivity(), REQUEST_RESOLVE_ERROR);
            }

            @Override
            public void onDismiss(DialogInterface dialog) {
                ((LocationPickerActivity)getActivity()).onDialogDismissed();
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }
}
