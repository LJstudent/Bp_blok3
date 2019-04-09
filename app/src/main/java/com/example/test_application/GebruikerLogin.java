package com.example.test_application;

import android.os.StrictMode;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

// class waar alles wordt gecheckt
public class GebruikerLogin {
    private final String JSONURL = "http://192.168.178.116:9090/ords/hr/gebruiker/";
    JSONObject jo = new JSONObject();
    ArrayList<Gebruiker> list = new ArrayList<>();


    public GebruikerLogin() {
        // working json url
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        JSON();
    }

    public void voegtoe( Gebruiker gebruiker ) {
        list.add( gebruiker );
    }

    public boolean checkUserPass(String username, String password) {
        Gebruiker gebruiker = zoekUserPass( username, password);
        if( gebruiker != null )
            return true;
        else
            return false;
    }

    public Gebruiker zoekUserPass( String username, String password ) {
        for( Gebruiker gebruiker : list ) {
            if( username.equals( gebruiker.getUsername())&& password.equals( gebruiker.getPasswoord()))
                return gebruiker;
        }
        return null;
    }

    public boolean checkUser(String username) {
        Gebruiker gebruiker = zoekUser(username);
        if( gebruiker != null )
            return true;
        else
            return false;
    }

    public Gebruiker zoekUser( String username) {
        for( Gebruiker gebruiker : list ) {
            if( username.equals( gebruiker.getUsername()))
                return gebruiker;
        }
        return null;
    }
    public boolean checkEmail(String email) {
        Gebruiker gebruiker = zoekEmail(email);
        if( gebruiker != null )
            return true;
        else
            return false;
    }

    public Gebruiker zoekEmail( String email) {
        for( Gebruiker gebruiker : list ) {
            if( email.equals( gebruiker.getEmail()))
                return gebruiker;
        }
        return null;
    }
    public boolean checkRasp(String raspID) {
        Gebruiker gebruiker = zoekID(raspID);
        if( gebruiker != null )
            return true;
        else
            return false;
    }

    public Gebruiker zoekID( String raspID) {
        for( Gebruiker gebruiker : list ) {
            if( raspID.equals( gebruiker.getRasp_id()))
                return gebruiker;
        }
        return null;
    }



    public void JSON() {

        JSONArray ja = new JSONArray();

        try {
            JSONObject result = readUrl(JSONURL);
            JSONArray features = (JSONArray) result.get("items");


            for (Object o : features) {
                JSONObject jsonobject = (JSONObject) o;
                String email = checkValue(jsonobject, "email");
                String username = checkValue(jsonobject, "username");
                String passwoord = checkValue(jsonobject, "password");
                String latitude = checkValue(jsonobject, "latitude");
                String longitude = checkValue(jsonobject, "longitude");
                String rasp_id = checkValue(jsonobject, "id");
                /*
                System.out.println(email);
                System.out.println(username);
                System.out.println(passwoord);
                System.out.println(latitude);
                System.out.println(longitude);
                System.out.println(rasp_id);
                */
                Gebruiker gebruiker = new Gebruiker(email,username,passwoord,latitude,longitude,rasp_id);
                voegtoe(gebruiker);

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
