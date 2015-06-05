package com.example.user.nurture;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;


public class AchievementsActivity extends ActionBarActivity {

    ProgressBar progressBar = new ProgressBar(AchievementsActivity.this);
    ListView lvToShow = (ListView)findViewById(R.id.achievesListView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Your Achievements");
        ArrayList <String> mAchieves = new ArrayList<>();


        ArrayAdapter<String> adapter;
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

    private class AchievesAdapter extends ArrayAdapter<String> {
        //creating variables
        private int mResource;
        private ArrayList<String> mAchieves;

        public AchievesAdapter (Context context, int resource, ArrayList<String> achievements) {
            super(context, resource, achievements);
            mResource = resource;
            mAchieves = achievements;
        }

        //display subject data in every row of listView
        @Override
        public View getView(final int position, View row, ViewGroup parent) {
            if (row == null) {
                row = getLayoutInflater().inflate(mResource, parent, false);
            }
            //get the homework to be displayed in row
            final String currentAchieve = mAchieves.get(position);
            //display data from homework into row
            TextView titleTextView = (TextView) row.findViewById(R.id.achievesTitle);
            titleTextView.setText(currentAchieve);
            final TextView subtitleTextView = (TextView) row.findViewById(R.id.achievesSubtitle);
            ParseQuery<ParseUser> check = ParseUser.getQuery();
            Button achievesIcon = (Button) row.findViewById(R.id.achievesPic);
            achievesIcon.setBackgroundResource(R.drawable.logouticon);
            return row;
        }
    }
}
