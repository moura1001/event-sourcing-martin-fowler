package moura.learn.eventsourcing.event;

import moura.learn.eventsourcing.domain.Cargo;
import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;

import java.util.Date;

public class LoadEvent extends DomainEvent {
    private Cargo cargo;

    public LoadEvent(Date occurred, Cargo cargo, Ship ship) {
        super(occurred);
        super.ship = ship;
        this.cargo = cargo;
    }

    @Override
    public void Process() { this.cargo.HandleLoad(this); }

    @Override
    public void Reverse() { this.cargo.ReverseLoad(this); }

    public Cargo GetCargo() { return this.cargo; }
}
