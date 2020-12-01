import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadTest {
    @Test
    public void printTest() {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            Future<?> submit = executorService.submit(new PrintRunnable());
            System.out.println(submit.isDone());

        }
    }

}

class PrintRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println(11111111);
    }
}
