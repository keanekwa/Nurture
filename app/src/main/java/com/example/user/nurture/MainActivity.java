package com.example.user.nurture;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Button mGiveButton;
    private LinearLayout mReceiveButton;
    private ImageSwitcher mPlantie;
    private TextView mMeter;
    int imageIDs[]={R.drawable.plant_seed,R.drawable.plant_shoot,R.drawable.plant_seedling,R.drawable.plant_small,R.drawable.plant_withered};
    int messageCount=imageIDs.length;
    int currentIndex=0;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mPlantie.setImageResource(imageIDs[0]);

        //mPlantie.setImageResource(imageIDs[currentIndex]);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        if(ParseUser.getCurrentUser()!=null) actionBar.setTitle("Welcome, " + ParseUser.getCurrentUser().getUsername());
        actionBar.setIcon(R.drawable.nurturelogoicon);

        pb = (ProgressBar)findViewById(R.id.spinner);
        pb.setVisibility(View.GONE);
        mGiveButton = (Button) findViewById(R.id.GiveButton);
        mReceiveButton = (LinearLayout) findViewById(R.id.ReceiveButton);
        mPlantie = (ImageSwitcher)findViewById(R.id.Plant);
        mGiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GiveActivity.class);
                startActivity(intent);
            }
        });


        mPlantie.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });

        //animation
        Animation aniIn = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        aniIn.setDuration(3000);
        Animation aniOut = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        aniOut.setDuration(3000);

        // Declare the animations and initialize them
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);

        // set the animation type to imageSwitcher
        mPlantie.setInAnimation(in);
        mPlantie.setOutAnimation(out);
        refresh();
    }

    public void refresh() {
        pb.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
        if(ParseUser.getCurrentUser()!=null) query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() == 1) {
                    ParseObject userInfo = parseObjects.get(0);
                    if (userInfo.getString("receiver")==null && userInfo.getBoolean("hasDoneKindness")) {
                        userInfo.put("hasDoneKindness", false);
                        int currentPlantStage = userInfo.getInt("plantStage");
                        if(currentPlantStage<(messageCount-1)) userInfo.put("plantStage", currentPlantStage+1);
                        mPlantie.setImageResource(imageIDs[currentPlantStage]);
                        userInfo.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                pb.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        mPlantie.setImageResource(imageIDs[userInfo.getInt("plantStage")]);
                        pb.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
        else if (id == R.id.action_logout) {
            ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            });
        }
        else if (id==R.id.action_refresh){
            refresh();
        }
        else if (id==R.id.action_profile_main){
            Intent intent = new Intent(MainActivity.this, AchievementsActivity.class);
            MainActivity.this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
        pb.setVisibility(View.VISIBLE);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
        if(ParseUser.getCurrentUser()!=null) query.whereEqualTo("receiver", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() == 1) {
                    ParseObject userInfo = parseObjects.get(0);
                    if (userInfo.getString("kindnessToBeDone") != null && !userInfo.getBoolean("hasDoneKindness")) {
                        mReceiveButton.setEnabled(true);
                        mReceiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Dialog();
                                pb.setVisibility(View.GONE);
                            }
                        });
                    } else {
                        mReceiveButton.setEnabled(false);
                        pb.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    public void Dialog() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View mTextEntryView = li.inflate(R.layout.addreceiver_dialog_text_entry, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(mTextEntryView);

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.nurturelogoicon)
                .setTitle("Who helped you?")
                .setView(mTextEntryView);

        final EditText usernameEditText = (EditText)mTextEntryView.findViewById(R.id.helperUsernameEditText);
        builder.setTitle("Who showed you kindness today?");
        builder.setPositiveButton("Confirm", null);
        builder.setNegativeButton("Cancel", null);
        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button posbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                Button negbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                posbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
                        query.whereEqualTo("username", usernameEditText.getText().toString());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                if (e == null && parseObjects.size() == 1) {
                                    final ParseObject userInfo = parseObjects.get(0);
                                    userInfo.put("hasDoneKindness", true);
                                    userInfo.remove("receiver");
                                    userInfo.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            alertMessage(userInfo.getString("username") + "'s kindness has been recorded!");
                                            mReceiveButton.setEnabled(false);
                                        }
                                    });
                                } else {
                                    alertMessage("This user does not exist! Check the spelling.");
                                }
                            }
                        });
                    }
                });
                negbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.cancel();
                    }
                });
            }
        });
        alert.show();
    }

    public void alertMessage(String Message)
    {
        Toast.makeText(MainActivity.this, Message, Toast.LENGTH_SHORT).show();
    }

}
