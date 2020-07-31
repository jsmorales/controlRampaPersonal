package com.jsmorales.controlpersonalrampa.Events;

import com.jsmorales.controlpersonalrampa.Models.Itinerary;

public class FlightSelectedEvent {

    public final Itinerary itinerary;

    public FlightSelectedEvent(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public Itinerary getItinerary(){
        return this.itinerary;
    }
}
