import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public class Main {
    public static final int AMOUNT_PRODUCT = 30;
    public static final int RANDOM_NUBER = 1000;
    public static final int TIMEOUT = 3;
    public static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(processors);
        LongAdder longAdder = new LongAdder();
        List<Long[]> listProceeds = createListProceeds();
        for (Long[] proceeds : listProceeds) {
            createThread(executorService, longAdder, proceeds);
        }
        executorService.awaitTermination(TIMEOUT, TimeUnit.SECONDS);
        System.out.println("Выручка по всем магазинам: " + longAdder.sum());
        executorService.shutdown();
    }

    public static List<Long[]> createListProceeds() {
        List<Long[]> listProceeds = Arrays.asList(new Long[AMOUNT_PRODUCT], new Long[AMOUNT_PRODUCT], new Long[AMOUNT_PRODUCT]);
        for (Long[] proceeds : listProceeds) {
            for (int i = 0; i < proceeds.length; i++) {
                proceeds[i] = (long) random.nextInt(RANDOM_NUBER);
            }
        }
        return listProceeds;
    }

    public static void createThread(ExecutorService executorService, LongAdder longAdder, Long[] listProceeds) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                for (Long proceeds : listProceeds) {
                    longAdder.add(proceeds);
                }
            }
        };
        executorService.submit(thread);
    }
}