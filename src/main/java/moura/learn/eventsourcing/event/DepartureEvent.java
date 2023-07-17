package moura.learn.eventsourcing.event;

import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;

import java.util.Date;

public class DepartureEvent extends DomainEvent {
    public DepartureEvent(Date occurred, Port port, Ship ship) {
        super(occurred);
        super.port = port;
        super.ship = ship;
    }

    @Override
    public void Process() {
        super.ship.HandleDeparture(this);
    }
}
