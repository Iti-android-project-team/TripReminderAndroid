package com.example.tripreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.data.model.db.Note;

import java.util.ArrayList;
import java.util.List;

public class HistoryNoteAdapter extends RecyclerView.Adapter<HistoryNoteAdapter.NoteViewHolder> {

    private List<Note> notes;
    private Context context;

    public HistoryNoteAdapter(Context context,List<Note> notes) {
        this.context = context;
        this.notes = notes;
    }

    @NonNull
    @Override
    public HistoryNoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.note_row_items, parent, false);

        HistoryNoteAdapter.NoteViewHolder viewHolder = new HistoryNoteAdapter.NoteViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryNoteAdapter.NoteViewHolder holder, int position) {
        holder.addNote.setText(notes.get(position).getNote());

    }

    @Override
    public int getItemCount() {
        if (notes.size() != 0)
            return notes.size();
        else
            return 0;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView addNote;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            addNote = itemView.findViewById(R.id.txt_note);

        }
    }
}
