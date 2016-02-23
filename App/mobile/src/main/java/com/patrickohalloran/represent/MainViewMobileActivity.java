package com.patrickohalloran.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainViewMobileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_mobile);
    }

    public void getCongressMembers(View view) {
        Intent intent = new Intent(this, CongressionalViewActivity.class);
        startActivity(intent);
        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
        sendIntent.putExtra("CAT_NAME", "Fred");
        startService(sendIntent);
    }
}
