package moura.learn.eventsourcing.event;

import java.util.ArrayList;
import java.util.List;

public class EventProcessor {
    private final List<DomainEvent> log = new ArrayList<>();

    public void Process(DomainEvent e){
        e.Process();
        log.add(e);
    }
}
