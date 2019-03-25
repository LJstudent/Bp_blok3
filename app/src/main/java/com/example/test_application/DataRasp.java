package com.example.test_application;

public class DataRasp {
    String  datum,temp,instraling, latitude, longitude;

    public DataRasp(String datum, String temp, String instraling, String latitude, String longitude) {

        this.datum=datum;
        this.temp=temp;
        this.instraling=instraling;
        this.latitude=latitude;
        this.longitude=longitude;
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
}
