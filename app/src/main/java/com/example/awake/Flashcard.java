package com.example.awake;

import java.lang.String;

/**
 * Holds data for one job
 */
public class Flashcard {
    private String title;
    private String content;

    Flashcard(String title, String content) {
        this.setAll(title, content);
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }

    public void setAll(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
