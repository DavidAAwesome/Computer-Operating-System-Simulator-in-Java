import java.util.Arrays;

public class Init extends UserlandProcess{

    @Override
    public void main()
    {
        // Processes commented for the sake of focusing testing to memory
//        OS.CreateProcess(new HelloWorld());
//        OS.CreateProcess(new GoodbyeWorld());
//        OS.CreateProcess(new RealTimeNoSleepProcess(), OS.Priority.RealTime);
//        OS.CreateProcess(new BackgroundSleepProcess(), OS.Priority.Background);
//        OS.CreateProcess(new InteractiveSleepProcess(), OS.Priority.Interactive);
//        OS.CreateProcess(new DeviceTestProcess());
//        OS.CreateProcess(new DeviceCooperateProcess());
//        OS.CreateProcess(new DeviceCooperateProcess2());
//        OS.CreateProcess(new Ping());
//        OS.CreateProcess(new Pong());
//        OS.CreateProcess(new MessengerTestProcess());
//        OS.CreateProcess(new MessengerTestProcess2());
        OS.CreateProcess(new MemoryTestProcess());
        OS.CreateProcess(new MemoryCooperateProcess());
        OS.CreateProcess(new MemoryCooperateProcess2());
        OS.Exit();
    }
}
