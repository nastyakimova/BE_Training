package queue;

import java.util.concurrent.BlockingQueue;

public class QueueConsumer implements Runnable {
    private BlockingQueue<Message> queue;

    public QueueConsumer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Message msg;
            while ((msg = queue.take()).getMsg() != "exit") {
                Thread.sleep(10);
                System.out.println(msg.getMsg());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
