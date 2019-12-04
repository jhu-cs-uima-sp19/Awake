package com.example.awake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 */
public class FlashcardAdapter extends ArrayAdapter<Flashcard>  {

    private int resource;

    public FlashcardAdapter(Context ctx, int res, List<Flashcard> items)
    {
        super(ctx, res, items);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout flashcardView;
        Flashcard fc = getItem(position);

        if (convertView == null) {
            flashcardView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, flashcardView, true);
        } else {
            flashcardView = (LinearLayout) convertView;
        }

        TextView addrView = (TextView) jobView.findViewById(R.id.address_text);
        TextView dateView = (TextView) jobView.findViewById(R.id.date_text);
        TextView paidView = (TextView) jobView.findViewById(R.id.paid_view);

        addrView.setText(jb.getWhere());
        dateView.setText(jb.getWhen());
        paidView.setText(jb.getPaid()==1 ? "PAID" : "unpaid");

        return jobView;
    }


}

