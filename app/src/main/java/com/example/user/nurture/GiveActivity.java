package com.example.user.nurture;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;


public class GiveActivity extends ActionBarActivity {
    private ImageView mProfilePic;
    private TextView mNameTextView;
    private TextView mClassTextView;
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private ArrayList<String> listOfKindness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give);

        ////////////////////////
        // set user "profile" //
        ////////////////////////
        mProfilePic = (ImageView)findViewById(R.id.profilePic);
        mNameTextView = (TextView)findViewById(R.id.nameTextView);
        mClassTextView = (TextView)findViewById(R.id.classTextView);

        Intent intent = getIntent();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
        query.whereEqualTo("username", intent.getStringExtra("USERNAME"));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null && parseObjects.size()==1){
                    ParseObject userInfo = parseObjects.get(0);
                    mNameTextView.setText(userInfo.getString("username"));
                    mClassTextView.setText(userInfo.getString("class"));
                    //mProfilePic.setImageDrawable(userInfo.getFile("profilePic"));
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
                    mAdapter = new ArrayAdapter<>(GiveActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, listOfKindness);
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
}
