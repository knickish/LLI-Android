package com.pwny.sauruk.preptracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pwny.sauruk.preptracker.m_JSON.ItemType;
import com.pwny.sauruk.preptracker.m_JSON.ItemTypeData;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SelectItemActivity extends AppCompatActivity {

    public ArrayAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_select_item);
        Toolbar toolbar = findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        SharedPreferences pref2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext ());
        String itemDBInfoStr = pref2.getString ("itemDB","error DB String not loaded");
        Map<Integer, ItemType> itemInfo = null;
        try {
            itemInfo = ItemTypeData.toList (itemDBInfoStr);
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        List<Integer> keyList = new ArrayList<>(itemInfo.keySet ());
        List<String> tempList = new ArrayList<> ();
        for (int i = 0; i<keyList.size ();i++){
            ItemType temp = itemInfo.get (keyList.get (i));
            tempList.add (temp.name + " - "+ temp.category);
        }
        final List<String> displayList = new ArrayList<>(tempList);

        FloatingActionButton fab = findViewById (R.id.fab);
        fab.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent i;
                i = new Intent(getApplicationContext(), addItemActivity.class);
                startActivity(i);
            }
        });
        getSupportActionBar ().setDisplayHomeAsUpEnabled (true);

        final ListView lv = findViewById (R.id.selectItem);
        adapter2 = new ArrayAdapter<String> (getApplicationContext (), android.R.layout.simple_list_item_1, tempList);
        lv.setAdapter (adapter2);
        lv.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String  itemValue    = (String) lv.getItemAtPosition(i);
                String itemName = "";
                int j=0;
                while(j<itemValue.length ()&& itemValue.charAt (j)!='-'){
                    itemName+=itemValue.charAt (j);
                    j++;
                }
                //System.out.println(itemName);
                Intent r;
                r = new Intent(getApplicationContext(), AddLabelActivity.class);
                r.putExtra ("name",itemName);
                startActivity(r);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart ();

        boolean redraw= getIntent().getBooleanExtra ("redraw", false);
        if (redraw){
            adapter2.notifyDataSetChanged ();
        }
    }
}
