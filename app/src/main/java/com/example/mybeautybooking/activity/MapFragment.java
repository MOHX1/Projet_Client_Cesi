package com.example.mybeautybooking.activity;


import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybeautybooking.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    SupportMapFragment mapFragment;

    String location = "Paris" ;
    private String adress;
    private  ArrayList<Professionnel> Ap=new ArrayList<>();
    static ArrayList<Professionnel> listPro = null;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View v =  inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment= (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);

        if (mapFragment == null){
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment= SupportMapFragment.newInstance();
            ft.replace(R.id.map,mapFragment).commit();
        }
        Bundle b = getArguments();

        Ap= (ArrayList<Professionnel>)b.getSerializable("lol");
        System.out.print(Ap.get(0).getNom());

            mapFragment.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Location> locationList=null;
        List<Address> addressList = null;
        Address address = null;
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //Toast.makeText(getContext(), "Veuillez choisir une adresse", Toast.LENGTH_SHORT).show();
                LatLng s = marker.getPosition();
                listPro=new ArrayList<>();
                Location LocAP;
                for (Professionnel p:Ap){
                  LocAP= adresseToLocation(stringToAdresse(p.getVille()));
                  if(LocAP.getLatitude()==s.latitude && LocAP.getLongitude()==s.longitude){
                      listPro.add((Professionnel) (p)) ;
                      break;
                  }
            }
                Intent intent = new Intent(getContext(),Detail_Pro.class);
                intent.putExtra("P_Clicked" , listPro);
                getContext().getApplicationContext().startActivity(intent);
                return false;
            }
        });
        for (Professionnel p:Ap
             ){
            addressList = null;
            if (p.getVille() != null || !p.getVille().equals("")) {
                Geocoder geocoder = new Geocoder(this.getContext());
                try {
                    addressList = geocoder.getFromLocationName(p.getVille(), 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());// la postion pour la camera
                mMap.addMarker(new MarkerOptions().position(latLng).title(p.getNom()));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10),200,null); // camera sur la position repère
            }
        }
    }
    public Location adresseToLocation(Address address){
        Location locationA = new Location("");
        locationA.setLatitude(address.getLatitude());
        locationA.setLongitude(address.getLongitude());
        return locationA;
    }

    public Address stringToAdresse(String s ){
        Address address = null;

        List<Address> addressList = null;

        Geocoder geocoder = new Geocoder(this.getContext());
        try {
            addressList = geocoder.getFromLocationName(s, 1);
            address=addressList.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    }


