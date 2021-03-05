package swt6.timer.interfaces;

public interface Timer {
    void start();
    void stop();
    boolean isActive();
    String getInfo();
    void addTimerListener(TimerListener tl);
    void removeTimerListener(TimerListener tl);
}
