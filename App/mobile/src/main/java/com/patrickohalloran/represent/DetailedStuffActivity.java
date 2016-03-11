package com.patrickohalloran.represent;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailedStuffActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_stuff);
        Bundle args = getIntent().getExtras();
        //set background color
        LinearLayout ll = (LinearLayout) findViewById(R.id.detailed_background_id);
        if (args.getString("party").equals("D")) {
            ll.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (args.getString("party").equals("R")) {
            ll.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            ll.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }

        //Set the name
        TextView nameView = (TextView) findViewById(R.id.detailed_name_id);
        nameView.setText(args.getString("firstName") + " " + args.getString("lastName"));

    }
}
