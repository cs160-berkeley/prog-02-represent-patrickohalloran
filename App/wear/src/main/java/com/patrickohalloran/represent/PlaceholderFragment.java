package com.patrickohalloran.represent;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by patrickohalloran on 2/21/16.
 */
public class PlaceholderFragment extends Fragment{
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ROW = "row_number";
    private static final String COL = "col_number";
    private View _view;
    private int _col;


    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance(int row, int col) {
        Fragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ROW, row);
        args.putInt(COL, col);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int row = getArguments().getInt(ROW);
        int col = getArguments().getInt(COL);
        this._col = col;
        int layout_int;
        //layout_int = R.layout.fragment_bb;
        if (col == 0) {
            layout_int = R.layout.fragment_vote;
        } else if (col == 1) {
            layout_int = R.layout.fragment_bb;
        } else if (col == 2){
            layout_int = R.layout.fragment_df;
        } else {
            layout_int = R.layout.fragment_di;
        }
        View rootView = inflater.inflate(layout_int, container, false);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = getActivity();
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, "BRUHHHHHHH", duration);
//                toast.show();
//                Activity currActivity = (Activity) v.getContext();
//                TextView nameView = (TextView) currActivity.findViewById(R.id.congress_member_name);
//                System.out.println("YEEEEESSSSSS");
//                String nameString = nameView.getText().toString();
                if (_col != 0) {
                    Intent sendIntent = new Intent(getActivity().getBaseContext(), WatchToPhoneService.class);
                    sendIntent.putExtra("PERSON", Integer.toString(_col));
                    getActivity().startService(sendIntent);
                }
            }
        });
        this._view = rootView;
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
    }
}