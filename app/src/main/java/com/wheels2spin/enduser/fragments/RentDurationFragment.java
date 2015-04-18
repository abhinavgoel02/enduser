package com.wheels2spin.enduser.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wheels2spin.enduser.R;
import com.wheels2spin.enduser.enums.Duration;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RentDurationFragment.RentDurationEventListener} interface
 * to handle interaction events.
 * Use the {@link RentDurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RentDurationFragment extends Fragment {
    private RentDurationEventListener mListener;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RentDurationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RentDurationFragment newInstance() {
        RentDurationFragment fragment = new RentDurationFragment();
        return fragment;
    }

    public RentDurationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_rent_duration, container, false);

        Button oneDayButton = (Button) fragmentView.findViewById(R.id.one_day);
        oneDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDurationSelected(Duration.ONE_DAY);
            }
        });

        Button oneWeekButton = (Button) fragmentView.findViewById(R.id.one_week);
        oneWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDurationSelected(Duration.ONE_WEEK);
            }
        });

        Button oneMonthButton = (Button) fragmentView.findViewById(R.id.one_month);
        oneMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDurationSelected(Duration.ONE_MONTH);
            }
        });

        Button selectDropOffDateButton = (Button) fragmentView.findViewById(R.id.select_drop_off_date);
        selectDropOffDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSelectDropOffDate();
            }
        });

        return fragmentView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (RentDurationEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface RentDurationEventListener {
        // TODO: Update argument type and name
        public void onDurationSelected(Duration duration);
        public void onSelectDropOffDate();
    }

}
