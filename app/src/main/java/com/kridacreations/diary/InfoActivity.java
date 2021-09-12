package com.kridacreations.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InfoActivity extends AppCompatActivity {

    private EditText mNameEditText;

    private EditText mOldPinEditText;

    private EditText mPinEditText;

    private EditText mConfirmEditText;

    public static SharedPreferences sharedInfo;

    private Boolean intentValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mNameEditText = (EditText) findViewById(R.id.entername);
        mPinEditText = (EditText) findViewById(R.id.enterpin);
        mConfirmEditText = (EditText) findViewById(R.id.enterconfirmpin);
        mOldPinEditText = (EditText) findViewById(R.id.enteroldpin);

        sharedInfo = getSharedPreferences("name_pin", MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedInfo.edit();

        Intent intent = getIntent();
        intentValue = intent.getBooleanExtra("change_information_order", false);

        if (intentValue){

            setTitle("Update Information");

            TextView oldpin = (TextView) findViewById(R.id.oldpin);
            oldpin.setVisibility(View.VISIBLE);
            mOldPinEditText.setVisibility(View.VISIBLE);

            Button savebutton = (Button) findViewById(R.id.savebutton);
            savebutton.setText("UPDATE");
            savebutton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // your handler code here

                    if ((mPinEditText.getText().toString().length() >= 3 &&
                            mPinEditText.getText().toString().length() <= 10) &&
                            mConfirmEditText.getText().toString().length() != 0 &&
                            mOldPinEditText.getText().toString().length() != 0 &&
                            mNameEditText.getText().toString().trim().length() != 0) {

                        if (Integer.parseInt(mOldPinEditText.getText().toString()) == Integer.parseInt(sharedInfo.getString("personpin", null))) {
                            if (Integer.parseInt(mPinEditText.getText().toString()) == Integer.parseInt(mConfirmEditText.getText().toString())) {

                                editor.putString("personname", mNameEditText.getText().toString().trim());
                                editor.putString("personpin", mPinEditText.getText().toString());

                                editor.apply();

                                showToast(R.string.information_updated);
                                finish();

                            } else {
                                showToast(R.string.mismatched_pin);
                            }
                        } else {
                           showToast(R.string.old_pin_mismatched);
                        }
                    } else {
                        showToast(R.string.invalid_information);
                    }
                }
            });

        } else {

            if (sharedInfo.getString("personname", null) == null) {
                setTitle("Create id");

                Button savebutton = (Button) findViewById(R.id.savebutton);
                savebutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
//                    Log.v("main", mPinEditText.getText().toString() + "\n d    " + mConfirmEditText.getText().toString());
                        if ((mPinEditText.getText().toString().length() >= 3 &&
                                mPinEditText.getText().toString().length() <= 10) &&
                                mConfirmEditText.getText().toString().length() != 0 &&
                                mNameEditText.getText().toString().trim().length() != 0) {

                            if (Integer.parseInt(mPinEditText.getText().toString()) == Integer.parseInt(mConfirmEditText.getText().toString())) {

                                String namestring = mNameEditText.getText().toString().trim();
                                String pinstring = mPinEditText.getText().toString().trim();

                                editor.putString("personname", namestring);
                                editor.putString("personpin", pinstring);

                                editor.apply();

                                Intent i = new Intent(InfoActivity.this, DaysActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                showToast(R.string.mismatched_pin);
                            }
                        } else {
                            showToast(R.string.invalid_information);
                        }
                    }
                });
            } else {
                setTitle("Login");

                // person saved password one time
                TextView nametext = (TextView) findViewById(R.id.name);
                EditText enternametext = (EditText) findViewById(R.id.entername);
                TextView confirmpintext = (TextView) findViewById(R.id.confirmpin);
                EditText enterconfirmtext = (EditText) findViewById(R.id.entername);

            /*
            nametext.setVisibility(View.INVISIBLE);
            enternametext.setVisibility(View.INVISIBLE);
            confirmpintext.setVisibility(View.GONE);
            enterconfirmtext.setVisibility(View.GONE);*/

                nametext.setVisibility(View.GONE);
                mNameEditText.setVisibility(View.GONE);
                confirmpintext.setVisibility(View.GONE);
                mConfirmEditText.setVisibility(View.GONE);

                Button savebutton = (Button) findViewById(R.id.savebutton);
                savebutton.setText("ENTER");
                savebutton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (Integer.parseInt(mPinEditText.getText().toString()) == Integer.parseInt(sharedInfo.getString("personpin", null))) {
                            Intent i = new Intent(InfoActivity.this, DaysActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            showToast(R.string.incorrect_pin);
                        }
                    }
                });

            }
        }

    }

    void showToast(int toastString){
        Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
    }
}