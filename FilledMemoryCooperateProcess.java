public class FilledMemoryCooperateProcess extends UserlandProcess{
    @Override
    public void main() {
        //Fills every space in this pcb to test filled memory
        int virtualMemory = OS.AllocateMemory(100 * Hardware.PAGE_SIZE);
        System.out.println("Virtual Memory: " + virtualMemory);// Tests if allocating still works
        boolean memory2freed = OS.FreeMemory(99 * Hardware.PAGE_SIZE, Hardware.PAGE_SIZE);
        System.out.println("Memory2 Freed: " + memory2freed);// Tests if freeing still works
        OS.Sleep(20);
        int num = 0;
        while(true)
        {
            try {
                Thread.sleep(50);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            for(int i = 0; i < 99; i ++)
            {
                Hardware.Write(i * Hardware.PAGE_SIZE, (byte) num); // Write testing
                byte readFromVirtualMemory = Hardware.Read(i * Hardware.PAGE_SIZE);
                System.out.println("Byte read from virtual memory: " + readFromVirtualMemory); // Read testing
                System.out.println("Num: " + num);
                num++;
                // If statement added to keep the int and the byte the same number,
                // because when int goes above 128, the byte becomes negative.
                if(num>=127)
                    num = 0;
                System.out.println("//");
            }
            cooperate();
        }
    }
}
