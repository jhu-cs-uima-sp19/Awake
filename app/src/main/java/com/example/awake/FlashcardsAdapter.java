package com.example.awake;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsAdapter extends ArrayAdapter<Flashcard> {
    private int resource;
    private List<Flashcard> list = new ArrayList<Flashcard>();
    private MainActivity mA;

    public FlashcardsAdapter(Context ctx, int res, List<Flashcard> items)
    {
        super(ctx, res, items);
        this.list = items;
        this.resource = res;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout flashcardsView;
        final Flashcard f = getItem(position);
        if (convertView == null) {
            flashcardsView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, flashcardsView, true);
        } else {
            flashcardsView = (LinearLayout) convertView;
        }

        final EditText term_view = (EditText) flashcardsView.findViewById(R.id.term_input);
        final EditText definition_view = (EditText) flashcardsView.findViewById(R.id.definition_input );
        ImageButton delete = (ImageButton) flashcardsView.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mA)
                        .setTitle("Warning")
                        .setMessage("Delete " + f.getName() + " card?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                list.remove(position);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                notifyDataSetChanged();
            }
        });

        term_view.setText(f.getName());
        definition_view.setText(f.getContent());

        term_view.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                f.setName(term_view.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        definition_view.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                f.setName(definition_view.getText().toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return flashcardsView;
    }

}
