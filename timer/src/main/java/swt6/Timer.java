package swt6;

public interface Timer extends Runnable {
    boolean isActive();
    void start();
    void stop();

    void setInterval(int ms);
    int getInterval();

    void setIterations(int iterations);
    int getIterations();

    void addTimerListener(TimerListener tl);
    void removeTimerListener(TimerListener tl);
}
