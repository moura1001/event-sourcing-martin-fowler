package moura.learn.eventsourcing.service;

import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;

import java.util.Date;

public interface CustomNotificationGateway {
    void Notify(Date occurred, Ship ship, Port port);
}
