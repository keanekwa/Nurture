package com.example.user.nurture;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.util.List;


public class ReceiveActivity extends ActionBarActivity {
    private ImageView mProfilePic;
    private TextView mNameTextView;
    private TextView mSchoolTextView;

    private TextView mDoingTextView;
    private Button mDoneButton;

    private ParseUser giver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);

        ////////////////////////
        // set user "profile" //
        ////////////////////////
        mProfilePic = (ImageView)findViewById(R.id.profilePic);
        mNameTextView = (TextView)findViewById(R.id.nameTextView);
        mSchoolTextView = (TextView)findViewById(R.id.schoolTextView);

        mDoingTextView = (TextView)findViewById(R.id.doingTextView);
        mDoneButton = (Button)findViewById(R.id.doneButton);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
        query.whereEqualTo("receiver", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if(e==null && parseObjects.size()==1){
                    final ParseObject giverUserInfo = parseObjects.get(0);
                    final String giverUsername = giverUserInfo.getString("username");
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", giverUsername);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        @Override
                        public void done(List<ParseUser> users, ParseException e) {
                            if(e==null && users.size()==1){
                                giver = users.get(0);
                                mNameTextView.setText(giver.getUsername());
                                mSchoolTextView.setText(giver.getString("school"));
                                //TODO: profile pic

                                //////////////////////
                                // set bottom stuff //
                                //////////////////////
                                mDoingTextView.setText(giverUserInfo.getString("kindnessToBeDone"));
                                mDoneButton.setText(giverUsername+" has shown me kindness!");
                                mDoneButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        giverUserInfo.put("hasDoneKindness", true);
                                        giverUserInfo.saveInBackground();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_receive, menu);
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
