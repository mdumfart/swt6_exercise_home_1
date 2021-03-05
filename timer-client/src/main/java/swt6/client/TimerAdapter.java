package swt6.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swt6.timer.util.TimerEvent;
import swt6.timer.interfaces.TimerListener;

public class TimerAdapter implements TimerListener {
    private static final Logger logger = LogManager.getLogger(TimerAdapter.class);

    @Override
    public void timerTicked(TimerEvent e) {
        logger.info(e.toString());
    }

    @Override
    public void timerElapsed(TimerEvent e) {
        logger.info(String.format("--- Timer [%s] elapsed ---", e.getId()));
    }
}
