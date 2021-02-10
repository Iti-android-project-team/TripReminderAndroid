package com.example.tripreminder;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.example.tripreminder.data.model.db.Note;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapsFragment extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String TAG = "MapsFragment";

    private List<Double> latitudeStartPointList = new ArrayList<>();
    private List<Double> longitudeStartPointList = new ArrayList<>();
    private List<Double> latitudeEndPointList = new ArrayList<>();
    private List<Double> longitudeEndPointList = new ArrayList<>();

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
        String latitudeString = getIntent().getStringExtra("LATITUDE_START-POINT");
        String longitudeString = getIntent().getStringExtra("LONGITUDE_START_POINT");
        String latitudeEndPointString = getIntent().getStringExtra("LATITUDE_END_POINT");
        String longitudeEndPointString = getIntent().getStringExtra("LONGITUDE_END_POINT");
        Type collectionType = new TypeToken<List<Double>>() {
        }.getType();
        latitudeStartPointList = new Gson()
                .fromJson(latitudeString, collectionType);

        longitudeStartPointList = new Gson()
                .fromJson(longitudeString, collectionType);

        latitudeEndPointList = new Gson()
                .fromJson(latitudeEndPointString, collectionType);

        longitudeEndPointList = new Gson()
                .fromJson(longitudeEndPointString, collectionType);
        Log.i(TAG, latitudeEndPointString);
        Log.i(TAG, latitudeString);
        Log.i(TAG, longitudeEndPointString);
        Log.i(TAG, longitudeString);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        for (int i = 0; i < longitudeStartPointList.size(); i++) {
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
            PolylineOptions opts = new PolylineOptions().addAll(path).color(getRandomColor()).width(12);
            mMap.addPolyline(opts);
        }
    }

    public int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}