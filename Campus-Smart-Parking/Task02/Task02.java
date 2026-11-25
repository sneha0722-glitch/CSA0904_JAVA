public class Task02 {
    public static void main(String[] args) {
        boolean[][] occupied = new boolean[3][];
        occupied[0] = new boolean[4];
        occupied[1] = new boolean[3];
        occupied[2] = new boolean[5];
        occupied[0][1] = true;
        occupied[2][3] = true;
        for (int floor = 0; floor < occupied.length; floor++) {
            System.out.print("Floor " + (floor + 1) + ": ");
            for (int slot = 0; slot < occupied[floor].length; slot++)
                System.out.print(occupied[floor][slot] ? "[X] " : "[ ] ");
            System.out.println();
        }
        System.out.println("Total slots: " + (occupied[0].length + occupied[1].length + occupied[2].length));
    }
}