class ParkingCharge {
    double calculateFee(int hours) { return hours * 20.0; }
    double calculateFee(int hours, boolean monthlyPass) { return monthlyPass ? 0.0 : calculateFee(hours); }
}
abstract class ParkVehicle { abstract double fee(int hours); }
class ParkCar extends ParkVehicle { @Override double fee(int hours) { return hours * 40.0; } }
class ParkEV extends ParkVehicle { @Override double fee(int hours) { return hours * 30.0; } }
class ParkBike extends ParkVehicle { @Override double fee(int hours) { return hours * 15.0; } }
public class Task08 {
    public static void main(String[] args) {
        ParkingCharge charge = new ParkingCharge();
        System.out.println("Normal fee: Rs." + charge.calculateFee(2));
        System.out.println("Pass holder fee: Rs." + charge.calculateFee(2, true));
        ParkVehicle[] vehicles = { new ParkCar(), new ParkEV(), new ParkBike() };
        for (ParkVehicle v : vehicles) System.out.println(v.getClass().getSimpleName() + " fee: Rs." + v.fee(2));
    }
}