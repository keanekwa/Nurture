package com.example.user.nurture;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class GiveActivity extends ActionBarActivity {
    private ImageView mProfilePic;
    private TextView mNameTextView;
    private TextView mSchoolTextView;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private ArrayList<String> listOfKindness;

    private ParseUser receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);

        ////////////////////////
        // set user "profile" //
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
<<<<<<< HEAD
                    ParseObject userInfo = parseObjects.get(0);
                    mNameTextView.setText(userInfo.getString("username"));
                    mSchoolTextView.setText(userInfo.getString("class"));
                    //mProfilePic.setImageDrawable(userInfo.getFile("profilepic"));
=======
                    String receiverUsername = parseObjects.get(0).getString("receiver");
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", receiverUsername);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> users, ParseException e) {
                            if(e==null && users.size()==1){
                                receiver = users.get(0);
                                mNameTextView.setText(receiver.getUsername());
                                mSchoolTextView.setText(receiver.getString("school"));
                                //TODO: profile pic
                            }
                        }
                    });
>>>>>>> origin/master
                }
            }
        });

        //////////////////
        // set listview //
        //////////////////
        mListView = (ListView)findViewById(android.R.id.list);
        listOfKindness = new ArrayList<>();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("suggestedKindness");
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null) {
                    for (ParseObject i : parseObjects) {
                        listOfKindness.add(i.getString("kindness"));
                    }
                    mAdapter = new SuggestedAdapter(GiveActivity.this, R.layout.suggested_list_item, listOfKindness);
                    mListView.setAdapter(mAdapter);
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
            doItButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
                    query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> parseObjects, ParseException e) {
                            if(e==null && parseObjects.size()==1) {
                                ParseObject userInfo = parseObjects.get(0);
                                userInfo.put("kindnessToBeDone", kindnessSuggestion);
                                userInfo.put("hasDoneKindness", false);
                                userInfo.saveInBackground();
                            }
                        }
                    });
                }
            });

            return row;
        }
    }
}
