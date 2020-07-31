package com.jsmorales.controlpersonalrampa.Events;

import com.jsmorales.controlpersonalrampa.Models.Ubication;

public class UbicationSelectedEvent {

    public final Ubication ubication;

    public UbicationSelectedEvent(Ubication ubication) {
        this.ubication = ubication;
    }

    public Ubication getUbication(){
        return this.ubication;
    }
}
