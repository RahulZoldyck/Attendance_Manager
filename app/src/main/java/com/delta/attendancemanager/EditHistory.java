package com.delta.attendancemanager;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.PendingIntent.getActivity;


public class EditHistory extends ActionBarActivity {
    AtAdapter atAdapter;
    String subname;
    DatePicker dp;
    Date date;
    int p = 1;
    int pos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_history);
        atAdapter = new AtAdapter(getApplicationContext());
        subname = getIntent().getStringExtra("sname");

        RecyclerView reclist = (RecyclerView) findViewById(R.id.editcardList);
        reclist.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        reclist.setLayoutManager(llm);
        atAdapter.fetch_subject_data(subname);
        EditAdapter edadapter = new EditAdapter(getApplicationContext(),createList(subname,atAdapter.getDt(),atAdapter.getPresint()));
        reclist.setAdapter(edadapter);
    }

    private List<EditCardInfo> createList(String sname, ArrayList<String> datetime, ArrayList<Integer> present) {

        List<EditCardInfo> result = new ArrayList<EditCardInfo>();
        for (int i=0; i < datetime.size(); i++) {
            EditCardInfo eci = new EditCardInfo();
            eci.coursename=sname;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try{
                date = sdf.parse(datetime.get(i));
            }
            catch(Exception e){
                Log.d("hel", e.toString());
            }
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/M/yyyy");                      //do not change the time format here. giving error in database. change it while displaying if necessary.
            SimpleDateFormat sdf2 = new SimpleDateFormat("h:m");
            eci.classdate = sdf1.format(date);
            eci.classtime = sdf2.format(date);
            if (present.get(i) == 1)
            eci.attendance=Boolean.TRUE;
            else
            eci.attendance=Boolean.FALSE;
            result.add(eci);

        }

        return result;
    }
    public void addclass(View v){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_att_dialog);
        dialog.setTitle("Add Attendance");
        Button cancel = (Button) dialog.findViewById(R.id.cancelbutton);
        Button add = (Button) dialog.findViewById(R.id.addclass);
        Spinner sp = (Spinner) dialog.findViewById(R.id.spinner);
        dp = (DatePicker) dialog.findViewById(R.id.datePicker);
        ToggleButton toggleButton= (ToggleButton) dialog.findViewById(R.id.toggleButton);
        List<String> categories = new ArrayList<String>();
        for(int i = 1;i<=8;i++) {
            categories.add(String.format("%02d", TTimings.hour[i]) + ":" + String.format("%02d", TTimings.min[i]));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(dataAdapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        /*final RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.addrg);
        for(int i=1;i<=8;i++){
            RadioButton rb = new RadioButton(getApplicationContext());
            rb.setText(Integer.toString(TTimings.hour[i])+" "+Integer.toString(TTimings.min[i]));
            rb.setTextColor(getResources().getColor(R.color.darkb));
            rg.addView(rb);
        }*/
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        p = 1;
        toggleButton.setChecked(true);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    p = 1;
                else
                    p = -1;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int pos = rg.getCheckedRadioButtonId();
                AtAdapter atAdapter = new AtAdapter(getApplicationContext());
                String format = "yyyy-MM-dd HH:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                Calendar now = Calendar.getInstance();
                Date date = new Date(dp.getYear()-1900, dp.getMonth(), dp.getDayOfMonth(), TTimings.hour[pos + 1], TTimings.min[pos + 1]);                                                                  //1900+yyyy;      TODO: check whther the normal date is working or change it to 1900+yyyy.
                atAdapter.add_attendance(subname, sdf.format(date), p);
                RecyclerView reclist = (RecyclerView) findViewById(R.id.editcardList);
                reclist.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                reclist.setLayoutManager(llm);
                atAdapter.fetch_subject_data(subname);
                EditAdapter edadapter = new EditAdapter(getApplicationContext(),createList(subname,atAdapter.getDt(),atAdapter.getPresint()));
                dialog.cancel();
                reclist.setAdapter(edadapter);
            }
        });

        dialog.show();


    }
}