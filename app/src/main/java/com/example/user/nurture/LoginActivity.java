package com.example.user.nurture;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity {

    TextView mSignUpTextView;
    EditText user;
    EditText pass;
    String mUsername;
    String mPassword;
    Button mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
<<<<<<< HEAD
=======
<<<<<<< HEAD

=======
        //getActionBar().hide();
>>>>>>> origin/master
>>>>>>> origin/master

        mSignUpTextView = (TextView) findViewById(R.id.signUpTextView);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        user = (EditText) findViewById(R.id.userEditText);
        pass = (EditText) findViewById(R.id.passEditText);

        mLogin = (Button) findViewById(R.id.loginButton);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = user.getText().toString();
                mPassword = pass.getText().toString();
                if (mUsername.equals("") | mPassword.equals("")){
                    alertMessage("Please fill in the empty fields.");
                    //checks for empty fields
                }
                else {
                    try {
                        ParseUser.logIn(mUsername, mPassword);
                    } catch (ParseException e) {
                        alertMessage("Login failed. Please try again.");
                    }
            }
        }});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void alertMessage(String Message)
    {
        Toast.makeText(LoginActivity.this, Message, Toast.LENGTH_SHORT).show();
    }
}
