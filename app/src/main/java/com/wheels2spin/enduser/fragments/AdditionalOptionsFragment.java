package com.wheels2spin.enduser.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wheels2spin.enduser.R;
import com.wheels2spin.enduser.activity.BikePickerActivity;
import com.wheels2spin.enduser.activity.ListBikeActivity;
import com.wheels2spin.enduser.activity.LocationPickerActivity;


public class AdditionalOptionsFragment extends Fragment {

    private static final int REQUEST_CODE_SELECT_LOCATION = 1;
    private static final int REQUEST_CODE_SELECT_BIKE_MODEL = 2;

    public static AdditionalOptionsFragment newInstance() {
        return new AdditionalOptionsFragment();
    }

    private AddtionalOptionEventListener mListener;

    public AdditionalOptionsFragment() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AddtionalOptionEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AddtionalOptionEventListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_additional_options, container, false);

        Button addLocationButton = (Button) rootView.findViewById(R.id.addLocationButton);
        addLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addLocation = new Intent(getActivity(), LocationPickerActivity.class);
                startActivityForResult(addLocation, REQUEST_CODE_SELECT_LOCATION);
            }
        });

        Button addModelButton = (Button) rootView.findViewById(R.id.addBikeModelButton);
        addModelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addBikeModel = new Intent(getActivity(), BikePickerActivity.class);
                startActivityForResult(addBikeModel, REQUEST_CODE_SELECT_BIKE_MODEL);
            }
        });

        Button searchButton = (Button) rootView.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listBike = new Intent(getActivity(), ListBikeActivity.class);
                startActivity(listBike);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_LOCATION:
                    break;
                case REQUEST_CODE_SELECT_BIKE_MODEL:
                    break;
            }
        }
    }

    public interface AddtionalOptionEventListener {

        public void onSearch();

    }

}
