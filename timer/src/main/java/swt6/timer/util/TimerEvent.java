package swt6.timer.util;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.EventObject;

public class TimerEvent extends EventObject {
    private final int id;
    private final Duration elapsedTime;
    private final int elapsedEvents;
    private final int maxEvents;

    public TimerEvent(Object source, int id, Duration elapsedTime, int elapsedEvents, int maxEvents) {
        super(source);
        this.id = id;
        this.elapsedTime = elapsedTime;
        this.elapsedEvents = elapsedEvents;
        this.maxEvents = maxEvents;
    }

    public int getId() {
        return id;
    }

    public Duration getElapsedTime() {
        return elapsedTime;
    }

    public int getElapsedEvents() {
        return elapsedEvents;
    }

    public int getMaxEvents() {
        return maxEvents;
    }

    @Override
    public String toString() {
        return String.format("Timer [%s]:%n" +
                        "  Events: %s of %s",
                id, elapsedEvents, maxEvents);
    }
}
