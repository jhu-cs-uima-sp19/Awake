package com.example.awake;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;

public class AlarmsListFragment extends Fragment {
    /**
     * Activity this fragment is apart of.
     */
    private MainActivity mA;
    private ListView alarm_list_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.alarms_list_fragment, container, false);
        // Naming toolbar.
        mA.getSupportActionBar().setTitle("Alarms");

        // Use the view for this fragment to search for UI components.
        alarm_list_view = (ListView) view.findViewById(R.id.alarm_list);
        alarm_list_view.setLongClickable(true);
        update_list_view();

        // program a short click on the list item
        alarm_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("Alarm from list selected.");
                mA.edit = true;
                mA.position = position;
                mA.start_set_alarm_fragment();
            }
        });

        alarm_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                System.out.println("Long Click Detected");
                final int fin_pos = pos;
                Alarm a = mA.alarms.get(fin_pos);
                new AlertDialog.Builder(mA)
                        .setTitle("Warning")
                        .setMessage("Delete " + a.alarm_time + " alarm?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mA.deleteAlarm(fin_pos);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });

        // Setting up floating button to add an alarm
        final FloatingActionButton add = view.findViewById(R.id.add_alarm);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mA.start_set_alarm_fragment();
            }
        });

        return view;
    }

    public void update_list_view() {
        /*
        I need to book keep that fact that the switch can turn by tapping and also by updating or setting
        a new alarm.
         */

        Collections.sort(mA.alarms);

        // make array adapter to bind arraylist to listview with new custom item layout
        AlarmsAdapter aa = new AlarmsAdapter(mA, R.layout.alarm_entry, mA.alarms);
        alarm_list_view.setAdapter(aa);
        registerForContextMenu(alarm_list_view);
        aa.notifyDataSetChanged();  // to refresh items in the list
    }

    // Used to access activity in fragment.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity){
            mA =(MainActivity) context;
        }
    }
}

