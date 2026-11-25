import java.util.concurrent.*;

class Wallet {
    private int balance;
    Wallet(int balance) { this.balance = balance; }
    int getBalance() { return balance; }

    // Always lock wallets in ascending identity-hash order to avoid circular wait.
    static void transfer(Wallet from, Wallet to, int amount) {
        Wallet first = System.identityHashCode(from) < System.identityHashCode(to) ? from : to;
        Wallet second = first == from ? to : from;
        synchronized (first) {
            synchronized (second) {
                if (amount < 0 || from.balance < amount) {
                    System.out.println("Transfer rejected.");
                    return;
                }
                from.balance -= amount;
                to.balance += amount;
            }
        }
    }
}
class TokenCounter {
    private final int[] buffer = new int[5];
    private int count = 0, in = 0, out = 0;

    public synchronized void put(int token) throws InterruptedException {
        while (count == buffer.length) wait();
        buffer[in] = token;
        in = (in + 1) % buffer.length;
        count++;
        notifyAll();
    }
    public synchronized int take() throws InterruptedException {
        while (count == 0) wait();
        int token = buffer[out];
        out = (out + 1) % buffer.length;
        count--;
        notifyAll();
        return token;
    }
}
public class ConcurrencyDemo {
    public static void main(String[] args) throws InterruptedException {
        Wallet a = new Wallet(1000), b = new Wallet(1000);
        Thread t1 = new Thread(() -> { for (int i=0;i<1000;i++) Wallet.transfer(a,b,1); });
        Thread t2 = new Thread(() -> { for (int i=0;i<1000;i++) Wallet.transfer(b,a,1); });
        t1.start(); t2.start(); t1.join(); t2.join();
        System.out.println("Wallet total: " + (a.getBalance() + b.getBalance()));

        TokenCounter queue = new TokenCounter();
        int tokens = 20;
        int[] sum = {0};
        Thread producer = new Thread(() -> {
            try { for (int i=1;i<=tokens;i++) queue.put(i); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        Thread consumer = new Thread(() -> {
            try { for (int i=1;i<=tokens;i++) synchronized (sum) { sum[0] += queue.take(); } }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        });
        producer.start(); consumer.start(); producer.join(); consumer.join();
        System.out.println("Sum of tokens collected: " + sum[0] + " (expected 210)");
    }
}
/*
Deadlock requires mutual exclusion, hold-and-wait, no pre-emption, and
circular wait. Consistent lock ordering breaks circular wait. wait() releases
the monitor; sleep() does not. Use while, not if, because wakeups may be
spurious and another thread may consume the item first. notifyAll() wakes
all waiters so each can re-check its condition. A bounded ArrayBlockingQueue
is a production alternative. Each put/take is O(1); buffer space is O(5).
*/