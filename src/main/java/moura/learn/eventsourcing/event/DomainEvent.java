package moura.learn.eventsourcing.event;

import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;

import java.util.Date;

abstract class DomainEvent {
    protected Date occurred;
    protected Date recorded;
    protected Port port;
    protected Ship ship;

    protected DomainEvent(Date occurred) {
        this.occurred = occurred;
        this.recorded = new Date();
    }

    public abstract void Process();

    public Port GetPort() { return port; }

    public Ship GetShip() {
        return ship;
    }

    public Date GetOccurred() {
        return occurred;
    }
}
