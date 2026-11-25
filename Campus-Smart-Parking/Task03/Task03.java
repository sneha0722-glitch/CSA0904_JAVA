import java.util.Arrays;
class ParkingFacility {
    private final boolean[][] occupied;
    ParkingFacility(int... slotsPerFloor) {
        occupied = new boolean[slotsPerFloor.length][];
        for (int i = 0; i < slotsPerFloor.length; i++) occupied[i] = new boolean[slotsPerFloor[i]];
    }
    int[] allocate() {
        for (int f = 0; f < occupied.length; f++)
            for (int s = 0; s < occupied[f].length; s++)
                if (!occupied[f][s]) { occupied[f][s] = true; return new int[]{f, s}; }
        return null;
    }
    boolean release(int floor, int slot) {
        if (floor < 0 || floor >= occupied.length || slot < 0 || slot >= occupied[floor].length || !occupied[floor][slot]) return false;
        occupied[floor][slot] = false;
        return true;
    }
}
public class Task03 {
    public static void main(String[] args) {
        ParkingFacility p = new ParkingFacility(2, 1);
        System.out.println(Arrays.toString(p.allocate()));
        System.out.println(Arrays.toString(p.allocate()));
        System.out.println(Arrays.toString(p.allocate()));
        System.out.println(p.allocate());
        System.out.println(p.release(0, 1) + " " + p.release(0, 1) + " " + p.release(5, 0));
        System.out.println(Arrays.toString(p.allocate()));
    }
}