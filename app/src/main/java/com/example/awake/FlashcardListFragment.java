package com.example.awake;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


import java.util.ArrayList;
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
        FlashcardsAdapter adapter = new FlashcardsAdapter(list, this);
        flashcard_list_view.setAdapter(adapter);

        return view;
    }


}
