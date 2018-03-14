package pool;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;

public class CustomThreadPool implements Executor {
    private final BlockingDeque<Runnable> queue = new LinkedBlockingDeque<>();
    private volatile boolean isRunning = true;


    private CustomThreadPool(int nThreads) {
        for (int i = 0; i < nThreads; i++) {
            Thread workerThread = new Thread(new CustomWorker());
            workerThread.setName("worker-" + i);
            workerThread.start();
        }
    }

    public static CustomThreadPool newCustomFixedThreadPool(int nThreads) {
        return new CustomThreadPool(nThreads);
    }


    public void shutdown() {
        this.isRunning = false;
        System.out.println("thread pool was shutdown");
    }

    @Override
    public void execute(Runnable command) {
        if (isRunning) {
            queue.offer(command);
            System.out.println("task is added to the queue");
        }
    }

    private final class CustomWorker implements Runnable {

        @Override
        public void run() {
            while (isRunning) {
                Runnable task = queue.poll();
                if (task != null) {
                    System.out.println("task is retrieved from the queue by " + Thread.currentThread().getName());
                    task.run();
                }
            }
        }
    }
}
