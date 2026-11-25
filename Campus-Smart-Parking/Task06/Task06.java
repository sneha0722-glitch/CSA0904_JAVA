class BaseVehicle {
    protected final String registrationNumber;
    BaseVehicle(String registrationNumber) { this.registrationNumber = registrationNumber; }
    void showType() { System.out.println("General vehicle: " + registrationNumber); }
}
class Car extends BaseVehicle {
    Car(String registrationNumber) { super(registrationNumber); }
    @Override void showType() { System.out.println("Car: " + registrationNumber); }
}
class ElectricVehicle extends Car {
    ElectricVehicle(String registrationNumber) { super(registrationNumber); }
    @Override void showType() { System.out.println("Electric car: " + registrationNumber + " (charging compatible)"); }
}
class TwoWheeler extends BaseVehicle {
    TwoWheeler(String registrationNumber) { super(registrationNumber); }
    @Override void showType() { System.out.println("Two-wheeler: " + registrationNumber); }
}
public class Task06 {
    public static void main(String[] args) {
        BaseVehicle[] vehicles = { new Car("TN01CA1111"), new ElectricVehicle("TN01EV2222"), new TwoWheeler("TN01TW3333") };
        for (BaseVehicle v : vehicles) v.showType();
    }
}