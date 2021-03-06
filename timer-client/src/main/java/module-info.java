import swt6.timer.interfaces.TimerServiceFactory;

module timer.client {
    requires timer;
    requires org.apache.logging.log4j;
    uses TimerServiceFactory;
}