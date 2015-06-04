package com.example.user.nurture;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Button mGiveButton;
    private Button mReceiveButton;
    private Button mNewButton;
    private ImageSwitcher mPlantie;
    private TextView mMeter;
    int imageIDs[]={R.drawable.plant_seed,R.drawable.plant_shoot,R.drawable.plant_seedling,R.drawable.plant_small,R.drawable.plant_big,R.drawable.plant_withered};
    int messageCount=imageIDs.length;
    int currentIndex=0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mPlantie.setImageResource(imageIDs[0]);

        //mPlantie.setImageResource(imageIDs[currentIndex]);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("Home");
        actionBar.setIcon(R.drawable.nurturelogoicon);

        mGiveButton = (Button) findViewById(R.id.GiveButton);
        mReceiveButton = (Button) findViewById(R.id.ReceiveButton);
        mPlantie = (ImageSwitcher)findViewById(R.id.Plant);
        mNewButton = (Button)findViewById(R.id.NewButton);
        mGiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GiveActivity.class);
                startActivity(intent);
            }
        });
        mNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex++;
                if(currentIndex==messageCount){
                    currentIndex=0;
                }
                mPlantie.setImageResource(imageIDs[currentIndex]);
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
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refresh();
            handler.postDelayed(runnable, 100);
        }
    };

    public void refresh(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("userInfo");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                if (e == null && parseObjects.size() == 1) {
                    ParseObject userInfo = parseObjects.get(0);
                    if (userInfo.getString("receiver")==null && userInfo.getBoolean("hasDoneKindness")) {
                        userInfo.put("hasDoneKindness", false);
                        userInfo.put("plantStage", 1+userInfo.getInt("plantStage"));
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
            ParseUser.getCurrentUser().logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
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
                            }
                        });
                    } else {
                        mReceiveButton.setEnabled(false);
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
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        final AlertDialog alert = builder.create();
        alert.show();

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
    }

    public void alertMessage(String Message)
    {
        Toast.makeText(MainActivity.this, Message, Toast.LENGTH_SHORT).show();
    }

}
