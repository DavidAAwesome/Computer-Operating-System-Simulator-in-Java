import java.lang.Thread;
import java.util.Arrays;
import java.util.LinkedList;

public class PCB {
    private static int nextpid = 0;
    public int pid;

    public UserlandProcess up;
    public OS.Priority priority;
    public int timeOutCount = 0;
    public long wakeUpTime = 0;

    public int[] devices = new int[10];

    public String name;
    LinkedList<KernelMessage> MessageQueue = new LinkedList<>();

    public int[] pageTable = new int[100];

    public PCB(UserlandProcess up) /* creates thread, sets pid */
    {
        this.up = up;
        pid = nextpid++;
        name = up.getClass().getSimpleName();
        Arrays.fill(devices, -1);
        Arrays.fill(pageTable, -1);
    }
    public void stop() /* calls userlandprocess’ stop. Loops with Thread.sleep() until ulp.isStopped() is true.  */
    {
        up.stop();
        while(!up.isStopped())
        {
            try {
                Thread.sleep(10);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    boolean isDone() /* calls userlandprocess’ isDone() */
    {
        //call isDone on up
        return up.isDone();
    }
    void start() /* calls userlandprocess’ start() */
    {
        //call start on up
        up.start();
    }

    public int spaceToAllocate(int size) {
        int pageAmount = size / Hardware.PAGE_SIZE;
        for (int i = 0; i < pageTable.length - pageAmount; i++) {
            boolean enoughSpace = true;
            for (int j = i; j < i + pageAmount; j++) {
                if (pageTable[j] != -1)
                    enoughSpace = false;
            }
            if (enoughSpace)
            {
                System.out.println("Space To Allocate starting index: " + i);
                return i;
            }

        }
        return -1;// Not enough space to allocate page number
    }







}
