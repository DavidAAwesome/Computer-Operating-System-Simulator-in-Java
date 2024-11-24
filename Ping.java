import java.util.Arrays;

public class Ping extends UserlandProcess{
    @Override
    public void main() {
        int myPID = OS.GetPid();
        int pongPID = OS.GetPidByName("Pong");
        System.out.println("I am PING, pong = " + pongPID);
        KernelMessage myMessage = new KernelMessage(pongPID);
        myMessage.message = -1;
        while(true)
        {
            myMessage.message ++;
            OS.SendMessage(myMessage);
            KernelMessage pongMessage = OS.WaitForMessage();
            System.out.println("PING: from " + pongPID + " to " + myPID + " what: " + pongMessage.message);
            OS.Sleep(50);
        }
    }
}
