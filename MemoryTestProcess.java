public class MemoryTestProcess extends UserlandProcess{
    @Override
    public void main() {
        int virtualMemory = OS.AllocateMemory(1024);
        System.out.println("Virtual Memory: " + virtualMemory);// Allocation testing

        int virtualMemory2 = OS.AllocateMemory(2048);
        System.out.println("Virtual Memory2: " + virtualMemory2);// Allocation testing

        int virtualMemory3 = OS.AllocateMemory(2048);
        System.out.println("Virtual Memory3: " + virtualMemory3);// Allocation testing

        boolean memory2freed = OS.FreeMemory(virtualMemory2, 2048);
        System.out.println("Memory2 Freed: " + memory2freed);// Memory Freeing Testing

        Hardware.Write(virtualMemory, (byte) 65); // Write testing
        byte readFromVirtualMemory = Hardware.Read(virtualMemory);
        System.out.println("Byte read for virtual memory: " + readFromVirtualMemory); // Read testing

        Hardware.Write(virtualMemory + 2, (byte) 70);// Write testing with offset
        readFromVirtualMemory = Hardware.Read(virtualMemory + 2);
        System.out.println("Byte read for virtual memory: " + readFromVirtualMemory); // Read testing with offset

        Hardware.Write(virtualMemory3 + 20, (byte) 75);// Write testing with offset
        byte readFromVirtualMemory3 = Hardware.Read(virtualMemory3 + 20);
        System.out.println("Byte read for virtual memory3: " + readFromVirtualMemory3); // Read testing with offset

        int virtualMemory4 = OS.AllocateMemory(5120);// Making sure it allocates to an address with enough space
        System.out.println("Virtual Memory4: " + virtualMemory4);// Allocation testing

        // Tests killing process from segmentation fault when uncommented
//        byte readFromVirtualMemory2 = Hardware.Read(virtualMemory2);

        //Exit is here to test Memory being freed on end of process
        OS.Exit();


    }
}
