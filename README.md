# SWT 6 Timer Module Exercise

## Timer & TimerService
The Timer can be used without the TimerService. It is recommended to use the TimerService, since it is responsible for validating iterations and interval of the timers.
Furthermore, the TimerService can hold and manage multiple instances of Timers.

## ServiceLoader
The client gets the concrete Timer implementation with a ServiceLoader.

## Thread safety
Thread safety should be given. Atomic data types and CopyOnWriteArrayLists are used to ensure it.

## Compile into single jar
To compile the two modules timer and timer-client into a single jar ``mvn install`` is used.
The .jar can be executed with ``java -jar fileName.jar``

## Run with powershell script
First, run ``mvn install``.
A powershell script ``start-timer-example.ps1`` is provided, in order to directly start the application from there.


## Time spent
~16h