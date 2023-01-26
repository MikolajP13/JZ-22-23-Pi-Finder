package pl.wszib.edu.mpudelko;

import pl.wszib.edu.mpudelko.thread.MyThread;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    public static final List<Integer> pointsInCircleSumList = new ArrayList<>();
    public static CountDownLatch latch;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        BigDecimal bigDecimal;
        int numberOfThreads;
        long onTargetSum;
        long totalNumberOfPoints;

        System.out.print("Enter number of threads: ");
        numberOfThreads = scanner.nextInt();
        latch = new CountDownLatch(numberOfThreads);

        System.out.print("Enter total number of points: ");
        totalNumberOfPoints = scanner.nextLong();

        ExecutorService executor = Executors.newFixedThreadPool(5);
        long pointsPerThread = totalNumberOfPoints/numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            if ((i == numberOfThreads - 1) && (numberOfThreads * pointsPerThread != totalNumberOfPoints)) { // odd number of threads -> alignment
                executor.submit(new MyThread(i + 1, pointsPerThread + totalNumberOfPoints - (pointsPerThread * numberOfThreads)));
            } else {
                executor.submit(new MyThread(i + 1, pointsPerThread));
            }
        }
        executor.shutdown();

        try{
            latch.await();
        }catch (InterruptedException e){

        }

        onTargetSum = pointsInCircleSumList.stream()
                .mapToLong(l -> l)
                .sum();

        bigDecimal = new BigDecimal(onTargetSum);
        bigDecimal = bigDecimal.multiply(BigDecimal.valueOf(4));
        bigDecimal = bigDecimal.divide(BigDecimal.valueOf(totalNumberOfPoints), MathContext.DECIMAL128);
        System.out.println("Calculated pi value: " + bigDecimal);
    }
}