package com.example.snwak_000.ghn;

import android.app.DatePickerDialog;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.text.*;
import java.util.*;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.fingerprint.FingerprintManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.*;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCognitoIdentityProvider;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.google.ads.AdRequest;
//import com.example.snwak_000.CognitoYourUserPoolsDemo.R;


public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    int day, month, year, dayFinal, monthFinal, yearFinal;
    String emailFinal, passwordFinal, usernameFinal, genderFinal;
    boolean genderChecked;
    EditText email;
    EditText password;
    EditText username;
    EditText dob;
    RadioGroup gender;
    RadioButton male;
    RadioButton female;
    Button signUp;
    TextView errorMessage;
    TextView signUpMessage;
    private AlertDialog userDialog;
    private ProgressBar waitDialog = null;
    private String usernameInput, userPassword, passwordInput, emailInput, dobInput, genderInput;
    int radiobutton;        //checked radio button id
    private static int reqCode = 10;
    RadioButton rb; //checked radio button
    AppHelper helper;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /* Assigning Variables to IDs*/
        helper = new AppHelper();
        waitDialog = (ProgressBar) findViewById(R.id.signUpProgressBar);
        email = (EditText) findViewById(R.id.emailSignUp);
        password = (EditText) findViewById(R.id.passwordSignUp);
        username = (EditText) findViewById(R.id.usernameSignUp);
        dob = (EditText)findViewById(R.id.DOBSignUp);
        gender = (RadioGroup)findViewById(R.id.radioGroup);
        male = (RadioButton)findViewById(R.id.maleRadioButton);
        female = (RadioButton) findViewById(R.id.femaleRadioButton);
        signUp = (Button)findViewById(R.id.signUpButton);
        errorMessage = (TextView) findViewById(R.id.signUpErrorMessage);
        signUpMessage = (TextView) findViewById(R.id.signingUpMessage);
        genderChecked = false;

        init();

    }
    public void init(){

        dob.setOnClickListener(new View.OnClickListener() {         //adding listener to date of birth button

             @Override
             public void onClick(View view) {

                 Calendar c = Calendar.getInstance();
                 year = c.get(Calendar.YEAR);
                 month = c.get(Calendar.MONTH);
                 day = c.get(Calendar.DAY_OF_MONTH);

                 DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this, SignUpActivity.this, year, month, day);
                 datePickerDialog.show();

             }
        });

        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                radiobutton = gender.getCheckedRadioButtonId();
                rb = (RadioButton) findViewById(radiobutton);
                genderChecked = true;
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email.setBackground(getDrawable(R.drawable.text_border_valid));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                emailInput = email.getText().toString();
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                username.setBackground(getDrawable(R.drawable.text_border_valid));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                usernameInput = username.getText().toString();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password.setBackground(getDrawable(R.drawable.text_border_valid));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                passwordInput = password.getText().toString();
                userPassword = passwordInput;
            }
        });

        dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                dob.setBackground(getDrawable(R.drawable.text_border_valid));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                dobInput = dob.getText().toString();
            }
        });


        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                rb = (RadioButton) gender.findViewById(i);
                gender.setBackground(getDrawable(R.drawable.text_border_valid));
                switch (i){
                    case R.id.maleRadioButton:
                        genderChecked = true;
                        genderFinal = "Male";
                        break;
                    case R.id.femaleRadioButton:
                        genderChecked = true;
                        genderFinal = "Female";
                        break;
                }
            }
        });

         signUp.setOnClickListener(new View.OnClickListener() {
             private String userPoolId = "us-east-2_tG8CtKfyN";

             private String clientId = "9iv0ttetr96u8jlte069dvg5c";

             private String clientSecret = "odp9oflji3noaspkijrv7ovq6suld1bj1tbkp8gs047bpe9jj3g";

             private Regions cognitoRegion = Regions.US_EAST_2;



             @Override
             public void onClick(View view) {

                 // Read user data and register

                 CognitoUserAttributes userAttributes = new CognitoUserAttributes();
                 CognitoUserPool userPool = new CognitoUserPool(context, userPoolId, clientId, clientSecret, cognitoRegion);


                 if (usernameInput.isEmpty()) {
                     errorMessage.setText("Enter a valid username");
                     username.setBackground(getDrawable(R.drawable.text_border_error));
                     return;
                 }


                 if (passwordInput == null || passwordInput.isEmpty()) {
                     errorMessage.setText("Enter a valid password");
                     password.setBackground(getDrawable(R.drawable.text_border_error));
                     return;
                 }


                 if(emailInput == null || emailInput.isEmpty()){
                     errorMessage.setText("Enter a valid email");
                     email.setBackground(getDrawable(R.drawable.text_border_error));
                     return;
                 }


                 //String dobFinal = "" + yearFinal + monthFinal + dayFinal;

                 if( dobInput == null || dobInput.isEmpty()){
                     errorMessage.setText("Enter valid date of birth");
                     dob.setBackground(getDrawable(R.drawable.text_border_error));
                     return;
                 }
                     //userAttributes.addAttribute(dob.getHint().toString(), userInput);
                     //userAttributes.addAttribute(AppHelper.getSignUpFieldsC2O().get(dob.getHint()).toString(), userInput);

                 if(genderChecked == false){
                     errorMessage.setText("Select gender");
                     gender.setBackground(getDrawable(R.drawable.text_border_error));
                     return;
                 }else {
                     if (genderFinal.equalsIgnoreCase(male.getText().toString())) {
                         genderInput = "male";
                     } else {
                         genderInput = "female";
                     }
                 }

                 userAttributes.addAttribute("email", emailInput);
                 userAttributes.addAttribute("gender", genderInput);

                 SignUpHandler signUpHandler = new SignUpHandler() {

                     @Override
                     public void onSuccess(CognitoUser cognitoUser, boolean signUpConfirmationState, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
                         // Check signUpConfirmationState to see if the user is already confirmed
                         closeWaitDialog();
                         //Boolean regState = signUpConfirmationState;
                         if (signUpConfirmationState) {
                             // User is already confirmed
                             showDialogMessage("Sign up successful!",usernameInput+" has been Confirmed", true);
                         } else {
                             // User is not confirmed
                             confirmSignUp(cognitoUserCodeDeliveryDetails);
                             showDialogMessage("A confirmation email has been sent to ",emailInput +" has been Confirmed", true);
                         }
                     }

                     @Override
                     public void onFailure(Exception e) {
                         closeWaitDialog();
                         errorMessage.setText("Sign up failed");
                         //username.setBackground(getDrawable(R.drawable.text_border_error));
                         //showDialogMessage("Sign up failed",AppHelper.formatException(e),false);
                         showDialogMessage("Sign up failed",AppHelper.formatException(e),false);

                     }
                 };



                 showWaitDialog();
                 userPool.signUpInBackground(usernameInput,passwordInput,userAttributes, null, signUpHandler);
                 //next();

                 //AppHelper.getPool().signUpInBackground(usernameInput, passwordInput, userAttributes, null, signUpHandler);
                 //helper.getPool().signUpInBackground(usernameInput, passwordInput, userAttributes, null, signUpHandler);
                 //helper.getPool().signUpInBackground(usernameInput, passwordInput, userAttributes, null, signUpHandler);

             }
         });

    }

     public void next(){ //test
         closeWaitDialog();
         Intent intent = new Intent(this, ConfirmSignUp.class);
         startActivity(intent);
     }



    private void confirmSignUp(CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
        System.out.println("Worked!!!");
        Intent intent = new Intent(this, ConfirmSignUp.class);
        intent.putExtra("source","signup");
        intent.putExtra("name", usernameInput);
        intent.putExtra("destination", cognitoUserCodeDeliveryDetails.getDestination());
        intent.putExtra("deliveryMed", cognitoUserCodeDeliveryDetails.getDeliveryMedium());
        intent.putExtra("attribute", cognitoUserCodeDeliveryDetails.getAttributeName());
        startActivityForResult(intent, reqCode);
       // startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == reqCode) {
            if(resultCode == RESULT_OK){
                String name = null;
                if(data.hasExtra("name")) {
                    name = data.getStringExtra("name");
                }
                exit(name, userPassword);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        closeWaitDialog();
    }


    private void showWaitDialog() {
       // closeWaitDialog();
        //waitDialog = new ProgressBar(this);
        waitDialog.setVisibility(View.VISIBLE);
        signUpMessage.setVisibility(View.VISIBLE);
        //waitDialog.setMessage(message);
       // waitDialog.show();
    }

    private void showDialogMessage(String title, String body, final boolean exit) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();
                    if(exit) {
                        exit(usernameInput);
                    }
                } catch (Exception e) {
                    if(exit) {
                        exit(usernameInput);
                    }
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    private void closeWaitDialog() {
        try {
            waitDialog.setVisibility(View.GONE);
            signUpMessage.setVisibility(View.GONE);
        }
        catch (Exception e) {
            //
        }
    }

    private void exit(String name) {

        exit(name, null);
    }

    private void exit(String name, String password) {
        Intent intent = new Intent();
        if (name == null) {
            name = "";
        }
        if (password == null) {
            password = "";
        }
        intent.putExtra("name", name);
        intent.putExtra("password", password);
        setResult(RESULT_OK, intent);
        finish();
    }
     public void rbGroup(View v){
         radiobutton = gender.getCheckedRadioButtonId();
         rb = (RadioButton) findViewById(radiobutton);
        genderChecked = true;
    }

     public void setDate(View view){
         PickerDialogs pickerDialogs = new PickerDialogs();
         pickerDialogs.show(getSupportFragmentManager(), "date_picker");
     }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        yearFinal = i;
        monthFinal = i1 + 1;
        dayFinal = i2;
        dob.setText(dayFinal + "/" + monthFinal + "/" + yearFinal);

    }
}