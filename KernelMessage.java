import java.util.Arrays;

public class KernelMessage {

    int senderPID;
    int targetPID;
    int message;
    byte[] data;

    public KernelMessage(int targetPID)
    {
        this.targetPID = targetPID;
    }

    public KernelMessage (KernelMessage kernelMessage)
    {
        this.senderPID = kernelMessage.senderPID;
        this.targetPID = kernelMessage.targetPID;
        this.message = kernelMessage.message;
        this.data = kernelMessage.data;
    }

    @Override
    public String toString() {
        return "SenderPID: " + senderPID + "\nTargetPID: " + targetPID + "\nMessage: " + message + "\nData: " + Arrays.toString(data) + "\n";
    }
}
