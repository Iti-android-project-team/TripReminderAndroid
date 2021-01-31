package com.example.tripreminder.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.google.api.ResourceDescriptor;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v= inflater.inflate(R.layout.item_history_trip,parent,false);

        HistoryViewHolder viewHolder = new HistoryViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {

    }



    @Override
    public int getItemCount() {
        return 0;
    }
    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        public TextView date , time , name , startPoint , endPoint , status;
        public ImageView imageView;
        public Button viewDetails;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_date);
            time = itemView.findViewById(R.id.txt_time);
            name = itemView.findViewById(R.id.txt_name);
            startPoint = itemView.findViewById(R.id.startPointText);
            endPoint = itemView.findViewById(R.id.endPointText);
            status = itemView.findViewById(R.id.statusText);
            imageView = itemView.findViewById(R.id.historyImageView);
            viewDetails = itemView.findViewById(R.id.viewDetailsBtn);
        }
    }
}
