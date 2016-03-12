package com.patrickohalloran.represent;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;


/*
 * A good deal of this code was inspired from: http://www.learnandroidwear.com/gridviewpager-cardfragment/
 */
public class MainViewWatchActivity extends FragmentActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mSensor;

    /** Minimum movement force to consider. */
    private static final int MIN_FORCE = 10;

    /**
     * Minimum times in a shake gesture that the direction of movement needs to
     * change.
     */
    private static final int MIN_DIRECTION_CHANGE = 2;

    /** Maximum pause between movements. */
    private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 10000;

    /** Maximum allowed time for shake gesture. */
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 150000;

    /** Time when the gesture started. */
    private long mFirstDirectionChangeTime = 0;

    /** Time when the last movement started. */
    private long mLastDirectionChangeTime;

    /** How many movements are considered so far. */
    private int mDirectionChangeCount = 0;

    /** The last x position. */
    private float lastX = 0;

    /** The last y position. */
    private float lastY = 0;

    /** The last z position. */
    private float lastZ = 0;

    //data structure that contains all of the member data
    private ArrayList<String[]> memberInfo = new ArrayList<String[]>();

    private String lat;
    private String lon;
    private String zip;
    private String DEBUG_TAG = "bruh debugging";
    private String locationInfo;
    private static String jsonString;
    private static JSONObject json2012;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        System.out.println("IN MAIN WATCH");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_watch);

        try {
            InputStream stream = getAssets().open("newelectioncounty2012.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            jsonString = new String(buffer, "UTF-8");
            try {
                json2012 = new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Intent intent = getIntent();
        String memberData = intent.getStringExtra("CONGRESS_DATA");
        System.out.println(memberData);
        String[] rows = memberData.split(";");
        String[][] matrix = new String[rows.length][];
        for (String row : rows) {
            System.out.println(row);
            memberInfo.add(row.split(","));
        }

        //get the location
        String[] location = memberInfo.remove(memberInfo.size() - 1);
        if (location.length == 1) {
            this.zip = location[0];
            //myClickHandler(true);
        } else {
            this.lat = location[0];
            this.lon = location[1];
            //myClickHandler(false);
        }

        //set up sensor manager
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        final DotsPageIndicator mPageIndicator;
        final GridViewPager mViewPager;

        String[] innerData = new String[memberInfo.size() + 1];
        final String[][] data = {innerData};

        // Get UI references
        mPageIndicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
        mViewPager = (GridViewPager) findViewById(R.id.pager);

        // Assigns an adapter to provide the content for this pager
        mViewPager.setAdapter(new GridPagerAdapter(getFragmentManager(), data));
        mPageIndicator.setPager(mViewPager);
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        // get sensor data
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];

        // calculate movement
        float totalMovement = Math.abs(x + y + z - lastX - lastY - lastZ);

        if (totalMovement > MIN_FORCE) {

            // get time
            long now = System.currentTimeMillis();

            // store first movement time
            if (mFirstDirectionChangeTime == 0) {
                mFirstDirectionChangeTime = now;
                mLastDirectionChangeTime = now;
            }

            // check if the last movement was not long ago
            long lastChangeWasAgo = now - mLastDirectionChangeTime;
            if (lastChangeWasAgo < MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE) {

                // store movement data
                mLastDirectionChangeTime = now;
                mDirectionChangeCount++;

                // store last sensor data
                lastX = x;
                lastY = y;
                lastZ = z;

                // check how many movements are so far
                if (mDirectionChangeCount >= MIN_DIRECTION_CHANGE) {

                    // check total duration
                    long totalDuration = now - mFirstDirectionChangeTime;
                    if (totalDuration < MAX_TOTAL_DURATION_OF_SHAKE) {
//                        mShakeListener.onShake();
//                        Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_LONG);
//                        toast.show();
                        resetShakeParameters();

                        randomLocation();
                    }
                }

            } else {
                resetShakeParameters();
            }
        }
    }

    public void randomLocation() {
        int westBound = -117;
        int eastBound = -81;
        int northBound = 41;
        int southBound = 33;
        Random rand = new Random();
        float tempLat = (-81 - (rand.nextFloat() * 36));
        float tempLon = 33 + (rand.nextFloat() * 8);
        DecimalFormat df = new DecimalFormat("0.####");
        df.setRoundingMode(RoundingMode.DOWN);
        double outputLat = Double.valueOf(df.format(tempLat));
        double outputLon = Double.valueOf(df.format(tempLon));
        Intent sendIntent = new Intent(this, WatchToPhoneService.class);
        Log.d("COOORDINATESSS", String.valueOf(outputLon) + "," + String.valueOf(outputLat));
        sendIntent.putExtra("COORDINATES", String.valueOf(outputLon) + "," + String.valueOf(outputLat));
        startService(sendIntent);
    }

    /**
     * Resets the shake parameters to their default values.
     */
    private void resetShakeParameters() {
        mFirstDirectionChangeTime = 0;
        mDirectionChangeCount = 0;
        mLastDirectionChangeTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public class GridPagerAdapter extends FragmentGridPagerAdapter {

        String[][] mData;

        private GridPagerAdapter(FragmentManager fm, String[][] data) {
            super(fm);
            mData = data;
        }

        @Override
        public Fragment getFragment(int row, int column) {
            if (column < memberInfo.size()) {
                Bundle b = new Bundle();
                b.putStringArray("PDATA", memberInfo.get(column));
                Fragment frag = new PlaceholderFragment();
                frag.setArguments(b);
                return frag;
            } else {
                Bundle b = new Bundle();
                b.putString("2012", constructStats());
                Fragment frag = new PlaceholderFragment();
                frag.setArguments(b);
                return frag;
            }
        }

        public String constructStats() {
            try {
                JSONObject arr = json2012.getJSONObject(locationInfo);
                String obama = arr.getString("obama");
                String romney = arr.getString("romney");
                return locationInfo + "\n" + "Obama: " + obama + "\n" + "Romney: " + romney;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }



        @Override
        public int getRowCount() {
            return mData.length;
        }

        @Override
        public int getColumnCount(int row) {
            return mData[row].length;
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
                return downloadUrl(urls[0]);
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
            try {
                json = new JSONObject(result);
                ArrayList<String> locationData = new ArrayList<String>();
                JSONObject results = json.getJSONObject("results");
                JSONArray addressComponents = results.optJSONArray("address_components");
                for (int i=0; i < addressComponents.length(); i++) {
                    JSONObject currItem = addressComponents.getJSONObject(i);
                    JSONArray currTypes = currItem.optJSONArray("types");
                    if (currTypes.get(0).equals("administrative_area_level_2")) {
                        String county = (String) currItem.get("long_name");
                        locationData.add(county);
                    } else if (currTypes.get(0).equals("administrative_area_level_1")) {
                        String state = (String) currItem.get("short_name");
                        locationData.add(state);
                    }
                    locationInfo = locationData.get(0) + ", " + locationData.get(1);
                }
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

    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void myClickHandler(boolean isZip) {
        // Gets the URL from the UI's text field.
        //String stringUrl = "http://www.espn.com";
        String locationUrl;
        if (!isZip) {
            locationUrl = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
                    + this.lat
                    + ","
                    + this.lon
                    + "&region=us";
        } else {
            locationUrl = "http://maps.googleapis.com/maps/api/geocode/json?address=" + this.zip + "&region=us";
        }
        String[] stringUrls = {locationUrl};
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrls);
        } else {
            Log.d(DEBUG_TAG, "No network connection available.");
        }
    }
}
