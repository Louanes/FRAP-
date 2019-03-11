package fr.android.ppe.frap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private double myLatitude;
    private double myLongitude;
    private double latitude;
    private double longitude;
    private String interventionPlace="";
    private Location location;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean mRequestingLocationUpdates;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;


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
        LatLng place = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(place).title(interventionPlace.toString()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place,15));

        readFromDatabase();
    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mDatabase=FirebaseDatabase.getInstance();
        myRef=mDatabase.getReference().child("Hydrant");

        getLocationPermission();
        createLocationRequest();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                System.out.println("Je suis dans le onLocationResult");
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    onLocationChanged(location);
                    System.out.println("Longitude/Lattitude: " + myLongitude + "/" + myLatitude);

                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            },LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        System.out.println("Je suis dans le Resume");

        onLocationChanged(location);
        System.out.println("Longitude/Lattitude: " + myLongitude + "/" + myLatitude);
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                null /* Looper */);
    }

    @Override
    protected void onPause() {
        System.out.println("Je suis dans le Onpause");
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void getLocationPermission() {
        System.out.println("Je suis dans les permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        myLongitude=location.getLongitude();
        myLatitude=location.getLatitude();
    }

    private void readFromDatabase(){
        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot coordinate : dataSnapshot.getChildren()) {
                    double lat= (double) coordinate.child("Lieu_lat").getValue();
                    double lng= (double) coordinate.child("Lieu_lng").getValue();
                    String dispo=(String)coordinate.child("Disponibilite_name").getValue();
                    LatLng location = new LatLng(lat,lng);
                    mMap.addMarker(new MarkerOptions().position(location).title(dispo))
                            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hydrant));
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

}
