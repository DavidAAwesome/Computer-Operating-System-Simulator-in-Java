import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FakeFileSystem implements Device {

    RandomAccessFile[] randomAccessFiles = new RandomAccessFile[10];

    @Override
    public int Open(String s) {

        if(s.isEmpty() || s == null)
            throw new IllegalArgumentException("Need a name for the file!");

        RandomAccessFile file;
        try {
            file = new RandomAccessFile(s, "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int index;
        for(index = 0; index < randomAccessFiles.length; index ++)
        {
            if(randomAccessFiles[index] == null)
            {
                randomAccessFiles[index] = file;
                break;
            }
        }
        return index;
    }

    @Override
    public void Close(int id) {
        try {
            randomAccessFiles[id].close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        randomAccessFiles[id] = null;
    }

    @Override
    public byte[] Read(int id, int size) {
        byte[] byteArray = new byte[size];
        try {
            randomAccessFiles[id].read(byteArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return byteArray;
    }

    @Override
    public void Seek(int id, int to) {
        try {
            randomAccessFiles[id].seek(to);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int Write(int id, byte[] data) {
        try {
            randomAccessFiles[id].write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data.length;
    }
}
