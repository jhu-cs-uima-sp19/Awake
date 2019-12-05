package com.example.awake;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardListFragment extends Fragment {
    /**
     * Activity this fragment is apart of.
     */
    private MainActivity mA;
    private ListView flashcard_list_view;
    public List<Flashcard> flashcards = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.flashcards_list_fragment, container, false);
        // Naming toolbar.
        mA.getSupportActionBar().setTitle("Add Flashcards");

        //load a set here
        // ArrayList<Flashcard> list =
        // Use the view for this fragment to search for UI components.
        flashcard_list_view = (ListView) view.findViewById(R.id.flashcard_list);
        FlashcardsAdapter adapter = new FlashcardsAdapter(this, R.layout.flashcards_list_fragment, flashcards);
        flashcard_list_view.setAdapter(adapter);

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
