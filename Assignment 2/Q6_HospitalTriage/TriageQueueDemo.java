import java.util.*;

class Patient {
    final String name; final int severity; final long arrival;
    Patient(String n,int s,long a){name=n;severity=s;arrival=a;}
    public String toString(){return name+"(severity="+severity+", arrival="+arrival+")";}
}
public class TriageQueueDemo {
    static long sequence=0;
    static Patient patient(String name,int severity){return new Patient(name,severity,sequence++);}
    static Comparator<Patient> order=Comparator.comparingInt((Patient p)->p.severity)
      .thenComparingLong(p->p.arrival);
    public static void main(String[] args) {
        PriorityQueue<Patient> queue=new PriorityQueue<>(order);
        queue.add(patient("Asha",3)); queue.add(patient("Bala",1));
        queue.add(patient("Charan",2)); queue.add(patient("Divya",1));
        queue.add(patient("Esha",5)); queue.add(patient("Farah",3));
        queue.add(patient("Gopal",2)); queue.add(patient("Hari",4));
        System.out.println("Treatment order (1 = most severe):");
        while(!queue.isEmpty())System.out.println(queue.poll());

        for(int n:new int[]{100,1000,5000}) {
            PriorityQueue<Patient> q=new PriorityQueue<>(order);
            long start=System.nanoTime();
            for(int i=0;i<n;i++)q.offer(new Patient("P"+i,1+(i%5),i));
            while(!q.isEmpty())q.poll();
            System.out.println("Queue size "+n+" offer+poll ns="+(System.nanoTime()-start));
        }
        System.out.println("Aging policy: every 10 minutes waiting, improve effective severity by one level, minimum 1.");
    }
}
