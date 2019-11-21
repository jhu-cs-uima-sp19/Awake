package com.example.awake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class AlarmsAdapter extends ArrayAdapter<Alarm> {
    private int resource;
    private MainActivity mA;

    public AlarmsAdapter(Context ctx, int res, List<Alarm> items)
    {
        super(ctx, res, items);
        mA = (MainActivity) ctx;
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout alarmsView;
        Alarm a = getItem(position);

        if (convertView == null) {
            alarmsView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, alarmsView, true);
        } else {
            alarmsView = (LinearLayout) convertView;
        }

        TextView time_view = (TextView) alarmsView.findViewById(R.id.time);
        TextView repeat_view = (TextView) alarmsView.findViewById(R.id.repeat);
        Switch on = (Switch) alarmsView.findViewById(R.id.on);

        time_view.setText(a.alarm_time);
        repeat_view.setText(a.repeat_str);

        on.setChecked(a.on);
        // Allows us to identify which alarm has been turned on. Cannot store position as instance variable as it
        // Does not get updated when a button is clicked.
        on.setTag(position);
        on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (Integer) buttonView.getTag();
                Alarm a = mA.alarms.get(position);
                if (isChecked) {
                    // Switch is on.
                    System.out.println(a.alarm_time + ": Toggle on");
                    a.on = true;
                    // No need to delete and insert new alarm since code will just replace alarm with same requestCode.
                    mA.create_alarm(position);
                } else {
                    // Switch is off.
                    System.out.println(a.alarm_time + ": Toggle off");
                    a.on = false;
                    mA.cancelAlarm(position);
                }
            }
        });

        return alarmsView;
    }

}
