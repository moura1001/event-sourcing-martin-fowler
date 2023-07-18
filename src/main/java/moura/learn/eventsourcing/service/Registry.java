package moura.learn.eventsourcing.service;

public class Registry {
    private CustomNotificationGateway gateway;

    public Registry(CustomNotificationGateway gateway){
        this.gateway = gateway;
    }

    public CustomNotificationGateway GetCustomNotificationGateway() { return gateway; }
}
