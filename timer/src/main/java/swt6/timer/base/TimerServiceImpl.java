package swt6.timer.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swt6.timer.interfaces.Timer;
import swt6.timer.interfaces.TimerService;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class TimerServiceImpl implements TimerService {
    private static final int MIN_INTERVAL = 50;
    private static final int MIN_ITERATIONS = 1;

    private Logger logger = LogManager.getLogger();
    private List<Timer> timers = new CopyOnWriteArrayList<>();

    @Override
    public Timer create(int interval, int iterations) throws IllegalArgumentException {
        if (interval < MIN_INTERVAL) {
            throw new IllegalArgumentException(String.format("Illegal argument for interval: Interval must be greater or equals to %sms", MIN_INTERVAL));
        }

        if (iterations < MIN_ITERATIONS) {
            throw new IllegalArgumentException(String.format("Illegal argument for iterations: Iterations must be greater %s", MIN_ITERATIONS - 1));
        }

        int id = timers.size();
        Timer t = new TimerImpl(id, interval, iterations);

        timers.add(t);

        logger.info(String.format("Timer created: %s", t.getInfo()));
        return t;
    }

    @Override
    public List<Timer> getAllTimers() {
        return timers;
    }

    @Override
    public List<Timer> getActiveTimers() {
        return timers.stream().filter(t -> t.isActive()).collect(Collectors.toList());
    }

    @Override
    public List<Timer> getInActiveTimers() {
        return timers.stream().filter(t -> !t.isActive()).collect(Collectors.toList());
    }
}
