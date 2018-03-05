package com.example.jti.locationnote.gui;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.jti.locationnote.R;
import com.example.jti.locationnote.datamanagement.HandleFiles;
import com.example.jti.locationnote.datamanagement.TimeManager;
import com.example.jti.locationnote.service.LocationIntentService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static Intent intent;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private ArrayList<String> myStringArray = new ArrayList<String>();
    private HandleFiles handleFiles;
    TextInput dialog = new TextInput();
    private Geocoder geocoder;
    private EditText editText;
    private TextView textView;
    private TimeManager timeManager = new TimeManager();
    private double latitude;
    private double longitude;
    private boolean listChecked =false;
    private Location location;
    private String note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setLocationManager();

        final Button button = (Button) findViewById(R.id.button_dialog);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get new locations to fill list
                showNoticeDialog();
            }
        });

        final Button button2 = (Button) findViewById(R.id.button_list);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get new locations to fill list
                startListActivity();
            }
        });

        try {
            if(handleFiles==null)
                handleFiles= new HandleFiles(myStringArray,this);
            this.myStringArray = handleFiles.getList();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    /*  Start listaActivity to show data of note file.
      */
    public void startListActivity() {
        Intent intent = new Intent(this, TaskListActivity.class);
        //For getting back information from listActivity saved on intent.
        startActivityForResult(intent, 1);
    }

    /*
    Start dialog for adding new note.
     */
    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        geocoder= new Geocoder(this, Locale.getDefault());
        dialog.setgeoCoder(geocoder);
        dialog.setlocation(location);
        dialog.show(getFragmentManager(), "TextInput");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void setMarker(Location location) {

        LatLng myposition = new LatLng(location.getAltitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(myposition).title("My position"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myposition));
    }

    private void setLocationManager() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {


            public void onLocationChanged(Location location) {


                // Called when a new location is found by the network location provider.
                if(! listChecked)
                     makeUseOfNewLocation(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

// Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 000, 0, locationListener);

    }

    private void makeUseOfNewLocation(Location location) {
            this.location =location;
            setMarker(location);
             latitude = location.getLatitude();
             Math math;

             latitude = Math.nextAfter(latitude,0.00);
             Log.d("LATITUDE",String.valueOf(latitude));

             longitude = location.getLongitude();
            // if(Double.toString(latitude)!=null) {
                // intent.putExtra("LATITUDE", String.valueOf(latitude));
                // intent.putExtra("LONGITUDE", String.valueOf(longitude));
              //   intent.putExtra("LOCATION",location)*/;

                     if (compareListlocationToCurrentlocation(location)) {
                         intent.putExtra("NOTE", note);
                         intent.setAction("start");
                         startService(intent);

                     }

    }

        //notification("Testi");


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> myStringArray3 = data.getStringArrayListExtra("Item deleted");
                if (myStringArray3.size() != myStringArray.size() || myStringArray3.isEmpty()) {
                    try {
                        handleFiles.saveModifiedList(myStringArray3);
                        intent = new Intent(this, LocationIntentService.class);
                        intent.putExtra("UPDATED", "Updated");


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                 //  startService(intent);

                }
            }

        }
    }


    public void positiveButton(TextInput dialog ) throws IOException {

        editText = dialog.getEditText();
        textView = dialog.getTextView();
        Intent intent;
        //Text1 sisältää muistilistan tietoa.
        String text1 = dialog.getEditText().getText().toString();
        String text2 = dialog.getTextView().getText().toString();
        StringBuilder buffer = new StringBuilder();
        //If note comes without coordinates them added from loaction
        if (text2.equals("")) {
            text2 = Double.valueOf(latitude).toString() + "-" + Double.valueOf(longitude);
        }
        //Replace coordinates comma by .hyphen.
        else {
            String temp;
            temp = text2.replace(",", "-");
            text2 = temp;
        }
        //Appended note text and location same string.
        buffer.append(text1 + "-").append(text2).append("-");
        String text3;
        //and  timestamp added
        text3 = timeManager.getCurrentTimeStamp();
        buffer.append(text3);
        String data = buffer.toString();

        myStringArray=handleFiles.updateoOrCreateFile(data);
        listChecked=false;
    }

    private boolean compareListlocationToCurrentlocation(Location location) {
        listChecked =true;
        if(myStringArray.isEmpty())
            return false;
        Log.d("LOCATION", "New location");
        float distance;
        boolean alarmDistance = false;
        Location locationfromList;
        locationfromList = new Location(location);
        double latitude;
        double longitude;
        String[] latitudeAndlongitude;
        boolean time = false;
        String data2 = null;
        for (String data : myStringArray) {
            data2 = data;
            timeManager.compareTime(data);
            // if (time) {
                latitudeAndlongitude=timeManager.getLatitudeAndLongitude();
                latitude = Double.valueOf(latitudeAndlongitude[1]);
                longitude=Double.valueOf(latitudeAndlongitude[2]);
                locationfromList.setLatitude(latitude);
                locationfromList.setLongitude(longitude);
                distance = location.distanceTo(locationfromList);
                if (distance < 1000) {
                    note = latitudeAndlongitude[0];
                    return true;
                }
            //}
        }

        return false;

    }
}
