class FeeVehicle {
    protected final String registrationNumber;
    FeeVehicle(String registrationNumber) { this.registrationNumber = registrationNumber; }
    double calculateFee(int hours) { return hours * 20.0; }
    @Override public String toString() { return getClass().getSimpleName() + "[" + registrationNumber + "]"; }
}
class FeeCar extends FeeVehicle {
    FeeCar(String registrationNumber) { super(registrationNumber); }
    @Override double calculateFee(int hours) { return hours * 40.0; }
}
class FeeTwoWheeler extends FeeVehicle {
    FeeTwoWheeler(String registrationNumber) { super(registrationNumber); }
    @Override double calculateFee(int hours) { return hours * 15.0; }
}
class FeeEV extends FeeVehicle {
    FeeEV(String registrationNumber) { super(registrationNumber); }
    @Override double calculateFee(int hours) { return hours * 30.0; }
}
public class Task07 {
    public static void main(String[] args) {
        FeeVehicle[] vehicles = { new FeeCar("TN01CA1111"), new FeeTwoWheeler("TN01TW2222"), new FeeEV("TN01EV3333") };
        for (FeeVehicle v : vehicles) System.out.println(v + " | 3-hour fee: Rs." + v.calculateFee(3));
    }
}