package moura.learn.eventsourcing.domain;

import moura.learn.eventsourcing.event.ArrivalEvent;

public class Cargo {
    private String name;
    private Ship ship;
    private Port port;
    private boolean hasBeenInCanada;

    public Cargo(String name){
        this.name = name;
        this.ship = null;
        this.port = null;
    }

    public boolean HasBeenInCanada() { return hasBeenInCanada; }

    public void HandleArrival(ArrivalEvent ev) {
        if (Country.CANADA == ev.GetPort().GetCountry())
            hasBeenInCanada = true;
    }
}
