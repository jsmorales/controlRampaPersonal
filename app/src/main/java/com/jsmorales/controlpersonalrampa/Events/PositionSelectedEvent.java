package com.jsmorales.controlpersonalrampa.Events;

import com.jsmorales.controlpersonalrampa.Models.Position;

public class PositionSelectedEvent {

    public final Position position;

    public PositionSelectedEvent(Position position) {
        this.position = position;
    }

    public Position getPosition(){
        return this.position;
    }
}
