package com.patrickohalloran.represent;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DetailedViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String person = intent.getStringExtra("PERSON");
        if (person.equals("1")) {
            setContentView(R.layout.bb_detailed_view);
        } else if (person.equals("2")) {
            setContentView(R.layout.df_detailed_view);
        } else {
            setContentView(R.layout.di_detailed_view);
        }

    }
}
