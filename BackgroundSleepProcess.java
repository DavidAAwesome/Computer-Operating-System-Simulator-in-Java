public class BackgroundSleepProcess extends UserlandProcess{
    @Override
    public void main() {
        while(true)
        {
            System.out.println("BGSP");
            System.out.println("BGSP");
            System.out.println("BGSP");
            System.out.println("BGSP");

            try {
                Thread.sleep(50); // sleep for 50 ms
            } catch (Throwable e) {
                e.printStackTrace();
            }

            OS.Sleep(200);
        }
    }
}
