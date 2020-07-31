package com.jsmorales.controlpersonalrampa.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable {

    private String firstName;
    private String lastName;
    private String position;
    private String region;
    private String filterUbication;


    public Employee(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        position = in.readString();
        region = in.readString();
        filterUbication = in.readString();
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

    public Employee() {

    }

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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFilterUbication() {
        return filterUbication;
    }

    public void setFilterUbication(String filterUbication) {
        this.filterUbication = filterUbication;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(position);
        dest.writeString(region);
        dest.writeString(filterUbication);
    }
}
