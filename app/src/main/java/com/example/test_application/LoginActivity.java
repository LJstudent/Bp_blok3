package com.example.test_application;

import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputRaspId;
    private TextInputLayout textInputUsernameLogin;
    private TextInputLayout textInputPasswordLogin;
    private TextView        textMelding;
    private Switch          keuzeDelen;
    private Button          btnbevestigen, btnbevestigenLogin;

    private final String JSONURL = "http://192.168.178.116:9090/ords/hr/gebruiker/";
    JSONObject jo = new JSONObject();
    ArrayList<Gebruiker> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        // working json url
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        JSON();

        //Login
        textInputUsernameLogin = findViewById(R.id.txtUsernameLogin);
        textInputPasswordLogin = findViewById(R.id.txtPasswordLogin);
        textMelding = findViewById(R.id.melding);

        btnbevestigenLogin = findViewById(R.id.btnLogin);

        // Registreren
        textInputUsername = findViewById(R.id.txtUsername);
        textInputPassword = findViewById(R.id.txtPassword);
        textInputEmail = findViewById(R.id.txtEmail);
        textInputRaspId = findViewById(R.id.txtIdRasp);
        keuzeDelen = findViewById(R.id.keuze_delen);

        btnbevestigen = findViewById(R.id.btnBevestigen);

        btnbevestigen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validateUsername() && validatePassword() && validateEmail() && validateRaspId()) {
                    String usernameInput = textInputUsername.getEditText().getText().toString().trim();

                    DialogFragment dialog = new AccountMade();
                    Bundle bundle = new Bundle();
                    bundle.putString("header",usernameInput);
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");
                    textInputUsername.getEditText().setText(null);
                    textInputPassword.getEditText().setText(null);
                    textInputEmail.getEditText().setText(null);
                    textInputRaspId.getEditText().setText(null);
                }
            }

        });

        btnbevestigenLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Text melding reset
                textMelding.setText("");
                // Get username for the rest of the program
                String username = textInputUsernameLogin.getEditText().getText().toString().trim();
                if (validateLogin()) {


                    Intent i = new Intent(v.getContext(), MapsActivity.class);
                    i.putExtra("user", username);
                    startActivity(i);

                }
            }

        });


    }

    private boolean validateLogin() {
        String usernameInputLogin = textInputUsernameLogin.getEditText().getText().toString().trim();
        String passwordInputLogin = textInputPasswordLogin.getEditText().getText().toString().trim();

        boolean CheckUserPass = false;

        if(usernameInputLogin.isEmpty() && passwordInputLogin.isEmpty()) {
            textMelding.setVisibility(View.VISIBLE);
            textMelding.setText("Username of password niet ingevuld");
            CheckUserPass = false;
        }
        for( Gebruiker gebruiker : list ) {
            if(usernameInputLogin.equals(gebruiker.getUsername()) && passwordInputLogin.equals(gebruiker.getPasswoord())) {
                CheckUserPass = true;
            }
        }

        return CheckUserPass;
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();

        if(usernameInput.isEmpty()) {
            textInputUsername.setError("Username veld is leeg");
            return false;
        }
        if(usernameInput.length() > 20) {
            textInputUsername.setError("Username is te lang");
            return false;
        } else {
            textInputUsername.setError(null);
            return true;
        }
    }
    private boolean validatePassword() {
        String passwordInput = textInputPassword.getEditText().getText().toString().trim();

        if(passwordInput.isEmpty()) {
            textInputPassword.setError("Passwoord veld is leeg");
            return false;
        } else {
            textInputPassword.setError(null);
            return true;
        }
    }
    private boolean validateEmail() {
        String emailInput = textInputEmail.getEditText().getText().toString().trim();

        if(emailInput.isEmpty()) {
            textInputEmail.setError("Email veld is leeg");
            return false;
        } else {
            textInputEmail.setError(null);
            return true;
        }
    }
    private boolean validateRaspId() {
        String raspIdInput = textInputRaspId.getEditText().getText().toString().trim();

        if(raspIdInput.isEmpty()) {
            textInputRaspId.setError("ID raspberry is leeg");
            return false;
        } else {
            textInputRaspId.setError(null);
            return true;
        }

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
                list.add(gebruiker);

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



