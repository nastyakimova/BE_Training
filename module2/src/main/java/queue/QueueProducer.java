package queue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class QueueProducer implements Runnable {
    private BlockingQueue<Message> queue;

    public QueueProducer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(new Random().nextInt(500));
                queue.add(new Message("element " + i));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(10);
            queue.add(new Message("exit"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
