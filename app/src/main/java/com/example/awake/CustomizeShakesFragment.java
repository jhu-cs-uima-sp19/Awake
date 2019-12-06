package com.example.awake;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CustomizeShakesFragment extends Fragment {

    private MainActivity mA;
    private EditText input;
    private Button submit;
    private SharedPreferences sharedPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_customize_shakes, container, false);
        mA.getSupportActionBar().setTitle("Set Shakes");

        input = view.findViewById(R.id.num_shakes);
        submit = view.findViewById(R.id.submit);

        sharedPref = getActivity().getSharedPreferences("alarms", Context.MODE_PRIVATE);
        int old_shakes = sharedPref.getInt("shakes", 25);
        System.out.println(old_shakes);
        input.setText(old_shakes + "");

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String num_shakes_str = input.getText().toString();
                int num_shakes = Integer.parseInt(num_shakes_str);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("shakes", num_shakes);
                editor.apply();
                mA.start_set_alarm_fragment();
            }
        });

        return view;
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
