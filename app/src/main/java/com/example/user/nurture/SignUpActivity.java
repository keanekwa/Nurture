package com.example.user.nurture;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.Arrays;
import java.util.List;


public class SignUpActivity extends Activity {

    EditText mUsernameBlank;
    EditText mPasswordBlank;
    EditText mCPasswordBlank;
    EditText mEmailAddBlank;
    Button mSchoolBlank;
    String usernameInput;
    String passwordInput;
    String schoolInput;
    String roleInput;
    String cPasswordInput;
    String emailInput;
    Button mAddAccount;
    Button mRoleBlank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mUsernameBlank = (EditText) findViewById(R.id.UsernameBlank);
        mPasswordBlank = (EditText) findViewById(R.id.PasswordBlank);
        mCPasswordBlank = (EditText) findViewById(R.id.PasswordBlank2);
        mEmailAddBlank = (EditText) findViewById(R.id.EmailBlank);
        mSchoolBlank = (Button) findViewById(R.id.SchoolBlank);
        mRoleBlank = (Button) findViewById(R.id.roleButton);

        mSchoolBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();


            }
        });

        mRoleBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog2();


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
                    userObject.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null) {
                                final ParseObject newUserInfo = new ParseObject("userInfo");
                                newUserInfo.put("username", usernameInput);
                                newUserInfo.put("hasDoneKindness", false);
                                newUserInfo.put("plantStage", 0);
                                newUserInfo.put("kindnessCount", 0);
                                newUserInfo.put("school", schoolInput);
                                newUserInfo.put("role", roleInput);
                                byte[] data = "default_profile.png".getBytes();
                                final ParseFile profImg = new ParseFile("profilePic", data);
                                profImg.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        newUserInfo.put("profilePic", profImg);
                                    }
                                });

                                newUserInfo.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        ParseUser.logInInBackground(usernameInput, passwordInput, new LogInCallback() {
                                            public void done(ParseUser user, ParseException e) {
                                                if (user != null && e == null) {
                                                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                    SignUpActivity.this.startActivity(intent);
                                                } else {
                                                    alertMessage(e.toString());
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
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
         AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
         builder.setTitle("Choose your school");
         builder.setItems(R.array.mSchoolList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<String> array = Arrays.asList(getResources().getStringArray(R.array.mSchoolList));
                        schoolInput = array.get(which);
                        mSchoolBlank.setText(schoolInput);
                    }
                });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void Dialog2() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Choose your role");
        builder.setItems(R.array.mRoleList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> array = Arrays.asList(getResources().getStringArray(R.array.mRoleList));
                roleInput = array.get(which);
                mRoleBlank.setText(roleInput);
            }
        });
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
