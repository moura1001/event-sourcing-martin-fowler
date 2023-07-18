package moura.learn.eventsourcing.event;

import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;
import moura.learn.eventsourcing.service.CustomNotificationGateway;

import java.util.Date;

public class MockCustomEventGateway implements CustomNotificationGateway {

    private boolean notified;

    public boolean isNotified() {
        return notified;
    }

    @Override
    public void Notify(Date occurred, Ship ship, Port port) {
        this.notified = true;
    }
}