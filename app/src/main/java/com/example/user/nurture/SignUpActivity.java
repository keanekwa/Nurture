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
    ArrayList<String> mSchoolList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mSchoolList = new ArrayList<>(Arrays.asList("Admiralty Primary School","Ahmad Ibrahim Primary School","Ai Tong School","Alexandra Primary School","Anchor Green Primary School","Anglo-Chinese School (Junior)","Anglo-Chinese School (Primary)","Angsana Primary School", "Balestier Hill Primary School", "Beacon Primary School ", "Bedok Green Primary School", "Bendemeer Primary School", "Blangah Rise Primary School", "Boon Lay Garden Primary School", "Bukit Panjang Primary School", "Bukit Timah Primary School", "Bukit View Primary School", "Canberra Primary School", "Canossa Convent Primary School", "Cantonment Primary School", "Casuarina Primary School", "Catholic High", "Cedar Primary School", "Changkat Primary School", "CHIJ (Katong) Primary School", "CHIJ (Kellock) ", "CHIJ (Our Lady of Good Counsel) ", "CHIJ (Our Lady of the Nativity) ", "CHIJ (Our Lady Queen of Peace) ", "CHIJ Primary (Toa Payoh) ", "Chongfu Primary School", "Chongzheng Primary School", "Chua Chu Kang Primary School", "Clementi Primary School", "Compassvale Primary School", "Concord Primary School", "Coral Primary School", "Corporation Primary School", "Damai Primary School", "Dazhong Primary School", "De La Salle School", "East Coast Primary School", "East Spring Primary School", "East View Primary School", "Edgefield Primary School", "Elias Park Primary School", "Endeavour Primary School", "Eunos Primary School", "Evergreen Primary School", "Fairfield Methodist School (Primary) ", "Farrer Park Primary School", "Fengshan Primary School", "Fernvale Primary School", "First Toa Payoh Primary School", "Frontier Primary School", "Fuchun Primary School ", "Fuhua Primary School", "Gan Eng Seng Primary School", "Geylang Methodist School (Primary) ", "Gongshang Primary School", "Greendale Primary School", "Greenridge Primary School", "Greenwood Primary School", "Guangyang Primary School", "Haig Girls’ School", "Henry Park Primary School", "Holy Innocents’ Primary School", "Hong Wen School", "Horizon Primary School", "Hougang Primary School", "Huamin Primary School", "Innova Primary School ", "Jiemin Primary School", "Junyuan Primary School", "Jurong Primary School", "Jurong West Primary School", "Juying Primary School", "Keming Primary School", "Kheng Cheng School", "Kong Hwa School", "Kranji Primary School", "Kuo Chuan Presbyterian Primary School", "Lakeside Primary Schoolv", "Lianhua Primary School", "Loyang Primary School", "MacPherson Primary School", "Maha Bodhi School", "Maris Stella High (Primary) ", "Marsiling Primary School", "Marymount Convent School", "Mee Toh School", "Meridian Primary School", "Methodist Girls’ School (Primary) ", "Montfort Junior School", "Nan Chiau Primary School", "Nan Hua Primary School", "Nanyang Primary School", "Naval Base Primary School", "New Town Primary School", "Ngee Ann Primary School", "North Spring Primary School", "North View Primary School", "North Vista Primary School", "Northland Primary School", "Northoaks Primary School", "Oasis Primary School", "Opera Estate Primary School", "Palm View Primary School", "Park View Primary School", "Pasir Ris Primary School", "Paya Lebar Methodist Girls’ School (Primary) ", "Pei Chun Public School", "Pei Hwa Presbyterian Primary School", "Pei Tong Primary School", "Peiying Primary School", "Pioneer Primary School", "Poi Ching School", "Princess Elizabeth Primary School", "Punggol Cove Primary School", "Punggol Green Primary School", "Punggol Primary School", "Punggol View Primary School", "Qifa Primary School", "Qihua Primary School", "Queenstown Primary School", "Radin Mas Primary School", "Raffles Girls’ Primary School", "Red Swastika School", "River Valley Primary School", "Riverside Primary School", "Rivervale Primary School", "Rosyth School", "Rulang Primary School", "S’pore Chinese Girls’ (Primary) ", "Sembawang Primary School", "Seng Kang Primary School", "Sengkang Green Primary School", "Shuqun Primary School", "Si Ling Primary School", "South View Primary School", "Springdale Primary School", "St Andrew’s Junior School", "St Anthony’s Canossian Primary School", "St Anthony’s Primary School", "St Gabriel’s Primary School", "St Hilda’s Primary School", "St Joseph’s Institution Junior", "St Margaret’s Primary School", "St Stephen’s School", "Stamford Primary School", "Tampines North Primary School", "Tampines Primary School", "Tanjong Katong Primary School", "Tao Nan School", "Teck Whye Primary School", "Telok Kurau Primary School", "Temasek Primary School", "Unity Primary School", "Waterway Primary School", "Wellington Primary School", "West Grove Primary School", "West Spring Primary School", "West View Primary School", "Westwood Primary School", "White Sands Primary School", "Woodgrove Primary School", "Woodlands Primary School", "Woodlands Ring Primary School", "Xinghua Primary School", "Xingnan Primary School", "Xinmin Primary School", "Xishan Primary School", "Yangzheng Primary School", "Yew Tee Primary School", "Yio Chu Kang Primary School", "Yishun Primary School", "Yu Neng Primary School", "Yuhua Primary School", "Yumin Primary School", "Zhangde Primary School", "Zhenghua Primary School", "Zhonghua Primary School"));

        mUsernameBlank = (EditText) findViewById(R.id.UsernameBlank);
        mPasswordBlank = (EditText) findViewById(R.id.PasswordBlank);
        mCPasswordBlank = (EditText) findViewById(R.id.PasswordBlank2);
        mEmailAddBlank = (EditText) findViewById(R.id.EmailBlank);
        mSchoolBlank = (EditText) findViewById(R.id.SchoolBlank);

        mAddAccount = (Button) findViewById(R.id.CreateButton);
        mAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameInput = mUsernameBlank.getText().toString();
                passwordInput = mPasswordBlank.getText().toString();
                cPasswordInput = mCPasswordBlank.getText().toString();
                emailInput = mEmailAddBlank.getText().toString();

                if (usernameInput.equals("") | passwordInput.equals("") | cPasswordInput.equals("") | emailInput.equals("")){
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
                .setItems(mSchoolList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                })
                .show();
    }
}
