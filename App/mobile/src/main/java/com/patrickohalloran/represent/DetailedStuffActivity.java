package com.patrickohalloran.represent;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DetailedStuffActivity extends AppCompatActivity {

    private String firstName;
    private String lastName;
    private String party;
    private String endTerm;
    private String bioguideID;
    private String DEBUG_TAG = "bruh debugging";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_stuff);
        String args = getIntent().getStringExtra("PERSON");
        String[] newArgs = args.split(",");
        //set background color
        this.firstName = newArgs[0];
        this.lastName = newArgs[1];
        this.party = newArgs[5];
        this.bioguideID = newArgs[6];
        this.endTerm = newArgs[7];
        LinearLayout ll = (LinearLayout) findViewById(R.id.detailed_background_id);
        if (this.party.equals("D")) {
            ll.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (this.party.equals("R")) {
            ll.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            ll.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }


        //Set the name
        TextView nameView = (TextView) findViewById(R.id.detailed_name_id);
        nameView.setText(this.firstName + " " + this.lastName);

        //Set the party
        TextView partyView = (TextView) findViewById(R.id.detailed_party);
        if (this.party.equals("D")) {
            partyView.setText("Democrat");
        } else if (this.party.equals("R")) {
            partyView.setText("Republican");
        } else {
            partyView.setText("Independent");
        }


        //Set the end term
        TextView termEnd = (TextView) findViewById(R.id.detailed_dates_in_office);
        termEnd.setText("Term end date: " + this.endTerm);

        myClickHandler();

    }

    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void myClickHandler() {
        // Gets the URL from the UI's text field.
        //String stringUrl = "http://www.espn.com";
        String committeesUrl =
                "https://congress.api.sunlightfoundation.com/committees?member_ids="
                + this.bioguideID
                + "&apikey=" + getResources().getString(R.string.sunlight_API_key);
        String billsUrl =
                "https://congress.api.sunlightfoundation.com/bills?sponsor_id="
                        + this.bioguideID
                        + "&apikey=" + getResources().getString(R.string.sunlight_API_key);
        String[] stringUrls = {committeesUrl, billsUrl};
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrls);
        } else {
            Log.d(DEBUG_TAG, "No network connection available.");
        }
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                StringBuilder s = new StringBuilder();
                for (String url : urls) {
                    s.append(downloadUrl(url));
                }
                return s.toString();
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //textView.setText(result);
            Log.d("BROOOOOO", result);
            JSONObject json = null;
            String[] newResult = result.split("\n");
            try {
                //committee stuff
                String committees = newResult[0];
                json = new JSONObject(committees);
                StringBuilder committeesInvolved = new StringBuilder();
                JSONArray comms = json.optJSONArray("results");
                for (int i=0; i < comms.length(); i++) {
                    JSONObject currComm = comms.getJSONObject(i);
                    String committeeName = currComm.getString("name");
                    committeesInvolved.append(committeeName + "\n");
                }

                TextView commView = (TextView) findViewById(R.id.detailed_committees);
                commView.setText(committeesInvolved.toString());

                //Bills stuff
                String bills = newResult[1];
                json = new JSONObject(bills);
                StringBuilder billsSponsored = new StringBuilder();
                JSONArray b = json.optJSONArray("results");
                for (int i=0; i < b.length(); i++) {
                    JSONObject currBill = b.getJSONObject(i);
                    String dateIntroduced = currBill.getString("introduced_on");
                    String billName = currBill.getString("official_title");
                    billsSponsored.append(billName + "\n" + "Date introduced: " + dateIntroduced + "\n\n");
                }

                TextView billView = (TextView) findViewById(R.id.detailed_bills);
                billView.setText(billsSponsored.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
// the web page content as a InputStream, which it returns as
// a string.
    private String downloadUrl(String myurl) throws IOException {
        //String email = emailText.getText().toString();
        // Do some validation here

        try {
            URL url = new URL(myurl);
            //URL url = new URL(API_URL + "email=" + email + "&apiKey=" + API_KEY);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                return stringBuilder.toString();
            }
            finally{
                urlConnection.disconnect();
            }
        }
        catch(Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            return null;
        }
    }
}
