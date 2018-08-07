package com.smove.smovebot;

/**
 * Created by zahir on 6/8/2018.
 */

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class maps extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;String fulladdressontrip,fulladdressnotontrip;
    Double latitude_ontrip,longitude_ontrip,latitude_notontrip,longitude_notontrip;
    Geocoder geocoder = new Geocoder(maps.this, Locale.getDefault());
    ArrayList lat_ontrip,long_ontrip,lat_notontrip,long_notontrip;Bundle bundleobject1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

         bundleobject1=getIntent().getExtras();


         lat_ontrip=(ArrayList)bundleobject1.getSerializable("key1");
         long_ontrip=(ArrayList)bundleobject1.getSerializable("key2");
         lat_notontrip=(ArrayList)bundleobject1.getSerializable("key3");
         long_notontrip=(ArrayList)bundleobject1.getSerializable("key4");//getting variable from mainactivity











    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i=0;i<lat_ontrip.size();i++)
        {


            latitude_ontrip=Double.valueOf(String.valueOf(lat_ontrip.get(i)));
            longitude_ontrip=Double.valueOf(String.valueOf(long_ontrip.get(i)));
            LatLng car_location_ontrip = new LatLng(latitude_ontrip, longitude_ontrip);

            try {

                List<Address> addressontrip = geocoder.getFromLocation(latitude_ontrip, longitude_ontrip, 1);
                 fulladdressontrip= addressontrip.get(0).getAddressLine(0);



            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(maps.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        mMap.addMarker(new MarkerOptions().position(car_location_ontrip).title("Cars on trip").snippet(fulladdressontrip));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(car_location_ontrip));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(15f));


        }


        for (int k=0;k<lat_notontrip.size();k++)
        {latitude_notontrip=Double.valueOf(String.valueOf(lat_notontrip.get(k)));
            longitude_notontrip=Double.valueOf(String.valueOf(long_notontrip.get(k)));
            LatLng car_location_notontrip = new LatLng(latitude_notontrip, longitude_notontrip);

            try {

                List<Address> addressnotontrip = geocoder.getFromLocation(latitude_notontrip, longitude_notontrip, 1);
                fulladdressnotontrip= addressnotontrip.get(0).getAddressLine(0);



            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(maps.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
            mMap.addMarker(new MarkerOptions().position(car_location_notontrip).title("Cars at rest").
                    icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).snippet(fulladdressnotontrip));;

            mMap.moveCamera(CameraUpdateFactory.newLatLng(car_location_notontrip));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(9f));}




    }

}
