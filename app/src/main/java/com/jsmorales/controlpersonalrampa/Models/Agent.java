package com.jsmorales.controlpersonalrampa.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Agent implements Parcelable {

    public String firstName;
    public String lastName;
    public String socialNumber;
    public String position;

    public Agent() {

    }

    protected Agent(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        socialNumber = in.readString();
        position = in.readString();
    }

    public static final Creator<Agent> CREATOR = new Creator<Agent>() {
        @Override
        public Agent createFromParcel(Parcel in) {
            return new Agent(in);
        }

        @Override
        public Agent[] newArray(int size) {
            return new Agent[size];
        }
    };

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(String socialNumber) {
        this.socialNumber = socialNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(socialNumber);
        dest.writeString(position);
    }
}
