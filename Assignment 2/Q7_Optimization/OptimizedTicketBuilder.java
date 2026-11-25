public class OptimizedTicketBuilder {
    // Reuses one pre-sized builder for each ticket and avoids repeated immutable concatenation.
    static String createTicket(int ticketId, String[] fields) {
        int capacity=32;
        for(String field:fields) capacity+=field.length()+2;
        StringBuilder ticket=new StringBuilder(capacity);
        ticket.append("TICKET-").append(ticketId);
        for(int i=0;i<fields.length;i++)ticket.append('|').append(i+1).append('=').append(fields[i]);
        return ticket.toString();
    }
    public static void main(String[] args) {
        String[] fields={"Passenger","From","To","Date","Seat"};
        System.out.println(createTicket(1001,fields));
    }
}
