package moura.learn.eventsourcing.event;

import java.util.Date;

abstract class DomainEvent {
    protected Date occured;
    protected Date recorded;

    protected DomainEvent(Date occured) {
        this.occured = occured;
        this.recorded = new Date();
    }

    public abstract void Process();
}
