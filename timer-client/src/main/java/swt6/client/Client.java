package swt6.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import swt6.timer.interfaces.Timer;
import swt6.timer.interfaces.TimerService;
import swt6.timer.interfaces.TimerServiceFactory;

import java.util.List;
import java.util.Scanner;
import java.util.ServiceLoader;

public class Client {
    private static boolean isRunning = true;
    private static TimerService timerService = getTimerService();

    private static Scanner scanner = new Scanner(System.in);
    private final static Logger logger = LogManager.getLogger(Client.class);


    public static void main(String[] args) {

        printCommands();

        while(isRunning) {
            computeInput(scanner.nextLine());
        }
    }

    private static void computeInput(String cmd) {
        switch (cmd) {
            case "/create":
                computeCreate();
                break;
            case "/start":
                computeStart();
                break;
            case "/stop":
                computeStop();
                break;
            case "/change":
                computeChange();
                break;
            case "/reset":
                computeReset();
                break;
            case "/list":
                computeList();
                break;
            case "/quit":
                computeQuit();
                break;
            default:
                computeUnknownCommand(cmd);
        }
    }

    private static void computeCreate() {
        boolean created = false;
        Timer t = null;

        // read interval
        do {
            System.out.println("Enter interval in ms and number of iterations > ");
            System.out.println("Format: [interval], [iterations]");

            String inputCreate = scanner.nextLine();
            inputCreate = inputCreate.strip();

            try {
                // parse values
                int commaIndex = inputCreate.indexOf(',');
                if (commaIndex < 0) throw new IllegalArgumentException("Invalid format specified");

                int interval = Integer.parseInt(inputCreate.substring(0, commaIndex));
                int iterations = Integer.parseInt(inputCreate.substring(commaIndex + 1).strip());

                // create timer
                t = timerService.create(interval, iterations);
                created = true;
            } catch (Exception e) {
                logger.error(e);
            }
        } while (!created);


        if (t != null) {
            // add listener
            t.addTimerListener(new TimerAdapter());

            // read start timer input
            String startTimerCmd;
            do {
                System.out.println("Start timer now? (y, n) >");
                startTimerCmd = scanner.nextLine();
            } while (!startTimerCmd.equals("y") && !startTimerCmd.equals("n"));

            if (startTimerCmd.equals("y")) t.start();
        }
    }

    private static void computeStart() {
        List<Timer> inactiveTimers = timerService.getInActiveTimers();

        System.out.println("==================================================");

        if (inactiveTimers.size() == 0) {
            logger.info("There are no inactive timers");
            System.out.println("==================================================");
            return;
        }

        printStartStopList(inactiveTimers);

        System.out.println("==================================================");

        Timer t = getTimerFromInput();

        try {
            if (t != null) {
                t.start();
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private static void computeStop() {
        List<Timer> activeTimers = timerService.getActiveTimers();

        System.out.println("==================================================");

        if (activeTimers.size() == 0) {
            logger.info("There are no active timers");
            System.out.println("==================================================");
            return;
        }

        printStartStopList(activeTimers);

        System.out.println("==================================================");

        Timer t = getTimerFromInput();

        try {
            if (t != null) {
                t.stop();
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private static void computeChange() {
        computeList();

        Timer t = getTimerFromInput();

        String changeCmd;
        do {
            System.out.println("Do you want to change maximum iterations (iter) or interval (inter)? >");

            changeCmd = scanner.nextLine();
        } while (!changeCmd.equals("iter") && !changeCmd.equals("inter"));

        if (changeCmd.equals("iter"))
            computeChangeIterations(t);
        else
            computeChangeInterval(t);

    }

    private static void computeChangeIterations(Timer t) {
        boolean successfulChange = false;

        do {
            System.out.println(t.getInfo());
            System.out.println("Enter new iteration amount  >");

            try {
                int iterations = Integer.parseInt(scanner.nextLine());
                timerService.changeTimerIterations(t, iterations);

                successfulChange = true;
                logger.info("Timer updated:");
                logger.info(t.getInfo());
            } catch (Exception e) {
                logger.error(e);
            }

        } while (!successfulChange);
    }

    private static void computeChangeInterval(Timer t) {
        boolean successfulChange = false;

        do {
            System.out.println(t.getInfo());
            System.out.println("Enter new interval in ms (>50ms)  >");

            try {
                int interval = Integer.parseInt(scanner.nextLine());
                timerService.changeTimerInterval(t, interval);

                successfulChange = true;
                logger.info("Timer updated:");
                logger.info(t.getInfo());
            } catch (Exception e) {
                logger.error(e);
            }

        } while (!successfulChange);
    }

    private static void computeReset() {
        computeList();

        Timer t = getTimerFromInput();

        try {
            if (t != null) {
                t.reset();
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private static void computeList() {
        List<Timer> timers = timerService.getAllTimers();

        System.out.println("==================================================");

        if (timers.size() == 0) {
            logger.info("No timers instantiated");
        }
        else {
            for (Timer t : timers) {
                logger.info(t.getInfo() + ", " + (t.isActive() ? "Active": "Inactive"));
            }
        }

        System.out.println("==================================================");
    }

    private static void computeQuit() {
        List<Timer> timers = timerService.getAllTimers();

        for (Timer t : timers) {
            if (t.isActive())
                t.stop();
        }

        isRunning = false;
    }

    private static void printStartStopList(List<Timer> l) {
        for (Timer t : l) {
            logger.info(t.getInfo() + ", " + (t.isActive() ? "Active": "Inactive"));
        }
    }

    private static Timer getTimerFromInput() {
        boolean validId = false;
        int id = -1;
        Timer t = null;

        do {
            System.out.println("Enter timer id >");
            String inputStart = scanner.nextLine();

            try {
                id = Integer.parseInt(inputStart.strip());

                t = timerService.getAllTimers().get(id);
                validId = true;
            } catch (Exception e) {
                if (e instanceof IndexOutOfBoundsException) {
                    logger.error(String.format("Timer with Id [%s] not found", id));
                } else {
                    logger.error(e);
                }
            }
        } while (!validId);

        return t;
    }

    private static void printCommands() {
        System.out.println("Available commands:");
        System.out.println("/create, /start, /stop, /change, /reset, /list, /quit");
    }

    private static void computeUnknownCommand(String cmd) {
        System.out.printf("Unknown command: %s%n", cmd);
        printCommands();
    }

    private static TimerService getTimerService () {
        TimerService t = null;

        ServiceLoader<TimerServiceFactory> loader = ServiceLoader.load(TimerServiceFactory.class);

        for (TimerServiceFactory tsf : loader)
            if (tsf.providesFeature("TimerServiceImpl"))
                t = tsf.getTimerService();

        return t;
    }
}