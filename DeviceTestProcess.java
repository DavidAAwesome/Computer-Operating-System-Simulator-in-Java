import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class DeviceTestProcess extends UserlandProcess{

    @Override
    public void main() {
        // This process tests all new Functions
        int randID = OS.Open("random 5");
        System.out.println("Random ID: " + randID);
        byte[] bytes = OS.Read(randID,10);
        System.out.println("Random byte array: " + Arrays.toString(bytes));
        OS.Seek(randID,10);

        int fileID = OS.Open("file cool");
        System.out.println("File ID: " + fileID);
        System.out.println("byte array written to fileID: " + Arrays.toString(bytes));
        System.out.println("size of array written to fileID: " + OS.Write(fileID, bytes));
        OS.Seek(fileID,0);
        System.out.println("byte array read from fileID" + Arrays.toString(OS.Read(fileID, 10)));

        int rand2ID = OS.Open("random 10");
        System.out.println("Random2 ID: " + rand2ID);
        byte[] bytes1 = OS.Read(rand2ID,5);
        System.out.println("Random2 byte1 array: " + Arrays.toString(bytes1));
        OS.Seek(randID,10);

        int file2ID = OS.Open("file cool2");
        System.out.println("File2 ID: " + file2ID);
        System.out.println("byte1 array written to file2ID: " + Arrays.toString(bytes1));
        System.out.println("size of array written to file2ID: " + OS.Write(file2ID, bytes1));
        OS.Seek(file2ID,0);
        System.out.println("byte1 array read from file2ID" + Arrays.toString(OS.Read(file2ID, 5)));
        OS.Close(randID);
        OS.Close(fileID);
        //Rand2 ID is closed by process ending
        //File2 ID is closed by process ending
        OS.Exit();
    }
}
