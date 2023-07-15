package moura.learn.eventsourcing.domain;

public class Ship {
    private String name;
    private Port port;

    public Ship(String name) {
        this.name = name;
        this.port = null;
    }
}
