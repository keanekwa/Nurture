package com.example.user.nurture;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
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

import com.parse.ParseException;
import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity {

    private Button mGiveButton;
    private Button mReceiveButton;
    private Button mNewButton;
    private ImageSwitcher mPlantie;
    private TextView mMeter;
    int imageIDs[]={R.drawable.plant_seed,R.drawable.plant_shoot,R.drawable.plant_seedling,R.drawable.plant_small,R.drawable.plant_big};
    int messageCount=imageIDs.length;
    int currentIndex=0;
    View mTextEntryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
<<<<<<< HEAD
        checkLogin();
        //mPlantie.setImageResource(imageIDs[0]);
=======
        //mPlantie.setImageResource(imageIDs[currentIndex]);
>>>>>>> origin/master

        mGiveButton = (Button) findViewById(R.id.GiveButton);
        mReceiveButton = (Button) findViewById(R.id.ReceiveButton);
        mPlantie = (ImageSwitcher)findViewById(R.id.Plant);
        mMeter = (TextView) findViewById(R.id.Meter);
        mNewButton = (Button)findViewById(R.id.NewButton);
        mGiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GiveActivity.class);
                startActivity(intent);
            }
        });
        mReceiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReceiveActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        else if (id == R.id.action_logout) {
            ParseUser.getCurrentUser().logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void Dialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        mTextEntryView = factory.inflate(R.layout.addreceiver_dialog_text_entry, null);

        new AlertDialog.Builder(this)
                .setIcon(R.drawable.nurturelogoicon)
                .setTitle("Who helped you?")
                .setView(mTextEntryView)

                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        positiveButton();
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }

    public void positiveButton()
    {
        EditText mUsernameField = (EditText) mTextEntryView.findViewById(R.id.helperUsernameEditText);
        String usernameInput = mUsernameField.getText().toString();
        if (usernameInput.equals("")){
            alertMessage("Please fill in the empty field.");
            //checks for empty fields
        }
        else {
            //TODO: Use ParseQuery and check the username.

        }
    }

    public void alertMessage(String Message)
    {
        Toast.makeText(MainActivity.this, Message, Toast.LENGTH_SHORT).show();
    }

}
