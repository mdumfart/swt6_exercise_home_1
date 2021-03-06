import swt6.timer.factories.TimerServiceFactoryImpl;
import swt6.timer.interfaces.TimerServiceFactory;

module timer {
    exports swt6.timer.interfaces;
    exports swt6.timer.factories;
    exports swt6.timer.util;
    provides TimerServiceFactory
            with TimerServiceFactoryImpl;
    requires org.apache.logging.log4j;
}