package swt6.timer.factories;

import swt6.timer.interfaces.TimerServiceFactory;
import swt6.timer.services.TimerServiceImpl;
import swt6.timer.interfaces.TimerService;

public class TimerServiceFactoryImpl implements TimerServiceFactory {
    public TimerService getTimerService() {
        return new TimerServiceImpl();
    }
    public boolean providesFeature(String feature) {
        return feature.equals("TimerServiceImpl");
    }
}
