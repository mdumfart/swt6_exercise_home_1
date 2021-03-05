package swt6.timer.interfaces;

import swt6.timer.util.TimerEvent;

import java.util.EventListener;

public interface TimerListener extends EventListener {
    void timerTicked(TimerEvent e);
    void timerElapsed(TimerEvent e);
}
