package moura.learn.eventsourcing.domain;

public class Cargo {
    private String name;
    private Ship ship;
    private Port port;

    public Cargo(String name){
        this.name = name;
        this.ship = null;
        this.port = null;
    }
}
