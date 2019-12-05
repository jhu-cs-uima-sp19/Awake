package com.example.awake;

import java.lang.String;
import java.util.List;

/**
 * Holds data for one job
 */
public class FlashcardSet {
    private String title;
    private List<Flashcard> cardlist;
    private int listlen;

    FlashcardSet(String title, List<Flashcard> cardlist) {
        this.title = title;
        this.cardlist = cardlist;
        this.listlen = cardlist.size();
    }

    public String getTitle() { return title; }
    public List<Flashcard> getCardlist() { return cardlist; }
    public int getListlen() { return listlen; }

    public void setTitle(String title) { this.title = title; }
    public void setList(List<Flashcard> cardlist) {
        this.cardlist = cardlist;
        this.listlen = cardlist.size();
    }

    public void refreshLen() {
        this.listlen = cardlist.size();
    }

}

