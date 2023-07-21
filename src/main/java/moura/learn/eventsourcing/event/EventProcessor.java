package moura.learn.eventsourcing.event;

import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class EventProcessor {
    private final List<DomainEvent> log = new ArrayList<>();

    private boolean isActive;

    private final Map<Class, Function<DomainEvent, Boolean>> strategy = Map.ofEntries(
            Map.entry(ArrivalEvent.class, ev -> isValidArrivalEvent((ArrivalEvent) ev)),
            Map.entry(DepartureEvent.class, ev -> isValidDepartureEvent((DepartureEvent) ev))
    );

    public void Process(DomainEvent e){
        isActive = true;
        Function<DomainEvent, Boolean> isValid = strategy.getOrDefault(e.getClass(), ev -> true);
        if (isValid.apply(e)) {
            e.Process();
            log.add(e);
        }
        isActive = false;
    }

    public int NumberOfEvents() { return log.size(); }

    public boolean isActive() { return isActive; }

    private boolean isValidArrivalEvent(ArrivalEvent ev) {
        if (ev.GetShip() != null && ev.GetPort() != null && !ev.GetPort().equals(Port.AT_SEA)) {

            Ship ship = ev.GetShip();
            Date occurred = ev.GetOccurred();

            for (int i = log.size() - 1; i >= 0 ; i--) {
                DomainEvent e = log.get(i);
                Ship s = e.GetShip();
                Date o = e.GetOccurred();

                if (ship.equals(s)){
                    if(e instanceof DepartureEvent){
                        return o.before(occurred);
                    } else if (e instanceof ArrivalEvent) {
                        return false;
                    }
                }
            }

            return true;

        } else {
            return false;
        }
    }

    private boolean isValidDepartureEvent(DepartureEvent ev) {
        if (ev.GetShip() != null && ev.GetPort() != null && !ev.GetPort().equals(Port.AT_SEA)) {

            Ship ship = ev.GetShip();
            Port port = ev.GetPort();
            Date occurred = ev.GetOccurred();

            for (int i = log.size() - 1; i >= 0 ; i--) {
                DomainEvent e = log.get(i);
                Ship s = e.GetShip();
                Port p = e.GetPort();
                Date o = e.GetOccurred();

                if (ship.equals(s)){
                    if(e instanceof DepartureEvent){
                        return false;
                    } else if (e instanceof ArrivalEvent) {
                        return port.equals(p) && o.before(occurred);
                    }
                }
            }

            return true;

        } else {
            return false;
        }
    }
}
