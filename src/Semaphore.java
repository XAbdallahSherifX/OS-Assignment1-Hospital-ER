public class Semaphore {
    protected int value = 0;

    public Semaphore() {
        value = 0;
    }

    public Semaphore(int initial) {
        value = initial;
    }

    public synchronized void P() {
        value--;
        if (value < 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized boolean P(long timeout) {
        value--;
        if (value < 0) {
            long startTime = System.currentTimeMillis();
            try {
                wait(timeout);
            } catch (InterruptedException e) {

            }
            long elapsedTime = System.currentTimeMillis() - startTime;

            if (elapsedTime >= timeout) {
                value++;
                return false;
            }
        }
        return true;
    }

    public synchronized void V() {
        value++;
        if (value <= 0) {
            notify();
        }
    }

    public synchronized int getValue() {
        return value;
    }
}