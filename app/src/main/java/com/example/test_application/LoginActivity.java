package com.example.test_application;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {
    private TextInputLayout textInputUsername;
    private TextInputLayout textInputPassword;
    private TextInputLayout textInputEmail;
    private TextInputLayout textInputRaspId;
    private TextInputLayout textInputUsernameLogin;
    private TextInputLayout textInputPasswordLogin;
    private TextView        textMelding;
    private Button          btnbevestigen, btnbevestigenLogin;
    private ImageView image;
    GebruikerLogin gebruiker = new GebruikerLogin();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


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

        btnbevestigen = findViewById(R.id.btnBevestigen);
        image=findViewById(R.id.imgCheck);

        btnbevestigen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validateUsername() && validatePassword() && validateEmail() && validateRaspId()) {

                    //Gebruiken voor dialogfragment
                    String usernameInput = textInputUsername.getEditText().getText().toString().trim();

                    DialogFragment dialog = new AccountMade();
                    Bundle bundle = new Bundle();
                    bundle.putString("header",usernameInput);
                    dialog.setArguments(bundle);
                    dialog.show(getSupportFragmentManager(), "MyDialogFragmentTag");

                    resetReg();
                }
            }

        });

        btnbevestigenLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Get username for the rest of the program
                String username = textInputUsernameLogin.getEditText().getText().toString().trim();
                if (validateLogin()) {
                    // Animation
                    image.setImageResource(R.drawable.ic_check_black_24dp);
                    btnbevestigenLogin.setBackgroundColor(Color.GREEN);
                    btnbevestigenLogin.setTextColor(Color.WHITE);
                    btnbevestigenLogin.setText("Bevestigd");
                    // new activity
                    Intent i = new Intent(v.getContext(), MapsActivity.class);
                    i.putExtra("user", username);
                    startActivity(i);

                    resetLog();
                }
            }

        });


    }

    private boolean validateLogin() {
        String usernameInputLogin = textInputUsernameLogin.getEditText().getText().toString().trim();
        String passwordInputLogin = textInputPasswordLogin.getEditText().getText().toString().trim();

        if (usernameInputLogin.isEmpty() && passwordInputLogin.isEmpty()) {
            textMelding.setVisibility(View.VISIBLE);
            textMelding.setText("Username of password niet ingevuld");
            return false;
        }
        if (gebruiker.checkUserPass(usernameInputLogin, passwordInputLogin)) {
            textMelding.setVisibility(View.INVISIBLE);
            return true;

        }else {
            textMelding.setVisibility(View.VISIBLE);
            textMelding.setText("Username of password niet correct");
            return false;
        }
    }

    private boolean validateUsername() {
        String usernameInput = textInputUsername.getEditText().getText().toString().trim();


        if (usernameInput.isEmpty()) {
            textInputUsername.setError("Username veld is leeg");
            return false;
        }
        if (usernameInput.length() > 20) {
            textInputUsername.setError("Username is te lang");
            return false;

        }
        if(gebruiker.checkUser(usernameInput)) {
            textInputUsername.setError("Username is al in gebruik");
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
        }
        if(gebruiker.checkEmail(emailInput)) {
            textInputEmail.setError("Email adres is al in gebruik");
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
        }
        if(gebruiker.checkRasp(raspIdInput)) {
            textInputRaspId.setError("ID is al in gebruik, of bestaat niet");
            return false;
        } else {
            textInputRaspId.setError(null);
            return true;
        }

    }
    public void resetReg() {
        textInputUsername.getEditText().setText(null);
        textInputPassword.getEditText().setText(null);
        textInputEmail.getEditText().setText(null);
        textInputRaspId.getEditText().setText(null);
    }
    public void resetLog() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                btnbevestigenLogin.setBackgroundResource(android.R.drawable.btn_default);
                btnbevestigenLogin.setTextColor(Color.BLACK);
                btnbevestigenLogin.setText("Login");
                image.setImageResource(0);
            }
        }, 3000);   //3 seconds langer dan normaal om het effect te laten zien

    }

}



