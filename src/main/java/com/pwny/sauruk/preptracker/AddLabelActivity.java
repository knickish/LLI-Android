package com.pwny.sauruk.preptracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pwny.sauruk.preptracker.m_JSON.CurrentTime;
import com.pwny.sauruk.preptracker.m_JSON.ItemType;
import com.pwny.sauruk.preptracker.m_JSON.ItemTypeData;
import com.pwny.sauruk.preptracker.m_JSON.JSONStrtoClassList;
import com.pwny.sauruk.preptracker.m_JSON.Label;
import com.pwny.sauruk.preptracker.m_JSON.addItemServer;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddLabelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_add_label);
        String itemName = getIntent ().getStringExtra ("name");
        ItemType type = null;
        int itemNumber = 0;


        SharedPreferences pref2 = PreferenceManager.getDefaultSharedPreferences(getApplicationContext ());
        String itemDBInfoStr = pref2.getString ("itemDB","error DB String not loaded");
        Map<Integer, ItemType> itemInfo = null;
        try {
            itemInfo = ItemTypeData.toList (itemDBInfoStr);
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        for(Integer key : itemInfo.keySet ()){
            ItemType temp = itemInfo.get(key);
            if (((temp.name).trim ()).equals (itemName.trim ())){
                itemNumber = key;
                type = itemInfo.get (key);
                System.out.println("Match Found");
            }
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext ());
        String jsonLabelDBString = pref.getString ("labelDB","error DB String not loaded");
        Map<String, List<Label>> labelDB = null;
        final List<Label> list = new ArrayList<>();
        try {
            labelDB = JSONStrtoClassList.convert (jsonLabelDBString) ;
            for (List<Label> value : labelDB.values ()) {
                for(Label temp : value){
                    list.add (temp);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        final ItemType typeFinal = type;
        System.out.println("Final Cat Test: " + typeFinal.category);
        final int itemNo = itemNumber;
        System.out.println("itemNumber for submit: "+ Integer.toString (itemNumber));
        final EditText et = findViewById (R.id.uId);
        Button submitButton = findViewById (R.id.labelSubmitButton);
        submitButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                boolean isUnique = true;
                int potentialUid = Integer.valueOf ((et.getText ()).toString ().trim ());
                for(int m=0; m<list.size ();m++){
                    Label temp = list.get (m);
                    if (temp.uId==potentialUid){
                        et.setText ("");
                        isUnique = false;
                        Toast toast=Toast.makeText(getApplicationContext(),"Number Already In Use",Toast.LENGTH_SHORT);
                        toast.setMargin(50,50);
                        toast.show();
                    }

                }
                if(isUnique){
                    Label x = null;
                    x = new Label (typeFinal.name, typeFinal.category, typeFinal.itemNumber,typeFinal.lifetime, potentialUid, CurrentTime.currTime ());
                    System.out.println(potentialUid);
                    addItemServer serve = new addItemServer (x, AddLabelActivity.this);
                    serve.execute ();
                    Toast toast=Toast.makeText(getApplicationContext(),"Item Submitted",Toast.LENGTH_SHORT);
                    toast.setMargin(50,50);
                    toast.show();
                    Intent i;
                    Intent r;
                    r = new Intent(getApplicationContext(), SelectItemActivity.class);
                    startActivity(r);
                }
            }
        });



    }
}
