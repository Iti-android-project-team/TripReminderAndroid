package com.example.tripreminder.ui.fragment.history;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tripreminder.R;
import com.example.tripreminder.adapter.HistoryAdapter;
import com.example.tripreminder.adapter.HistoryNoteAdapter;
import com.example.tripreminder.data.local.SharedPref;
import com.example.tripreminder.data.model.db.Note;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


public class NoteFragment extends DialogFragment {
    private RecyclerView recyclerNote;
    private HistoryNoteAdapter adapter;
    private Button btnClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        init(view);
        return view;
    }

    private void init(View view) {
        btnClose = view.findViewById(R.id.btnClose);
        recyclerNote = view.findViewById(R.id.recyclerNote);
        recyclerNote.setLayoutManager(new LinearLayoutManager(getActivity()));


        SharedPref.createPrefObject(getContext());
        String notesString = SharedPref.getNotes();
        Log.i("notesString", notesString);
        if (!notesString.equals("")) {
            Type collectionType = new TypeToken<List<Note>>() {
            }.getType();
            List<Note> noteList = new Gson()
                    .fromJson(notesString, collectionType);
            Log.i("notes",noteList.get(0).getNote());
            adapter = new HistoryNoteAdapter(getContext(),noteList);
            recyclerNote.setAdapter(adapter);

        }
        buttonClickedCall();
    }

    private void buttonClickedCall(){
        btnClose.setOnClickListener(v->{
            dismiss();
        });
    }


}

