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

        //Set the name
        TextView partyView = (TextView) findViewById(R.id.detailed_party);
        partyView.setText(args.getString("party"));

        //Set the name
        TextView termEnd = (TextView) findViewById(R.id.detailed_dates_in_office);
        termEnd.setText(args.getString("termEnd"));

    }

//    // Before attempting to fetch the URL, makes sure that there is a network connection.
//    public void myClickHandler() {
//        // Gets the URL from the UI's text field.
//        //String stringUrl = "http://www.espn.com";
//        String stringUrl;
//        if (isZip) {
//            stringUrl = "https://congress.api.sunlightfoundation.com/legislators/locate?"
//                    + "zip=" + this.zipCode
//                    + "&apikey=" + getResources().getString(R.string.sunlight_API_key);
//        } else {
//            stringUrl = "https://congress.api.sunlightfoundation.com/legislators/locate?"
//                    + "latitude=" + this.lat
//                    + "&longitude=" + this.lon
//                    + "&apikey=" + getResources().getString(R.string.sunlight_API_key);
//        }
//        ConnectivityManager connMgr = (ConnectivityManager)
//                getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()) {
//            new DownloadWebpageTask().execute(stringUrl);
//        } else {
//            Log.d(DEBUG_TAG, "No network connection available.");
//        }
//    }
//
//    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
//    // URL string and uses it to create an HttpUrlConnection. Once the connection
//    // has been established, the AsyncTask downloads the contents of the webpage as
//    // an InputStream. Finally, the InputStream is converted into a string, which is
//    // displayed in the UI by the AsyncTask's onPostExecute method.
//    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls) {
//
//            // params comes from the execute() call: params[0] is the url.
//            try {
//                return downloadUrl(urls[0]);
//            } catch (IOException e) {
//                return "Unable to retrieve web page. URL may be invalid.";
//            }
//        }
//        // onPostExecute displays the results of the AsyncTask.
//        @Override
//        protected void onPostExecute(String result) {
//            //textView.setText(result);
//            Log.d("BROOOOOO", result);
//            JSONObject json = null;
//            try {
//                json = new JSONObject(result);
//                ArrayList<String[]> memberData = new ArrayList<String[]>();
//                JSONArray members = json.optJSONArray("results");
//                for (int i=0; i < members.length(); i++) {
//                    JSONObject currMember = members.getJSONObject(i);
//                    String firstName = currMember.getString("first_name");    //0
//                    String lastName = currMember.getString("last_name");      //1
//                    String website = currMember.getString("website");         //2
//                    String email = currMember.getString("oc_email");          //3
//                    String title = currMember.getString("title");             //4
//                    String party = currMember.getString("party");             //5
//                    String bioguide = currMember.getString("bioguide_id");    //6
//                    String endDate = currMember.getString("term_end");        //7
//                    String[] entry = {firstName, lastName, website, email, title, party, bioguide, endDate};
//                    memberData.add(entry);
//                }
////                memberInfo = memberData;
////                finishOnCreate();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // Given a URL, establishes an HttpUrlConnection and retrieves
//// the web page content as a InputStream, which it returns as
//// a string.
//    private String downloadUrl(String myurl) throws IOException {
//        //String email = emailText.getText().toString();
//        // Do some validation here
//
//        try {
//            URL url = new URL(myurl);
//            //URL url = new URL(API_URL + "email=" + email + "&apiKey=" + API_KEY);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            try {
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(line).append("\n");
//                }
//                bufferedReader.close();
//                return stringBuilder.toString();
//            }
//            finally{
//                urlConnection.disconnect();
//            }
//        }
//        catch(Exception e) {
//            Log.e("ERROR", e.getMessage(), e);
//            return null;
//        }
//    }
}
