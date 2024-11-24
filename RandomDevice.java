import java.util.Random;

public class RandomDevice implements Device{

    Random[] randomArray = new Random[10];

    @Override
    public int Open(String s) {
        Random random = new Random();
        if(!s.isEmpty() && s!= null)
            random.setSeed(Integer.parseInt(s));

        int index;
        for(index = 0; index < randomArray.length; index ++)
        {
            if(randomArray[index] == null)
            {
                randomArray[index] = random;
                break;
            }
        }
        return index;
    }

    @Override
    public void Close(int id) {
        randomArray[id] = null;
    }

    @Override
    public byte[] Read(int id, int size) {
        byte[] byteArray = new byte[size];
        randomArray[id].nextBytes(byteArray);
        return byteArray;
    }

    @Override
    public void Seek(int id, int to) {
        byte[] bytes = new byte[to];
        randomArray[id].nextBytes(bytes);
    }

    @Override
    public int Write(int id, byte[] data) {
        return 0;
    }
}
