package com.example.awake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

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
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LinearLayout cardsetView;
        FlashcardSet cs = getItem(position);

        if (convertView == null) {
            cardsetView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, cardsetView, true);
        } else {
            cardsetView = (LinearLayout) convertView;
        }

        EditText titleView = (EditText) cardsetView.findViewById(R.id.title_text);
        EditText lenView = (EditText) cardsetView.findViewById(R.id.len_text);
        Button edit = (Button) cardsetView.findViewById(R.id.edit_cardset);
        Button delete = (Button) cardsetView.findViewById(R.id.delete_cardset);

        titleView.setText(cs.getTitle());
        lenView.setText(cs.getListlen());

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

