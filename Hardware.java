import java.util.Random;

public class Hardware {

    public static final int PAGE_SIZE = 1024;
    public static final int MEMORY_SIZE = PAGE_SIZE * PAGE_SIZE;

    private static final byte[] memory = new byte[MEMORY_SIZE]; //1MB of physical data
    private static final int[][] TLB = {{-1,-1},{-1,-1}};
    private static final Random random = new Random();

    public static byte Read(int address)
    {
        int virtualPage = address / PAGE_SIZE;
        int offset = address % PAGE_SIZE;
        int physicalPage = virtualToPhysicalPage(virtualPage);
        int physicalAddress = physicalPage * PAGE_SIZE + offset;

        System.out.println("Read from physical address: " + physicalAddress);
        System.out.println("Read from physical page: " + physicalPage);
        System.out.println("Offset was: " + offset);
        return memory[physicalAddress];
    }

    public static void Write(int address, byte value)
    {
        int virtualPage = address / PAGE_SIZE;
        int offset = address % PAGE_SIZE;
        int physicalPage = virtualToPhysicalPage(virtualPage);
        int physicalAddress = physicalPage * PAGE_SIZE + offset;

        memory[physicalAddress] = value;
    }

    private static int virtualToPhysicalPage(int virtualPage)
    {
        while(true)
        {
            if(TLB[0][0] == virtualPage)
                return TLB[0][1];
            if(TLB[1][0] == virtualPage)
                return TLB[1][1];
            OS.GetMapping(virtualPage);
        }

    }


    public void setRandomTLB(int virtualPage, int physicalPage)
    {
        int position = random.nextInt(2);
        TLB[position][0] = virtualPage;
        TLB[position][1] = physicalPage;
    }


    public boolean freeMemory(int location)
    {
        for(int i = location; i < location + PAGE_SIZE; i ++)
        {
            if(memory[i] != -1)
                memory[i] = -1;

        }
        System.out.println("Physical memory freed starting from: " + location);
        return true;
    }


    public void clearTLB()
    {
        TLB[0][0] = -1;
        TLB[1][0] = -1;
        TLB[0][1] = -1;
        TLB[1][1] = -1;
    }


}
