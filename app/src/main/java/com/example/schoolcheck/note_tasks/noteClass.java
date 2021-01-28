package com.example.schoolcheck.note_tasks;

import androidx.recyclerview.widget.RecyclerView;

public class noteClass {                                                                            //definisce i parametri delle "note" che saranno utilizzate come oggetti

    private int id, state;
    private String note_str;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getNoteText() {
        return note_str;
    }

    public void setNoteText(String note) {
        this.note_str = note;
    }
}
