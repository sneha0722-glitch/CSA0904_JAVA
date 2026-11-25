import java.util.*;

public class PharmacyMapBenchmark {
    static volatile int sink;
    public static void main(String[] args) {
        System.out.println("Size,Map,BuildNs,100kLookupNs,ReportNs");
        for(int n:new int[]{1000,10000,50000}) {
            for(int type=0;type<3;type++) {
                Map<String,Integer> map= type==0?new HashMap<>():type==1?new TreeMap<>():new LinkedHashMap<>();
                long b=System.nanoTime();
                for(int i=0;i<n;i++)map.put(String.format("MED-%06d",i),i);
                b=System.nanoTime()-b;
                long l=System.nanoTime();
                for(int i=0;i<100000;i++)sink+=map.getOrDefault(String.format("MED-%06d",i%n),0);
                l=System.nanoTime()-l;
                long r=System.nanoTime(); int count=0;
                for(String key:map.keySet())count+=key.length();
                r=System.nanoTime()-r; sink=count;
                System.out.printf("%d,%s,%d,%d,%d%n",n,type==0?"HashMap":type==1?"TreeMap":"LinkedHashMap",b,l,r);
            }
        }
        Map<String,Integer> stock=new Hashtable<>();
        stock.put("PARA",25); System.out.println("Legacy Hashtable sample: "+stock);
        String[] sorted={"MED-0001","MED-0002","MED-0003"};
        System.out.println("Sorted-array index: "+Arrays.binarySearch(sorted,"MED-0002"));
    }
}
