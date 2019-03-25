package com.example.test_application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private UiSettings mUiSettings;
    private GoogleMap mMap;
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private TextView welkomWoord;
    private Button btnDatabase;

    private final String JSONURL = "http://192.168.178.116:9090/ords/hr/kaartview/";
    JSONObject jo = new JSONObject();
    ArrayList<DataRasp> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //retrieve string
        Bundle LoginUser = getIntent().getExtras();
        String username = LoginUser.getString("user");

        // get ID textView
        welkomWoord = findViewById(R.id.WelkomWoord);
        welkomWoord.setText("Welkom " + username);

        //get to database
        btnDatabase = findViewById(R.id.btnDatabase);

        // working json url
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        JSON();


        btnDatabase.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), NavigationActivity.class);
                startActivity(i);
            }

        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Den bosch, Netherlands.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        for( DataRasp data : list ) {
            // convert to double
            double latitude = Double.parseDouble(data.getLatitude());
            double longitude = Double.parseDouble(data.getLongitude());
            options.position(new LatLng(latitude, longitude));
            options.title(data.getDatum());
            options.snippet("instraling " + data.getInstraling() + " W/m²" + "\n" + "temp " + data.getTemp() +"°");
            googleMap.addMarker(options);

                googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                    @Override
                    public View getInfoWindow(Marker marker) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {

                        Context context = getApplicationContext();

                        LinearLayout info = new LinearLayout(context);
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(context);
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(context);
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);
                        return info;
                    }
                });

            }

        // central camera point from starting or user(preference)
        LatLng amsterdam = new LatLng(52.370216, 4.895168);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(amsterdam));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(amsterdam, 7));


        /*
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(51.688278, 5.289100);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Den bosch"));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */

    }
    public void JSON() {

        JSONArray ja = new JSONArray();

        try {
            JSONObject result = readUrl(JSONURL);
            JSONArray features = (JSONArray) result.get("items");


            for (Object o : features) {
                JSONObject jsonobject = (JSONObject) o;
                String datum = checkValue(jsonobject, "datum");
                String temp = checkValue(jsonobject, "temp");
                String instraling = checkValue(jsonobject, "instraling");
                String latitude = checkValue(jsonobject, "latitude");
                String longitude = checkValue(jsonobject, "longitude");

                /*
                System.out.println(email);
                System.out.println(username);
                System.out.println(passwoord);
                System.out.println(latitude);
                System.out.println(longitude);
                System.out.println(rasp_id);
                */
                DataRasp data = new DataRasp(datum,temp,instraling,latitude,longitude);
                list.add(data);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    private String checkValue(JSONObject jo, String attribute) {
        String waarde = "";
        try {
            if (jo.get(attribute).toString() != null) {
                waarde = jo.get(attribute).toString();
            }
        } catch (Exception e) {
        }
        return waarde;
    }

    private JSONObject readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        String data = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/xml");
            BufferedReader br
                    = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            data = br.readLine();
            connection.disconnect();
            jo = (JSONObject) JSONValue.parseWithException(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return jo;
    }
}
