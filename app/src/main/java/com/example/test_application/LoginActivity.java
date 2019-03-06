package com.example.test_application;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

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
                textMelding.setText("");
                if (validateUsernameLogin() && validatePasswordLogin()) {


                    Intent i = new Intent(v.getContext(), MapsActivity.class);
                    startActivity(i);

                }
            }

        });


    }

    private boolean validateUsernameLogin() {
        String usernameInputLogin = textInputUsernameLogin.getEditText().getText().toString().trim();

        if(usernameInputLogin.isEmpty()) {
            textMelding.setVisibility(View.VISIBLE);
            textMelding.setText("Username of password niet correct");
            return false;
        } else {
            return true;
        }
    }
    private boolean validatePasswordLogin() {
        String passwordInputLogin = textInputPasswordLogin.getEditText().getText().toString().trim();

        if(passwordInputLogin.isEmpty()) {
            textMelding.setVisibility(View.VISIBLE);
            textMelding.setText("Username of password niet correct");
            return false;
        } else {
            return true;
        }
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

}



