package com.patrickohalloran.represent;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MainViewWatchActivity extends Activity {

    private TextView mTextView;
    private Button mFeedBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_watch);

        //mFeedBtn = (Button) findViewById(R.id.feed_btn);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
        pager.setAdapter(new GridPagerAdapter(this, getFragmentManager(), 3));
//        if (extras != null) {
//            String catName = extras.getString("CAT_NAME");
//            mFeedBtn.setText("Feed " + catName);
//        }

//        mFeedBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
//                startService(sendIntent);
//            }
//        });
    }

    public class GridPagerAdapter extends FragmentGridPagerAdapter {

        private final Context mContext;
        private List mRows;
        private int nTabs;

        public GridPagerAdapter(Context ctx, FragmentManager fm) {
            super(fm);
            mContext = ctx;
        }

        public GridPagerAdapter(Context ctx, FragmentManager fm, int numTabs) {
            super(fm);
            mContext = ctx;
            this.nTabs = numTabs;
        }

        final int[] BG_IMAGES = new int[]{
                R.drawable.barbara_boxer,
                R.drawable.dianne_feinstein,
                R.drawable.darrell_issa
        };

        @Override
        public Fragment getFragment(int row, int col) {
            String title = "Test";
            String text ="Bruh";
            CardFragment fragment = CardFragment.create(title, text, R.drawable.darrell_issa);

            // Advanced settings (card gravity, card expansion/scrolling)
//            fragment.setCardGravity(page.cardGravity);
//            fragment.setExpansionEnabled(page.expansionEnabled);
//            fragment.setExpansionDirection(page.expansionDirection);
//            fragment.setExpansionFactor(page.expansionFactor);
            return fragment;
        }

        // Obtain the number of pages (vertical)
        @Override
        public int getRowCount() {
            return 1;
        }

        // Obtain the number of pages (horizontal)
        @Override
        public int getColumnCount(int rowNum) {
            return this.nTabs;
        }

        // Obtain the background image for the row
        @Override
        public Drawable getBackgroundForRow(int row) {
            return mContext.getResources().getDrawable(
                    (BG_IMAGES[row % BG_IMAGES.length]), null);
        }
    }


//    // A simple container for static data in each page
//    private static class Page {
//        // static resources
//        int titleRes;
//        int textRes;
//        int iconRes;
//        ...
//    }
//    }
}
