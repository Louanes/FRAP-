package fr.android.ppe.frap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import com.google.android.gms.location.LocationCallback;
import android.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import fr.android.ppe.frap.direction.FetchUrl;
import fr.android.ppe.frap.direction.TaskLoadedCallback;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, TaskLoadedCallback {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private double myLatitude;
    private double myLongitude;
    private double latitude;
    private double longitude;
    private String interventionPlace="";
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean mRequestingLocationUpdates;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private ImageButton imageButton;
    LatLng place;
    private Marker marker;
    private LatLng myPlace;
    private Polyline currentPolyline;
    private LatLng location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getLocationPermission();
        imageButton=findViewById(R.id.button);

        //Add back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase=FirebaseDatabase.getInstance();
        myRef=mDatabase.getReference().child("Hydrant");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,20));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_type, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //finish();
                onBackPressed();
                return true;
            // Change the map type based on the user's selection.
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.hybrid_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        //Execute your code here
        finish();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("La map est prête");
        mMap = googleMap;

        //Enable current location
        mMap.setMyLocationEnabled(true);
        /*LatLng CurrentPlace = new LatLng(lattitude, longitude);
        mMap.addMarker(new MarkerOptions().position(CurrentPlace));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CurrentPlace, 30));*/

        //Get data from intent
        Intent intent= getIntent();
        interventionPlace=intent.getStringExtra("location_name");
        if (intent.hasExtra("latitude")&&intent.hasExtra("longitude")){
            latitude=intent.getDoubleExtra("latitude",0.0);
            longitude=intent.getDoubleExtra("longitude",0.0);
            System.out.println("latitude: "+latitude);
            System.out.println("longitude: "+longitude);
        }

        //Save origin and destination
        place = new LatLng(latitude, longitude);
        myPlace=new LatLng(myLatitude,myLongitude);

        mMap.addMarker(new MarkerOptions().position(myPlace).title("My place"));
        mMap.addMarker(new MarkerOptions().position(place).title(interventionPlace));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,15));

        readFromDatabase();
        getDataHydrant();
        float results[]=new float[10];
        Location.distanceBetween(latitude,longitude,myLatitude,myLongitude,results);
        System.out.println("Distance:" +results[0]);

        String url=getUrl(myPlace,place,"driving");
        new FetchUrl(MapsActivity.this).execute(getUrl(myPlace, place, "driving"), "driving");
    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5,this);
        onLocationChanged(location);

        System.out.println("Je suis dans le Resume");
        System.out.println("Longitude/Lattitude: " + myLongitude + "/" + myLatitude);
    }

    @Override
    protected void onPause() {
        System.out.println("Je suis dans le Onpause");
        super.onPause();
    }

    private void getLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },MY_PERMISSIONS_REQUEST_LOCATION);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        }
        if(locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,10000,0,this);
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0,this);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSIONS_REQUEST_LOCATION){
            getLocationPermission();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            // Getting latitude of the current location
            myLatitude = location.getLatitude();
            // Getting longitude of the current location
            myLongitude = location.getLongitude();
        }
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
    private void readFromDatabase(){
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (final DataSnapshot coordinate : dataSnapshot.getChildren()) {
                    double lat=(double) coordinate.child("Lieu_lat").getValue();
                    double lng=(double) coordinate.child("Lieu_lng").getValue();
                    String dispo=(String)coordinate.child("Disponibilite_name").getValue();
                    String type_hydrant= (String) coordinate.child("TypeHydrant_name").getValue();
                    location = new LatLng(lat,lng);
                    if(type_hydrant.equals("PI")){
                        /*mMap.addMarker(new MarkerOptions().position(location).title(dispo))
                                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pi));*/
                        mMap.addMarker(new MarkerOptions().position(location).title(dispo))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    }
                    else{
                        mMap.addMarker(new MarkerOptions().position(location).title(dispo))
                                .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void getDataHydrant(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if((!marker.getTitle().equals("My place"))&&(!marker.getTitle().equals(interventionPlace))){
                    myRef.orderByChild("Lieu_lat").equalTo(marker.getPosition().latitude).addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Iterator<DataSnapshot> items = dataSnapshot.getChildren().iterator();
                                    while (items.hasNext()) {
                                        DataSnapshot item = items.next();
                                        String numero_hydrant=item.child("NumeroHydrant").getValue().toString();
                                        //numero_hydrant= (Long) coordinate.child("NumeroHydrant").getValue();
                                        Intent intent=new Intent(MapsActivity.this,DataHydrant.class);
                                        intent.putExtra("numero_hydrant",numero_hydrant);
                                        //intent.putExtra("latitude",lati);
                                        //intent.putExtra("longitude",longi);
                                        startActivity(intent);
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.w(TAG, "Failed to read value.", databaseError.toException());

                                }
                            }
                    );
                }
                return false;
            }
        });
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyB_ICx5Kmob0uwXqCQ2Zj93PAOikD_uglQ";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


}
