package com.example.awake;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
    private Button donefcset;
    private Button cancelfcset;
    private List<String> oldtitles = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.cardset_list_fragment, container, false);
        // Naming toolbar.
        mA.getSupportActionBar().setTitle("Manage Flashcards");

        for (int i = 0; i < mA.cardsets.size(); i++) {
            oldtitles.add(mA.cardsets.get(i).getTitle());
        }

        //load a set here
        // ArrayList<Flashcard> list =
        // Use the view for this fragment to search for UI components.
        cardset_list_view = (ListView) view.findViewById(R.id.cardset_list);
        final FlashcardSetAdapter adapter = new FlashcardSetAdapter(getActivity(), R.layout.flashcard_set, mA.cardsets);
        cardset_list_view.setAdapter(adapter);

        cardset_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mA.setCardNumber(position);
                mA.currentset = position;
                adapter.notifyDataSetChanged();
            }
        });

        final FloatingActionButton add = view.findViewById(R.id.add_cardset);
        add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<Flashcard> new_cards = new ArrayList<Flashcard>();
                new_cards.add(new Flashcard());
                FlashcardSet new_set = new FlashcardSet("New Set", new_cards);
                mA.cardsets.add(new_set);
                update_list_view();
            }
        });

        donefcset = view.findViewById(R.id.donefcset);
        cancelfcset = view.findViewById(R.id.cancelfcset);

        donefcset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Update or create new alarm. THIS NEEDS TO BE UPDATED ONCE REPEATS ARE IMPLEMENTED.
                save();
                // Will switch in ListView fragment and update the ArrayAdapter.
                mA.start_set_alarm_fragment();
            }
        });

        cancelfcset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Will switch in ListView fragment and update the ArrayAdapter.
                for (int i = 0; i < mA.cardsets.size(); i++) {
                    mA.cardsets.get(i).setTitle(oldtitles.get(i));
                }
                mA.start_set_alarm_fragment();
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

    public void save() {
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

    }


}

