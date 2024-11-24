import java.util.Arrays;

public class MessengerTestProcess2 extends UserlandProcess{
    @Override
    public void main() {
        int myPID = OS.GetPid();
        int messengerTestProcessID = OS.GetPidByName("MessengerTestProcess");
        System.out.println("I am MessengerTestProcess2, MessengerTestProcess = " + messengerTestProcessID);
        KernelMessage myMessage = new KernelMessage(messengerTestProcessID);
        myMessage.message = -1;
        while(true)
        {
            myMessage.message ++;
            myMessage.data = new byte[]{60, 44, 90};
            OS.SendMessage(myMessage);
            KernelMessage Message = OS.WaitForMessage();
            System.out.print("MTP2: from " + messengerTestProcessID + " to " + myPID + " message: " + Message.message + ", ");
            System.out.println("Byte array gotten: " + Arrays.toString(Message.data));
            OS.Sleep(50);
        }
    }
}
