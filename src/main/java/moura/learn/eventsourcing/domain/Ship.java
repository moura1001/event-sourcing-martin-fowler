package moura.learn.eventsourcing.domain;

import moura.learn.eventsourcing.event.ArrivalEvent;
import moura.learn.eventsourcing.event.DepartureEvent;
import moura.learn.eventsourcing.event.LoadEvent;
import moura.learn.eventsourcing.event.UnloadEvent;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private String name;
    private Port port;
    private final List<Cargo> cargo = new ArrayList<>();

    public Ship(String name) {
        this.name = name;
        this.port = null;
    }

    public Port GetPort() { return this.port; }

    public int LoadQuantity() { return cargo.size(); }

    public void HandleDeparture(DepartureEvent ev) {
        this.port = Port.AT_SEA;
    }

    public void HandleArrival(ArrivalEvent ev) {
        this.port = ev.GetPort();
        for (Cargo c: this.cargo) {
            c.HandleArrival(ev);
        }
    }

    public void HandleLoad(LoadEvent ev) { this.cargo.add(ev.GetCargo()); }

    public void HandleUnload(UnloadEvent ev) { this.cargo.removeIf(c -> (c == ev.GetCargo())); }
}
