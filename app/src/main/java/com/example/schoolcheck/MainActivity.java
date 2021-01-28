package com.example.schoolcheck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.schoolcheck.DB_pack.dbHandler;
import com.example.schoolcheck.adapter_pack.note_adapter;
import com.example.schoolcheck.dialog_pack.DialogCloseListener;
import com.example.schoolcheck.note_tasks.addNoteClass;
import com.example.schoolcheck.note_tasks.noteClass;
import com.example.schoolcheck.recycler_pack.recyclerHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    public final String TAG_MAIN = "debug MainAct.java =>";

    private RecyclerView noteViewRec;
    private note_adapter noteAdapter;
    private dbHandler db;
    private FloatingActionButton btn_add;

    private List<noteClass> noteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new dbHandler(MainActivity.this);
        db.openDB();

        noteViewRec = findViewById(R.id.recycleview);
        noteViewRec.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        noteAdapter = new note_adapter(db, MainActivity.this);
        noteViewRec.setAdapter(noteAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new recyclerHelper(noteAdapter));
        itemTouchHelper.attachToRecyclerView(noteViewRec);

        noteList = db.getNotes();
        Collections.reverse(noteList);

        noteAdapter.setNoteList(noteList);

        btn_add = findViewById(R.id.plusbtn);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addNoteClass aux;
                aux = addNoteClass.addNote();
                aux.show(getSupportFragmentManager(), addNoteClass.TAG);

            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialogInterface) {

        noteList = db.getNotes();
        Collections.reverse(noteList);                                                              /*il db viene letto "normalmente" quindi per avere la lista ordinata utilizzo questo metodo
                                                                                                    invece che "sporcare" la funzione in "dbhandler"
                                                                                                    (anche perchè inizialmente non ci avevo proprio pensato)
                                                                                                    */
        noteAdapter.setNoteList(noteList);
        Log.e(TAG_MAIN,"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        noteAdapter.notifyDataSetChanged();

    }
}