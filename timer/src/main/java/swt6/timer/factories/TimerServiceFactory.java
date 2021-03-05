package swt6.timer.factories;

import swt6.timer.services.TimerServiceImpl;
import swt6.timer.interfaces.TimerService;

public class TimerServiceFactory {
    static public TimerService getTimerService() {
        return new TimerServiceImpl();
    }
}
