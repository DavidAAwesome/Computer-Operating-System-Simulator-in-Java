import java.util.Arrays;

public class DeviceCooperateProcess extends UserlandProcess{

    @Override
    public void main() {
        //This process uses random device at the same time as DeviceCooperateProcess2
        int randID = OS.Open("random 100");
        System.out.println("DeviceCooperateProcess ID: " + randID);
        while(true)
        {
            byte[] bytes = OS.Read(randID,10);
            System.out.println("Device Cooperate Process: " + Arrays.toString(bytes));
            cooperate();
        }
    }
}
