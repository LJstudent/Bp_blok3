package com.example.test_application;

// class gebruiker nodig voor gebruikerlogin
public class Gebruiker {
    private String email,username,passwoord,latitude,longitude,rasp_id;


    public Gebruiker(String email, String username, String passwoord, String latitude, String longitude, String rasp_id) {
            this.email=email;
            this.username=username;
            this.passwoord=passwoord;
            this.latitude=latitude;
            this.longitude=longitude;
            this.rasp_id=rasp_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswoord() {
        return passwoord;
    }

    public void setPasswoord(String passwoord) {
        this.passwoord = passwoord;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRasp_id() {
        return rasp_id;
    }

    public void setRasp_id(String rasp_id) {
        this.rasp_id = rasp_id;
    }

    @Override
    public String toString() {
        return String.format( "Email " + email + "\n" +
        "Username"  + username + "\n" +
        "Password " + passwoord + "\n" +
        "Latitude " + latitude + "\n" +
        "Longitude " + longitude + "\n" +
        "Raspberry piID " + rasp_id);
    }
}
