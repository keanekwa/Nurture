package com.example.user.nurture;

import android.app.Application;
import android.os.Bundle;
import com.parse.Parse;

public class NurtureApplication extends Application {
    protected void onCreate(Bundle savedInstanceState) {
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "Zctf49We7TfLhWb68KJowNSBLYaSk84tSynUJPPE", "EUZqqBYGF9H4f88kZgLQ1TpJAlFrQ1j32tawxX5s");
    }
}
