package com.example.awake;

import java.lang.String;
import java.util.ArrayList;

/**
 * Holds data for one job
 */
public class FlashcardSet {
    private String title;
    private ArrayList<Flashcard> cardlist;
    private int listlen;

    FlashcardSet(String title, ArrayList<Flashcard> cardlist) {
        this.title = title;
        this.cardlist = cardlist;
        this.listlen = cardlist.size();
    }

    public String getTitle() { return title; }
    public ArrayList<Flashcard> getCardlist() { return cardlist; }
    public int getListlen() { return listlen; }

}

