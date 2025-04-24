import java.util.ArrayList;

public class OS {

    private static Kernel kernel;
    public static CallType currentCall;
    public static ArrayList<Object> parameters = new ArrayList<>();
    public static Object returnValue;



    public static void Startup(UserlandProcess init)
    {
        kernel = new Kernel();
        CreateProcess(new Idle(),Priority.Background);
        CreateProcess(init);
    }

    public static int CreateProcess(UserlandProcess up)
    {
        parameters.clear();
        parameters.add(up);
        parameters.add(Priority.Interactive);
        SwitchToKernel(CallType.CreateProcess);
        while(returnValue == null)
        {
            try {
                Thread.sleep(10);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return (Integer) CleanUp();
    }

    public static int CreateProcess(UserlandProcess up, Priority priority)
    {
        parameters.clear();
        parameters.add(up);
        parameters.add(priority);
        SwitchToKernel(CallType.CreateProcess);
        while(returnValue == null)
        {
            try {
                Thread.sleep(10);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return (Integer) CleanUp();
    }

    // Method is used whenever switching to the kernel is needed
    private static void SwitchToKernel(CallType callType)
    {
        currentCall = callType;
        kernel.start();
        if (kernel.getCurrentlyRunning() != null)
            kernel.getCurrentlyRunning().stop();
    }

    // Method is useful for clearing the return value and parameters to not affect future uses of OS
    private static Object CleanUp()
    {
        parameters.clear();
        Object temp = returnValue;
        returnValue = null;
        return temp;
    }

    public static void SwitchProcess()
    {
        SwitchToKernel(CallType.SwitchProcess);
        CleanUp();
    }

    public static void Sleep(int milliseconds)
    {
        parameters.clear();
        parameters.add(milliseconds);
        SwitchToKernel(CallType.Sleep);
        CleanUp();
    }

    public static void Exit()
    {
        SwitchToKernel(CallType.Exit);
        CleanUp();
    }

    public static int Open(String s)
    {
        parameters.clear();
        parameters.add(s);
        SwitchToKernel(CallType.Open);
        return (Integer) CleanUp();
    }

    public static void Close(int id)
    {
        parameters.clear();
        parameters.add(id);
        SwitchToKernel(CallType.Close);
        CleanUp();
    }

    public static byte[] Read(int id, int size)
    {
        parameters.clear();
        parameters.add(id);
        parameters.add(size);
        SwitchToKernel(CallType.Read);
        return (byte[]) CleanUp();
    }

    public static int Write(int id, byte[] data)
    {
        parameters.clear();
        parameters.add(id);
        parameters.add(data);
        SwitchToKernel(CallType.Write);
        return (Integer) CleanUp();
    }

    public static void Seek(int id, int to)
    {
        parameters.clear();
        parameters.add(id);
        parameters.add(to);
        SwitchToKernel(CallType.Seek);
        CleanUp();
    }

    public static int GetPid() // - returns the current process' pid
    {
        SwitchToKernel(CallType.GetPID);
        return (Integer) CleanUp();
    }

    public static int GetPidByName(String name) // – returns the pid of a process with that name.
    {
        parameters.clear();
        parameters.add(name);
        SwitchToKernel(CallType.GetPIDName);
        return (Integer) CleanUp();
    }

    public static void SendMessage(KernelMessage km)
    {
        km.senderPID = GetPid();
        parameters.clear();
        parameters.add(new KernelMessage(km));
        SwitchToKernel(CallType.SendMessage);
        CleanUp();
    }

    public static KernelMessage WaitForMessage()
    {
        // Because of CleanUp, returnValue will be null, meaning that if a message is not gotten, it will switch processes
        do {
            SwitchToKernel(CallType.WaitForMessage);
        } while(returnValue == null);

        return (KernelMessage) CleanUp();
    }

    public static void GetMapping(int virtualPageNumber)
    {
        parameters.clear();
        parameters.add(virtualPageNumber);
        SwitchToKernel(CallType.GetMapping);
        CleanUp();
    }

    public static int AllocateMemory(int size) // – returns the start virtual address
    {
        if(size % Hardware.PAGE_SIZE != 0)
            return -1;//Failure
        parameters.clear();
        parameters.add(size);
        SwitchToKernel(CallType.AllocateMemory);
        return (Integer) CleanUp();
    }

    public static boolean FreeMemory(int pointer, int size) // – takes the virtual address and the amount to free
    {
        if(size % Hardware.PAGE_SIZE != 0 || pointer % Hardware.PAGE_SIZE != 0)
            return false;//Failure
        parameters.clear();
        parameters.add(pointer);
        parameters.add(size);
        SwitchToKernel(CallType.FreeMemory);
        return (boolean) CleanUp();
    }



    public enum CallType
    {
        //Processes
        CreateProcess, SwitchProcess, Sleep, Exit,
        //File System
        Open, Read, Write, Close, Seek,
        //Messaging
        GetPID, GetPIDName, SendMessage, WaitForMessage,
        //Paging
        GetMapping, AllocateMemory, FreeMemory,

    }

    public enum Priority
    {
        RealTime, Interactive, Background
    }
}
