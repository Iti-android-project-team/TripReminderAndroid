package com.example.tripreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.R;
import com.example.tripreminder.data.model.db.Trips;
import com.example.tripreminder.ui.fragment.history.HistoryFragment;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<Trips> trips = new ArrayList<>();
    private OnItemClickListener listener;
    private Fragment parent;
    private Context context;

    public interface OnItemClickListener {
        void showNotesButtonClicked(int position);

        void deleteTripButtonClicked(int position);
    }

    public HistoryAdapter(Fragment parent, Context context) {
        this.parent = parent;
        this.context = context;
    }
    public Trips getItem(int position) {
        return trips.get(position);
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View v = inflater.inflate(R.layout.item_history_trip, parent, false);

        HistoryViewHolder viewHolder = new HistoryViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {

        holder.name.setText(trips.get(position).getTripName());
        holder.status.setText(trips.get(position).getStatus());
        holder.startPoint.setText(trips.get(position).getStartPoint());
        holder.endPoint.setText(trips.get(position).getEndPoint());
        holder.time.setText(trips.get(position).getTime());
        holder.date.setText(trips.get(position).getDate());

    }


    @Override
    public int getItemCount() {
        //if (trips.size() != 0)
            return trips.size();
       // else
           // return 0;
    }

    public class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView date, time, name, startPoint, endPoint, status;
        public ImageView imageView;
        public Button viewDetails;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateTextView);
            time = itemView.findViewById(R.id.timeTextView);
            name = itemView.findViewById(R.id.nameTextView);
            startPoint = itemView.findViewById(R.id.startPointText);
            endPoint = itemView.findViewById(R.id.endPointText);
            status = itemView.findViewById(R.id.statusText);
            imageView = itemView.findViewById(R.id.imgDeleteTrip);
            viewDetails = itemView.findViewById(R.id.viewDetailsBtn);

            viewDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener = (HistoryFragment) parent;
                    listener.showNotesButtonClicked(position);
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    listener = (HistoryFragment) parent;
                    listener.deleteTripButtonClicked(position);
                }
            });
        }
    }
    public void loadData( List<Trips> trips) {
            this.trips = new  ArrayList(trips);
            notifyDataSetChanged();
    }
}
