package moura.learn.eventsourcing.event;

import java.util.ArrayList;
import java.util.List;

public class EventProcessor {
    private final List<DomainEvent> log = new ArrayList<>();

    private boolean isActive;

    public void Process(DomainEvent e){
        isActive = true;
        e.Process();
        isActive = false;
        log.add(e);
    }

    public int NumberOfEvents() { return log.size(); }

    public boolean isActive() { return isActive; }
}
