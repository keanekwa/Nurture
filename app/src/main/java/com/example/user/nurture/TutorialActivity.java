package com.example.user.nurture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;


public class TutorialActivity extends ActionBarActivity {

    int NEWimageIDs[]={R.drawable.tutorial_home,R.drawable.tutorial_achievement,R.drawable.tutorial_give,R.drawable.tutorial_receive};
    int messageCount=NEWimageIDs.length;
    int currentIndex=-1;
    Button mButton;
    private ImageSwitcher mTutorialImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        mTutorialImage = (ImageSwitcher)findViewById(R.id.tutorialImage);
        mButton = (Button) findViewById(R.id.StartButton);

        mTutorialImage.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {

                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("currentIndex", Integer.toString(currentIndex));

                currentIndex++;

                if (currentIndex == messageCount) {
                    Intent intent = new Intent(TutorialActivity.this, LoginActivity.class);
                    startActivity(intent);
                    currentIndex = 0;
                } else {
                    mTutorialImage.setImageResource(NEWimageIDs[currentIndex]);
                }
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
        mTutorialImage.setInAnimation(in);
        mTutorialImage.setOutAnimation(out);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
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
