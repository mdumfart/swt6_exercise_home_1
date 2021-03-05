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

public class TimerImpl implements Timer {
    private static Logger logger = LogManager.getLogger(TimerImpl.class);
    private List<TimerListener> timerListeners = new CopyOnWriteArrayList<>();

    private final int id;
    private boolean isActive = false;
    private int interval;
    private int iterations;
    private int elapsedIterations = 0;
    private LocalDateTime startedAt;

    public TimerImpl(int id, int interval, int iterations) {
        this.id = id;
        this.interval = interval;
        this.iterations = iterations;
    }

    public void setInterval(int ms) {
        interval = ms;
    }

    public int getInterval() {
        return interval;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getIterations() {
        return iterations;
    }

    @Override
    public void start() {
        if (isActive) throw new IllegalArgumentException(String.format("Timer with Id [%s] is already running", id));

        isActive = true;
        startedAt = LocalDateTime.now();
        run();
    }

    @Override
    public void stop() {
        if (!isActive) throw new IllegalArgumentException(String.format("Timer with Id [%s] is not running", id));

        isActive = false;
    }

    @Override
    public boolean isActive() {
        return isActive;
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

            while(isActive && elapsedIterations != iterations) {
                try {
                    Thread.sleep(interval);

                    if (isActive){
                        fireTickEvent();
                    }
                    else {
                        logger.info(String.format("Timer [%s] stopped manually", id));
                        manualStop = true;
                    }
                } catch (InterruptedException e) {
                    isActive = false;
                    logger.error("Timer unexpectedly interrupted: Timer stopped");
                }
            }

            if (!manualStop) fireElapsedEvent();
        });
        thread.start();
    }

    // events

    private void fireTickEvent() {
        Duration elapsedTime = Duration.between(startedAt, LocalDateTime.now());

        TimerEvent event = new TimerEvent(this, id, elapsedTime, ++elapsedIterations, iterations);

        for (TimerListener listener : timerListeners) {
            listener.timerTicked(event);
        }
    }

    private void fireElapsedEvent() {
        Duration elapsedTime = Duration.between(startedAt, LocalDateTime.now());

        TimerEvent event = new TimerEvent(this, id, elapsedTime, ++elapsedIterations, iterations);

        for (TimerListener listener : timerListeners) {
            listener.timerElapsed(event);
        }
    }
}
