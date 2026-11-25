import java.util.*;

public class PlaylistBenchmark {
    static volatile int sink;
    static long time(Runnable task) { long s=System.nanoTime(); task.run(); return System.nanoTime()-s; }
    public static void main(String[] args) {
        System.out.println("Size,Structure,AppendNs,FrontInsertNs,MiddleInsertNs,RandomGetNs,IterationNs");
        for (int n : new int[]{1000,10000,50000}) {
            for (boolean linked : new boolean[]{false,true}) {
                List<Integer> list = linked ? new LinkedList<>() : new ArrayList<>();
                long append=time(() -> { for(int i=0;i<n;i++) list.add(i); });
                long front=time(() -> { for(int i=0;i<100;i++) list.add(0,-i); });
                long middle=time(() -> { for(int i=0;i<100;i++) list.add(list.size()/2,-i); });
                long get=time(() -> { for(int i=0;i<10000;i++) sink += list.get((i*31)%list.size()); });
                long iterate=time(() -> { int sum=0; for(int x:list) sum+=x; sink=sum; });
                System.out.printf(Locale.US,"%d,%s,%d,%d,%d,%d,%d%n",n,linked?"LinkedList":"ArrayList",append,front,middle,get,iterate);
            }
        }
        Deque<String> upNext=new ArrayDeque<>();
        upNext.addLast("Song A"); upNext.addLast("Song B"); upNext.addFirst("Play Next");
        System.out.println("Up Next: "+upNext);
    }
}
