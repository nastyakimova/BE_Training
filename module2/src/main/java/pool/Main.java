package pool;

public class Main {
    public static void main(String[] args) {
        CustomThreadPool threadPool = CustomThreadPool.newCustomFixedThreadPool(4);
        for (int i = 0; i < 100; i++) {
            threadPool.execute(() -> System.out.println("Hello"));
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPool.shutdown();
    }
}
