package com.college.emergencysewa;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;

import static android.content.Context.SENSOR_SERVICE;

public class activity_map extends Fragment implements LocationListener {

    private MapView map;
    private MyLocationNewOverlay mLocationOverlay = null;
    private CompassOverlay compassOverlay;
    public Road road;
    public GeoPoint startPoint, endPoint = null;
    double longi;
    double lati;
    public RoadManager roadManager;
    public ArrayList<GeoPoint> waypoints;
    public LocationManager locationManager;
    public String bestProvider;
    public Context ctx;
    public IMapController mapController;


    public activity_map() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_map, container, false);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ctx = getActivity().getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);


        map.setHorizontalMapRepetitionEnabled(false);   // solve the repetition of map
        map.setVerticalMapRepetitionEnabled(false);

        mapController = map.getController();
        mapController.setZoom(10);


        //show Compass
        this.compassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        this.compassOverlay.enableCompass();
        map.getOverlays().add(this.compassOverlay);

        // add the my location overlay
        GpsMyLocationProvider myLocation = new GpsMyLocationProvider(this.getContext());
        myLocation.setLocationUpdateMinTime(2);
        myLocation.setLocationUpdateMinDistance(5);
        myLocation.addLocationSource(LocationManager.GPS_PROVIDER);
        myLocation.addLocationSource(LocationManager.NETWORK_PROVIDER);

        mLocationOverlay = new MyLocationNewOverlay(myLocation, map);
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



        getLocation();

        map.invalidate();

        return view;
    }


    public static boolean isLocationEnabled(Context context)
    {
        return true;
    }

    protected  void getLocation(){
        if(isLocationEnabled(getActivity()))
        {

            locationManager = (LocationManager)
                    getActivity().getSystemService(Context.LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

                    if (ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        System.out.println("PERMISSION GRANTTED");

                    }

            Location location = locationManager.getLastKnownLocation(bestProvider);
            System.out.println(location);
                    if(location != null) {
                        double lati = location.getLatitude();
                        double longi = location.getLongitude();
                        System.out.println(lati + "," + longi);
                        startPoint = new GeoPoint(lati, longi);
                        endPoint = new GeoPoint(27.683126, 85.348968);
                        mapController.setCenter(endPoint);
                        mapController.setZoom(18);

                            if(endPoint !=null) {
                                addMarker2(ctx, map, endPoint);
                                //Display Route between two points
                                roadWay();
                            }else
                            {
                                Toast.makeText(getActivity(), "NO AGENT FOUND", Toast.LENGTH_SHORT).show();
                            }
            }else
                    {
                        Toast.makeText(getActivity(), "Location is NULL", Toast.LENGTH_SHORT).show();
                            locationManager.requestLocationUpdates(bestProvider, 1000, 0, this );
                    }
        }else
        {
            Toast.makeText(getActivity(),"Please enable locaiton", Toast.LENGTH_SHORT).show();
        }
    }

    public void roadWay()
    {

        roadManager = new OSRMRoadManager(this.getContext());
        waypoints = new ArrayList<GeoPoint>();
        waypoints.add(endPoint);
        waypoints.add(startPoint);
        System.out.println(waypoints);

        road = null;
            try {
                road = roadManager.getRoad(waypoints);
                Toast.makeText(getActivity(), "You are here", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

                if (road.mStatus != Road.STATUS_OK) {
                    Toast.makeText(getActivity(), "Error when loading the road - status=" + road.mStatus, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                    roadOverlay.setColor(getContext().getResources().getColor(R.color.colorAccent));
                    roadOverlay.setWidth(16);
                    roadOverlay.setGeodesic(false);
                    map.getOverlays().add(roadOverlay);
                }

        double d = road.mDuration; // get duration
        double x = road.mLength; // get length
        String st_timeLeft = String.format("%s min", String.valueOf(myRound(d / 60, 3)));//format string
        String st_dist = String.valueOf(myRound(x * 1000, 2)) + "m";
        System.out.println(st_timeLeft + "," + st_dist);
//        timeLeft.setText(st_timeLeft);
//        distGoal.setText(st_dist);

        map.invalidate();

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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationOverlay.disableMyLocation();
        mLocationOverlay.disableFollowLocation();
        locationManager.removeUpdates(this);
        map.onPause();

    }

    @Override
    public void onLocationChanged(Location location) {
        //Hey, a non null location! Sweet!

        //remove location callback:
        locationManager.removeUpdates(this);

        //open the map:
        lati = location.getLatitude();
        longi = location.getLongitude();;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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