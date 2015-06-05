package com.example.user.nurture;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.parse.ParseUser;


public class MainActivity extends ActionBarActivity {

    private Button mGiveButton;
    private Button mReceiveButton;
    private Button mNewButton;
    private ImageSwitcher mPlantie;
    private TextView mMeter;
    int imageIDs[]={R.drawable.image1,R.drawable.image2,R.drawable.image3};
    int messageCount=imageIDs.length;
    int currentIndex=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkLogin();

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

    public void checkLogin(){
        //checks if userBlank is logged in
        if (ParseUser.getCurrentUser()==null)
        {
            Intent intent = new Intent (this, LoginActivity.class);
            this.startActivity(intent);

        }


    }
}
