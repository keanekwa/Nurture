package com.example.user.nurture;

import android.app.Application;

import com.parse.Parse;

public class NurtureApplication extends Application {
    @Override
    public void onCreate() {
<<<<<<< HEAD
=======
        super.onCreate();
>>>>>>> origin/master
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Zctf49We7TfLhWb68KJowNSBLYaSk84tSynUJPPE", "EUZqqBYGF9H4f88kZgLQ1TpJAlFrQ1j32tawxX5s");
    }
}
