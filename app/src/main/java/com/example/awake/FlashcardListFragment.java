package com.example.awake;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardListFragment extends Fragment {
    /**
     * Activity this fragment is apart of.
     */
    private MainActivity mA;
    private ListView flashcard_list_view;
    private List<Flashcard> newset = new ArrayList<Flashcard>();

    private Button donefc;
    private Button cancelfc;
    private int setnumber;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.flashcards_list_fragment, container, false);
        // Naming toolbar.
        mA.getSupportActionBar().setTitle("Add Flashcards");

        sharedPref = getActivity().getSharedPreferences("alarms", Context.MODE_PRIVATE);
        setnumber = sharedPref.getInt("FlashcardSet", 0);

        List<Flashcard> oldset =  mA.cardsets.get(setnumber).getCardlist();
        for (int i = 0; i < oldset.size(); i++) {
            newset.add(new Flashcard(oldset.get(i).getName(), oldset.get(i).getContent()));
        }

        //load a set here
        // ArrayList<Flashcard> list =
        // Use the view for this fragment to search for UI components.
        flashcard_list_view = (ListView) view.findViewById(R.id.flashcard_list);
        FlashcardsAdapter adapter = new FlashcardsAdapter(getActivity(), R.layout.card_entry, newset);
        flashcard_list_view.setAdapter(adapter);

        final FloatingActionButton add = view.findViewById(R.id.add_flashcard);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mA.cardsets.get(setnumber).getCardlist().add(new Flashcard("", ""));
                update_list_view();
            }
        });

        donefc = view.findViewById(R.id.donefc);
        cancelfc = view.findViewById(R.id.cancelfc);

        donefc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Update or create new alarm. THIS NEEDS TO BE UPDATED ONCE REPEATS ARE IMPLEMENTED.
                mA.cardsets.get(setnumber).setList(newset);
                // Will switch in ListView fragment and update the ArrayAdapter.
                mA.start_cardset_list_fragment();
            }
        });

        cancelfc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Will switch in ListView fragment and update the ArrayAdapter.
                mA.start_cardset_list_fragment();
            }
        });

        return view;
    }

    public void update_list_view() {

        // make array adapter to bind arraylist to listview with new custom item layout
        FlashcardsAdapter aa = new FlashcardsAdapter(getActivity(), R.layout.card_entry, newset);
        flashcard_list_view.setAdapter(aa);
        registerForContextMenu(flashcard_list_view);
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
