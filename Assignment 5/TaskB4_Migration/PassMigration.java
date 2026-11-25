import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.util.*;
public class PassMigration{
 public static void main(String[] a)throws Exception{
  if(a.length!=1){System.out.println("Usage: java PassMigration passes.csv");return;}
  String url=System.getenv().getOrDefault("TEST_DB_URL","jdbc:mysql://localhost:3306/persistence_lab");
  String user=System.getenv().getOrDefault("TEST_DB_USER","root"),pw=System.getenv().getOrDefault("TEST_DB_PASSWORD","");
  List<String[]> good=new ArrayList<>();List<String> bad=new ArrayList<>();
  try(BufferedReader r=Files.newBufferedReader(Path.of(a[0]))){String line;int no=0;
   while((line=r.readLine())!=null){no++;String[] f=line.split(",",-1);
    try{if(f.length!=5||f[0].isBlank()||f[1].isBlank()||f[2].isBlank())throw new IllegalArgumentException("fields/required value");
     Integer.parseInt(f[3]);double amount=Double.parseDouble(f[4]);if(!Double.isFinite(amount)||amount<0)throw new IllegalArgumentException("amount");
     good.add(f);
    }catch(RuntimeException ex){bad.add("line "+no+": "+ex.getMessage()+" ["+line+"]");}
   }
  }
  try(Connection c=DriverManager.getConnection(url,user,pw)){
   DatabaseMetaData m=c.getMetaData();System.out.println("Database: "+m.getDatabaseProductName()+" "+m.getDatabaseProductVersion());
   System.out.println("Driver: "+m.getDriverName()+" "+m.getDriverVersion());
   try(ResultSet t=m.getTables(c.getCatalog(),null,"%",new String[]{"TABLE"})){while(t.next())System.out.println("Table: "+t.getString("TABLE_NAME"));}
   System.out.println("Migrated: "+PassLoader.loadAll(c,good));
  }
  System.out.println("Rejected: "+bad.size());for(String x:bad)System.out.println(x);
 }
}
// Shared DB is preferable for 12 campuses: central indexed search, transactions,
// constraints and coordinated concurrent writers. Per-campus files need aggregation
// and conflict resolution across all 12 stores.