import java.time.Clock;
import java.util.*;

public class Scheduler {

    private Kernel kernel;

    public UserlandProcess currentlyRunning;
    public PCB currentlyRunningPCB;

    private final LinkedList<PCB> realTimeQueue = new LinkedList<>();
    private final LinkedList<PCB> interactiveQueue = new LinkedList<>();
    private final LinkedList<PCB> backgroundQueue = new LinkedList<>();
    private final LinkedList<PCB> sleeping = new LinkedList<>();
    private final HashMap<Integer, PCB> allProcessesByPid = new HashMap<>();
    private final HashMap<String, PCB> allProcessesByName = new HashMap<>();
    private final HashMap<Integer, PCB> waiting = new HashMap<>();

    private final Timer timer = new Timer();
    private final Random random = new Random();
    private final Clock clock = Clock.systemUTC();

    public Scheduler(Kernel kernel)
    {
        this.kernel = kernel;
        TimerTask interrupt = new TimerTask() {
            @Override
            public void run() {
                if(currentlyRunning != null)
                    currentlyRunning.requestStop();
            }
        };
        timer.schedule(interrupt,250,250);
    }

    public int CreateProcess(PCB pcb, OS.Priority priority)
    {
        switch(priority) // Adds priority to correct queue
        {
            case RealTime:
                realTimeQueue.add(pcb);
                pcb.priority = OS.Priority.RealTime;
                break;
            case Interactive:
                interactiveQueue.add(pcb);
                pcb.priority = OS.Priority.Interactive;
                break;
            case Background:
                backgroundQueue.add(pcb);
                pcb.priority = OS.Priority.Background;
                break;
        }
        allProcessesByPid.put(pcb.pid,pcb);
        allProcessesByName.put(pcb.name,pcb);

        if(currentlyRunning == null) // If nothing else is running, call switch process to get started.
        {
            SwitchProcess(false, false);
        }

        return pcb.pid; //PID

    }

    public void SwitchProcess(boolean exiting, boolean waitingForMessage)
    {
        //Adds to list if currentlyRunning is not null and not done and not exiting and not sleeping and not waiting
        if(currentlyRunningPCB != null && !currentlyRunningPCB.isDone() && !exiting && currentlyRunningPCB.wakeUpTime == 0 && !waitingForMessage)
        {
            currentlyRunningPCB.timeOutCount++;
            if(currentlyRunningPCB.timeOutCount <= 5)
            {
                switch(currentlyRunningPCB.priority)
                {
                    case RealTime:
                        realTimeQueue.add(currentlyRunningPCB);
                        break;
                    case Interactive:
                        interactiveQueue.add(currentlyRunningPCB);
                        break;
                    case Background:
                        backgroundQueue.add(currentlyRunningPCB);
                        break;
                }
            }
            else
            {
                currentlyRunningPCB.timeOutCount = 0;
                switch(currentlyRunningPCB.priority)
                {
                    case RealTime:
                        currentlyRunningPCB.priority = OS.Priority.Interactive;
                        interactiveQueue.add(currentlyRunningPCB);
                        break;
                    case Interactive:
                        currentlyRunningPCB.priority = OS.Priority.Background;
                        backgroundQueue.add(currentlyRunningPCB);
                        break;
                    case Background:
                        backgroundQueue.add(currentlyRunningPCB);
                        break;
                }
            }
        }// Adds current to bottom of the list

        // If currently running process is done, close all devices
        if(currentlyRunningPCB != null && (currentlyRunningPCB.isDone() || exiting))
        {
            allProcessesByPid.remove(currentlyRunningPCB.pid);
            allProcessesByName.remove(currentlyRunningPCB.name);
            for(int i = 0; i < currentlyRunningPCB.devices.length; i ++)
            {
                if(currentlyRunningPCB.devices[i] != -1)
                {
                    kernel.Close(i);
                    System.out.println("Something closed automatically");
                }

            }
        }

        // Gives awoken processes a chance to run again
        if(!sleeping.isEmpty())
        {
            LinkedList<PCB> remove = new LinkedList<>();
            for(var pcb : sleeping)
            {
                //System.out.println(pcb.up.getClass() + ": " + (pcb.wakeUpTime <= clock.millis()));
                if(pcb.wakeUpTime <= clock.millis())
                {
                    pcb.wakeUpTime = 0;
                    remove.add(pcb);
                    switch(pcb.priority)
                    {
                        case RealTime:
                            realTimeQueue.add(pcb);
                            break;
                        case Interactive:
                            interactiveQueue.add(pcb);
                            break;
                        case Background:
                            backgroundQueue.add(pcb);
                            break;
                    }
                }
            }
            for(var pcb : remove)
            {
                sleeping.remove(pcb);
            }

        }

        //switches to a random queue based on priority
        while(true)
        {
            int randomNumber = random.nextInt(10);
            if (randomNumber < 6 && !realTimeQueue.isEmpty())// 6/10 chance
            {
                currentlyRunningPCB = realTimeQueue.pollFirst();
                currentlyRunning = currentlyRunningPCB.up;
                return;
            }
            else if (randomNumber < 9 && !interactiveQueue.isEmpty())// 3/10 chance
            {
                currentlyRunningPCB = interactiveQueue.pollFirst();
                currentlyRunning = currentlyRunningPCB.up;
                return;
            }
            else if(!backgroundQueue.isEmpty())// 1/10 chance
            {
                currentlyRunningPCB = backgroundQueue.pollFirst();
                currentlyRunning = currentlyRunningPCB.up;
                return;
            }
        }

        //Kernel starts the current process.
    }

    public void Sleep(int milliseconds)
    {
        currentlyRunningPCB.wakeUpTime = clock.millis() + milliseconds;
        currentlyRunningPCB.timeOutCount = 0;
        sleeping.add(currentlyRunningPCB);

        SwitchProcess(false, false);
    }

    public int GetPid()
    {
        return currentlyRunningPCB.pid;
    }

    public int GetPidByName(String name)
    {
        if(allProcessesByName.containsKey(name))
        {
            return allProcessesByName.get(name).pid;
        }
        return -1;/////////////////////////////////////////////
    }

    public void SendMessage(KernelMessage km)
    {
        PCB target = allProcessesByPid.get(km.targetPID);
        target.MessageQueue.add(km);
        waiting.remove(km.targetPID);
        switch(target.priority)
        {
            case RealTime:
                realTimeQueue.add(target);
                break;
            case Interactive:
                interactiveQueue.add(target);
                break;
            case Background:
                backgroundQueue.add(target);
                break;
        }
    }

    public KernelMessage WaitForMessage()
    {
        if(!currentlyRunningPCB.MessageQueue.isEmpty())
            return currentlyRunningPCB.MessageQueue.pollFirst();
        waiting.put(currentlyRunningPCB.pid,currentlyRunningPCB);
        SwitchProcess(false, true);
        return null;
    }






}
