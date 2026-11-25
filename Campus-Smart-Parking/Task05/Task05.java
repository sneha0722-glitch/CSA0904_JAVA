class TicketIssuer {
    private static int nextTicketNumber = 1000;
    private final int facilityId;
    TicketIssuer(int facilityId) { this.facilityId = facilityId; }
    int issueTicket() { return ++nextTicketNumber; }
    static int getNextTicketNumber() { return nextTicketNumber; }
    int getFacilityId() { return facilityId; }
}
public class Task05 {
    public static void main(String[] args) {
        TicketIssuer north = new TicketIssuer(1);
        TicketIssuer south = new TicketIssuer(2);
        System.out.println("North ticket: " + north.issueTicket());
        System.out.println("South ticket: " + south.issueTicket());
        System.out.println("Next available number: " + TicketIssuer.getNextTicketNumber());
    }
}