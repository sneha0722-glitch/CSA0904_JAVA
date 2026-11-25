public class SeatBooking {
    private final boolean[] booked;
    private int bookingsMade = 0;

    SeatBooking(int seats) { booked = new boolean[seats]; }

    // The check-and-update operation is atomic for this SeatBooking object.
    public synchronized void book(int seat) {
        if (seat < 0 || seat >= booked.length) {
            System.out.println("Invalid seat " + seat);
            return;
        }
        if (!booked[seat]) {
            booked[seat] = true;
            bookingsMade++;
            System.out.println(Thread.currentThread().getName() + " booked seat " + seat);
        } else {
            System.out.println(Thread.currentThread().getName() + " could not book seat " + seat);
        }
    }

    public synchronized int getBookingsMade() { return bookingsMade; }

    public static void main(String[] args) throws InterruptedException {
        SeatBooking screen = new SeatBooking(10);
        Thread[] users = new Thread[100];
        for (int i = 0; i < users.length; i++) {
            final int seat = i % 10;
            users[i] = new Thread(() -> screen.book(seat), "User-" + (i + 1));
            users[i].start();
        }
        for (Thread t : users) t.join();
        System.out.println("Seats sold: " + screen.getBookingsMade());
        System.out.println("Expected seats sold: 10");
    }
}
/*
Without synchronization, two threads can both observe booked[seat] == false
before either updates it, causing duplicate sales. synchronized uses the
object's intrinsic monitor; only one thread executes the critical section
for this instance at a time. A synchronized block can lock only the
check/update section. For shared static state, use a static lock or
static synchronized method. Alternatives: AtomicBoolean per seat,
ReentrantLock, or ConcurrentHashMap.putIfAbsent. Time O(1) per booking;
space O(number of seats).
*/