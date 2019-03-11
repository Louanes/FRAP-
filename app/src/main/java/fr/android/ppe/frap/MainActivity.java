package fr.android.ppe.frap;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private AutoCompleteTextView autoCompleteTextView;
    private Button button;
    private AutocompleteSupportFragment placeAutocompleteFragment;
    private GoogleApiClient mGoogleApiClient;
    private double lati;
    private double longi;
    LatLng latLng;
    String location_name="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isServicesOK()){
            init();
        }

    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void init(){
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyB_ICx5Kmob0uwXqCQ2Zj93PAOikD_uglQ");
        }
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG,Place.Field.NAME));
        autocompleteFragment.setCountry("FR");
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                longi=place.getLatLng().longitude;
                lati=place.getLatLng().latitude;
                location_name=place.getName();
                System.out.println("latitude: "+lati);
                System.out.println("longitude: "+longi);
                Intent intent=new Intent(MainActivity.this,MapsActivity.class);
                intent.putExtra("location_name",location_name);
                intent.putExtra("latitude",lati);
                intent.putExtra("longitude",longi);
                startActivity(intent);
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getApplicationContext(), "" + status.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

}
