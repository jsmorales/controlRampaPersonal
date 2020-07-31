package com.jsmorales.controlpersonalrampa.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Flight implements Parcelable {

    public String Id;
    public String FlightNumber;
    public String Airline;
    public String DestinyCity;
    public String OriginCity;
    public String Sta;
    public String Std;

    protected Flight(Parcel in) {
        Id = in.readString();
        FlightNumber = in.readString();
        Airline = in.readString();
        DestinyCity = in.readString();
        OriginCity = in.readString();
        Sta = in.readString();
        Std = in.readString();
    }

    public Flight(){

    }

    public static final Creator<Flight> CREATOR = new Creator<Flight>() {
        @Override
        public Flight createFromParcel(Parcel in) {
            return new Flight(in);
        }

        @Override
        public Flight[] newArray(int size) {
            return new Flight[size];
        }
    };

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFlightNumber() {
        return FlightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        FlightNumber = flightNumber;
    }

    public String getAirline() {
        return Airline;
    }

    public void setAirline(String airline) {
        Airline = airline;
    }

    public String getDestinyCity() {
        return DestinyCity;
    }

    public void setDestinyCity(String destinyCity) {
        DestinyCity = destinyCity;
    }

    public String getOriginCity() {
        return OriginCity;
    }

    public void setOriginCity(String originCity) {
        OriginCity = originCity;
    }

    public String getSta() {
        return Sta;
    }

    public void setSta(String sta) {
        Sta = sta;
    }

    public String getStd() {
        return Std;
    }

    public void setStd(String std) {
        Std = std;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(FlightNumber);
        dest.writeString(Airline);
        dest.writeString(DestinyCity);
        dest.writeString(OriginCity);
        dest.writeString(Sta);
        dest.writeString(Std);
    }
}
