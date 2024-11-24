public class InteractiveSleepProcess extends UserlandProcess{
    @Override
    public void main() {
        while(true)
        {
            System.out.println("ISP");
            System.out.println("ISP");
            System.out.println("ISP");
            System.out.println("ISP");

            try {
                Thread.sleep(50); // sleep for 50 ms
            } catch (Throwable e) {
                e.printStackTrace();
            }

            OS.Sleep(300);
        }
    }
}
