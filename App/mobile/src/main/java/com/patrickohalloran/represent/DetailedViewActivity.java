package com.patrickohalloran.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DetailedViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String person = intent.getStringExtra("PERSON");
        if (person == "BOXER") {
            setContentView(R.layout.bb_detailed_view);
        } else if (person == "FIENSTEIN") {
            setContentView(R.layout.activity_detailed_view);
        } else {
            setContentView(R.layout.activity_detailed_view);
        }

    }
}
