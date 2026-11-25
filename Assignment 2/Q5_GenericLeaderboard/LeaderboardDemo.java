import java.util.*;

class Team {
    final String name; final int points, goalDiff;
    Team(String n,int p,int g){name=n;points=p;goalDiff=g;}
    public String toString(){return name+" (pts="+points+", GD="+goalDiff+")";}
}
class Leaderboard<T> {
    private final List<T> entries=new ArrayList<>();
    private final Comparator<? super T> order;
    Leaderboard(Comparator<? super T> order){this.order=order;}
    void add(T value){entries.add(value);entries.sort(order);}
    List<T> getEntries(){return Collections.unmodifiableList(entries);}
}
public class LeaderboardDemo {
    public static void main(String[] args) {
        Comparator<Team> ranking=Comparator.comparingInt((Team t)->t.points).reversed()
          .thenComparing(Comparator.comparingInt((Team t)->t.goalDiff).reversed())
          .thenComparing(t->t.name,String.CASE_INSENSITIVE_ORDER);
        Leaderboard<Team> board=new Leaderboard<>(ranking);
        board.add(new Team("Zebra",10,4));
        board.add(new Team("Alpha",10,4));
        board.add(new Team("Falcons",10,6));
        board.add(new Team("Rovers",8,9));
        System.out.println("Leaderboard:");
        board.getEntries().forEach(System.out::println);
        Comparator<Team> faulty=(a,b)->a.points>b.points?1:-1;
        System.out.println("Faulty comparator example (equal points never return 0): "+faulty.compare(board.getEntries().get(0),board.getEntries().get(1)));
        List<String> names=new ArrayList<>(Arrays.asList("b","a","c"));
        Collections.sort(names); System.out.println("Collections sort: "+names);
    }
}
