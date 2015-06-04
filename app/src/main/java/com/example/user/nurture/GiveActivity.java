package com.example.user.nurture;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class GiveActivity extends ActionBarActivity {
    private ImageView mProfilePic;
    private TextView mNameTextView;
    private TextView mSchoolTextView;
    private TextView mKindnessTextView;

    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private ArrayList<String> listOfKindness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);

        ////////////////////////
        // set userBlank "profile" //
        ////////////////////////
        mProfilePic = (ImageView)findViewById(R.id.profilePic);
        mNameTextView = (TextView)findViewById(R.id.nameTextView);
        mSchoolTextView = (TextView)findViewById(R.id.schoolTextView);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null && parseObjects.size()==1){
                    ParseObject userInfo = parseObjects.get(0);
                    mNameTextView.setText("Name:" + " " + userInfo.getString("username"));
                    mSchoolTextView.setText("School:" + " " + userInfo.getString("school"));
                    //mProfilePic.setImageDrawable(userInfo.getFile("profilepic"));
                    //////////////////
                    // set listview //
                    //////////////////
                    mListView = (ListView)findViewById(android.R.id.list);
                    listOfKindness = new ArrayList<>();
                    mKindnessTextView = (TextView)findViewById(R.id.kindnessTextView);


                    if(userInfo.getString("kindnessToBeDone") == null) {
                        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("suggestedKindness");
                        query1.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                if (e == null) {
                                    for (ParseObject i : parseObjects) {
                                        listOfKindness.add(i.getString("kindness"));
                                    }
                                    mAdapter = new SuggestedAdapter(GiveActivity.this, R.layout.suggested_list_item, listOfKindness);
                                    mListView.setAdapter(mAdapter);
                                    mListView.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                    }
                    else{
                        mKindnessTextView.setText(userInfo.getString("kindnessToBeDone"));
                        mKindnessTextView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_give, menu);
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

    public class SuggestedAdapter extends ArrayAdapter<String>{
        private int mResource;
        private ArrayList<String> mSuggested;

        public SuggestedAdapter(Context context, int resource, ArrayList<String> suggestedKindnesses){
            super(context, resource, suggestedKindnesses);
            this.mResource = resource;
            this.mSuggested = suggestedKindnesses;
        }

        @Override
        public View getView(int position, View row, ViewGroup parent){
            if (row==null){
                row = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            }

            TextView suggestedTextView = (TextView)row.findViewById(R.id.suggestedTextView);
            final String kindnessSuggestion = mSuggested.get(position);
            suggestedTextView.setText(kindnessSuggestion);

            Button doItButton = (Button)row.findViewById(R.id.doItButton);
            doItButton.setText("Do It!");
            doItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
                    query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if(e==null && parseObjects.size()==1) {
                                final ParseObject userInfo = parseObjects.get(0);
                                userInfo.put("kindnessToBeDone", kindnessSuggestion);
                                userInfo.put("hasDoneKindness", false);
                                userInfo.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        mListView.setVisibility(View.GONE);
                                        mKindnessTextView.setText(userInfo.getString("kindnessToBeDone")+"\nFor "+userInfo.getString("username"));
                                    }
                                });
                            }
                        }
                    });
                }
            });

            return row;
        }
    }
}
