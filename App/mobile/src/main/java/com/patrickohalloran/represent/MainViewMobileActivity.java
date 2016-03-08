package com.patrickohalloran.represent;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.Wearable;

public class MainViewMobileActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_mobile);
    }

    public void enterZipCode(View view) {
        Intent intent = new Intent(this, EnterZipCodeActivity.class);
        startActivity(intent);
    }

    public void getCongressMembers(View view) {
        Intent intent = new Intent(this, GPSActivity.class);
        startActivity(intent);
//        Intent intent = new Intent(this, CongressionalViewActivity.class);
//        startActivity(intent);
//        Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
//        sendIntent.putExtra("CAT_NAME", "Fred");
//        startService(sendIntent);
    }
}
