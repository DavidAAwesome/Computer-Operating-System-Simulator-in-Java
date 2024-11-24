public class Idle extends UserlandProcess{

    public void main() {
        while(true)
        {
            cooperate();
            try {
                Thread.sleep(50);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
