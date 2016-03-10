package com.patrickohalloran.represent;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by patrickohalloran on 2/21/16.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public PlaceholderFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber, String[] memInfo) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString("firstName", memInfo[0]);
        args.putString("lastName", memInfo[1]);
        args.putString("website", memInfo[2]);
        args.putString("email", memInfo[3]);
        args.putString("title", memInfo[4]);
        args.putString("party", memInfo[5]);
        args.putString("bioguide", memInfo[6]);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        int person = args.getInt(ARG_SECTION_NUMBER);
        int layout_int = R.layout.fragment_template;

        View view = inflater.inflate(layout_int, container, false);

        //set background color
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.background_id);
        if (args.getString("party").equals("D")) {
            ll.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else if (args.getString("party").equals("R")) {
            ll.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        } else {
            ll.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }

        //Set the name
        TextView nameView = (TextView) view.findViewById(R.id.name_id);
        nameView.setText(args.getString("firstName") + " " + args.getString("lastName"));

        //set the email
        TextView emailView = (TextView) view.findViewById(R.id.email_id);
        emailView.setText(args.getString("email"));

        //set the website
        TextView websiteView = (TextView) view.findViewById(R.id.website_id);
        websiteView.setText(args.getString("website"));

        //set the image
        Drawable img = LoadImageFromWebOperations("https://theunitedstates.io/images/congress/450x550/"
                + args.getString("bioguide")
                +".jpg");
        ImageView photoView = (ImageView) view.findViewById(R.id.photo_id);
        photoView.setImageDrawable(img);

        return view;
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

//    public void getDetailedView(View view) {
//        int id = view.getId();
//        Intent intent = new Intent(getActivity(), DetailedViewActivity.class);
//        String person;
//        if (id == R.id.bb_button) {
//            person = "BOXER";
//        } else if (id == R.id.df_button) {
//            person = "FEINSTEIN";
//        } else {
//            person = "ISSA";
//        }
//        intent.putExtra("PERSON", person);
//        startActivity(intent);
//    }
}