import java.util.Arrays;

public class DeviceCooperateProcess2 extends UserlandProcess{

    @Override
    public void main() {
        //This process uses random device at the same time as DeviceCooperateProcess1
        int randID = OS.Open("random 25");
        System.out.println("Device Cooperate Process2 ID: " + randID);
        while(true)
        {
            byte[] bytes = OS.Read(randID,10);
            System.out.println("Device Cooperate Process2: " + Arrays.toString(bytes));
            cooperate();
        }
    }
}
