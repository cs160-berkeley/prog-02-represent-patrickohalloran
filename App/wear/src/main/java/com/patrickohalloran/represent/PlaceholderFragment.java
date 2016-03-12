package com.patrickohalloran.represent;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.wearable.view.CardFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by patrickohalloran on 2/21/16.
 */
public class PlaceholderFragment extends CardFragment implements View.OnClickListener{
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
    private Bundle bun;
    private String[] mArgs;
    private String county;
    private String state;
    private String romney;
    private String obama;


    public PlaceholderFragment() {
    }


    @Override
    public View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        bun = getArguments();
        if (bun.containsKey("PDATA")) {
            view = inflater.inflate(R.layout.fragment_template_watch, container, false);
            mArgs = bun.getStringArray("PDATA");
            Log.i("I SET THE THINGS", "AS U CAN TELL");
            //Set the name and party
            TextView nameView = (TextView) view.findViewById(R.id.congress_member_name);
            nameView.setText(mArgs[0] + " " + mArgs[1] + System.lineSeparator() + mArgs[5]);
            view.setOnClickListener(this);
        } else {
            view = inflater.inflate(R.layout.fragment_vote, container, false);
//            String stats = bun.getString("2012");
//            TextView statsView = (TextView) view.findViewById(R.id.vote_stats);
//            statsView.setText(stats);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("IN ONCLICKKK", "YAAAAAY");
        finishClick(v);
    }

    public void finishClick(View v) {
        Log.d("BEFORE INTENNNNT", "stkdlsjfdsf");
        Intent sendIntent = new Intent(getActivity(), WatchToPhoneService.class);
        String firstName = mArgs[0];
        String lastName = mArgs[1];
        String party = mArgs[5];
        String bioguide = mArgs[6];
        String termEnd = mArgs[7];
        String title = mArgs[4];
        String website = mArgs[2];
        String email = mArgs[3];

        String[] memberInfo = {firstName, lastName, website, email, title, party, bioguide, termEnd};
        String m = makeMessage(memberInfo);
        Bundle b = new Bundle();
        b.putString("PERSON", m);
        Log.d("AFTER INEEEETNT", "stufffff");
        sendIntent.putExtras(b);
        getActivity().startService(sendIntent);
    }

    //creates the huge string that has all of the member info and passes it to the watch
    //through phone service
    public String makeMessage(String[] memberData) {
        StringBuilder messageBuilder = new StringBuilder();
        for (String info : memberData) {
            for (int i=0; i < memberData.length; i++) {
                messageBuilder.append(memberData[i] + ",");
            }
        }
        String m = messageBuilder.toString();
        Log.d("HEEEEEREEE", m);
        return m;
    }

}