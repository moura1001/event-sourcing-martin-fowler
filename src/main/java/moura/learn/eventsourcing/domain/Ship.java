package moura.learn.eventsourcing.domain;

import moura.learn.eventsourcing.event.ArrivalEvent;
import moura.learn.eventsourcing.event.DepartureEvent;

public class Ship {
    private String name;
    private Port port;

    public Ship(String name) {
        this.name = name;
        this.port = null;
    }

    public void HandleDeparture(DepartureEvent ev) {
        this.port = Port.AT_SEA;
    }

    public void HandleArrival(ArrivalEvent ev) {
        this.port = ev.GetPort();
    }

    public Port GetPort() { return this.port; }
}
