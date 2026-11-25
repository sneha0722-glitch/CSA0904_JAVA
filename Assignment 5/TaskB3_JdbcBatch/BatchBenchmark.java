import java.sql.*;
import java.util.*;
public class BatchBenchmark{
 public static void main(String[] a)throws Exception{
  String url=System.getenv().getOrDefault("TEST_DB_URL","jdbc:mysql://localhost:3306/persistence_lab");
  String user=System.getenv().getOrDefault("TEST_DB_USER","root"),pw=System.getenv().getOrDefault("TEST_DB_PASSWORD","");
  for(int size:new int[]{10,1000,10000,100000}){
   List<String[]> rows=new ArrayList<>(size);
   for(int i=0;i<size;i++)rows.add(new String[]{"P"+size+"_"+i,"R"+i,"Student"+i,""+(i%20),""+(1000+i%500)});
   try(Connection c=DriverManager.getConnection(url,user,pw);Statement s=c.createStatement()){
    s.executeUpdate("DELETE FROM bus_pass");long t=System.nanoTime();int n=PassLoader.loadAll(c,rows);long ms=(System.nanoTime()-t)/1000000;
    try(ResultSet r=s.executeQuery("SELECT COUNT(*) FROM bus_pass")){r.next();System.out.printf("requested=%d inserted=%d verified=%d time=%d ms%n",size,n,r.getInt(1),ms);}
   }
  }
 }
}
// Timings vary by hardware, DB, indexes and driver. Record actual results.
// For rollback, use a duplicate pass_id or invalid numeric value halfway through;
// then verify count is zero after rollback in a fresh empty table.