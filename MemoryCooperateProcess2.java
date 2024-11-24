public class MemoryCooperateProcess2 extends UserlandProcess{
    @Override
    public void main() {
        int virtualMemory = OS.AllocateMemory(2048);
        System.out.println("Virtual Memory: " + virtualMemory);// Allocation testing
        int virtualMemory2 = OS.AllocateMemory(2048);
        System.out.println("Virtual Memory2: " + virtualMemory2);// Allocation testing
        boolean memory2freed = OS.FreeMemory(virtualMemory2, 2048);
        System.out.println("Memory2 Freed: " + memory2freed);// Memory Freeing Testing
        OS.Sleep(20);
        int num = 0;
        int num2 = 1;
        while(true)
        {
            try {
                Thread.sleep(50);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            System.out.println("MemoryCooperateProcess2 //////////////////////////////////");
            Hardware.Write(virtualMemory, (byte) num++); // Write testing
            byte readFromVirtualMemory = Hardware.Read(virtualMemory);
            System.out.println("Byte read for virtual memory: " + readFromVirtualMemory); // Read testing

            Hardware.Write(virtualMemory + 1, (byte) num2++);// Write testing with offset
            readFromVirtualMemory = Hardware.Read(virtualMemory + 1);
            System.out.println("Byte read for virtual memory: " + readFromVirtualMemory); // Read testing with offset
            System.out.println("//////////////////////////////////////////////////////////");
            cooperate();
        }
    }
}
