package com.example.schoolcheck.note_tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.JetPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.schoolcheck.DB_pack.dbHandler;
import com.example.schoolcheck.MainActivity;
import com.example.schoolcheck.R;
import com.example.schoolcheck.dialog_pack.DialogCloseListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class addNoteClass extends BottomSheetDialogFragment {

    public static final String TAG = "debug: addNoteFragment =>";

    private EditText txt_newNote;
    private Button btn_save;
    private dbHandler db;

    public static addNoteClass addNote(){
        return new addNoteClass();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, R.style.dialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.adding_note_layout, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_newNote = (EditText) Objects.requireNonNull(getView()).findViewById(R.id.newTaskText);
        btn_save = (Button) getView().findViewById(R.id.newTaskButton);
        boolean updated = false;

        final Bundle bundle = getArguments();
        if(bundle != null) {

            updated = true;

            String noteText_received = bundle.getString("note");
            txt_newNote.setText(noteText_received);

            if (noteText_received.length() > 0) {

                btn_save.setTextColor(ContextCompat.getColor(getContext(), R.color.buttonColor));
            }
        }

        db = new dbHandler(getActivity());
        db.openDB();

            //String colorStr = "#FF1A88CC";
            //btn_save.setTextColor(Color.parseColor(colorStr));

        txt_newNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if (s.toString().equals("")) {

                    btn_save.setEnabled(false);
                    btn_save.setTextColor(Color.GRAY);

                }
                else{

                    btn_save.setEnabled(true);
                    btn_save.setTextColor(ContextCompat.getColor(getContext(),R.color.buttonColor));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        boolean finalUpdated = updated;
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = txt_newNote.getText().toString();

                if(finalUpdated){

                    db.updateNoteText(bundle.getInt("id"), str);
                }
                else{

                    noteClass note_aux = new noteClass();
                    note_aux.setNoteText(str);
                    note_aux.setState(0);
                    db.insertNote(note_aux);

                }

                dismiss();
            }
        });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){                                                /*controlla se l'activity (MainActivity) contiene un'istanza di DialogCloseListener
                                                                                                    (contiene solo il metodo "CloseDialog()") */
            ((DialogCloseListener)activity).handleDialogClose(dialog);

        }
    }
}
