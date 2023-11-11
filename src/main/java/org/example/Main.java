package org.example;

import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread(() -> {
                int countR = 0;
                int freq;
                char[] chars =  generateRoute("RLRFR", 100).toCharArray();
                for(char ch: chars){
                    if(ch == 'R'){
                        countR ++;
                    }
                }
                System.out.println("Количество повторений в этом потоке = " + countR);
                synchronized (sizeToFreq){
                    if(sizeToFreq.containsKey(countR)){
                        freq = sizeToFreq.get(countR);
                        freq ++;
                    } else {
                        freq = 1;
                    }
                    sizeToFreq.put(countR, freq);
                }
            });
            threads.add(thread);
            thread.start();
        }
        for(var th: threads){
            th.join();
        }
        int maxValueKey = getMaxValueKey();
        System.out.println("Самое частое количество повторений "+ maxValueKey + " (встретилось "+ sizeToFreq.get(maxValueKey) +" раз)");
        sizeToFreq.remove(maxValueKey);
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            System.out.println("- " + entry.getKey() + " (" + entry.getValue().toString() + " раз)");
        }

    }
    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
    public static int getMaxValueKey() {
       return sizeToFreq.entrySet().stream()
               .max(Comparator.comparingInt(Map.Entry::getValue))
               .get().getKey();
    }
}