package com.example.user.nurture;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;


public class SignUpActivity extends ActionBarActivity {

    EditText mUsernameBlank;
    EditText mPasswordBlank;
    EditText mCPasswordBlank;
    EditText mEmailAddBlank;
    EditText mSchoolBlank;
    String usernameInput;
    String passwordInput;
    String schoolInput;
    String cPasswordInput;
    String emailInput;
    Button mAddAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mUsernameBlank = (EditText) findViewById(R.id.UsernameBlank);
        mPasswordBlank = (EditText) findViewById(R.id.PasswordBlank);
        mCPasswordBlank = (EditText) findViewById(R.id.PasswordBlank2);
        mEmailAddBlank = (EditText) findViewById(R.id.EmailBlank);
        mSchoolBlank = (EditText) findViewById(R.id.SchoolBlank);

        mSchoolBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
        });

        mAddAccount = (Button) findViewById(R.id.CreateButton);
        mAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameInput = mUsernameBlank.getText().toString();
                passwordInput = mPasswordBlank.getText().toString();
                cPasswordInput = mCPasswordBlank.getText().toString();
                emailInput = mEmailAddBlank.getText().toString();

                if (usernameInput.equals("") | passwordInput.equals("") | cPasswordInput.equals("") | emailInput.equals("")|schoolInput.equals("")){
                    alertMessage("Please fill in the empty fields.");
                    //checks for empty fields
                }
                else if (!passwordInput.equals(cPasswordInput)){
                    alertMessage("Your passwords do not match. Please reenter.");
                    mPasswordBlank.setText("");
                    mCPasswordBlank.setText("");

                }
                else {
                    ParseUser userObject = new ParseUser();
                    userObject.setUsername(usernameInput);
                    userObject.setPassword(passwordInput);
                    userObject.setEmail(emailInput);
                    userObject.signUpInBackground();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    SignUpActivity.this.startActivity(intent);
                }
            }});



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
        Toast.makeText(SignUpActivity.this, Message, Toast.LENGTH_SHORT).show();
    }

    public void Dialog() {
        new AlertDialog.Builder(this)
                .setTitle("Choose your school")
                .setItems(R.array.mSchoolList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] array = getResources().getStringArray(R.array.mSchoolList);
                        ArrayList<String> l =new ArrayList<>();
                        l.equals(array);
                        schoolInput = l.get(which);
                        mSchoolBlank.setText(schoolInput);
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }
}
