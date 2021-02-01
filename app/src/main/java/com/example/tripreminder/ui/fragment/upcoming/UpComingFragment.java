package com.example.tripreminder.ui.fragment.upcoming;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.R;
import com.example.tripreminder.model.db.Note;
import com.example.tripreminder.model.db.Notes;
import com.example.tripreminder.model.db.Trips;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UpComingFragment extends Fragment {

    private UpComingViewModel upComingViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trips,container,false);

        //upComingViewModel = new ViewModelProvider(this).get(UpComingViewModel.class);
upComingViewModel = new ViewModelProvider(this,
        new ViewModelProvider.AndroidViewModelFactory(Objects.requireNonNull(getActivity()).getApplication())).get(UpComingViewModel.class);
        Trips trips = new Trips();
        trips.setTripName("test");
        trips.setDate("hi");
        trips.setStatus("upComing");
        trips.setDirection(false);
        trips.setEndPoint("hi");
        trips.setRepeated("hi");
        trips.setTime("hi");
        trips.setStartPoint("hi");

        List<Note> n = new ArrayList<>();
        Note nn = new Note();
        nn.setNote("rrrrr ");
        n.add(nn);
        n.add(nn);
        n.add(nn);
        n.add(nn);
//        List<Notes> notes = new ArrayList<>();
//        notes.add(n);

        upComingViewModel.insert(trips);
        upComingViewModel.insertNote(n,19);

        //upComingViewModel.updateTrip("upComing",1);

        upComingViewModel.getAllTrips().observe(getViewLifecycleOwner(), it -> {
            if (it.size() != 0) {
                if(it != null){
                    List<Trips> t= it;
                    for(int i = 0 ; i< t.size(); i++){
//                 Log.i("Data",t.get(i).getTime());
//                 Log.i("Data",t.get(i).getTid()+"");
                    }
                }

            }

        });

        upComingViewModel.getAllNotes(19).observe(getViewLifecycleOwner(), it -> {
            if (it.size() > 0) {
                List<String> t= it;
                for(int i = 0 ; i< t.size(); i++){
                    Log.i("Data",t.get(i));
                    //upComingViewModel.updateTrip("cancled",t.get(i).getTid());
                }

            }

        });


        return view;
    }
}
