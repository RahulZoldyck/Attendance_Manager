package com.delta.attendancemanager;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class UpcomingTT extends ActionBarActivity {
    TextView sub1,sub2,sub3,sub4,sub5,sub6,sub7,sub8;
    String[] all;
    MySqlHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_tt);
        handler=new MySqlHandler(this,null);
        all=new String[9];
        sub1=(TextView)findViewById(R.id.sub1);
        sub2=(TextView)findViewById(R.id.sub2);
        sub3=(TextView)findViewById(R.id.sub3);
        sub4=(TextView)findViewById(R.id.sub4);
        sub5=(TextView)findViewById(R.id.sub5);
        sub6=(TextView)findViewById(R.id.sub6);
        sub7=(TextView)findViewById(R.id.sub7);
        sub8=(TextView)findViewById(R.id.sub8);
        all=handler.get_tomo();
        sub1.setText(all[1]);
        sub2.setText(all[2]);
        sub3.setText(all[3]);
        sub4.setText(all[4]);
        sub5.setText(all[5]);
        sub6.setText(all[6]);
        sub7.setText(all[7]);
        sub8.setText(all[8]);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upcoming_tt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}