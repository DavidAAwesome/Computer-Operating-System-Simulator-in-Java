import java.io.IOException;

public class VFS implements Device{

    DeviceConnection[] deviceConnections = new DeviceConnection[10];
    RandomDevice randomDevice = new RandomDevice();
    FakeFileSystem fakeFileSystem = new FakeFileSystem();

    @Override
    public int Open(String s) {
        Device device;
        int id;
        String[] input = s.split(" ");
        if(input[0].equalsIgnoreCase("random"))
        {
            device = randomDevice;
            id = device.Open(input[1]);
        }
        else if(input[0].equalsIgnoreCase("file"))
        {
            device = fakeFileSystem;
            id = device.Open(input[1]);
        }
        else
            return -1; //fail

        int index;
        for(index = 0; index < deviceConnections.length; index ++)
        {
            if(deviceConnections[index] == null)
            {
                deviceConnections[index] = new DeviceConnection(device,id);
                System.out.println("DeviceConnection index is: " + id);
                break;
            }
        }

        return index;
    }

    @Override
    public void Close(int id) {
        deviceConnections[id].device.Close(deviceConnections[id].id);
        deviceConnections[id] = null;
    }

    @Override
    public byte[] Read(int id, int size) {
        return deviceConnections[id].device.Read(deviceConnections[id].id, size);
    }

    @Override
    public void Seek(int id, int to) {
        deviceConnections[id].device.Seek(deviceConnections[id].id, to);
    }

    @Override
    public int Write(int id, byte[] data) {
        return deviceConnections[id].device.Write(deviceConnections[id].id, data);
    }
}
