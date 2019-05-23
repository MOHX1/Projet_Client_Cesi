package com.example.mybeautybooking.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mybeautybooking.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Detail_Pro extends AppCompatActivity {
    TextView txt1;
    TextView txt2;
    TextView txt3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__pro);
        Intent i = getIntent();
        ArrayList<Professionnel> list = (ArrayList<Professionnel>) i
                .getSerializableExtra("P_Clicked");
        TextView txt1 = (TextView) findViewById(R.id.detail1);
        TextView txt2  = (TextView) findViewById(R.id.detail2);
        final TextView txt3 = (TextView) findViewById(R.id.detail3);
        txt1.setText(list.get(0).getNom());
        txt2.setText(list.get(0).getVille());
        txt3.setText(list.get(0).getNum_tel());
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+ txt3.getText()));
                startActivity(callIntent);
            }
        });
    }

}
