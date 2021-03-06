package swt6.timer.interfaces;

public interface TimerServiceFactory {
    TimerService getTimerService();
    boolean providesFeature(String feature);
}
