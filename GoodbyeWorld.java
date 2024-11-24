public class GoodbyeWorld extends UserlandProcess{

    @Override
    public void main()
    {
        while(true)
        {
            cooperate();
            System.out.println("Goodbye World");

            try {
                Thread.sleep(50); // sleep for 50 ms
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }
}
