package swt6.timer.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swt6.timer.interfaces.Timer;
import swt6.timer.interfaces.TimerListener;
import swt6.timer.util.TimerEvent;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TimerImpl implements Timer {
    private static Logger logger = LogManager.getLogger(TimerImpl.class);
    private List<TimerListener> timerListeners = new CopyOnWriteArrayList<>();

    private final int id;
    private AtomicBoolean isActive = new AtomicBoolean(false);
    private AtomicInteger interval = new AtomicInteger();
    private AtomicInteger iterations = new AtomicInteger();
    private AtomicInteger elapsedIterations = new AtomicInteger(0);
    private LocalDateTime startedAt;

    public TimerImpl(int id, int interval, int iterations) {
        this.id = id;
        this.interval.set(interval);
        this.iterations.set(iterations);
    }

    public void setInterval(int ms) {
        interval.set(ms);
    }

    public int getInterval() {
        return interval.get();
    }

    public void setIterations(int iterations) {
        this.iterations.set(iterations);
    }

    public int getIterations() {
        return iterations.get();
    }

    @Override
    public void start() {
        if (isActive.get()) throw new IllegalArgumentException(String.format("Timer with Id [%s] is already running", id));

        isActive.set(true);
        startedAt = LocalDateTime.now();
        run();
    }

    @Override
    public void stop() {
        if (!isActive.get()) throw new IllegalArgumentException(String.format("Timer with Id [%s] is not running", id));

        isActive.set(false);
    }

    @Override
    public void reset() {
        elapsedIterations.set(0);
        logger.info(String.format("Timer [%s] reset", id));
    }

    @Override
    public void changeInterval(int interval) {
        this.interval.set(interval);
    }

    @Override
    public void changeIterations(int iterations) {
        this.iterations.set(iterations);
    }

    @Override
    public boolean isActive() {
        return isActive.get();
    }

    @Override
    public String getInfo() {
        return String.format("Id: %s, Interval: %s, Iterations: %s", id, interval, iterations);
    }

    @Override
    public void addTimerListener(TimerListener tl) {
        timerListeners.add(tl);
        logger.info("New Timer Listener added");
    }

    @Override
    public void removeTimerListener(TimerListener tl) {
        timerListeners.remove(tl);
        logger.info("Timer Listener removed");
    }

    public void run() {
        Thread thread = new Thread(() -> {
            boolean manualStop = false;

            logger.info(String.format("+++ Timer [%s] started +++", id));

            while(isActive.get() && elapsedIterations.get() < iterations.get()) {
                try {
                    Thread.sleep(interval.get());

                    if (isActive.get() && elapsedIterations.get() < iterations.get()){
                        fireTickEvent();
                    }
                    else {
                        logger.info(String.format("Timer [%s] stopped manually", id));
                        manualStop = true;
                    }
                } catch (InterruptedException e) {
                    isActive.equals(false);
                    logger.error("Timer unexpectedly interrupted: Timer stopped");
                }
            }

            if (!manualStop) fireElapsedEvent();
            elapsedIterations.set(0);
            isActive.set(false);
        });
        thread.start();
    }

    // events

    private void fireTickEvent() {
        Duration elapsedTime = Duration.between(startedAt, LocalDateTime.now());

        TimerEvent event = new TimerEvent(this, id, elapsedTime, elapsedIterations.incrementAndGet(), iterations.get());

        for (TimerListener listener : timerListeners) {
            listener.timerTicked(event);
        }
    }

    private void fireElapsedEvent() {
        Duration elapsedTime = Duration.between(startedAt, LocalDateTime.now());

        TimerEvent event = new TimerEvent(this, id, elapsedTime, elapsedIterations.incrementAndGet(), iterations.get());

        for (TimerListener listener : timerListeners) {
            listener.timerElapsed(event);
        }
    }
}
