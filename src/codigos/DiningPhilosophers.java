package codigos;


import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class DiningPhilosophers {

    static Scanner sc;
    static int n;

    static Semaphore table;
    static Semaphore[] sticks;
    static Philosopher[] philosophers;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        n = sc.nextInt();
        table = new Semaphore(n - 1);
        sticks = new Semaphore[n];
        philosophers = new Philosopher[n];
        //Init Sticks
        for (int i = 0; i < n; i++) {
            sticks[i] = new Semaphore(1);
        }
        //Init Philosophers
        for (int i = 0; i < n; i++) {
            philosophers[i] = new Philosopher(i);
            Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
            t.start();
        }

    }

    static class Philosopher extends Thread {

        Integer philosopher;

        Philosopher(Integer i) {
            this.philosopher = i;
        }

        void think() throws InterruptedException {
            doAction("thinking");
        }

        void eat() throws InterruptedException {
            doAction("eating");
        }

        void doAction(String action) throws InterruptedException {
            System.out.println("Philosopher " + (philosopher + 1) + " is : " + action);
            Thread.sleep(((int) (Math.random() * 100)));
        }

        @Override
        public void run() {
            try {
                Integer left = philosopher;
                Integer right = (philosopher + 1) % n;
                while (true) {

                    think();
                    synchronized (table) {
                        table.acquire();
                        synchronized (left) {
                            sticks[left].acquire();
                            synchronized (right) {
                                sticks[right].acquire();
                                eat();
                                sticks[right].release();
                            }
                            sticks[left].release();
                        }
                        table.release();
                    }

                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
