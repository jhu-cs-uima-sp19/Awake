package com.example.awake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsAdapter extends ArrayAdapter<Flashcard> {
    private int resource;
    private Context context;
    private List<Flashcard> list = new ArrayList<Flashcard>();

    public FlashcardsAdapter(Context ctx, int res, List<Flashcard> items)
    {
        super(ctx, res, items);
        this.list = items;
        this.context = ctx;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout flashcardsView;
        Flashcard f = getItem(position);

        if (convertView == null) {
            flashcardsView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, flashcardsView, true);
        } else {
            flashcardsView = (LinearLayout) convertView;
        }

        EditText term_view = (EditText) flashcardsView.findViewById(R.id.term_input);
        EditText definition_view = (EditText) flashcardsView.findViewById(R.id.definition_input );
        ImageButton delete = (ImageButton) flashcardsView.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });

        term_view.setText(f.getName());
        definition_view.setText(f.getContent());


        return flashcardsView;
    }

}
