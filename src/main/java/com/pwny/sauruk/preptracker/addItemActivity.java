package com.pwny.sauruk.preptracker;

import android.content.Context;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pwny.sauruk.preptracker.m_JSON.ItemType;
import com.pwny.sauruk.preptracker.m_JSON.JSONStrtoClassList;
import com.pwny.sauruk.preptracker.m_JSON.Label;
import com.pwny.sauruk.preptracker.m_JSON.UpdateDB;
import com.pwny.sauruk.preptracker.m_JSON.addItemTypeServer;
import com.pwny.sauruk.preptracker.m_JSON.serverInterface;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class addItemActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String category = null;
    String itemName = null;
    int lifetime = 0;
    int itemNumber = 0;
    String newCategory = new String();
    final Context context = addItemActivity.this;

    AlertDialog.Builder builder;



    public void addToDB(String category, String name, int lifetime) {



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_item_type);



        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext ());
        String jsonLabelDBString = pref.getString ("labelDB","error DB String not loaded");
        Map<String, List<Label>> labelDB = null;

        try {
            labelDB = JSONStrtoClassList.convert (jsonLabelDBString) ;

        } catch (JSONException e) {
            e.printStackTrace ();
        }
        final List<String> list =  new ArrayList<> (Objects.requireNonNull (labelDB).keySet ());
        System.out.println(list);
        final Spinner spinner2 = findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener (this);
        System.out.println("Error below here");
        final ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(addItemActivity.this,  android.R.layout.simple_spinner_dropdown_item, list);
        adapter3.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter3);

        Button button = findViewById(R.id.newCatButton);
        button.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.popup_edit_text, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle ("New Category")
                        .setTitle ("Enter New Category Name")
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        // get user input and set it to result
                                        // edit text
                                        newCategory = userInput.getText ().toString ();
                                        list.add (newCategory);
                                        adapter3.notifyDataSetChanged ();
                                        spinner2.setSelection (adapter3.getCount ());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
        final EditText nameText = findViewById (R.id.itemNameInput);
        final EditText hrs = findViewById (R.id.hours);
        final EditText days = findViewById (R.id.days);
        final EditText months = findViewById (R.id.months);
        Button submitButton = findViewById(R.id.submitItemButton);
        submitButton.setOnClickListener(new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                String name;
                try{
                    name=nameText.getText ().toString ();
                } catch (Exception e){
                    e.printStackTrace ();
                    return;
                }
                int lifeHours = 0;
                try{
                    lifeHours+=Integer.valueOf(hrs.getText ().toString ());
                }catch (Exception e){
                    // may not have value
                }
                try{
                    lifeHours+=Integer.valueOf(days.getText ().toString ())*24;
                }catch (Exception e){
                    // may not have value
                }
                try{
                    lifeHours+=Integer.valueOf(months.getText ().toString ())*24*30;
                }catch (Exception e){
                    // may not have value
                }

                ItemType temp = new ItemType(name, spinner2.getSelectedItem ().toString (),0,lifeHours);
                addItemTypeServer x = new addItemTypeServer (temp, addItemActivity.this);

                try {
                    x.execute ();
                }catch (Exception e){
                    e.printStackTrace ();
                }


                try {

                    serverInterface sI = new serverInterface (getApplicationContext ());

                    sI.updateDatabases ();

                } catch (Exception e) {
                    e.printStackTrace ();

                    e.printStackTrace ();
                    Context context2 = getApplicationContext ();
                    CharSequence text2 = "UpdateDB error";
                    int duration2 = Toast.LENGTH_SHORT;
                    Toast toast2 = Toast.makeText (context2, text2, duration2);
                    toast2.show ();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                }

                Intent i;
                i = new Intent(getApplicationContext(), SelectItemActivity.class);
                i.putExtra ("redraw",true);
                startActivity(i);
            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
