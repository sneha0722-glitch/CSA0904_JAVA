public class ScannerStationsDemo {
    static class StationThread extends Thread {
        private final int station;
        StationThread(int station) { this.station = station; }
        public void run() { process(); }
        private void process() {
            for (int batch = 1; batch <= 3; batch++) {
                System.out.println(getName() + " processed batch " + batch);
                try { Thread.sleep(100); }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(getName() + " interrupted.");
                    return;
                }
            }
        }
    }
    static class StationTask implements Runnable {
        private final int station;
        StationTask(int station) { this.station = station; }
        public void run() {
            for (int batch = 1; batch <= 3; batch++) {
                System.out.println("Runnable station " + station + " batch " + batch);
                try { Thread.sleep(100); }
                catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread[] workers = new Thread[4];
        boolean useRunnable = args.length > 0 && args[0].equalsIgnoreCase("runnable");
        for (int i = 0; i < workers.length; i++) {
            workers[i] = useRunnable
                ? new Thread(new StationTask(i + 1), "Station-" + (i + 1))
                : new StationThread(i + 1);
            workers[i].start();
            System.out.println(workers[i].getName() + " alive after start: " + workers[i].isAlive());
        }
        for (Thread worker : workers) worker.join();
        System.out.println("All scanner stations completed.");
    }
}
/*
Compile: javac ScannerStationsDemo.java
Run Thread subclass: java ScannerStationsDemo
Run Runnable: java ScannerStationsDemo runnable
start() asks the JVM to schedule a new thread; calling run() directly
does not create a new thread. join() waits for completion. Output order
is scheduler-dependent. Typical lifecycle: NEW -> RUNNABLE -> (BLOCKED/
WAITING/TIMED_WAITING as needed) -> TERMINATED. Priority is only a hint,
not a guaranteed order. Daemon threads do not keep the JVM alive.
ExecutorService is preferred for production pools and task management.
*/