package com.example.user.nurture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AchievementsActivity extends ActionBarActivity {

    ProgressBar progressBar = new ProgressBar(AchievementsActivity.this);
    ListView lvToShow = (ListView)findViewById(R.id.achievesListView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Your Achievements");
        final ArrayList <ParseObject> mAchieves = new ArrayList<>();

        final List list = ParseUser.getCurrentUser().getList("achievements");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).toString().equals("[]")) {
                mAchieves.remove(i);
            } else {

                ParseQuery<ParseObject> check = ParseQuery.getQuery("Achievements");
                final int finalI = i;
                check.findInBackground(new FindCallback<ParseObject>() {
                                           @Override
                                           public void done(List<ParseObject> list2, ParseException e) {
                                               if (e == null) {
                                                   for (int j = 0; j < list2.size(); j++) {
                                                       if (list.get(finalI) == list2.get(j).getInt("achID")) {
                                                           mAchieves.add(list2.get(j));
                                                       }
                                                   }
                                               }

                                               else{
                                                   // Set a textView with "You do not have any achievements yet."
                                               }


                                           }
                                       }

                );


            }
        }

        ArrayAdapter<ParseObject> adapter;
        adapter = new AchievesAdapter(this, R.layout.list_achieves, mAchieves);
        lvToShow.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_achievements, menu);
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

    private class AchievesAdapter extends ArrayAdapter<ParseObject> {
        //creating variables
        private int mResource;
        private ArrayList<ParseObject> mAchieveTitles;

        public AchievesAdapter (Context context, int resource, ArrayList<ParseObject> achievementTitles) {
            super(context, resource, achievementTitles);
            mResource = resource;
            mAchieveTitles = achievementTitles;
        }

        //display subject data in every row of listView
        @Override
        public View getView(final int position, View row, ViewGroup parent) {
            if (row == null) {
                row = getLayoutInflater().inflate(mResource, parent, false);
            }

            final ParseObject currentAchieve = mAchieveTitles.get(position);
            TextView titleTextView = (TextView) row.findViewById(R.id.achievesTitle);
            titleTextView.setText(currentAchieve.get("name").toString());
            final TextView subtitleTextView = (TextView) row.findViewById(R.id.achievesSubtitle);
            subtitleTextView.setText(currentAchieve.get("subtitle").toString());
            ParseFile fileObject = currentAchieve.getParseFile("badgePic");
            final ImageView achievesIcon = (ImageView) row.findViewById(R.id.achievesPic);
            fileObject.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory
                                .decodeByteArray(
                                        data, 0,
                                        data.length);
                        achievesIcon.setImageBitmap(bmp);

                    }
                }
            });
            return row;
        }
    }
}
