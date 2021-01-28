package com.example.schoolcheck.DB_pack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.schoolcheck.note_tasks.noteClass;

import java.util.ArrayList;
import java.util.List;

public class dbHandler extends SQLiteOpenHelper {
                                                                                                    /*
                                                                                                    campi ed elementi per il DB
                                                                                                    */
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "notedb";
    private static final String NOTE_TABLE = "notetable";
    private static final String ID = "id";
    private static final String NOTE = "note";
    private static final String STATE = "state";
                                                                                                    //struttura tracciato record

    private static final String CREATE_NOTE_TABLE = "CREATE TABLE " + NOTE_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NOTE + " TEXT," + STATE + " INTEGER)";

    private SQLiteDatabase db;

    public dbHandler(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){                                                        //costruttore => crea la tabella delle note
        db.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV){
                                                                                                    //drop tabella precedente
        db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE);
                                                                                                    //crea la nuova tabella
        onCreate(db);
    }

    public void openDB(){
        db = this.getWritableDatabase();//passa o crea il database in maniera tale da poterci leggere/scrivere
    }

    public void insertNote(noteClass newNote){                                                      //inserisce la nota nel database

        ContentValues cv = new ContentValues();                                                     /*
                                                                                                    funzionamento simile ad un vettore
                                                                                                    */
        cv.put(NOTE, newNote.getNoteText());
        cv.put(STATE, 0);

        db.insert(NOTE_TABLE, null, cv);
    }

    public List<noteClass> getNotes(){                                                              //metodo per recuperare dati dal database (in questo caso le note verranno aggiunte ad una lista)
        List<noteClass> noteList_aux = new ArrayList<>();
        Cursor c = null;

        db.beginTransaction();                                                                      //inizia il trasferimento dei dati (db --> noteList)

        try{
            final String query = "SELECT * FROM " + NOTE_TABLE;
            c = db.rawQuery(query, null);
            if(c != null){
                if(c.moveToFirst()){
                    do {

                        noteClass note_c_aux = new noteClass();

                        note_c_aux.setId(c.getInt(c.getColumnIndex(ID)));
                        note_c_aux.setState(c.getInt(c.getColumnIndex(STATE)));
                        note_c_aux.setNoteText(c.getString(c.getColumnIndex(NOTE)));
                        noteList_aux.add(note_c_aux);

                    }while(c.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            c.close();
        }

        return noteList_aux;
    }

    public void updateState(int id_passed, int state_passed){
        ContentValues cv = new ContentValues();
        cv.put(STATE, state_passed);

        db.update(NOTE_TABLE, cv, ID + "=?", new  String[]{String.valueOf(id_passed)});
    }

    public void updateNoteText(int id_passed, String noteText_passed){
        ContentValues cv = new ContentValues();
        cv.put(NOTE, noteText_passed);

        db.update(NOTE_TABLE, cv, ID + "=?", new  String[]{String.valueOf(id_passed)});
    }

    public void deleteNote(int id_passed){
        db.delete(NOTE_TABLE, ID + "=?", new String[]{String.valueOf(id_passed)});
    }
}
