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

public class UPComingAdapter extends RecyclerView.Adapter<UPComingAdapter.UPComingViewHolder> {


    @NonNull
    @Override
    public UPComingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v= inflater.inflate(R.layout.item_trip,parent,false);

        UPComingViewHolder viewHolder = new UPComingViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UPComingViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class UPComingViewHolder extends RecyclerView.ViewHolder {
         public TextView date , time , name , startPoint , endPoint , status;
         public ImageView tripEdit , tripNote , imageView;
         public Button start;
        public UPComingViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.txt_date);
            time = itemView.findViewById(R.id.txt_time);
            name = itemView.findViewById(R.id.txt_name);
            startPoint = itemView.findViewById(R.id.txt_startPoint);
            endPoint = itemView.findViewById(R.id.txt_endPoint);
            status = itemView.findViewById(R.id.txt_status);
            tripEdit = itemView.findViewById(R.id.img_tripEdit);
            tripNote = itemView.findViewById(R.id.img_tripNote);
            imageView = itemView.findViewById(R.id.imageView);
            start = itemView.findViewById(R.id.btn_startTrip);
        }
    }
}
