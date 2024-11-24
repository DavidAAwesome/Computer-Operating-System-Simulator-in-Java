public class Kernel extends Process implements Device{

    private final Scheduler scheduler = new Scheduler(this);
    private final VFS vfs = new VFS();
    private final Hardware hardware = new Hardware();
    private final boolean[] memoryUsed = new boolean[Hardware.PAGE_SIZE];

    @Override
    public void main() {
        while(true)
        {
            //Sleep is here to wait for the currentlyRunning process to be stopped by the OS to not cause bugs
            try {
                Thread.sleep(50);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            switch(OS.currentCall)
            {
                case CreateProcess:
                    UserlandProcess process = (UserlandProcess) OS.parameters.getFirst();
                    OS.returnValue = scheduler.CreateProcess(new PCB(process), (OS.Priority)OS.parameters.get(1));
                    break;
                case SwitchProcess:
                    hardware.clearTLB();
                    scheduler.SwitchProcess(false, false);
                    break;
                case Sleep:
                    scheduler.Sleep((int)OS.parameters.getFirst());
                    break;
                case Exit:
                    FreeAllMemory();
                    scheduler.SwitchProcess(true, false);
                    break;
                case Open:
                    OS.returnValue = Open((String) OS.parameters.getFirst());
                    break;
                case Close:
                    Close((int) OS.parameters.getFirst());
                    break;
                case Read:
                    OS.returnValue = Read((int) OS.parameters.getFirst(), (int) OS.parameters.get(1));
                    break;
                case Write:
                    OS.returnValue = Write((int) OS.parameters.getFirst(), (byte[]) OS.parameters.get(1));
                    break;
                case Seek:
                    Seek((int) OS.parameters.getFirst(), (int) OS.parameters.get(1));
                    break;
                case GetPID:
                    OS.returnValue = GetPid();
                    break;
                case GetPIDName:
                    OS.returnValue = GetPidByName((String)OS.parameters.getFirst());
                    break;
                case SendMessage:
                    SendMessage((KernelMessage)OS.parameters.getFirst());
                    break;
                case WaitForMessage:
                    OS.returnValue = WaitForMessage();
                    break;
                case GetMapping:
                    GetMapping((int)OS.parameters.getFirst());
                    break;
                case AllocateMemory:
                    OS.returnValue = AllocateMemory((int)OS.parameters.getFirst());
                    break;
                case FreeMemory:
                    OS.returnValue = FreeMemory((int)OS.parameters.getFirst(), (int) OS.parameters.get(1));
                    break;


            }

            if(scheduler.currentlyRunningPCB != null)
                scheduler.currentlyRunningPCB.start();
            stop();
        }
    }

    public UserlandProcess getCurrentlyRunning()
    {
        return scheduler.currentlyRunning;
    }

    @Override
    public int Open(String s) {
        for(int index = 0; index < scheduler.currentlyRunningPCB.devices.length; index ++)
        {
            if(scheduler.currentlyRunningPCB.devices[index] == -1)
            {
                int id = vfs.Open(s);
                if(id == -1)
                    return -1;
                scheduler.currentlyRunningPCB.devices[index] = id;
                System.out.println("Device PCB index is " + index);
                System.out.println("Device VFS index is " + id);
                return index;
            }
        }
        return -1;
    }

    @Override
    public void Close(int id) {
        int vfsID = scheduler.currentlyRunningPCB.devices[id];
        vfs.Close(vfsID);
        scheduler.currentlyRunningPCB.devices[id] = -1;
    }

    @Override
    public byte[] Read(int id, int size) {
        int vfsID = scheduler.currentlyRunningPCB.devices[id];
        return vfs.Read(vfsID, size);
    }

    @Override
    public void Seek(int id, int to) {
        int vfsID = scheduler.currentlyRunningPCB.devices[id];
        vfs.Seek(vfsID, to);
    }

    @Override
    public int Write(int id, byte[] data) {
        int vfsID = scheduler.currentlyRunningPCB.devices[id];
        return vfs.Write(vfsID, data);
    }

    public int GetPid()
    {
        return scheduler.GetPid();
    }

    public int GetPidByName(String name)
    {
        return scheduler.GetPidByName(name);
    }

    public void SendMessage(KernelMessage km)
    {
        scheduler.SendMessage(km);
    }

    public KernelMessage WaitForMessage()
    {
        return scheduler.WaitForMessage();
    }

    public void GetMapping(int virtualPageNumber)
    {
        PCB current = scheduler.currentlyRunningPCB;
        int physicalPage = current.pageTable[virtualPageNumber];
        if(physicalPage == -1)
        {
            System.out.println("Segmentation Fault on " + scheduler.currentlyRunning.getClass() + "!");
            FreeAllMemory();
            scheduler.SwitchProcess(true, false);
        }
        else
            hardware.setRandomTLB(virtualPageNumber, physicalPage);
    }

    public int AllocateMemory(int size) // – returns the start virtual address
    {
        int pageAmount = size/Hardware.PAGE_SIZE;
        PCB current = scheduler.currentlyRunningPCB;
        int startingIndex = current.spaceToAllocate(size);
        if(startingIndex != -1)
        {
            for(int i = startingIndex; i < startingIndex + pageAmount; i++)
            {
                int memoryLocation = getFreeMemoryLocation();
                if(memoryLocation != -1)
                    current.pageTable[i] = memoryLocation;
                else
                    return -1;
            }
            for(int i = 0; i < current.pageTable.length; i ++)
            {
                System.out.print(current.pageTable[i] + ", ");
            }
            System.out.println();
            return startingIndex * Hardware.PAGE_SIZE;
        }
        return -1;
    }

    public boolean FreeMemory(int pointer, int size) // – takes the virtual address and the amount to free
    {
        int page = pointer/Hardware.PAGE_SIZE;
        PCB current = scheduler.currentlyRunningPCB;
        int pageAmount = size/Hardware.PAGE_SIZE;
        for(int i = page; i < page + pageAmount; i++)
        {
            if(current.pageTable[i] != -1)
            {
                if(memoryUsed[current.pageTable[i]])
                    memoryUsed[current.pageTable[i]] = false;
                else
                    return false;
                if(!hardware.freeMemory(current.pageTable[i]))
                {
                    System.out.println("Problem with freeing physical memory");
                    return false;
                }
                System.out.println("Virtual Memory Freed: " + current.pageTable[i]);
                current.pageTable[i] = -1;

            }
            else
            {
                System.out.println("Problem with freeing virtual memory");
                return false;
            }

        }
        return true;
    }

    private int getFreeMemoryLocation()
    {
        for(int i = 0; i < memoryUsed.length; i++)
        {
            if(!memoryUsed[i])
            {
                memoryUsed[i] = true;
                System.out.println("Memory Usage Number: " + i);
                return i;
            }
        }
        return -1;
    }

    private void FreeAllMemory()
    {
        PCB current = scheduler.currentlyRunningPCB;
        for(int i = 0; i < current.pageTable.length; i++)
        {
            if(current.pageTable[i] != -1)
            {
                FreeMemory(i * Hardware.PAGE_SIZE, Hardware.PAGE_SIZE);
            }
        }
        for(int i = 0; i < current.pageTable.length; i ++)
        {
            System.out.print(current.pageTable[i] + ", ");
        }
    }
}
