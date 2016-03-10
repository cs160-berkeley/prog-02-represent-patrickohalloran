package com.patrickohalloran.represent;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CongressionalViewActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static final String DEBUG_TAG = "HttpExample";
    private String zipCode;
    private String lat;
    private String lon;

    private ArrayList<String[]> memberInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congressional_view);

//        Intent httpIntent = new Intent(this, HTTPActivity.class);
//        startActivity(httpIntent);
        Intent locationIntent = getIntent();
        if (locationIntent.hasExtra(getResources().getString(R.string.zip_code))) {
            this.zipCode = locationIntent.getStringExtra(getResources().getString(R.string.zip_code));
            myClickHandler(true);
        } else {
            this.lat = locationIntent.getStringExtra(getResources().getString(R.string.latitude));
            this.lon = locationIntent.getStringExtra(getResources().getString(R.string.longitude));
            myClickHandler(false);
        }

    }

    //populate the tab headers and finish creating fragment layout
    public void finishOnCreate() {
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.colorPrimary));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //trying to set tabs here
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        for (String[] member : memberInfo) {
            tabLayout.addTab(tabLayout.newTab().setText(member[1]));
        }
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorBlack));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    // Before attempting to fetch the URL, makes sure that there is a network connection.
    public void myClickHandler(boolean isZip) {
        // Gets the URL from the UI's text field.
        //String stringUrl = "http://www.espn.com";
        String stringUrl;
        if (isZip) {
            stringUrl = "https://congress.api.sunlightfoundation.com/legislators/locate?"
                    + "zip=" + this.zipCode
                    + "&apikey=" + getResources().getString(R.string.sunlight_API_key);
        } else {
            stringUrl = "https://congress.api.sunlightfoundation.com/legislators/locate?"
                    + "latitude=" + this.lat
                    + "&longitude=" + this.lon
                    + "&apikey=" + getResources().getString(R.string.sunlight_API_key);
        }
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
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
                ArrayList<String[]> memberData = new ArrayList<String[]>();
                JSONArray members = json.optJSONArray("results");
                for (int i=0; i < members.length(); i++) {
                    JSONObject currMember = members.getJSONObject(i);
                    String firstName = currMember.getString("first_name");    //0
                    String lastName = currMember.getString("last_name");      //1
                    String website = currMember.getString("website");         //2
                    String email = currMember.getString("oc_email");          //3
                    String title = currMember.getString("title");             //4
                    String party = currMember.getString("party");             //5
                    String[] entry = {firstName, lastName, website, email, title, party};
                    memberData.add(entry);
                }
                memberInfo = memberData;
                finishOnCreate();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_congressional_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        int nTabs;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public SectionsPagerAdapter(FragmentManager fm, int numTabs) {
            super(fm);
            this.nTabs = numTabs;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position, memberInfo.get(position));
        }

        @Override
        public int getCount() {
            // Show nTabs total pages.
            return this.nTabs;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

//    private JSONObject parseURL(String inputUrl) throws IOException{
//        URL url = new URL(inputUrl);
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        try {
//            String result = null;
//            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            result = sb.toString();
//            JSONObject JSONresult = new JSONObject(result);
//            return JSONresult;
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } finally {
//            urlConnection.disconnect();
//        }
//        return null;
//    }

    public void getDetailedView(View view) {
        int id = view.getId();
        Intent intent = new Intent(this, DetailedViewActivity.class);
        String person;
        if (id == R.id.bb_button) {
            person = "1";
        } else if (id == R.id.df_button) {
            person = "2";
        } else {
            person = "3";
        }
        intent.putExtra("PERSON", person);
        intent.putExtra("SOURCE", "congressional_view");
        startActivity(intent);
    }
}
