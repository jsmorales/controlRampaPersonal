package com.jsmorales.controlpersonalrampa.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class UtilsMainApp implements Parcelable {

    public String hostProduction = "https://control-llegada-backend.azurewebsites.net/";
    public String hostTest = "https://control-llegada-backend-dev.azurewebsites.net/";
    public String hostAuth = "https://sai-auth.azurewebsites.net/api/authentication";
    public String hostFirebaseProduction = "https://control-ingreso-203f0.firebaseio.com/";
    public String hostFirebaseTest = "https://control-ingreso-test.firebaseio.com/";
    public Integer numTouch = 6;
    public Integer touched = 0;

    protected UtilsMainApp(Parcel in) {
        hostProduction = in.readString();
        hostTest = in.readString();
        hostAuth = in.readString();
        hostFirebaseProduction = in.readString();
        hostFirebaseTest = in.readString();
        if (in.readByte() == 0) {
            numTouch = null;
        } else {
            numTouch = in.readInt();
        }
        if (in.readByte() == 0) {
            touched = null;
        } else {
            touched = in.readInt();
        }
    }

    public static final Creator<UtilsMainApp> CREATOR = new Creator<UtilsMainApp>() {
        @Override
        public UtilsMainApp createFromParcel(Parcel in) {
            return new UtilsMainApp(in);
        }

        @Override
        public UtilsMainApp[] newArray(int size) {
            return new UtilsMainApp[size];
        }
    };

    public String getHostProduction() {
        return hostProduction;
    }

    public void setHostProduction(String hostProduction) {
        this.hostProduction = hostProduction;
    }

    public String getHostTest() {
        return hostTest;
    }

    public void setHostTest(String hostTest) {
        this.hostTest = hostTest;
    }

    public String getHostAuth() {
        return hostAuth;
    }

    public void setHostAuth(String hostAuth) {
        this.hostAuth = hostAuth;
    }

    public String getHostFirebaseProduction() {
        return hostFirebaseProduction;
    }

    public void setHostFirebaseProduction(String hostFirebaseProduction) {
        this.hostFirebaseProduction = hostFirebaseProduction;
    }

    public String getHostFirebaseTest() {
        return hostFirebaseTest;
    }

    public void setHostFirebaseTest(String hostFirebaseTest) {
        this.hostFirebaseTest = hostFirebaseTest;
    }

    public Integer getNumTouch() {
        return numTouch;
    }

    public void setNumTouch(Integer numTouch) {
        this.numTouch = numTouch;
    }

    public Integer getTouched() {
        return touched;
    }

    public void setTouched(Integer touched) {
        this.touched = touched;
    }

    public UtilsMainApp() {

    }

    public String getHost(){

        if(this.getTouched() >= this.getNumTouch()){
            return this.getHostTest();
        }else{
            return this.getHostProduction();
        }

    }

    public String getHostFireBase(){

        if(this.getTouched() >= this.getNumTouch()){
            return this.getHostFirebaseTest();
        }else{
            return this.getHostFirebaseProduction();
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hostProduction);
        dest.writeString(hostTest);
        dest.writeString(hostAuth);
        dest.writeString(hostFirebaseProduction);
        dest.writeString(hostFirebaseTest);
        if (numTouch == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(numTouch);
        }
        if (touched == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(touched);
        }
    }
}
