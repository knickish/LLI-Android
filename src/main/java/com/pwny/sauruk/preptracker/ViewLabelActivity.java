package com.pwny.sauruk.preptracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pwny.sauruk.preptracker.m_JSON.DeleteLabel;
import com.pwny.sauruk.preptracker.m_JSON.MyAppApplication;
import com.pwny.sauruk.preptracker.m_JSON.serverInterface;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewLabelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_view_label);
        int initTime = getIntent ().getIntExtra ("initTime", 0);
        final int uId = getIntent ().getIntExtra ("uId", 0);
        String itemName = getIntent ().getStringExtra ("name");
        String category = getIntent ().getStringExtra ("category");
        int lifetime = getIntent ().getIntExtra ("lifetime", 0);
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis ((long)initTime*100000);
        start.add(Calendar.HOUR_OF_DAY, lifetime);
        TextView a = (TextView) findViewById (R.id.expiresAtViewLabel);
        Date expiresAt = start.getTime ();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat ("hh:mm a - MM/dd/yyyy");
        String expireTime = "Expires At: " + simpleDateFormat.format (expiresAt);
        a.setText(expireTime);
        TextView b = (TextView) findViewById (R.id.nameViewLabel);
        TextView c = (TextView) findViewById (R.id.categoryViewLabel);
        String nameText = itemName + " #"+ Integer.toString (uId);
        b.setText(nameText);
        c.setText(category);

        Button removeLabel = (Button) findViewById (R.id.deleteLabelButton);
        removeLabel.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewLabelActivity.this);
                builder.setMessage ("Delete this item?");
                builder.setTitle ("Confirmation");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeleteLabel x = new DeleteLabel (ViewLabelActivity.this,uId);
                        x.execute();
                        Intent i;
                        i = new Intent(getApplicationContext (),MainActivity.class);
                        startActivity (i);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show ();
            }
        }
        );


    }
}
