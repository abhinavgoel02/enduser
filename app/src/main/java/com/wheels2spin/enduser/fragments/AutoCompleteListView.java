package com.wheels2spin.enduser.fragments;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.wheels2spin.enduser.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.wheels2spin.enduser.fragments.AutoCompleteListView.AutoCompleteListViewEventListener} interface
 * to handle interaction events.
 * Use the {@link AutoCompleteListView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AutoCompleteListView extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_HINT = "hint";
    private static final String ARG_ARRAY = "array";

    private EditText autoCompleteTextField;
    private ListView listView;
    private ImageButton doneButton;

    // TODO: Rename and change types of parameters
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> selectedItems = new ArrayList<String>();
    private String hint;
    private String [] array;

    private AutoCompleteListViewEventListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param hint Text to be displayed in the EditText field hinting what the user can input.
     * @param array Parameter 2.
     * @return A new instance of fragment AutoCompleteListView.
     */
    // TODO: Rename and change types and number of parameters
    public static AutoCompleteListView newInstance(String hint, String [] array) {
        AutoCompleteListView fragment = new AutoCompleteListView();
        Bundle args = new Bundle();
        args.putStringArray(ARG_ARRAY, array);
        args.putString(ARG_HINT, hint);
        fragment.setArguments(args);
        return fragment;
    }

    public AutoCompleteListView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hint = getArguments().getString(ARG_HINT);
            array = getArguments().getStringArray(ARG_ARRAY);

            listAdapter =
                    new ArrayAdapter<>(
                            getActivity(),
                            R.layout.auto_complete_list_view_item,
                            R.id.auto_complete_list_view_item,
                            array);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_auto_complete_list_view, container, false);

        autoCompleteTextField = (EditText) v.findViewById(R.id.auto_complete_text);
        autoCompleteTextField.setHint(hint);
        autoCompleteTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView = (ListView) v.findViewById(R.id.auto_complete_list_view);
        listView.setAdapter(listAdapter);

        doneButton = (ImageButton) v.findViewById(R.id.done);
        doneButton.setOnClickListener(this);

        return v;
    }

    public void onClick(View v) {

        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
        for (int i=0; i< listView.getAdapter().getCount(); i++) {
            if (checkedItems.get(i)) {
                CheckedTextView checkedTextView = (CheckedTextView) listView.getChildAt(i);
                selectedItems.add(String.valueOf(checkedTextView.getText()));
            }
        }

        if (mListener!=null) {
            mListener.onDone(selectedItems);
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AutoCompleteListViewEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement AutoCompleteListViewEventListener");
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
    public interface AutoCompleteListViewEventListener {
        // TODO: Update argument type and name
        public void onDone(ArrayList<String> selectedItems);
    }

}
