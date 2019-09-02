package com.pwny.sauruk.preptracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pwny.sauruk.preptracker.m_JSON.CurrentTime;
import com.pwny.sauruk.preptracker.m_JSON.ItemType;
import com.pwny.sauruk.preptracker.m_JSON.Label;
import com.pwny.sauruk.preptracker.m_JSON.MyAppApplication;
import com.pwny.sauruk.preptracker.m_JSON.UpdateDB;
import com.pwny.sauruk.preptracker.m_JSON.comparator;
import com.pwny.sauruk.preptracker.m_JSON.serverInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;
    int timeOfLastRefresh=0;
    List<Label> listviewList1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        Activity active = MainActivity.this;
        MyAppApplication x = new MyAppApplication (MainActivity.this);
        UpdateDB u = new UpdateDB(MainActivity.this, x);
        try {
            int DatabaseChecker = u.execute ().get ();
        } catch (Exception e){
            e.printStackTrace ();
        }
        Map<Integer, ItemType> itemInfo = new HashMap<> ();
        List<String> list = new ArrayList<> ();
        Map<String, List<Label>> labelDB = new HashMap<> ();

        FloatingActionButton fab = findViewById (R.id.fab);
        fab.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent (getApplicationContext (), SelectItemActivity.class);
                startActivity (i);
            }
        });
        timeOfLastRefresh = CurrentTime.currTime ();
        Toolbar toolbar = findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);


        final Map<Integer, ItemType> itemInfoFinal = x.itemInfo;
        final List<String> finalList = x.list;
        final Map<String, List<Label>> finalLabelDB = x.labelDB;
        System.out.println("144");
        System.out.println(finalLabelDB);

        final Spinner spinner;
        spinner = findViewById (R.id.spinner);
        final ListView lv = findViewById (R.id.lv);
        adapter = new ArrayAdapter<String> (getApplicationContext (), android.R.layout.simple_spinner_dropdown_item, x.list);
        adapter.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter (adapter);

        spinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                final List<Label> listviewList1;
                listviewList1 = new ArrayList<Label> (finalLabelDB.get (finalList.get (position)));
                Collections.sort (listviewList1, new comparator ());
                List<String> itemList = new ArrayList<> ();
                int currentTime = CurrentTime.currTime ();
                for (int i = 0; i < listviewList1.size (); i++) {
                    int lifeRemaining = (listviewList1.get (i).expiresAt - currentTime);
                    double lifeHr = lifeRemaining / 60;
                    String temp = itemInfoFinal.get (listviewList1.get (i).itemNumber).name + " - " + lifeHr + "hr ";
                    itemList.add (temp);
                }

                adapter2 = new ArrayAdapter<String> (getApplicationContext (), android.R.layout.simple_list_item_1, itemList);

                lv.setAdapter (adapter2);
                lv.setOnItemClickListener (new AdapterView.OnItemClickListener () {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String currentVal = (String) ((TextView) view).getText();
                        String checkVal ="";
                        int p = 0;
                        char x = '-';
                        while (currentVal.charAt (p)!= x){
                            checkVal+=currentVal.charAt (p);
                            p++;
                        }

                        for(int j =0;j< listviewList1.size ();j++){
                            String name  = itemInfoFinal.get (listviewList1.get (i).itemNumber).name;
                            System.out.println(name);
                            System.out.println(checkVal);
                            if (checkVal.trim().equalsIgnoreCase ( name.trim())){
                                Label label =listviewList1.get (i);
                                ItemType item = itemInfoFinal.get (label.itemNumber);

                                Intent r;
                                r = new Intent (getApplicationContext (), ViewLabelActivity.class);
                                r.putExtra ("uId", label.uId);
                                r.putExtra ("name", item.name);
                                r.putExtra ("category", item.category);
                                r.putExtra ("lifetime", item.lifetime);
                                r.putExtra ("initTime", label.initTime);
                                startActivity (r);
                                break;
                            }

                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        mSwipeRefreshLayout = findViewById (R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                /*if(timeOfLastRefresh-CurrentTime.currTime ()>1) {
                    Log.e (getClass ().getSimpleName (), "refresh");
                    try {
                        MyAppApplication x = new MyAppApplication (getApplicationContext ());
                        UpdateDB u = new UpdateDB (getApplicationContext (), x);
                        try {
                            int DatabaseChecker = u.execute ().get ();
                        } catch (Exception e) {
                            e.printStackTrace ();
                        }
                        final Map<Integer, ItemType> itemInfoFinal = x.itemInfo;
                        final List<String> finalList = x.list;
                        final Map<String, List<Label>> finalLabelDB = x.labelDB;
                        adapter.notifyDataSetChanged ();

                        listviewList1 = new ArrayList<Label> (finalLabelDB.get (finalList.get (spinner.getSelectedItemPosition ())));
                        Collections.sort (listviewList1, new comparator ());
                        List<String> itemList = new ArrayList<> ();
                        int currentTime = CurrentTime.currTime ();
                        for (int i = 0; i < listviewList1.size (); i++) {
                            int lifeRemaining = (listviewList1.get (i).expiresAt - currentTime);
                            double lifeHr = lifeRemaining / 60;
                            String temp = itemInfoFinal.get (listviewList1.get (i).itemNumber).name + "  " + lifeHr + "hr ";
                            itemList.add (temp);
                            System.out.println (temp);
                        }
                        adapter2 = new ArrayAdapter<String> (getApplicationContext (), android.R.layout.simple_list_item_1, itemList);
                        lv.setAdapter (adapter2);
                        lv.setOnItemClickListener (new AdapterView.OnItemClickListener () {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                String currentVal = (String) ((TextView) view).getText();
                                String checkVal ="";
                                int p = 0;
                                char x = '-';
                                while (currentVal.charAt (p)!= x){
                                    checkVal+=currentVal.charAt (p);
                                    p++;
                                }

                                for(int j =0;j< listviewList1.size ();j++){
                                    String name  = itemInfoFinal.get (listviewList1.get (i).itemNumber).name;
                                    System.out.println(name);
                                    System.out.println(checkVal);
                                    if (checkVal.trim().equalsIgnoreCase ( name.trim())){
                                        Label label =listviewList1.get (i);
                                        ItemType item = itemInfoFinal.get (label.itemNumber);

                                        Intent r;
                                        r = new Intent (getApplicationContext (), ViewLabelActivity.class);
                                        r.putExtra ("uId", label.uId);
                                        r.putExtra ("name", item.name);
                                        r.putExtra ("category", item.category);
                                        r.putExtra ("lifetime", item.lifetime);
                                        r.putExtra ("initTime", label.initTime);
                                        startActivity (r);
                                    }

                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace ();

                        e.printStackTrace ();
                        Context context2 = getApplicationContext ();
                        CharSequence text2 = "UpdateDB error";
                        int duration2 = Toast.LENGTH_SHORT;
                        Toast toast2 = Toast.makeText (context2, text2, duration2);
                        toast2.show ();
                    }

                }
                mSwipeRefreshLayout.setRefreshing (false);
*/             mSwipeRefreshLayout.setRefreshing (false);
            }

        });


    }
}
