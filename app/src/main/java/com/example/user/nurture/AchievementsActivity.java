package com.example.user.nurture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class AchievementsActivity extends ActionBarActivity {

    TextView noAchieves;
    ArrayList <ParseObject> mAchieves = new ArrayList<>();
    private TextView mNameTextView;
    private TextView mSchoolTextView;
    private TextView mRoleTextView;
    private ImageView mProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        ActionBar actionBar = getSupportActionBar();

        mNameTextView = (TextView)findViewById(R.id.nameTextView);
        mSchoolTextView = (TextView)findViewById(R.id.schoolTextView);
        mRoleTextView = (TextView)findViewById(R.id.roleTextView);
        mProfilePic = (ImageView) findViewById(R.id.profilePic);

        assert actionBar != null;
        actionBar.setTitle("Your Badges");
        actionBar.setDisplayHomeAsUpEnabled(true);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null && users.size() == 1) {
                    ParseObject receiverInfo = users.get(0);
                    mNameTextView.setText(receiverInfo.getString("username"));
                    mSchoolTextView.setText(receiverInfo.getString("school"));
                    mRoleTextView.setText(receiverInfo.getString("role"));

                    ParseFile fileObject = receiverInfo.getParseFile("profilePic");
                    fileObject.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                Bitmap bmp = BitmapFactory
                                        .decodeByteArray(
                                                data, 0,
                                                data.length);
                                mProfilePic.setImageBitmap(bmp);
                            }
                        }
                    });
                }
            }
        });


        ParseQuery<ParseObject> check = ParseQuery.getQuery("Achievements");
        check.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list2, ParseException e) {
                if (e == null) {
                    for (int j = 0; j < list2.size(); j++) {
                        List usernames = list2.get(j).getList("usernames");
                        for (int k = 0; k < usernames.size(); k++) {
                            if (usernames.get(k).toString().equals(ParseUser.getCurrentUser().getUsername())) {
                                mAchieves.add(list2.get(j));
                                break;
                            }
                        }
                    }
                    ListView lvToShow = (ListView)findViewById(R.id.achievesListView);
                    if (mAchieves.size() == 0){
                        lvToShow.setVisibility(View.GONE);
                        noAchieves.setVisibility(View.VISIBLE);
                    }

                    else {
                        lvToShow.setVisibility(View.VISIBLE);
                        noAchieves.setVisibility(View.GONE);
                        ArrayAdapter<ParseObject> adapter;
                        adapter = new AchievesAdapter(AchievementsActivity.this, R.layout.list_achieves, mAchieves);
                        lvToShow.setAdapter(adapter);
                    }

                }

            }

        });

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
        if (id == android.R.id.home) {
            Intent intent = new Intent(AchievementsActivity.this, MainActivity.class);
            AchievementsActivity.this.startActivity(intent);
        }

        else if (id == R.id.action_logout){
            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    Intent intent = new Intent(AchievementsActivity.this, LoginActivity.class);
                    AchievementsActivity.this.startActivity(intent);
                }
            });
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
            TextView subtitleTextView = (TextView) row.findViewById(R.id.achievesSubtitle);
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
