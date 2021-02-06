package com.example.tripreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.Notes;
import com.example.tripreminder.data.model.db.Trips;


import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    Context context;
    private List<Note> notes ;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemDeleteClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public NoteAdapter(Context context, List<Note> notes) {
        this.context=context;
        this.notes= notes;
    }
    @NonNull
    @Override
    public NoteAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v= inflater.inflate(R.layout.item_note,parent,false);

       NoteAdapter.NoteViewHolder viewHolder = new NoteAdapter.NoteViewHolder(v,mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
              holder.addNote.setText(notes.get(position).getNotes());

    }

 /*   public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }*/

    @Override
    public int getItemCount() {
        if (notes.size() != 0)
            return notes.size();
        else
            return 0;
    }


    public class NoteViewHolder extends RecyclerView.ViewHolder {
        public TextView addNote ;
        public ImageView deletNote ;
        public NoteViewHolder(@NonNull View itemView,OnItemClickListener listener) {
            super(itemView);
            addNote = itemView.findViewById(R.id.txt_note);
            deletNote = itemView.findViewById(R.id.delete_note);

            deletNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!= null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION);
                        listener.onItemDeleteClick(position);
                    }
                }
            });


        }
    }

}
