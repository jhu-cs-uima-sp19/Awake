package com.example.awake;

import java.lang.String;

public class Flashcard {
    private String name;
    private String content;

    Flashcard(String name, String content) {
        this.setAll(name, content);
    }

    public String getName() { return name; }
    public String getContent() { return content; }

    public void setAll(String title, String content) {
        this.name = title;
        this.content = content;
    }
}