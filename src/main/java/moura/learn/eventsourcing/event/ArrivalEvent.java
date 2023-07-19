package moura.learn.eventsourcing.event;

import moura.learn.eventsourcing.domain.Cargo;
import moura.learn.eventsourcing.domain.Port;
import moura.learn.eventsourcing.domain.Ship;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ArrivalEvent extends DomainEvent {
    private Map<Cargo, Boolean> priorCargoInCanada = new HashMap<>();

    public ArrivalEvent(Date occurred, Port port, Ship ship) {
        super(occurred);
        super.port = port;
        super.ship = ship;
    }

    @Override
    public void Process() { super.ship.HandleArrival(this); }

    @Override
    public void Reverse() { super.ship.ReverseArrival(this); }

    public void SetPriorCargoInCanada(Cargo cargo, boolean hasBeenInCanada) {
        priorCargoInCanada.put(cargo, hasBeenInCanada);
    }

    public boolean PriorCargoInCanada(Cargo cargo) {
        return priorCargoInCanada.getOrDefault(cargo, false);
    }
}
