package com.example.schoolcheck.adapter_pack;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolcheck.DB_pack.dbHandler;
import com.example.schoolcheck.MainActivity;
import com.example.schoolcheck.R;
import com.example.schoolcheck.note_tasks.addNoteClass;
import com.example.schoolcheck.note_tasks.noteClass;

import java.util.List;

public class note_adapter extends RecyclerView.Adapter<note_adapter.ViewHolder> {

    private List<noteClass> noteList;                                                               //lista delle note
    private MainActivity activity;
    private dbHandler db;

    public note_adapter(dbHandler database, MainActivity activity){

        this.db = database;
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public Context getContext() {
        return activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox note;

        ViewHolder(View view){
            super(view);

            note = view.findViewById(R.id.note_checkbox);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        db.openDB();

        noteClass item = noteList.get(position);
        holder.note.setText(item.getNoteText());
        holder.note.setChecked(toBoolean(item.getState()));

        holder.note.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    db.updateState(item.getId(), 1);
                }
                else{
                    db.updateState(item.getId(), 0);
                }
            }
        });
    }

    private boolean toBoolean(int i){                                                               //metodo per traformare intero (note.state) in booleano
        return i != 0;                                                                              //se e solo se != 0
    }

    @Override
    public int getItemCount() {                                                                     //invia il numero di elementi nella lista
        return noteList.size();
    }

    public void setNoteList(List<noteClass> noteList_passed){

        this.noteList = noteList_passed;
        notifyDataSetChanged();                                                                     //observer notifica cambiamento dei dati (struttura e id)
                                                                                                    //RecicleView aggiornato
    }

    public void editNote(int pos){

        noteClass note = noteList.get(pos);

        Bundle bundle = new Bundle();
        bundle.putInt("id", note.getId());
        bundle.putString("note", note.getNoteText());

        addNoteClass fragment = new addNoteClass();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), addNoteClass.TAG);
    }

    public void deleteNote(int pos){

        noteClass note = noteList.get(pos);
        int id = note.getId();
        noteList.remove(pos);
        db.deleteNote(id);
        notifyItemRemoved(pos);

    }

}
