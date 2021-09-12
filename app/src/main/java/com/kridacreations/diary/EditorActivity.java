package com.kridacreations.diary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kridacreations.diary.data.DiaryContract;

public class EditorActivity extends AppCompatActivity {

    private EditText mDateEditText;
    private EditText mMonthEditText;
    private EditText mYearEditText;
    private EditText mFeelingEditText;
    private EditText mDescEditText;

    private Uri mCurrentDayUri;

    private Uri mModifiedDayUri;

    private boolean mDayHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mDayHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mDateEditText = (EditText) findViewById(R.id.date);
        mMonthEditText = (EditText) findViewById(R.id.month);
        mYearEditText = (EditText) findViewById(R.id.year);
        mFeelingEditText = (EditText) findViewById(R.id.feeling);
        mDescEditText = (EditText) findViewById(R.id.desc);

        mDateEditText.setOnTouchListener(mTouchListener);
        mMonthEditText.setOnTouchListener(mTouchListener);
        mYearEditText.setOnTouchListener(mTouchListener);
        mFeelingEditText.setOnTouchListener(mTouchListener);
        mDescEditText.setOnTouchListener(mTouchListener);

        Intent intent = getIntent();
        mCurrentDayUri = intent.getData();

        if (mCurrentDayUri == null){
            setTitle(R.string.editor_activity_new_day);

            invalidateOptionsMenu();
        } else {
            setTitle(R.string.editor_activity_edit_day);

            mModifiedDayUri = Uri.withAppendedPath(DiaryContract.DaysEntry.DAYS_CONTENT_URI, String.valueOf(Integer.parseInt(mCurrentDayUri.getLastPathSegment())+1));

            showValueToUpdate();
        }

    }

    private void showValueToUpdate() {
        int date = DaysActivity.daydetails.get(Integer.parseInt(mCurrentDayUri.getLastPathSegment())).getDate();
        int month = DaysActivity.daydetails.get(Integer.parseInt(mCurrentDayUri.getLastPathSegment())).getMonth();
        int year = DaysActivity.daydetails.get(Integer.parseInt(mCurrentDayUri.getLastPathSegment())).getYear();
        String feel = DaysActivity.daydetails.get(Integer.parseInt(mCurrentDayUri.getLastPathSegment())).getFeeling();
        String desc = DaysActivity.daydetails.get(Integer.parseInt(mCurrentDayUri.getLastPathSegment())).getDesc();

        mDateEditText.setText(String.valueOf(date));
        mMonthEditText.setText(String.valueOf(month));
        mYearEditText.setText(String.valueOf(year));
        mFeelingEditText.setText(feel);
        mDescEditText.setText(desc);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_save:
                if (mModifiedDayUri == null){
                    saveDay();
                } else {
                    updateDay();
                }
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mDayHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<font color='#FF0187'>" + "Discard your changes and quit editing?" + "</font>"));
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void updateDay() {
        if (wrongEntries()){
            return;
        }

        int date = Integer.parseInt(mDateEditText.getText().toString().trim());
        int month = Integer.parseInt(mMonthEditText.getText().toString().trim());
        int year = Integer.parseInt(mYearEditText.getText().toString().trim());
        String feeling = mFeelingEditText.getText().toString().trim();
        String desc = mDescEditText.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(DiaryContract.DaysEntry.DAYS_COLUMN_DATE, date);
        values.put(DiaryContract.DaysEntry.DAYS_COLUMN_MONTH, month);
        values.put(DiaryContract.DaysEntry.DAYS_COLUMN_YEAR, year);
        values.put(DiaryContract.DaysEntry.DAYS_COLUMN_FEEL, feeling);
        values.put(DiaryContract.DaysEntry.DAYS_COLUMN_DESC, desc);

        int result = getContentResolver().update(mModifiedDayUri, values, null, null);
        if(result == 0){
            showToast(R.string.error_updating_day);
        } else {
            showToast(R.string.success_updating_day);
        }

        finish();
    }

    private Boolean wrongEntries(){
        String date = mDateEditText.getText().toString().trim();
        String month = mMonthEditText.getText().toString().trim();
        String year = mYearEditText.getText().toString().trim();
        String feeling = mFeelingEditText.getText().toString().trim();
        String desc = mDescEditText.getText().toString().trim();

        if(TextUtils.isEmpty(date) ||
                TextUtils.isEmpty(month) ||
                TextUtils.isEmpty(year) ||
                TextUtils.isEmpty(feeling) ||
                TextUtils.isEmpty(desc)) {

            showToast(R.string.empty_values);
            return true;
        }

        int dateint = Integer.parseInt(date);
        int monthint = Integer.parseInt(month);
        int yearint = Integer.parseInt(year);

        if(yearint <2000 || yearint > 2100) {
            showToast(R.string.invalid_year);
            return true;
        }
        if(monthint < 1 || monthint > 12){
            showToast(R.string.invalid_month);
            return true;
        }
        if (monthint == 2){
            if (yearint%4 == 0) {
                if (dateint < 1 || dateint > 29) {
                    showToast(R.string.invalid_date);
                    return true;
                }
            } else if (dateint < 1 || dateint > 28){
                showToast(R.string.invalid_date);
                return true;
            }
        } else if (monthint==1 || monthint==3 || monthint==5 || monthint==7 || monthint==8 || monthint==10 || monthint==12){
            if (dateint < 1 || dateint >31){
                showToast(R.string.invalid_date);
                return true;
            }
        } else if (monthint==4 || monthint==6 || monthint==9 || monthint==11){
            if (dateint < 1 || dateint > 30){
                showToast(R.string.invalid_date);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mDayHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<font color='#FF0187'>" + "Delete this day?" + "</font>"));
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteDay();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteDay() {
        /*  THIS_METHOD_IS_NOT_WORKING, THAT_IS_WHY_USED_ANOTHER_APPROACH
        int result = getContentResolver().delete(mModifiedDayUri, null, null);
         */
        int result = getContentResolver().delete(DiaryContract.DaysEntry.DAYS_CONTENT_URI, null, null);
        if(result == 0){
            Toast.makeText(this, getString(R.string.editor_delete_day_failed),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        DaysActivity.daydetails.remove(Integer.parseInt(mCurrentDayUri.getLastPathSegment()));
        for(int i=0; i<DaysActivity.daydetails.size(); i++){

            ContentValues values = new ContentValues();
            values.put(DiaryContract.DaysEntry.DAYS_COLUMN_DATE, DaysActivity.daydetails.get(i).getDate());
            values.put(DiaryContract.DaysEntry.DAYS_COLUMN_MONTH, DaysActivity.daydetails.get(i).getMonth());
            values.put(DiaryContract.DaysEntry.DAYS_COLUMN_YEAR, DaysActivity.daydetails.get(i).getYear());
            values.put(DiaryContract.DaysEntry.DAYS_COLUMN_FEEL, DaysActivity.daydetails.get(i).getFeeling());
            values.put(DiaryContract.DaysEntry.DAYS_COLUMN_DESC, DaysActivity.daydetails.get(i).getDesc());

            Uri newUri = getContentResolver().insert(DiaryContract.DaysEntry.DAYS_CONTENT_URI, values);
            if(newUri == null){
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_delete_day_failed),
                        Toast.LENGTH_SHORT).show();
                return;
            }

        }

        Toast.makeText(this, getString(R.string.editor_delete_day_successful),
                Toast.LENGTH_SHORT).show();

        finish();
    }

    private void saveDay() {

        if (wrongEntries()){
            return;
        }

        int date = Integer.parseInt(mDateEditText.getText().toString().trim());
        int month = Integer.parseInt(mMonthEditText.getText().toString().trim());
        int year = Integer.parseInt(mYearEditText.getText().toString().trim());
        String feeling = mFeelingEditText.getText().toString().trim();
        String desc = mDescEditText.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(DiaryContract.DaysEntry.DAYS_COLUMN_DATE, date);
        values.put(DiaryContract.DaysEntry.DAYS_COLUMN_MONTH, month);
        values.put(DiaryContract.DaysEntry.DAYS_COLUMN_YEAR, year);
        values.put(DiaryContract.DaysEntry.DAYS_COLUMN_FEEL, feeling);
        values.put(DiaryContract.DaysEntry.DAYS_COLUMN_DESC, desc);

        Uri newUri = getContentResolver().insert(DiaryContract.DaysEntry.DAYS_CONTENT_URI, values);

        if(newUri == null){
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_day_failed),
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_day_successful),
                    Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (mCurrentDayUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }


    void showToast(int toastString){
        Toast.makeText(this, toastString, Toast.LENGTH_SHORT).show();
    }


}