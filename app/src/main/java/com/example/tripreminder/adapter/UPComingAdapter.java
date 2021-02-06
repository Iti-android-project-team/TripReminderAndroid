package com.example.tripreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;

import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.ui.fragment.upcoming.UpComingFragment;

import java.util.List;

public class UPComingAdapter extends  RecyclerView.Adapter<UPComingAdapter.UPComingViewHolder> {
    Context context;
    private List<Trips> trips ;
    private Fragment parent;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemNoteClick(int position);
        void onItemEditClick(int position);
        void onItemStartClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public UPComingAdapter(Context context, List<Trips> trips, Fragment parent) {
        this.context=context;
        this.trips= trips;
        this.parent = parent;
    }

    @NonNull
    @Override
    public UPComingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v= inflater.inflate(R.layout.item_trip,parent,false);

        UPComingViewHolder viewHolder = new UPComingViewHolder(v,mListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UPComingViewHolder holder, int position) {
        holder.name.setText(trips.get(position).getTripName());
        holder.status.setText(trips.get(position).getStatus());
        holder.startPoint.setText(trips.get(position).getStartPoint());
        holder.endPoint.setText(trips.get(position).getEndPoint());
        holder.time.setText(trips.get(position).getTime());
        holder.date.setText(trips.get(position).getDate());


    }
    public void setTrips(List<Trips> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (trips.size() != 0)
            return trips.size();
        else
            return 0;


    }
    @Nullable
    public Trips getItem(int position) {
        return trips.get(position);
    }

    public class UPComingViewHolder extends RecyclerView.ViewHolder {
         public TextView date , time , name , startPoint , endPoint , status;
         public ImageView tripEdit , tripNote , imageView;
         public Button start;
        public UPComingViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
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

            tripNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  if (listener!= null){
                    //    int position = getAdapterPosition();
                      //  if (position!= RecyclerView.NO_POSITION);
                       // listener.onItemNoteClick(position);
                    //}
                    int position = getAdapterPosition();
                    mListener = (UpComingFragment)parent;
                    mListener.onItemNoteClick(position);
                }
            });
            tripEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                /*    if (listener!= null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION);
                        listener.onItemEditClick(position);
                    }*/
                    int position = getAdapterPosition();
                    mListener = (UpComingFragment)parent;
                    mListener.onItemEditClick(position);
                }
            });
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 /*   if (listener!= null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION);
                        listener.onItemStartClick(position);
                    }*/
                    int position = getAdapterPosition();
                    mListener = (UpComingFragment)parent;
                    mListener.onItemStartClick(position);
                }
            });

        }
    }
}
