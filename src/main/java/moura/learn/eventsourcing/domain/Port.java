package moura.learn.eventsourcing.domain;

public class Port {
    public static final Port AT_SEA = new Port(null, null);
    private String name;
    private Country country;

    public Port(String name, Country country) {
        this.name = name;
        this.country = country;
    }
}
