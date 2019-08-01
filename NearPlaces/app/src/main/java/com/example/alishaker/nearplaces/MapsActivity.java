package com.example.alishaker.nearplaces;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code =99;
    private double latitude,longtude;
    private int proximityRadius = 5000;
   // private String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkUserLocationPermition();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void onClick (View v)
    {
        String Mechanic="Mechanic",wheels_repair="wheels repair";
        Object transferData[]= new Object[2];
        GetNearbyPlaces getNearbyPlaces=new GetNearbyPlaces();


        switch (v.getId())
        {

            case R.id.search_address:
                EditText addressField =(EditText)findViewById(R.id.location_search);
                String address =addressField.getText().toString();

                List<Address> addressList=null;
                MarkerOptions userMarkerOptions=new MarkerOptions();

                if(!TextUtils.isEmpty(address))
                {
                    Geocoder geocoder =new Geocoder(this);

                    try
                    {
                        addressList =geocoder.getFromLocationName(address,6);

                        if(addressList != null)
                        {
                            for(int i=0;i<addressList.size();i++)
                            {
                                Address userAddress=addressList.get(i);
                                LatLng latLng =new LatLng(userAddress.getLatitude(),userAddress.getLongitude());

                                userMarkerOptions.position(latLng);
                                userMarkerOptions.title(address);
                                userMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));

                            }
                        }
                        else
                        {
                            Toast.makeText(this,"Location Not Found...",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(this,"Please Write any location Name...",Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.mechanic_nearby:
                try {
                    mMap.clear();
                    String url1 = getUrl(latitude, longtude, Mechanic);
                    transferData[0] = mMap;
                    transferData[1] = url1;
                    Toast.makeText(this,url1 , Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Searching for Nearby Mechanics...", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Showing Nearby Mechanics...", Toast.LENGTH_SHORT).show();


                getNearbyPlaces.execute(transferData);}
                catch (Exception e)
                {
                    Toast.makeText(this, "Ay 7aga", Toast.LENGTH_SHORT).show();
                }
                break;

            /*case R.id.wheels_nearby:
                try {
                    mMap.clear();
                    String url2 = getUrl(latitude, longtude, wheels_repair);
                    transferData[0] = mMap;
                    transferData[1] = url2;

                    getNearbyPlaces.execute(transferData);
                    Toast.makeText(this, "Searching for Nearby Wheels shops...", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "Showing Nearby Wheels shops...", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(this, "Ay 7aga", Toast.LENGTH_SHORT).show();
                }
                break;*/
        }
    }

    private String getUrl(double latitude,double longtude,String nearbyPlace)
    {
        StringBuilder googleURL =new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location=" + latitude +","+ longtude);
        googleURL.append("&radius=" +proximityRadius);
        googleURL.append("&types=" + nearbyPlace);
        googleURL.append("&sensor=true" );
        googleURL.append("&key=" + "AIzaSyCoaTKcRXrjNMyIloa6NgdmYrZpZyHp3Xw");

        Log.d("MapsActivity","url = " + googleURL.toString());
        return googleURL.toString();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap)

    {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            buildGoogleAPIclient();
            mMap.setMyLocationEnabled(true);
        }

    }

    public boolean checkUserLocationPermition()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
          if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
          {
              ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);

          }
          else
          {
              ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_User_Location_Code);
          }
          return false;

        }
        else
        {
            return true;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case Request_User_Location_Code:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient == null)
                        {
                            buildGoogleAPIclient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this,"permition Denied",Toast.LENGTH_LONG).show();
                }
                return;

        }
    }

    protected synchronized void buildGoogleAPIclient()
    {

        googleApiClient =new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        latitude=location.getLatitude();
        longtude=location.getLongitude();

        lastLocation =location;

        if(currentUserLocationMarker != null)
        {
            currentUserLocationMarker.remove();
        }
        LatLng latLng =new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        currentUserLocationMarker= mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if(googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }



    @Override
    public void onConnected(@Nullable Bundle bundle)
    {

        locationRequest =new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(locationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
