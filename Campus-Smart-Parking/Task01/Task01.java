// Task 1: Identify classes and responsibilities in the parking system.
class Vehicle {
    private final String registrationNumber;
    private final String type;
    Vehicle(String registrationNumber, String type) {
        this.registrationNumber = registrationNumber;
        this.type = type;
    }
    void displayDetails() {
        System.out.println("Vehicle: " + registrationNumber + " | Type: " + type);
    }
}
class ParkingSlot {
    private final int slotNumber;
    private boolean occupied;
    ParkingSlot(int slotNumber) { this.slotNumber = slotNumber; this.occupied = false; }
    void park() { occupied = true; }
    boolean isOccupied() { return occupied; }
    int getSlotNumber() { return slotNumber; }
}
public class Task01 {
    public static void main(String[] args) {
        Vehicle vehicle = new Vehicle("TN01AB1234", "Car");
        ParkingSlot slot = new ParkingSlot(101);
        vehicle.displayDetails();
        System.out.println("Slot number: " + slot.getSlotNumber());
        System.out.println("Occupied before parking: " + slot.isOccupied());
        slot.park();
        System.out.println("Occupied after parking: " + slot.isOccupied());
    }
}