import java.util.Arrays;

public class Pong extends UserlandProcess{
    @Override
    public void main() {
        int myPID = OS.GetPid();
        int pingPID = OS.GetPidByName("Ping");
        System.out.println("I am PONG, ping = " + pingPID);
        KernelMessage myMessage = new KernelMessage(pingPID);
        myMessage.message = -1;
        while(true)
        {
            KernelMessage pingMessage = OS.WaitForMessage();
            myMessage.message ++;
            OS.SendMessage(myMessage);
            System.out.println("PONG: from " + pingPID + " to " + myPID + " what: " + pingMessage.message);
            OS.Sleep(50);
        }
    }
}
