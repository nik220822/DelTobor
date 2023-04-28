import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        int lengthOfTheRout = 100;
        int routesNumber = 1000;
        for (int i = 0; i < routesNumber; i++) {
//            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String route = generateRoute("RLRFR", lengthOfTheRout);
                    int n = (int) route.chars()
                            .filter(ch -> ch == 'R')
                            .count();
                    synchronized (sizeToFreq) {
                        if (sizeToFreq.containsKey(n)) {
                            sizeToFreq.put(n, sizeToFreq.get(n) + 1);
                        } else {
                            sizeToFreq.put(n, 1);
                        }
                    }
                }
            }).start();
        }
        Optional<Map.Entry<Integer, Integer>> maxOptional = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());
        Map.Entry<Integer, Integer> max = maxOptional.get();
        System.out.println("Самое частое количество поворотов направо на маршруте: " + max.getKey() + " (встретилось " + max.getValue() + " раз)");
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
