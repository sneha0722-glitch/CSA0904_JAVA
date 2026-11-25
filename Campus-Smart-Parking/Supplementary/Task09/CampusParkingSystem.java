import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

enum VehicleType { TWO_WHEELER, CAR, EV }
enum SlotType { STANDARD, EV_CHARGING }

abstract class Vehicle {
    private final String registrationNumber;
    private final VehicleType type;
    private final boolean monthlyPassHolder;
    Vehicle(String registrationNumber, VehicleType type, boolean monthlyPassHolder) {
        this.registrationNumber = registrationNumber;
        this.type = type;
        this.monthlyPassHolder = monthlyPassHolder;
    }
    String getRegistrationNumber() { return registrationNumber; }
    VehicleType getType() { return type; }
    boolean hasMonthlyPass() { return monthlyPassHolder; }
    abstract double hourlyRate();
}
class Car extends Vehicle {
    Car(String number, boolean pass) { super(number, VehicleType.CAR, pass); }
    @Override double hourlyRate() { return 40.0; }
}
class TwoWheeler extends Vehicle {
    TwoWheeler(String number, boolean pass) { super(number, VehicleType.TWO_WHEELER, pass); }
    @Override double hourlyRate() { return 15.0; }
}
class ElectricCar extends Vehicle {
    ElectricCar(String number, boolean pass) { super(number, VehicleType.EV, pass); }
    @Override double hourlyRate() { return 30.0; }
}
class ParkingSlot {
    private final int floorNumber;
    private final int slotNumber;
    private final SlotType slotType;
    private Vehicle parkedVehicle;
    ParkingSlot(int floorNumber, int slotNumber, SlotType slotType) {
        this.floorNumber = floorNumber; this.slotNumber = slotNumber; this.slotType = slotType;
    }
    boolean isAvailable() { return parkedVehicle == null; }
    boolean canFit(Vehicle vehicle) {
        return isAvailable() && (slotType == SlotType.STANDARD || vehicle.getType() == VehicleType.EV);
    }
    void park(Vehicle vehicle) { parkedVehicle = vehicle; }
    void release() { parkedVehicle = null; }
    int getFloorNumber() { return floorNumber; }
    int getSlotNumber() { return slotNumber; }
    SlotType getSlotType() { return slotType; }
}
class ParkingTicket {
    private static final AtomicInteger COUNTER = new AtomicInteger(1000);
    private final int ticketNumber = COUNTER.incrementAndGet();
    private final Vehicle vehicle;
    private final ParkingSlot slot;
    private final Instant entryTime = Instant.now();
    ParkingTicket(Vehicle vehicle, ParkingSlot slot) { this.vehicle = vehicle; this.slot = slot; }
    int getTicketNumber() { return ticketNumber; }
    Vehicle getVehicle() { return vehicle; }
    ParkingSlot getSlot() { return slot; }
    Instant getEntryTime() { return entryTime; }
}
class ParkingFacility {
    private final List<ParkingSlot> slots = new ArrayList<>();
    // Every fifth slot on each floor is designated for EV charging.
    ParkingFacility(int... slotsPerFloor) {
        for (int floor = 0; floor < slotsPerFloor.length; floor++)
            for (int slot = 1; slot <= slotsPerFloor[floor]; slot++) {
                SlotType type = (slot % 5 == 0) ? SlotType.EV_CHARGING : SlotType.STANDARD;
                slots.add(new ParkingSlot(floor + 1, slot, type));
            }
    }
    ParkingTicket park(Vehicle vehicle) {
        if (vehicle.getType() == VehicleType.EV)
            for (ParkingSlot slot : slots)
                if (slot.getSlotType() == SlotType.EV_CHARGING && slot.canFit(vehicle)) {
                    slot.park(vehicle); return new ParkingTicket(vehicle, slot);
                }
        for (ParkingSlot slot : slots)
            if (slot.getSlotType() == SlotType.STANDARD && slot.canFit(vehicle)) {
                slot.park(vehicle); return new ParkingTicket(vehicle, slot);
            }
        return null;
    }
    double exit(ParkingTicket ticket, Instant exitTime) {
        ParkingSlot slot = ticket.getSlot();
        if (!slot.isAvailable()) slot.release();
        if (ticket.getVehicle().hasMonthlyPass()) return 0.0;
        long minutes = Math.max(1, Duration.between(ticket.getEntryTime(), exitTime).toMinutes());
        long hours = Math.max(1, (minutes + 59) / 60);
        return hours * ticket.getVehicle().hourlyRate();
    }
    int availableSlots() {
        int count = 0;
        for (ParkingSlot slot : slots) if (slot.isAvailable()) count++;
        return count;
    }
}
public class CampusParkingSystem {
    public static void main(String[] args) {
        ParkingFacility facility = new ParkingFacility(10, 8, 12);
        Vehicle car = new Car("TN01AB1234", false);
        Vehicle ev = new ElectricCar("TN01EV5678", false);
        Vehicle staffBike = new TwoWheeler("TN01TW9999", true);
        ParkingTicket carTicket = facility.park(car);
        ParkingTicket evTicket = facility.park(ev);
        ParkingTicket passTicket = facility.park(staffBike);
        showTicket(carTicket); showTicket(evTicket); showTicket(passTicket);
        System.out.println("Available slots after entry: " + facility.availableSlots());
        Instant simulatedExit = Instant.now().plus(Duration.ofHours(2)).plus(Duration.ofMinutes(10));
        System.out.println("Car fee for 2h 10m: Rs." + facility.exit(carTicket, simulatedExit));
        System.out.println("EV fee for 2h 10m: Rs." + facility.exit(evTicket, simulatedExit));
        System.out.println("Monthly pass fee: Rs." + facility.exit(passTicket, simulatedExit));
        System.out.println("Available slots after exit: " + facility.availableSlots());
    }
    private static void showTicket(ParkingTicket ticket) {
        if (ticket == null) { System.out.println("No compatible slot available."); return; }
        ParkingSlot slot = ticket.getSlot();
        System.out.println("Ticket #" + ticket.getTicketNumber() + " | " + ticket.getVehicle().getRegistrationNumber()
            + " | Floor " + slot.getFloorNumber() + " Slot " + slot.getSlotNumber()
            + " | " + slot.getSlotType() + " | Entry " + LocalDateTime.now());
    }
}