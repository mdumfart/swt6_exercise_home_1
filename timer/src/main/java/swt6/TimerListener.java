package swt6;

import java.util.EventListener;

public interface TimerListener extends EventListener {
    void timerTicked(TimerEvent e);
    void timerElapsed(TimerEvent e);
}
