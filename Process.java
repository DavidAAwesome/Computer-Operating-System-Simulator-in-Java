
import java.lang.Thread;
import java.util.concurrent.Semaphore;

public abstract class Process implements Runnable {

    Thread thread = new Thread(this);
    Semaphore semaphore = new Semaphore(0);
    boolean quantumExpired = false;

    public Process()
    {
        this.thread.start();

    }

    public void requestStop() //– sets the boolean indicating that this process’ quantum has expired
    {
        this.quantumExpired = true;
    }
    public abstract void main(); //– will represent the main of our “program”

    public boolean isStopped() //– indicates if the semaphore is 0
    {
        return this.semaphore.availablePermits() == 0;
    }
    public boolean isDone() //– true when the Java thread is not alive
    {
        return !this.thread.isAlive();
    }
    public void start() //– releases (increments) the semaphore, allowing this thread to run
    {
        this.semaphore.release();
    }
    public void stop() //– acquires (decrements) the semaphore, stopping this thread from running
    {
        try {
            this.semaphore.acquire();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }
    public void run() //– acquire the semaphore, then call main
    {
        try {
            this.semaphore.acquire();
            main();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }
    public void cooperate() //– if the boolean is true, set the boolean to false and call OS.switchProcess()
    {
        if (this.quantumExpired) {
            this.quantumExpired = false;
            OS.SwitchProcess();
        }
    }
}