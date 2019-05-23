package com.example.mybeautybooking.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mybeautybooking.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Formulaire extends AppCompatActivity {

    //////////////////////
    private FusedLocationProviderClient client;
    static private Location ma_pos = null ;
    //////////////////////////////////
    private RequestQueue requestQueue;
    private static final String URL = "http://cc82b90f.ngrok.io/ProjetClient/select_vente.php";
    private StringRequest request;
    Professionnel pro;
    static ArrayList<Professionnel> listPro = null;
    static ArrayList<Professionnel> listPro1 =null;
    static ArrayList<Professionnel> listPro2 =null;
    static int rtn;
    private String s ;
    EditText  location_tf;
    TextView text;
    SeekBar seekBarKm;
    Button chercher;
    static int progress = 10;
    private String location;
    private GoogleMap map;
    // private ParseContent parseContent;

    //private final int LoginTask = 1;

    static private CheckBox cb1;
    static private CheckBox cb2;
    static private CheckBox cb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulaire);
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);
        location_tf = (EditText) findViewById(R.id.ET_Adresse);
        seekBarKm = (SeekBar) findViewById(R.id.SeekBarKm);
        seekBarKm.setMax(40);
        seekBarKm.setProgress(progress);
        text = text = (TextView) findViewById(R.id.Peremetre);
        text.setText("" + progress + " Km");
        seekBarKm.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
                text.setText(progress+ " Km");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Button b1 = (Button) findViewById(R.id.button4);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(Formulaire.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(Formulaire.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            location_tf.setText("Ma position actuelle");
                            ma_pos=location;
                        }
                    }
                });
            }
        });
    }
    public void onSearch(View view)
    {
        if (location_tf.getText().toString().equals("") &&!is_Checked_box()) {
            Toast.makeText(this, "Veuillez choisir une adresse et une categorie", Toast.LENGTH_SHORT).show();
        }
        else if (location_tf.getText().toString().equals("") ) {
            Toast.makeText(this, "Veuillez choisir une adresse", Toast.LENGTH_SHORT).show();
        } else if (!is_Checked_box()) {
            Toast.makeText(this,"Veuillez choisir une categorie", Toast.LENGTH_SHORT).show();
        }else{
            chercher = (Button) findViewById(R.id.Chercher);
            location = location_tf.getText().toString();
            getpro();
        }

    }

    public void map_activity(){

        Intent intent = new Intent(this,Main_Act_Map_Listpro.class );
        startActivity(intent);
    }
    public void getpro(){
        listPro=new ArrayList<>();
        listPro1=new ArrayList<>();
        listPro2=new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // create json object with php reponse
                    JSONArray jsonArray = new JSONArray(response);
                    for ( int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json = null;
                        Professionnel pro = new Professionnel();
                        try {
                            json = jsonArray.getJSONObject(i);
                            pro.setNom(json.getString("Nom"));
                            pro.setVille(json.getString("Ville"));
                            pro.setSpecialite(json.getString("Specialite"));
                            pro.setNum_tel(json.getString("Telephone"));
                            // promo.setancien_prix(json.getString("ancien_prix"));
                            // promo.setnouveau_prix(json.getString("nouveau_prix"));
                            //promo.setimage(json.getString("image"));
                            // promo.setTexte(json.getString("texte"));

                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                        listPro.add(pro);
                    }
                    rtn=trie();
                    if (rtn!=0) {
                        Intent intent = new Intent(getApplicationContext(), Main_Act_Map_Listpro.class);
                        intent.putExtra("list", listPro2);
                        startActivity(intent);
                    }else{
                        showAlertDialogButtonClicked(getCurrentFocus());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) { Toast.makeText(getApplicationContext(), "TimeoutError", Toast.LENGTH_LONG).show();}

                if ( error instanceof NoConnectionError) { Toast.makeText(getApplicationContext(), "NoConnectionError" ,Toast.LENGTH_LONG).show();}

                if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "AuthFailureError" ,Toast.LENGTH_LONG).show();}
                else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "ServerError", Toast.LENGTH_LONG).show(); }

                else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_LONG).show(); }

                else if  (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "ParseError" ,Toast.LENGTH_LONG).show();}

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> hashMap = new HashMap<String, String>();

                return hashMap;
            }
        };

        requestQueue.add(request);

    }

    public int trie () {
        int rtn =1;
        List<Location> locationList = null;
        List<Address> addressList = null;
        Address address = null;
        Location lUser = null;
        List<String> listCat;

        listCat = list_categorie_cochée();
        boolean exist = false;

        if (!location.equals("") && !location.equals("Ma position actuelle")) {
            Address auser = stringToAdresse(location);
            lUser = adresseToLocation(auser);
        } else {

            lUser = ma_pos;
        }
        Location loc = null;

        String f;

        for (Professionnel p : listPro) {
            for (String s : listCat) {
                if (s.equals(p.getSpecialite().toString())) {
                    listPro1.add(p);
                }
            }
        }

        listPro2.clear();
        for (Professionnel p : listPro1) {
            addressList = null;
            if (p.getVille() != null || !p.getVille().equals("")) {
                Geocoder geocoder = new Geocoder(this);
                address = stringToAdresse(p.getVille());
                loc = adresseToLocation(address);
                if (lUser.distanceTo(loc) / 1000 < progress) {
                    listPro2.add(p);
                }
            }
        }

        if (listPro2.size() == 0 || listPro1.size()==0) {
            //showAlertDialogButtonClicked(getCurrentFocus());
            rtn = 0;
        }
        return rtn;
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

        Geocoder geocoder = new Geocoder(this);
        try {
            addressList = geocoder.getFromLocationName(s, 1);
            address=addressList.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
    public  List<String> list_categorie_cochée() {

        cb1 = (CheckBox) findViewById(R.id.checkBox2);
        cb2 = (CheckBox) findViewById(R.id.checkBox4);
        cb3= (CheckBox) findViewById(R.id.checkBox5);
        // btnDisplay = (Button) findViewById(R.id.btnDisplay);
        List<String> result = new ArrayList<>();
        if (cb1.isChecked()) {
            result.add((String) cb1.getText());
        }
        if (cb2.isChecked()) {
            result.add((String) cb2.getText());
        }
        if (cb3.isChecked()) {
            result.add((String) cb3.getText());
        }
        return result;
    }

    public boolean is_Checked_box(){
        cb1 = (CheckBox) findViewById(R.id.checkBox2);
        cb2 = (CheckBox) findViewById(R.id.checkBox4);
        cb3= (CheckBox) findViewById(R.id.checkBox5);
        if (cb1.isChecked()||cb2.isChecked()||cb3.isChecked()) {
            return true;
        }else {
            return false;
        }

    }

    public void showAlertDialogButtonClicked(View view) {
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("My title");
        builder.setMessage("Aucun resultat ne correspont à votre recherche, modifier vos paramettre");
        // add a button
        builder.setPositiveButton("OK", null);
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
