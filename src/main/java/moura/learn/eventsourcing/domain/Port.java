package moura.learn.eventsourcing.domain;

import moura.learn.eventsourcing.event.ArrivalEvent;
import moura.learn.eventsourcing.service.Registry;

import java.util.Objects;

public class Port {
    public static final Port AT_SEA = new Port(null, null);
    private String name;
    private Country country;
    private Registry registry;

    public Port(String name, Country country) {
        this.name = name;
        this.country = country;
    }

    public Port(String name, Country country, Registry registry) {
        this(name, country);
        this.registry = registry;
    }

    public String GetName() { return this.name; }

    public Country GetCountry() { return country; }

    public void HandleArrival(ArrivalEvent ev) {
        if (this.registry != null)
            this.registry.GetCustomNotificationGateway().Notify(ev.GetOccurred(), ev.GetShip(), ev.GetPort());
    }
}
