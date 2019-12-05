package com.example.awake;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardSetListFragment extends Fragment {
    /**
     * Activity this fragment is apart of.
     */
    private MainActivity mA;
    private ListView cardset_list_view;
    public List<FlashcardSet> set_cards = mA.cardsets;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cardset_list_fragment, container, false);
        // Naming toolbar.
        mA.getSupportActionBar().setTitle("Manage Flashcards");

        //load a set here
        // ArrayList<Flashcard> list =
        // Use the view for this fragment to search for UI components.
        cardset_list_view = (ListView) view.findViewById(R.id.flashcard_list);
        FlashcardsAdapter adapter = new FlashcardsAdapter(getActivity(), R.layout.flashcards_list_fragment, set_cards);
        cardset_list_view.setAdapter(adapter);

        final FloatingActionButton add = view.findViewById(R.id.add_cardset);
        add.setOnClickListener(new View.OnClickListener() {
            List<Flashcard> new_cards = new ArrayList<Flashcard>();
            set_cards.add(new FlashcardSet("New Set", new_cards));
        });
        return view;
    }

    public void update_list_view() {

        // make array adapter to bind arraylist to listview with new custom item layout
        FlashcardsAdapter aa = new FlashcardsAdapter(getActivity(), R.layout.card_entry, flashcards);
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


}

