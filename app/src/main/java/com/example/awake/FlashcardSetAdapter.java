package com.example.awake;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;
/**
 */
public class FlashcardSetAdapter extends ArrayAdapter<FlashcardSet>  {

    private List<FlashcardSet> list = new ArrayList<FlashcardSet>();
    private int resource;
    private MainActivity mA;

    public FlashcardSetAdapter(Context ctx, int res, List<FlashcardSet> items)
    {
        super(ctx, res, items);
        resource = res;
        this.list = items;
        mA = (MainActivity) ctx;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout cardsetView;
        final FlashcardSet cs = getItem(position);

        if (convertView == null) {
            cardsetView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, cardsetView, true);
        } else {
            cardsetView = (LinearLayout) convertView;
        }

        final EditText titleView = (EditText) cardsetView.findViewById(R.id.title_text);
        TextView lenView = (TextView) cardsetView.findViewById(R.id.len_text);
        ImageButton edit = (ImageButton) cardsetView.findViewById(R.id.edit_cardset);
        ImageButton delete = (ImageButton) cardsetView.findViewById(R.id.delete_cardset);

        titleView.setText(cs.getTitle());
        String cards = String.valueOf(cs.getListlen()) + " flashcards";
        lenView.setText(cards);

        titleView.setOnKeyListener(new View.OnKeyListener() {
                                       @Override
                                       public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                           if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                                               cs.setTitle(titleView.getText().toString());
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

        edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                mA.start_card_list_fragment();
            }
        });

        return cardsetView;
    }


}

