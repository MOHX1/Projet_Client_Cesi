package com.example.mybeautybooking.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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
import com.example.mybeautybooking.ClientProfilActivity;
import com.example.mybeautybooking.HomePage;
import com.example.mybeautybooking.R;
import com.example.mybeautybooking.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {

    ProgressBar progressBar;
    ProgressDialog progressDialog2;
    Button deleteButton,aboutUsButton, privacyButton;

    //RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
    ClientProfilActivity clientProfilActivity;

    private RequestQueue requestQueue;
    private static final String URL = "http://192.168.43.242/Test-Projet/delete_test.php";
    private StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        clientProfilActivity = new ClientProfilActivity();

        deleteButton = (Button)findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //confirmDelete();
                confirmDelete();
            }
        });

        aboutUsButton= (Button)findViewById(R.id.btn_about_us);
        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecte (1);

            }
        });


        privacyButton= (Button)findViewById(R.id.btn_privacy_policy);
        privacyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirecte (2);


            }
        });


    }

    void redirecte (Integer var){

        if (var==1){

            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        }else{

            Intent intent = new Intent(this, PrivacyPolicyActivity.class);
            startActivity(intent);
        }


    }

    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SettingsActivity.this);
        alertDialogBuilder.setMessage("Êtes-vous sûr de vouloir supprimer votre compte?");
        alertDialogBuilder.setPositiveButton("Supprimer",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //If the user confirms deletion, execute DeleteMovieAsyncTask
                        delete();

                    }
                });

        alertDialogBuilder.setNegativeButton("Annuler", null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }





    public void delete () {

        progressDialog2 = new ProgressDialog(SettingsActivity.this);
        progressDialog2.setMessage(" Suppression en cours. Veuillez patienter...");
        progressDialog2.setIndeterminate(false);
        progressDialog2.setCancelable(false);
        progressDialog2.show();

        request = new StringRequest(Request.Method.POST,URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    // create json object with php reponse
                    JSONObject jsonObject = new JSONObject(response);

                    /*verification jsonphp reponse*/
                    if (jsonObject.names().get(0).equals("success")) {

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        // onLoginSuccess
                                        destroy();
                                        finish();
//                                        progressBar.setVisibility(View.INVISIBLE);
                                        //progressDialog2.dismiss();
                                    }
                                }, 100);
                    }else {destroy();}

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError) {
                    Toast.makeText(getApplicationContext(), "TimeoutError", Toast.LENGTH_LONG).show();
                }

                if (error instanceof NoConnectionError) {
                    Toast.makeText(getApplicationContext(), "NoConnectionError", Toast.LENGTH_LONG).show();
                }

                if (error instanceof AuthFailureError) {
                    Toast.makeText(getApplicationContext(), "AuthFailureError", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getApplicationContext(), "ServerError", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getApplicationContext(), "NetworkError", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getApplicationContext(), "ParseError", Toast.LENGTH_LONG).show();
                }

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", SharedPrefManager.getInstance(getApplicationContext()).getKeyUserID().toString());

                return hashMap;
            }
        };

        requestQueue.add(request);


    }

    void destroy(){
        if ( progressDialog2!=null && progressDialog2.isShowing() ){
            progressDialog2.cancel();
        }
    }
}
