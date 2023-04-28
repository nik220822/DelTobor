import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int lengthOfTheRoute = 100;
        int routesNumber = 1000;
        Thread printMax = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    Optional<Map.Entry<Integer, Integer>> maxOptional = sizeToFreq.entrySet()
                            .stream()
                            .max(Map.Entry.comparingByValue());
                    Map.Entry<Integer, Integer> max = maxOptional.get();
                    System.out.println("Самое частое количество поворотов направо на маршруте: " + max.getKey() + " (встретилось " + max.getValue() + " раз)");
                }
            }
        });
        printMax.start();
        for (int i = 0; i < routesNumber; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String route = generateRoute("RLRFR", lengthOfTheRoute);
                    int n = (int) route.chars()
                            .filter(ch -> ch == 'R')
                            .count();
                    synchronized (sizeToFreq) {
                        if (sizeToFreq.containsKey(n)) {
                            sizeToFreq.put(n, sizeToFreq.get(n) + 1);
                        } else {
                            sizeToFreq.put(n, 1);
                        }
                        sizeToFreq.notify();
                    }
                }
            });
            thread.start();
            thread.join();
        }
        printMax.interrupt();
        printMax.join();
        System.out.println("Другие количества поворотов направо на маршруте:");
        sizeToFreq.forEach((key, value) -> System.out.println("Количество поворотов направо на маршруте: " + key + ". Количество маршрутов с таким числом поворотов направо: " + value));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
