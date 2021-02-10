package com.example.tripreminder;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.tripreminder.data.model.db.Note;
import com.example.tripreminder.data.model.db.Trips;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String TAG = "MapsFragment";

    private  final List<Double> latitudeStartPointList = new ArrayList<>();
    private final List<Double> longitudeStartPointList = new ArrayList<>();
    private final List<Double> latitudeEndPointList = new ArrayList<>();
    private final List<Double> longitudeEndPointList = new ArrayList<>();
    private  List<Trips> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_maps);
        init();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void init() {

        String historyTrips = getIntent().getStringExtra("HISTORY_TRIPS");

        Type collectionType = new TypeToken<List<Trips>>() {
        }.getType();


        historyList = new Gson()
                .fromJson(historyTrips, collectionType);

            for(int i = 0 ;i<historyList.size();i++){
                getLatLong(historyList.get(i).getStartPoint());
                getLatLongEndPoint(historyList.get(i).getEndPoint());
            }
            //openMap();
        //}

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < historyList.size(); i++) {
            drawMapLines(latitudeStartPointList.get(i), longitudeStartPointList.get(i),
                    latitudeEndPointList.get(i), longitudeEndPointList.get(i));
        }


        mMap.getUiSettings().setZoomControlsEnabled(true);
        LatLng zaragoza = new LatLng(26.77619851601672, 29.65819791593859);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza, 7));

    }

    private void drawMapLines(double latitudeStartPoint, double longitudeStartPoint,
                              double latitudeEndPoint, double longitudeEndPoint) {
        LatLng latitudePosition = new LatLng(latitudeStartPoint, longitudeStartPoint);
        mMap.addMarker(new MarkerOptions().position(latitudePosition));

        String latLogStartPoint = latitudeStartPoint + ", " + longitudeStartPoint;

        LatLng longitudePosition = new LatLng(latitudeEndPoint, longitudeEndPoint);
        mMap.addMarker(new MarkerOptions().position(longitudePosition));

        String latLogEndPoint = latitudeEndPoint + ", " + longitudeEndPoint;


        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBL_za9z0eWrk_VFVN1TCuP32mQW19P52o")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, latLogStartPoint, latLogEndPoint);
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(getRandomColor()).width(18);
            mMap.addPolyline(opts);
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }


    private void getLatLong(String address){
        if(Geocoder.isPresent()){
            try {
                String location = address;
                Geocoder gc = new Geocoder(this);
                List<Address> addresses= gc.getFromLocationName(location, 5); // get the found Address Objects

                List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
                if(ll.size()>0){
                    latitudeStartPointList.add(ll.get(0).latitude) ;
                    longitudeStartPointList.add( ll.get(0).longitude);
                    Log.i("IOException", String.valueOf(ll.get(0).latitude));
                    Log.i("IOException", String.valueOf(ll.get(0).longitude));
                }

            } catch (IOException e) {
                // handle the exception
                Log.i("IOException",e.getLocalizedMessage());
            }
        }

    }

    private void getLatLongEndPoint(String address){
        if(Geocoder.isPresent()){
            try {
                String location = address;
                Geocoder gc = new Geocoder(this);
                List<Address> addresses= gc.getFromLocationName(location, 5); // get the found Address Objects

                List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for(Address a : addresses){
                    if(a.hasLatitude() && a.hasLongitude()){
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    }
                }
                if(ll.size()>0){
                    latitudeEndPointList.add(ll.get(0).latitude) ;
                    longitudeEndPointList.add( ll.get(0).longitude);
                    Log.i("IOException", String.valueOf(ll.get(0).latitude));
                    Log.i("IOException", String.valueOf(ll.get(0).longitude));
                }

            } catch (IOException e) {
                // handle the exception
                Log.i("IOException",e.getLocalizedMessage());
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}