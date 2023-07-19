package moura.learn.eventsourcing.domain;

import moura.learn.eventsourcing.event.ArrivalEvent;
import moura.learn.eventsourcing.event.LoadEvent;
import moura.learn.eventsourcing.event.UnloadEvent;

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

    public Ship GetShip() { return ship; }

    public Port GetPort() { return port; }

    public boolean HasBeenInCanada() { return hasBeenInCanada; }

    public void HandleArrival(ArrivalEvent ev) {
        if (Country.CANADA == ev.GetPort().GetCountry())
            hasBeenInCanada = true;
    }

    public void HandleLoad(LoadEvent ev) {
        ev.SetPriorPort(port);
        this.port = null;
        this.ship = ev.GetShip();
        this.ship.HandleLoad(ev);
    }

    public void ReverseLoad(LoadEvent ev) {
        this.ship.ReverseLoad(ev);
        this.ship = null;
        this.port = ev.GetPriorPort();
    }

    public void HandleUnload(UnloadEvent ev) {
        this.port = ev.GetShip().GetPort();
        this.ship.HandleUnload(ev);
        this.ship = null;
    }
}
