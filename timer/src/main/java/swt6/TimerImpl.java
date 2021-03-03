package swt6;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TimerImpl implements Timer {
    private List<TimerListener> timerListeners = new CopyOnWriteArrayList<>();

    private boolean isActive = false;
    private int interval;
    private int iterations;
    private int elapsedIterations = 0;
    private LocalDateTime startedAt;

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void start() {
        isActive = true;
        startedAt = LocalDateTime.now();
    }

    @Override
    public void stop() {
        isActive = false;
    }

    @Override
    public void setInterval(int ms) {
        interval = ms;
    }

    @Override
    public int getInterval() {
        return interval;
    }

    @Override
    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Override
    public int getIterations() {
        return iterations;
    }

    @Override
    public void addTimerListener(TimerListener tl) {
        timerListeners.add(tl);
    }

    @Override
    public void removeTimerListener(TimerListener tl) {
        timerListeners.remove(tl);
    }

    @Override
    public void run() {
        while(isActive) {
            try {
                Thread.sleep(interval);
                fireTickEvent();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void fireTickEvent() {
        Duration elapsedTime = Duration.between(startedAt, LocalDateTime.now());

        TimerEvent event = new TimerEvent(this, elapsedTime, ++elapsedIterations);
    }
}
