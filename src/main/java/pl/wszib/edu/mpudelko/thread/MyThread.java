package pl.wszib.edu.mpudelko.thread;

import pl.wszib.edu.mpudelko.App;

import java.util.Random;

public class MyThread implements Runnable{
    private int threadId;
    private long points;
    private int pointsOnTarget;

    public MyThread() {
    }

    public MyThread(int threadId, long points) {
        this.threadId = threadId;
        this.points = points;
        pointsOnTarget = 0;
    }

    @Override
    public void run() {
        Random random = new Random();
        double x, y;

        for (int i = 0; i < points; i++) {
            x = (random.nextDouble() * 2) - 1;
            y = (random.nextDouble() * 2) - 1;

            if (isHitInCircle(x, y)) {
                pointsOnTarget++;
            }
        }
        System.out.println("On target: " + pointsOnTarget + " by thread " + threadId);
        synchronized (App.pointsInCircleSumList) {
            App.pointsInCircleSumList.add(pointsOnTarget);
        }
        App.latch.countDown();
    }
    private boolean isHitInCircle(double x, double y){
        return Math.sqrt((x*x)+(y*y)) <= 1;
    }

}
