package com.boaz.dragonski.mystalker;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private final static int OUTGOING_CALLS_CODE = 10;
    private final static int READ_PHONE_STATE_CODE = 11;
    private final static int SEND_SMS_CODE = 12;
    public static final String SP_KEY = "shared_prefs";
    public static final String PHONE_NUMBER = "phone_number";
    public static final String TEXT_MESSAGE = "text_message";
    public static final String PHONE_NUMBER_LEGIT_FORM = "^[+]?[0-9]{10,13}";
    public static final String READY_TEXT = "Ready to send SMS messages.";
    public static final String NOT_READY_TEXT = "Not ready to send SMS messages, missing fields. Please fill both fields.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_CODE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, OUTGOING_CALLS_CODE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_CODE);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SP_KEY, MODE_PRIVATE);
        String phoneNumber = sharedPreferences.getString(PHONE_NUMBER, null);
        String message = sharedPreferences.getString(TEXT_MESSAGE, null);

        final EditText phoneNumberForText = findViewById(R.id.phone_number_to_send_sms);
        final EditText smsMessage = findViewById(R.id.sms_message);
        final TextView infoText = findViewById(R.id.informative_text);
        if (!checkFields(smsMessage.getText().toString(), phoneNumberForText.getText().toString())) {
            infoText.setText(NOT_READY_TEXT);
        } else {
            infoText.setText(READY_TEXT);
        }

        smsMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence message, int start, int before, int count) {
                if (checkFields(message.toString(), phoneNumberForText.getText().toString())) {
                    infoText.setText(READY_TEXT);
                    SharedPreferences.Editor editor = getSharedPreferences(SP_KEY, MODE_PRIVATE).edit();
                    editor.putString(PHONE_NUMBER, phoneNumberForText.getText().toString());
                    editor.putString(TEXT_MESSAGE, message.toString());
                    editor.apply();
                } else {
                    infoText.setText(NOT_READY_TEXT);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        phoneNumberForText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence phoneNumber, int start, int before, int count) {
                if (checkFields(smsMessage.getText().toString(), phoneNumber.toString())) {
                    infoText.setText(READY_TEXT);
                    SharedPreferences.Editor editor = getSharedPreferences(SP_KEY, MODE_PRIVATE).edit();
                    editor.putString(PHONE_NUMBER, phoneNumber.toString());
                    editor.putString(TEXT_MESSAGE, smsMessage.getText().toString());
                    editor.apply();
                } else {
                    infoText.setText(NOT_READY_TEXT);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (phoneNumber != null)
            phoneNumberForText.setText(phoneNumber);
        if (message != null)
            smsMessage.setText(message);


    }

    private boolean checkFields(String message, String phoneNumber) {
        boolean retValue = true;
        if (message.isEmpty()) {
            retValue = false;
        }
        Pattern pattern = Pattern.compile(PHONE_NUMBER_LEGIT_FORM);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()) {
            retValue = false;
        }
        return retValue;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            return;
        switch (requestCode) {
            case READ_PHONE_STATE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_CODE);
                    }
                }
                break;
            }
            case OUTGOING_CALLS_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, OUTGOING_CALLS_CODE);
                    }
                }
                break;
            }
            case SEND_SMS_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_CODE);
                    } 
                }
            }
            break;
        }
    }
}