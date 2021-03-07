package swt6.timer.interfaces;

import java.util.List;

public interface TimerService {
    Timer create(int interval, int iterations);
    void changeTimerInterval(Timer t, int interval);
    void changeTimerIterations(Timer t, int iterations);
    List<Timer> getAllTimers();
    List<Timer> getActiveTimers();
    List<Timer> getInActiveTimers();
}
