package com.example.test_application;

// tabelview class
public class TabelView {
    private String user,raspID,datum,tijd,temp,instraling,hw;

    public TabelView(String user, String raspID, String datum, String tijd, String temp, String instraling, String hw) {
        this.user = user;
        this.raspID = raspID;
        this.datum = datum;
        this.tijd= tijd;
        this.temp = temp;
        this.instraling = instraling;
        this.hw = hw;

    }

    public String getUser() {
        return user;
    }

    public String getTijd() {
        return tijd;
    }

    public void setTijd(String tijd) {
        this.tijd = tijd;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRaspID() {
        return raspID;
    }

    public void setRaspID(String raspID) {
        this.raspID = raspID;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getInstraling() {
        return instraling;
    }

    public void setInstraling(String instraling) {
        this.instraling = instraling;
    }

    public String getHw() {
        return hw;
    }

    public void setHw(String hw) {
        this.hw = hw;
    }
}
