package com.patrickohalloran.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by patrickohalloran on 2/21/16.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int person = getArguments().getInt(ARG_SECTION_NUMBER);
        int layout_int;
        if (person == 3) {
            layout_int = R.layout.fragment_di_congressional;
        } else if (person == 2) {
            layout_int = R.layout.fragment_bb_congressional;
        } else {
            layout_int = R.layout.fragment_df_congressional;
        }
        View rootView = inflater.inflate(layout_int, container, false);
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }

//    public void getDetailedView(View view) {
//        int id = view.getId();
//        Intent intent = new Intent(view.getContext(), DetailedViewActivity.class);
//        String person;
//        if (id == R.id.bb_button) {
//            person = "BOXER";
//        } else if (id == R.id.df_button) {
//            person = "FEINSTEIN";
//        } else {
//            person = "ISSA";
//        }
//        intent.putExtra("PERSON", person);
//        startActivity(intent);
//    }
}