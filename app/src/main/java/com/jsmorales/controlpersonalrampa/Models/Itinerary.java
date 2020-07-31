package com.jsmorales.controlpersonalrampa.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Itinerary implements Parcelable {

    public String Id;
    public String AircraftId;
    public String AircraftType;
    public String AirlineCode;
    public String ArrivingFlightDestiny;
    public String ArrivingFlightNumber;
    public String ArrivingFlightOrigin;
    public String ArrivingTime;
    public String Date;
    public String DepartureFlightDestiny;
    public String DepartureFlightNumber;
    public String DepartureFlightOrigin;
    public String DepartureTime;

    public Itinerary(Parcel in) {
        Id = in.readString();
        AircraftId = in.readString();
        AircraftType = in.readString();
        AirlineCode = in.readString();
        ArrivingFlightDestiny = in.readString();
        ArrivingFlightNumber = in.readString();
        ArrivingFlightOrigin = in.readString();
        ArrivingTime = in.readString();
        Date = in.readString();
        DepartureFlightDestiny = in.readString();
        DepartureFlightNumber = in.readString();
        DepartureFlightOrigin = in.readString();
        DepartureTime = in.readString();
    }

    public static final Creator<Itinerary> CREATOR = new Creator<Itinerary>() {
        @Override
        public Itinerary createFromParcel(Parcel in) {
            return new Itinerary(in);
        }

        @Override
        public Itinerary[] newArray(int size) {
            return new Itinerary[size];
        }
    };

    public Itinerary() {

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getAircraftId() {
        return AircraftId;
    }

    public void setAircraftId(String aircraftId) {
        AircraftId = aircraftId;
    }

    public String getAircraftType() {
        return AircraftType;
    }

    public void setAircraftType(String aircraftType) {
        AircraftType = aircraftType;
    }

    public String getAirlineCode() {
        return AirlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        AirlineCode = airlineCode;
    }

    public String getArrivingFlightDestiny() {
        return ArrivingFlightDestiny;
    }

    public void setArrivingFlightDestiny(String arrivingFlightDestiny) {
        ArrivingFlightDestiny = arrivingFlightDestiny;
    }

    public String getArrivingFlightNumber() {
        return ArrivingFlightNumber;
    }

    public void setArrivingFlightNumber(String arrivingFlightNumber) {
        ArrivingFlightNumber = arrivingFlightNumber;
    }

    public String getArrivingFlightOrigin() {
        return ArrivingFlightOrigin;
    }

    public void setArrivingFlightOrigin(String arrivingFlightOrigin) {
        ArrivingFlightOrigin = arrivingFlightOrigin;
    }

    public String getArrivingTime() {
        return ArrivingTime;
    }

    public void setArrivingTime(String arrivingTime) {
        ArrivingTime = arrivingTime;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDepartureFlightDestiny() {
        return DepartureFlightDestiny;
    }

    public void setDepartureFlightDestiny(String departureFlightDestiny) {
        DepartureFlightDestiny = departureFlightDestiny;
    }

    public String getDepartureFlightNumber() {
        return DepartureFlightNumber;
    }

    public void setDepartureFlightNumber(String departureFlightNumber) {
        DepartureFlightNumber = departureFlightNumber;
    }

    public String getDepartureFlightOrigin() {
        return DepartureFlightOrigin;
    }

    public void setDepartureFlightOrigin(String departureFlightOrigin) {
        DepartureFlightOrigin = departureFlightOrigin;
    }

    public String getDepartureTime() {
        return DepartureTime;
    }

    public void setDepartureTime(String departureTime) {
        DepartureTime = departureTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Id);
        dest.writeString(AircraftId);
        dest.writeString(AircraftType);
        dest.writeString(AirlineCode);
        dest.writeString(ArrivingFlightDestiny);
        dest.writeString(ArrivingFlightNumber);
        dest.writeString(ArrivingFlightOrigin);
        dest.writeString(ArrivingTime);
        dest.writeString(Date);
        dest.writeString(DepartureFlightDestiny);
        dest.writeString(DepartureFlightNumber);
        dest.writeString(DepartureFlightOrigin);
        dest.writeString(DepartureTime);
    }
}
