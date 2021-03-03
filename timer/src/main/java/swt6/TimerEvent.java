package swt6;

import java.time.Duration;
import java.util.EventObject;

public class TimerEvent extends EventObject {
    private final Duration elapsedTime;
    private final int elapsedEvents;

    public TimerEvent(Object source, Duration elapsedTime, int elapsedEvents) {
        super(source);
        this.elapsedTime = elapsedTime;
        this.elapsedEvents = elapsedEvents;
    }

    public Duration getElapsedTime() {
        return elapsedTime;
    }

    public int getElapsedEvents() {
        return elapsedEvents;
    }
}
