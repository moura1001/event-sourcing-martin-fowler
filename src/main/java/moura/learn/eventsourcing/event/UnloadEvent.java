package moura.learn.eventsourcing.event;

import moura.learn.eventsourcing.domain.Cargo;
import moura.learn.eventsourcing.domain.Ship;

import java.util.Date;

public class UnloadEvent extends DomainEvent {
    private Cargo cargo;

    public UnloadEvent(Date occurred, Cargo cargo, Ship ship) {
        super(occurred);
        super.ship = ship;
        this.cargo = cargo;
    }

    @Override
    public void Process() {
        this.cargo.HandleUnload(this);
    }

    @Override
    public void Reverse() {}

    public Cargo GetCargo() { return this.cargo; }
}
