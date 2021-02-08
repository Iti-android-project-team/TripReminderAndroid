package com.example.tripreminder.ui.activities.addNote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tripreminder.R;
import com.example.tripreminder.adapter.NoteAdapter;
import com.example.tripreminder.adapter.UPComingAdapter;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.ui.activities.MainActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class AddNoteActivity extends AppCompatActivity {
    ImageView close;
    EditText body;
    Button addIt, reset;

    private List<Note> notesList = new ArrayList<>();
    private NoteAdapter adapter;
    private int id;
    private AddNoteViewModel notesViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Note> newNoteList = new ArrayList<>();
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        initializ();

        SharedPref.createPrefObject(AddNoteActivity.this);
        String notesString = SharedPref.getNotes();
        id = getIntent().getIntExtra("ID", 0);
        Log.i("id from note", String.valueOf(id));
        Log.i("string from note", notesString);
        if (notesString != null) {
            Type collectionType = new TypeToken<List<Note>>() {
            }.getType();
            notesList = new Gson().fromJson(notesString, collectionType);
            //Log.i("list", String.valueOf(notesList.size()));
            if (notesList != null) {
                for (int i = 0; i < notesList.size(); i++) {
                    newNoteList.add(notesList.get(i));
                }
            }

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(AddNoteActivity.this));

        if (newNoteList != null) {
            adapter = new NoteAdapter(this, newNoteList);
            recyclerView.setAdapter(adapter);
        }
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissKeyboard(AddNoteActivity.this);
                body.setText("");
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        addIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /*   Note n = new Note();
                    n.setNote(addNote.getText().toString());
                    Log.i("iadd Note","adddNote"+addNote.getText().toString() );
                    notesList.add(n);
*/
                if(!body.getText().toString().trim().isEmpty()){
                    Note no = new Note();
                    no.setNotes(body.getText().toString());
                    Log.i("note", "adddNote" + body.getText().toString());
                    newNoteList.add(no);
                    insertNotesInDB();
                    Toast.makeText(AddNoteActivity.this, "Note Add", Toast.LENGTH_SHORT).show();
                    dismissKeyboard(AddNoteActivity.this);
                    body.setText("");
                }else{
                    Toast.makeText(AddNoteActivity.this,"Please write your note first",Toast.LENGTH_SHORT).show();
                }

            }
        });
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemDeleteClick(int position) {
                newNoteList.remove(position);
                deleteNote();
//                adapter = new NoteAdapter(AddNoteActivity.this, newNoteList);
//                recyclerView.setAdapter(adapter);
                adapter.setNotes(newNoteList);
                Toast.makeText(AddNoteActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void insertNotesInDB() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            if (addIt.getText().toString().isEmpty()) {
                addIt.setError(getString(R.string.text_empty));
                return;
            }
        }
        notesViewModel.insertNote(newNoteList, id);

    }

    private void deleteNote() {
        notesViewModel.insertNote(newNoteList, id);

    }


    public void initializ() {

        SharedPref.createPrefObject(AddNoteActivity.this);
        userEmail = SharedPref.getUserEmail();
        if (!userEmail.equals(" ")) {
            notesViewModel = new ViewModelProvider(this, new AddNoteViewModelFactory(getApplication(),
                    userEmail)).get(AddNoteViewModel.class);
        }
        close = findViewById(R.id.note_img_close);
        body = findViewById(R.id.note_body);
        addIt = findViewById(R.id.note_btn_add);
        reset = findViewById(R.id.note_btn_reset);
        recyclerView = findViewById(R.id.addNote_recyclerView);

    }

    public void dismissKeyboard(AppCompatActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != activity.getCurrentFocus())
            imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }


}