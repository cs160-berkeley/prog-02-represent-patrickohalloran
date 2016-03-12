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
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        System.out.println("IN MAIN WATCH");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_watch);


        Intent intent = getIntent();
        String memberData = intent.getStringExtra("CONGRESS_DATA");
        System.out.println(memberData);
        String[] rows = memberData.split(";");
        String[][] matrix = new String[rows.length][];
        for (String row : rows) {
            System.out.println(row);
            memberInfo.add(row.split(","));
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
                b.putStringArray("PDATA", memberInfo.get(0));
                Fragment frag = new PlaceholderFragment();
                frag.setArguments(b);
                return frag;
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
}
