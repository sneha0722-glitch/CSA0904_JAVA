class ParkingTicket {
    private final int ticketNumber;
    private final String registrationNumber;
    private final long entryTime;
    ParkingTicket(int ticketNumber, String registrationNumber) {
        this(ticketNumber, registrationNumber, System.currentTimeMillis());
    }
    ParkingTicket(int ticketNumber, String registrationNumber, long entryTime) {
        this.ticketNumber = ticketNumber;
        this.registrationNumber = registrationNumber;
        this.entryTime = entryTime;
    }
    ParkingTicket(ParkingTicket other) {
        this(other.ticketNumber, other.registrationNumber, other.entryTime);
    }
    void display() {
        System.out.println("Ticket: " + ticketNumber + ", Vehicle: " + registrationNumber + ", Entry: " + entryTime);
    }
}
public class Task04 {
    public static void main(String[] args) {
        ParkingTicket original = new ParkingTicket(1001, "TN01AB1234");
        ParkingTicket copy = new ParkingTicket(original);
        original.display();
        copy.display();
    }
}