package com.miranda.javier.miclima;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.miranda.javier.miclima.ws.ClimaWS;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity  {
    private FusedLocationProviderClient mFusedLocationClient;
    TextView txtPais,txtTemperatura,txtHumedad,txtRain,txtSumary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtPais = (TextView) findViewById(R.id.txtPais);
        txtTemperatura = (TextView) findViewById(R.id.txtTemperatura);
        txtHumedad = (TextView) findViewById(R.id.txtHumedad);
        txtRain = (TextView) findViewById(R.id.txtRain);
        txtSumary = (TextView) findViewById(R.id.txtSumary);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            checkPermission();
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        llamarWs();
    }


    @SuppressLint("MissingPermission")
    public void onRefreshLocation(View v){
       llamarWs();



    }

    @SuppressLint("MissingPermission")
    public void llamarWs(){

        mFusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                if (location != null) {
                    new Clima().execute(location.getLatitude(),location.getLongitude());
                } else {
                    Toast.makeText(MainActivity.this, "No se encontro registro de clima", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    public class Clima extends AsyncTask<Double,Void,String>{

        @Override
        protected String doInBackground(Double... parameters) {
            String resultado = new ClimaWS().getClima(parameters[0],parameters[1]);
            return resultado;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject json = null;
            try {
                json = new JSONObject(s);
                txtPais.setText(json.getString("timezone"));
                JSONObject currently = json.getJSONObject("currently");
                txtSumary.setText(currently.getString("summary"));
                txtHumedad.setText((currently.getDouble("humidity")  * 100 )+ "%"  );
                txtTemperatura.setText(""+currently.getDouble("temperature") + "º");
                txtRain.setText(currently.getString("icon").equals("rain") ? "100%": "0%");
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Ups, Esto no suele pasar: " + s, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void checkPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ){//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }
}
