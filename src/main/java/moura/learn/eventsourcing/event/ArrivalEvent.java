package moura.learn.eventsourcing.event;

import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;

import java.util.Date;

public class ArrivalEvent extends DomainEvent {
    public ArrivalEvent(Date occured, Port port, Ship ship) {
        super(occured);
        super.port = port;
        super.ship = ship;
    }

    @Override
    public void Process() {
        super.ship.HandleArrival(this);
    }
}
