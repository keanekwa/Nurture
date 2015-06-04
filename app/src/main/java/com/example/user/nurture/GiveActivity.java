package com.example.user.nurture;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.Random;


public class GiveActivity extends ActionBarActivity {
    private ImageView mProfilePic;
    private TextView mNameTextView;
    private TextView mSchoolTextView;
    private TextView mRoleTextView;

    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private ArrayList<String> listOfKindness;

    private ParseObject curUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Give");
        /////////////////////////////
        // set userBlank "profile" //
        /////////////////////////////
        mProfilePic = (ImageView)findViewById(R.id.profilePic);
        mNameTextView = (TextView)findViewById(R.id.nameTextView);
        mSchoolTextView = (TextView)findViewById(R.id.schoolTextView);
        mRoleTextView = (TextView)findViewById(R.id.roleTextView);
        mListView = (ListView)findViewById(android.R.id.list);
        listOfKindness = new ArrayList<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
        if(ParseUser.getCurrentUser()!=null)query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null && parseObjects.size()==1){
                    curUserInfo = parseObjects.get(0);
                    String receiverUsername = curUserInfo.getString("receiver");

                    if(receiverUsername==null){
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
                        query.whereEqualTo("school", curUserInfo.getString("school"));
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                if(e==null && parseObjects.size()>0) {
                                    parseObjects.remove(curUserInfo);
                                    final ParseObject theOne = parseObjects.get(randInt(0, parseObjects.size() - 1));
                                    curUserInfo.put("receiver", theOne.getString("username"));
                                    curUserInfo.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            setProfile(theOne.getString("username"));
                                        }
                                    });
                                }
                                else if(e==null && parseObjects.size()==0){
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
                                    query.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> parseObjects, ParseException e) {
                                            if (e == null) {
                                                parseObjects.remove(curUserInfo);
                                                final ParseObject theOne = parseObjects.get(randInt(0, parseObjects.size() - 1));
                                                curUserInfo.put("receiver", theOne.getString("username"));
                                                curUserInfo.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        setProfile(theOne.getString("username"));
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    else {
                        setProfile(receiverUsername);
                    }
                    //////////////////
                    // set listview //
                    //////////////////
                    if(curUserInfo.getString("kindnessToBeDone") == null) setKindnessList();
                    else setKindnessText();
                }
            }
        });
    }

    public void setProfile(String username){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                if (e == null && users.size() == 1) {
                    ParseObject receiverInfo = users.get(0);
                    mNameTextView.setText(Html.fromHtml(receiverInfo.getString("username")));
                    mSchoolTextView.setText(receiverInfo.getString("school"));
                    mRoleTextView.setText(receiverInfo.getString("role"));
                    //TODO: profile pic
                }
            }
        });
    }

    public void setKindnessList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("suggestedKindness");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    listOfKindness.clear();
                    for (ParseObject i : parseObjects) {
                        listOfKindness.add(i.getString("kindness"));
                    }
                    mAdapter = new SuggestedAdapter(GiveActivity.this, R.layout.suggested_list_item, listOfKindness, true, "");
                    mListView.setAdapter(mAdapter);
                }
            }
        });
    }

    public void setKindnessText(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("suggestedKindness");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    listOfKindness.clear();
                    for (ParseObject i : parseObjects) {
                        listOfKindness.add(i.getString("kindness"));
                    }
                    String toBeDone = curUserInfo.getString("kindnessToBeDone");
                    listOfKindness.remove(toBeDone);
                    listOfKindness.add(0, toBeDone);
                    mAdapter = new SuggestedAdapter(GiveActivity.this, R.layout.suggested_list_item, listOfKindness, false, toBeDone);
                    mListView.setAdapter(mAdapter);
                }
            }
        });
    }

    public static int randInt(int min, int max) {
        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max - min) + 1) + min;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_give, menu);
        return super.onCreateOptionsMenu(menu);
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
        private boolean mIsList;
        private String mKindnessToDo;

        public SuggestedAdapter(Context context, int resource, ArrayList<String> suggestedKindnesses, boolean isList, String kindnessToDo){
            super(context, resource, suggestedKindnesses);
            this.mResource = resource;
            this.mSuggested = suggestedKindnesses;
            this.mIsList = isList;
            this.mKindnessToDo = kindnessToDo;
        }

        @Override
        public View getView(int position, View row, ViewGroup parent){
            if (row==null){
                row = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
            }

            TextView suggestedTextView = (TextView)row.findViewById(R.id.suggestedTextView);
            TextView doingThisTextView = (TextView)row.findViewById(R.id.doingThisTextView);
            final String kindnessSuggestion = mSuggested.get(position);
            suggestedTextView.setText(kindnessSuggestion);
            Button doItButton = (Button)row.findViewById(R.id.doItButton);

            if(!mIsList && kindnessSuggestion.equals(mKindnessToDo)) {
                suggestedTextView.setTextColor(Color.WHITE);
                row.setBackgroundColor(Color.parseColor("#3BB94B"));
                doItButton.setVisibility(View.GONE);
                doingThisTextView.setVisibility(View.VISIBLE);
                doingThisTextView.setText("Doing this!");
            }
            else if (!mIsList) {
                suggestedTextView.setTextColor(Color.parseColor("#6D6D6D"));
                row.setBackgroundColor(Color.parseColor("#EEEEEE"));
                doItButton.setText("Do This Instead!");
                doItButton.setVisibility(View.VISIBLE);
                doingThisTextView.setVisibility(View.GONE);
            }
            else{
                suggestedTextView.setTextColor(Color.parseColor("#6D6D6D"));
                row.setBackgroundColor(Color.parseColor("#EEEEEE"));
                doItButton.setText("Do This!");
                doItButton.setVisibility(View.VISIBLE);
                doingThisTextView.setVisibility(View.GONE);
            }

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
                                        setKindnessText();
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
