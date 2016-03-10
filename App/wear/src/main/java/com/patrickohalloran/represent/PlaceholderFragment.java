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
import android.widget.FrameLayout;
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
    private static final String ARG_SECTION_NUMBER = "section_number";
    private int numItems;


    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance(int row, int col, String[] memInfo, boolean showVoteView) {
        Fragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ROW, row);
        args.putInt(COL, col);
        args.putString("firstName", memInfo[0]);
        args.putString("lastName", memInfo[1]);
        args.putString("party", memInfo[5]);
        args.putString("bioguide", memInfo[6]);
        args.putBoolean("voteView", showVoteView);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args.getBoolean("voteView")) {
            return inflater.inflate(R.layout.fragment_vote, container, false);
        }
        int row = args.getInt(ROW);
        int col = args.getInt(COL);
        this._col = col;
        int layout_int = R.layout.fragment_template_watch;
        View view = inflater.inflate(layout_int, container, false);

        FrameLayout fl = (FrameLayout) view.findViewById(R.id.framelayout);
        if (args.getString("party").equals("D")) {
            fl.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (args.getString("party").equals("R")) {
            fl.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            fl.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }

        //Set the name and party
        TextView nameView = (TextView) view.findViewById(R.id.congress_member_name);
        nameView.setText(args.getString("firstName") + " " + args.getString("lastName") + System.lineSeparator() + args.getString("party"));


//        //layout_int = R.layout.fragment_bb;
//        if (col == 0) {
//            layout_int = R.layout.fragment_vote;
//        } else if (col == 1) {
//            layout_int = R.layout.fragment_bb;
//        } else if (col == 2){
//            layout_int = R.layout.fragment_df;
//        } else {
//            layout_int = R.layout.fragment_di;
//        }

        view.setOnClickListener(new View.OnClickListener() {
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
        //this._view = rootView;
//        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        return view;
    }
}