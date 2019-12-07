package com.example.awake;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
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
        this.mA = (MainActivity) ctx;
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

//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new AlertDialog.Builder(mA)
//                        .setTitle("Warning")
//                        .setMessage("Delete " + f.getName() + " card?")
//                        .setIcon(android.R.drawable.ic_dialog_alert)
//                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int whichButton) {
//                                list.remove(position);
//                            }})
//                        .setNegativeButton(android.R.string.no, null).show();
//                list.remove(position); //or some other task
//                notifyDataSetChanged();
//            }
//        });

        term_view.setText(f.getName());
        definition_view.setText(f.getContent());

        term_view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    f.setName(term_view.getText().toString());
                    notifyDataSetChanged();
                }
                return false;
            }
        });

        definition_view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    f.setContent(definition_view.getText().toString());
                    notifyDataSetChanged();
                }
                return false;
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });

        return flashcardsView;
    }

}
