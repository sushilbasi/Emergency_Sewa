package com.college.emergencysewa;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay2;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

import android.location.LocationManager;
import android.location.Location;

import com.google.android.gms.location.LocationServices;

/**
 * A simple {@link Fragment} subclass.
 */
public class activity_map extends Fragment {

    private MapView map;
    private MyLocationNewOverlay mLocationOverlay = null;
    private CompassOverlay compassOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private Road road;
    GeoPoint startPoint;

    public activity_map() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_map, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        Context ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        // slove the repetation of map
        map.setHorizontalMapRepetitionEnabled(false);
        map.setVerticalMapRepetitionEnabled(false);

        //scale bar
//        mScaleBarOverlay = new ScaleBarOverlay(map);
//        mScaleBarOverlay.setCentred(true);
//        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);


        //We can move the map on default view point. For this, we need access to map controller

//        double latitude = 27.693123;
//        double longitude = 85.321023;
        IMapController mapController = map.getController();
        mapController.setZoom(18);

        startPoint = new GeoPoint(27.683126, 85.348968);
        mapController.setCenter(startPoint);


        //show Compass
        this.compassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        this.compassOverlay.enableCompass();
//        map.getOverlays().add(this.compassOverlay);

        // add the my location overlay
        GpsMyLocationProvider myLocation = new GpsMyLocationProvider(this.getContext());
        myLocation.setLocationUpdateMinTime(2);
        myLocation.setLocationUpdateMinDistance(5);
        myLocation.addLocationSource(LocationManager.GPS_PROVIDER);
        myLocation.addLocationSource(LocationManager.NETWORK_PROVIDER);

        mLocationOverlay = new MyLocationNewOverlay(myLocation, map);
        Log.d(TAG, "THIS IS MAP LOCTATION");
        System.out.println(mLocationOverlay);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        mLocationOverlay.isFollowLocationEnabled();
        mLocationOverlay.isMyLocationEnabled();
        mLocationOverlay.setDrawAccuracyEnabled(true);

        map.getOverlays().add(this.mLocationOverlay);
        map.setClickable(true);
        map.setFocusable(true);
        map.setFocusableInTouchMode(true);
        mapController.setCenter(mLocationOverlay.getMyLocation());
        mapController.animateTo(mLocationOverlay.getMyLocation());
        System.out.println(mLocationOverlay.getMyLocation());

        LocationManager locationManager = (LocationManager)
                getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();


        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));

        double lati = location.getLatitude();
        double longi = location.getLongitude();

        System.out.println(lati + ","+longi);


        addMarker2(ctx,map,startPoint);
        //Display Route between two points
        RoadManager roadManager = new OSRMRoadManager(this.getContext());
        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
        waypoints.add(startPoint);
        GeoPoint endPoint = new GeoPoint(lati, longi);
        waypoints.add(endPoint);
        final double xMeters = ((GeoPoint) startPoint).distanceToAsDouble(endPoint);
        double distance = startPoint.distanceToAsDouble(endPoint);
        System.out.println(distance);
        Road road = null;
        try {
            road = roadManager.getRoad(waypoints);
            Toast.makeText(getActivity(), "You are here", Toast.LENGTH_SHORT).show();
        }catch (Exception e )
        {
            e.printStackTrace();
        }
        if(road.mStatus != Road.STATUS_OK) {
            Toast.makeText(getActivity(), "Error when loading the road - status="+road.mStatus, Toast.LENGTH_SHORT).show();
        }
        double d = road.mDuration; // get duration
        double x = road.mLength; // get length
        String st_timeLeft = String.format("%s min", String.valueOf(myRound(d / 60,3)));//format string
        String st_dist = String.valueOf(myRound(x * 1000,2)) + " m";
        System.out.println(st_timeLeft +","+st_dist);
//        timeLeft.setText(st_timeLeft);
//        distGoal.setText(st_dist);
        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
        roadOverlay.setColor(getContext().getResources().getColor(R.color.colorAccent));
        roadOverlay.setWidth(16);
        roadOverlay.setGeodesic(false);
        map.getOverlays().add(roadOverlay);
        map.invalidate();

//        myLocation(ctx,map,latitude,longitude);

//        addMarker(ctx,map,startPoint);




        return view;
    }

    static double myRound(double wert, int stellen) {
        return  Math.round(wert * Math.pow(10, stellen)) / Math.pow(10, stellen);
    }


    public static Marker addMarker(Context context, MapView map, GeoPoint position) {
        Marker marker = new Marker(map);
        marker.setPosition(position);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(context.getResources().getDrawable(R.drawable.ic_user));
        marker.setTitle("Latitude: " + position.getLatitude() + "\n" + "Longitude: " + position.getLongitude());
        marker.setPanToView(true);
        marker.setDraggable(true);
        map.getOverlays().add(marker);
        map.invalidate();
        return marker;
    }

    public static Marker addMarker2(Context context, MapView map, GeoPoint position) {
        Marker marker = new Marker(map);
        marker.setPosition(position);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(context.getResources().getDrawable(R.drawable.ic_officer));
        marker.setTitle("Latitude: " + position.getLatitude() + "\n" + "Longitude: " + position.getLongitude());
        marker.setPanToView(true);
        marker.setDraggable(true);
        map.getOverlays().add(marker);
        map.invalidate();
        return marker;
    }

//    public static Marker myLocation(Context context, MapView map, double latitude, double longitude) {
//        if (map == null || context == null) return null;
//        Marker marker = new Marker(map);
//        marker.setPosition(new GeoPoint(latitude, longitude));
//        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
//        marker.setIcon(context.getResources().getDrawable(R.drawable.ic_placeholder));
//        marker.setInfoWindow(null);
//        map.getOverlays().add(marker);
//        map.invalidate();
//        return marker;
//    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationOverlay.disableMyLocation();
        mLocationOverlay.disableFollowLocation();
        map.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableFollowLocation();
        map.onResume();

    }

}