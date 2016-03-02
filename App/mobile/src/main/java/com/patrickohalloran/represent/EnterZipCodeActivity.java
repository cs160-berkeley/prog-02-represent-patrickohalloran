package com.patrickohalloran.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EnterZipCodeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_zip_code);
    }

    public void findCongressMembers(View view) {
        EditText textView = (EditText) findViewById(R.id.user_zip_code);
        String zipString = textView.getText().toString();
        if (zipString.length() == 5) {
            Intent intent = new Intent(this, CongressionalViewActivity.class);
            startActivity(intent);
            Intent sendIntent = new Intent(getBaseContext(), PhoneToWatchService.class);
            sendIntent.putExtra("CAT_NAME", "Fred");
            startService(sendIntent);
        } else {
            //Context context = getActivity();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Please enter a valid zip code.", duration);
            toast.show();
        }
    }
}
