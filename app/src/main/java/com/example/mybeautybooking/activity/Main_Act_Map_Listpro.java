package com.example.mybeautybooking.activity;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mybeautybooking.R;

import java.util.ArrayList;

public class Main_Act_Map_Listpro extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__act__map__listpro);
        Intent i = getIntent();
        ArrayList<Professionnel> list = (ArrayList<Professionnel>) i
                .getSerializableExtra("list");
        MapFragment mapF = new MapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        android.app.FragmentManager fragmentManager1 = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction1= fragmentManager1.beginTransaction();
        Bundle b=new Bundle();
        b.putSerializable("lol",list);
        mapF.setArguments(b);
        fragmentTransaction.add(R.id.fragment,mapF);
        fragmentTransaction.commit();
        ListProFragment listProFragment = new ListProFragment();
        listProFragment.setArguments(b);
        fragmentTransaction1.add(R.id.fragment3,listProFragment);
        fragmentTransaction1.commit();
        //fragmentTransaction.add(R.id.placeholder, mapF);
    }

}

