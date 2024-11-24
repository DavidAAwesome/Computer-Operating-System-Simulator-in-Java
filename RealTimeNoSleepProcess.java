public class RealTimeNoSleepProcess extends UserlandProcess{
    @Override
    public void main() {
        while(true)
        {
            System.out.println("RTNSP");
            cooperate();

            try {
                Thread.sleep(50); // sleep for 50 ms
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
