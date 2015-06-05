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
import android.view.MotionEvent;
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

    private LinearLayout mGiveButton;
    private LinearLayout mReceiveButton;
    private ImageSwitcher mPlantie;
    private TextView mMeter;
    private ImageView mGiveImage;
    private ImageView mReceiveImage;
    int imageIDs[]={R.drawable.plant_seed,R.drawable.plant_shoot,R.drawable.plant_seedling,R.drawable.plant_small,R.drawable.plant_withered};
    int messageCount=imageIDs.length;
    int currentIndex=0;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ParseUser.getCurrentUser()==null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        //mPlantie.setImageResource(imageIDs[0]);

        //mPlantie.setImageResource(imageIDs[currentIndex]);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        if(ParseUser.getCurrentUser()!=null) actionBar.setTitle("Welcome, " + ParseUser.getCurrentUser().getUsername());
        actionBar.setIcon(R.drawable.nurturelogoicon);

        pb = (ProgressBar)findViewById(R.id.spinner);
        pb.setVisibility(View.GONE);
        mGiveButton = (LinearLayout) findViewById(R.id.GiveButton);
        mReceiveButton = (LinearLayout) findViewById(R.id.ReceiveButton);
        mGiveImage = (ImageView) findViewById(R.id.giveImage);
        mReceiveImage = (ImageView) findViewById(R.id.receiveImage);
        mPlantie = (ImageSwitcher)findViewById(R.id.Plant);
        mGiveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mGiveImage.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.helpsomeone_1));
                }
                Intent intent = new Intent(MainActivity.this, GiveActivity.class);
                startActivity(intent);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mGiveImage.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.helpsomeone));
                }
                return false;
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
                    if (userInfo.getString("receiver") == null && userInfo.getBoolean("hasDoneKindness")) {
                        userInfo.put("hasDoneKindness", false);
                        int currentPlantStage = userInfo.getInt("plantStage");
                        if (currentPlantStage < (messageCount - 1))
                            userInfo.put("plantStage", currentPlantStage + 1);
                        mPlantie.setImageResource(imageIDs[currentPlantStage]);
                        userInfo.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                pb.setVisibility(View.GONE);
                            }
                        });
                    }
                    else {
                        mPlantie.setImageResource(imageIDs[userInfo.getInt("plantStage")]);
                        pb.setVisibility(View.GONE);
                    }
                }
            }
        });

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("userInfo");
        if(ParseUser.getCurrentUser()!=null) query1.whereEqualTo("receiver", ParseUser.getCurrentUser().getUsername());
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null) {
                    boolean buttonActivated = false;
                    if(parseObjects.size() > 0) {
                        for (ParseObject i : parseObjects) {
                            if (i.getString("kindnessToBeDone") != null && !i.getBoolean("hasDoneKindness"))
                                buttonActivated = true;
                        }
                    }
                    if (buttonActivated) {
                        //Kindness to be done, but has not been done
                        mReceiveButton.setEnabled(true);
                        mReceiveImage.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.receivehelp));
                        mReceiveButton.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                                    mReceiveImage.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.receivehelp_3));
                                }
                                if (event.getAction() == MotionEvent.ACTION_UP) {
                                    mReceiveImage.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.receivehelp));
                                    Dialog();
                                }
                                pb.setVisibility(View.GONE);
                                return false;
                            }
                        });
                    } else {
                        mReceiveImage.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.receivehelp_2));
                        mReceiveButton.setEnabled(false);
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

    public void Dialog() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View mTextEntryView = li.inflate(R.layout.addreceiver_dialog_text_entry, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(mTextEntryView);
        builder.setIcon(R.drawable.nurturelogoicon);

        final EditText usernameEditText = (EditText)mTextEntryView.findViewById(R.id.helperUsernameEditText);
        builder.setTitle("Who showed you kindness today?");
        builder.setPositiveButton("Confirm", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton){
                dialog.cancel();
            }
        });
        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button posbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                posbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
                        query.whereEqualTo("username", usernameEditText.getText().toString());
                        query.whereEqualTo("receiver", ParseUser.getCurrentUser().getUsername());
                        query.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> parseObjects, ParseException e) {
                                if (e == null && parseObjects.size() == 1) {
                                    final ParseObject userInfo = parseObjects.get(0);
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                                    String strToShow = userInfo.getString("kindnessToBeDone");
                                    strToShow = strToShow.replace("him/her", "you");
                                    strToShow = strToShow.replace("his/her", "your");
                                    strToShow = strToShow.replace("!", "");
                                    // set dialog message
                                    alertDialogBuilder
                                            .setTitle("Act Of Kindness")
                                            .setMessage("Did "+userInfo.getString("username")+" "+strToShow+"?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yup",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface bloop,int id) {
                                                    // if this button is clicked, close
                                                    // current activity
                                                    userInfo.put("hasDoneKindness", true);
                                                    userInfo.remove("receiver");
                                                    userInfo.remove("kindnessToBeDone");
                                                    userInfo.put("kindnessCount", (1 + userInfo.getInt("kindnessCount")));
                                                    userInfo.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Achievements");
                                                            boolean toAddAchievement;
                                                            int blah = userInfo.getInt("kindnessCount");
                                                            switch (blah){
                                                                case 1: query.whereEqualTo("achID", 1);
                                                                    toAddAchievement = true;
                                                                    break;
                                                                case 5: query.whereEqualTo("achID", 5);
                                                                    toAddAchievement = true;
                                                                    break;
                                                                case 10: query.whereEqualTo("achID", 10);
                                                                    toAddAchievement = true;
                                                                    break;
                                                                case 20: query.whereEqualTo("achID", 20);
                                                                    toAddAchievement = true;
                                                                    break;
                                                                default:
                                                                    toAddAchievement = false;
                                                                    break;
                                                            }
                                                            if(toAddAchievement){
                                                                query.findInBackground(new FindCallback<ParseObject>() {
                                                                    @Override
                                                                    public void done(List<ParseObject> parseObjects, ParseException e) {
                                                                        if(e==null && parseObjects.size()==1){
                                                                            ParseObject achievement = parseObjects.get(0);
                                                                            achievement.addUnique("usernames", userInfo.getString("username"));
                                                                            achievement.saveInBackground(new SaveCallback() {
                                                                                @Override
                                                                                public void done(ParseException e) {
                                                                                    alertMessage(userInfo.getString("username") + "'s kindness has been recorded!");
                                                                                }
                                                                            });
                                                                        }
                                                                    }
                                                                });
                                                            }
                                                            else alertMessage(userInfo.getString("username") + "'s kindness has been recorded!");
                                                            mReceiveImage.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.receivehelp_2));
                                                            mReceiveButton.setEnabled(false);
                                                            dialog.cancel();
                                                        }
                                                    });
                                                }
                                            })
                                            .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    // if this button is clicked, just close
                                                    // the dialog box and do nothing
                                                    dialog.cancel();
                                                }
                                            });

                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder.create();

                                    // show it
                                    alertDialog.show();

                                } else {
                                    alertMessage("Error, please check your spelling.");
                                }
                            }
                        });
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
