import java.util.Arrays;

public class MessengerTestProcess extends UserlandProcess{
    @Override
    public void main() {
        int myPID = OS.GetPid();
        int messengerTestProcess2ID = OS.GetPidByName("MessengerTestProcess2");
        System.out.println("I am MessengerTestProcess, MessengerTestProcess2 = " + messengerTestProcess2ID);
        KernelMessage myMessage = new KernelMessage(messengerTestProcess2ID);
        myMessage.message = -1;
        while(true)
        {
            myMessage.message ++;
            myMessage.data = new byte[]{90, 44, 60};
            OS.SendMessage(myMessage);
            KernelMessage Message = OS.WaitForMessage();
            System.out.print("MTP1: from " + messengerTestProcess2ID + " to " + myPID + " message: " + Message.message + ", ");
            System.out.println("Byte array gotten: " + Arrays.toString(Message.data));
            OS.Sleep(50);
        }
    }
}
