package moura.learn.eventsourcing.event;

import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;
import moura.learn.eventsourcing.service.CustomNotificationGateway;

import java.util.Date;

public class CustomEventGateway implements CustomNotificationGateway {
    private EventProcessor processor;

    public CustomEventGateway(EventProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void Notify(Date arrivalDate, Ship ship, Port port) {
        if (processor.isActive()) {
            sendToCustoms(buildArrivalMessage(arrivalDate, ship, port));
        }
    }

    private void sendToCustoms(String arrivalMessage) {
        System.out.printf("Arrival message sent successfully: '%s'\n", arrivalMessage);
    }

    private String buildArrivalMessage(Date arrivalDate, Ship ship, Port port) {
        return String.format("Ship '%s' arrived at port '%s' on date '%s'", ship.GetName(), port.GetName(), arrivalDate.toString());
    }
}
