package com.kridacreations.diary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.kridacreations.diary.data.DiaryContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DaysActivity extends AppCompatActivity {

    public static ArrayList<Details> daydetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_days);

        setTitle("Hello, " + InfoActivity.sharedInfo.getString("personname", null));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DaysActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        showDatabaseInfo();
    }

    private void showDatabaseInfo() {

        String[] projection = {DiaryContract.DaysEntry.DAYS_ID,
                DiaryContract.DaysEntry.DAYS_COLUMN_DATE,
                DiaryContract.DaysEntry.DAYS_COLUMN_MONTH,
                DiaryContract.DaysEntry.DAYS_COLUMN_YEAR,
                DiaryContract.DaysEntry.DAYS_COLUMN_FEEL,
                DiaryContract.DaysEntry.DAYS_COLUMN_DESC };

        Cursor cursor = getContentResolver().query(
                DiaryContract.DaysEntry.DAYS_CONTENT_URI,
                projection,
                null,
                null,
                null);

        daydetails = FillPetDetails(cursor);

        DetailsAdapter mAdapter = new DetailsAdapter(this, daydetails);

        ListView listView = (ListView) findViewById(R.id.List);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(DaysActivity.this, EditorActivity.class);
//                Log.v("main activity", String.valueOf(Uri.parse(PetContract.PetEntry.CONTENT_URI + "/" + String.valueOf(position))));
//                intent.setData(Uri.parse(PetContract.PetEntry.CONTENT_URI + "/" + String.valueOf(position)));
                intent.setData(ContentUris.withAppendedId(DiaryContract.DaysEntry.DAYS_CONTENT_URI, id));
                startActivity(intent);
            }
        });

        listView.setAdapter(mAdapter);
        listView.setEmptyView((View) findViewById(R.id.empty_view));
    }

    private ArrayList<Details> FillPetDetails(Cursor cursor) {
        ArrayList <Details> daylist = new ArrayList<>();
        while (cursor.moveToNext()){
            int date = cursor.getInt(cursor.getColumnIndexOrThrow(DiaryContract.DaysEntry.DAYS_COLUMN_DATE));
            int month = cursor.getInt(cursor.getColumnIndexOrThrow(DiaryContract.DaysEntry.DAYS_COLUMN_MONTH));
            int year = cursor.getInt(cursor.getColumnIndexOrThrow(DiaryContract.DaysEntry.DAYS_COLUMN_YEAR));
            String feeling = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DaysEntry.DAYS_COLUMN_FEEL));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(DiaryContract.DaysEntry.DAYS_COLUMN_DESC));

            daylist.add(new Details(date, month, year, feeling, desc));
        }
        return daylist;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }


    private void showDeleteAllConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Html.fromHtml("<font color='#FF0187'>" + "Delete all days?" + "</font>"));
        builder.setPositiveButton(R.string.delete_all_confirm_msg, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllPets();
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


    private void deleteAllPets() {
        int result = getContentResolver().delete(DiaryContract.DaysEntry.DAYS_CONTENT_URI, null, null);
        if(result == 0){
            Toast.makeText(this, getString(R.string.delete_all_failed),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        daydetails.clear();
        showDatabaseInfo();
        Toast.makeText(this, getString(R.string.delete_all_successful),
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_settings:
                // option to change name and password
                sendIntentToInfoActivity();
                return true;
            case R.id.action_delete_all_days:
                // delete all days
                showDeleteAllConfirmationDialog();
                return true;
            case android.R.id.home:
                // check if value changed
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendIntentToInfoActivity() {
        Intent i = new Intent(DaysActivity.this, InfoActivity.class);
        i.putExtra("change_information_order", true);
        startActivity(i);
    }


    @Override
    protected void onStart() {
        super.onStart();
        showDatabaseInfo();
        setTitle("Hello, " + InfoActivity.sharedInfo.getString("personname", null));
    }
}