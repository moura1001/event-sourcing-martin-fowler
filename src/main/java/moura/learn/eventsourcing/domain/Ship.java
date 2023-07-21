package moura.learn.eventsourcing.domain;

import moura.learn.eventsourcing.event.ArrivalEvent;
import moura.learn.eventsourcing.event.DepartureEvent;
import moura.learn.eventsourcing.event.LoadEvent;
import moura.learn.eventsourcing.event.UnloadEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Ship {
    private String name;
    private Port port;
    private final List<Cargo> cargo = new ArrayList<>();

    public Ship(String name) {
        this.name = name;
        this.port = null;
    }

    public String GetName() { return this.name; }

    public Port GetPort() { return this.port; }

    public int LoadQuantity() { return cargo.size(); }

    public void HandleDeparture(DepartureEvent ev) {
        this.port = Port.AT_SEA;
    }

    public void HandleArrival(ArrivalEvent ev) {
        ev.SetPriorPort(this.port);
        this.port = ev.GetPort();
        this.port.HandleArrival(ev);
        for (Cargo c: this.cargo) {
            c.HandleArrival(ev);
        }
    }

    public void ReverseArrival(ArrivalEvent ev) {
        this.port = ev.GetPriorPort();
        for (Cargo c: this.cargo) {
            c.ReverseArrival(ev);
        }
    }

    public void HandleLoad(LoadEvent ev) { this.cargo.add(ev.GetCargo()); }

    public void ReverseLoad(LoadEvent ev) { this.cargo.remove(ev.GetCargo()); }

    public void HandleUnload(UnloadEvent ev) { this.cargo.removeIf(c -> (c == ev.GetCargo())); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ship)) return false;
        Ship ship = (Ship) o;
        return name.equals(ship.name) && port.equals(ship.port) && cargo.equals(ship.cargo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, port, cargo);
    }
}
