package com.example.tripreminder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {


    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v= inflater.inflate(R.layout.item_note,parent,false);

       NoteAdapter.NoteViewHolder viewHolder = new NoteAdapter.NoteViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView addNote ;
        public ImageView deletNote ;
        public CheckBox select;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            addNote = itemView.findViewById(R.id.txt_note);
            deletNote = itemView.findViewById(R.id.delete_note);
            select = itemView.findViewById(R.id.chBox);




        }
    }

}
