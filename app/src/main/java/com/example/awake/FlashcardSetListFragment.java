package com.example.awake;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlashcardSetListFragment extends Fragment {
    /**
     * Activity this fragment is apart of.
     */
    private MainActivity mA;
    private ListView cardset_list_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cardset_list_fragment, container, false);
        // Naming toolbar.
        mA.getSupportActionBar().setTitle("Manage Flashcards");

        //load a set here
        // ArrayList<Flashcard> list =
        // Use the view for this fragment to search for UI components.
        cardset_list_view = (ListView) view.findViewById(R.id.cardset_list);
        FlashcardSetAdapter adapter = new FlashcardSetAdapter(getActivity(), R.layout.flashcard_set, mA.cardsets);
        cardset_list_view.setAdapter(adapter);

        final FloatingActionButton add = view.findViewById(R.id.add_cardset);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Flashcard> new_cards = new ArrayList<Flashcard>();
                FlashcardSet new_set = new FlashcardSet("New Set", new_cards);
                mA.cardsets.add(new_set);
                update_list_view();
            }
        });
        return view;
    }

    public void update_list_view() {

        // make array adapter to bind arraylist to listview with new custom item layout
        FlashcardSetAdapter aa = new FlashcardSetAdapter(getActivity(), R.layout.flashcard_set, mA.cardsets);
        cardset_list_view.setAdapter(aa);
        registerForContextMenu(cardset_list_view);
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

    public void onDetach() {
        Moshi moshi = new Moshi.Builder().build();
        Type type = Types.newParameterizedType(List.class, FlashcardSet.class);
        JsonAdapter<List> adapter = moshi.adapter(type);

        String json = adapter.toJson(mA.cardsets);

        String filename = "cards.json";
        try {
            FileOutputStream fileout = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter outputWriter = new OutputStreamWriter(fileout);
            outputWriter.write(json);
            outputWriter.close();
            System.out.println("writing success");
        } catch (Exception e) {
            System.out.println("writing failed");
            e.printStackTrace();
        }

        super.onDetach();
    }


}

