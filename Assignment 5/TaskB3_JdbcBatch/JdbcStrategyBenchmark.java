import java.sql.*;
import java.util.*;

/** Compares one-row Statement, PreparedStatement and PreparedStatement batch inserts. */
public class JdbcStrategyBenchmark {
    static final String SQL="INSERT INTO bus_pass(pass_id,reg_no,name,route_no,amount) VALUES(?,?,?,?,?)";
    static String url(){return System.getenv().getOrDefault("TEST_DB_URL","jdbc:mysql://localhost:3306/persistence_lab");}
    static String user(){return System.getenv().getOrDefault("TEST_DB_USER","root");}
    static String password(){return System.getenv().getOrDefault("TEST_DB_PASSWORD","");}
    static List<String[]> data(int n,String prefix){
        List<String[]> rows=new ArrayList<>(n);
        for(int i=0;i<n;i++)rows.add(new String[]{prefix+i,"R"+i,"Student"+i,""+(i%20),""+(1000+i%500)});
        return rows;
    }
    static void clear(Connection c)throws SQLException{try(Statement s=c.createStatement()){s.executeUpdate("DELETE FROM bus_pass");}}
    static long statement(List<String[]> rows)throws SQLException{
        try(Connection c=DriverManager.getConnection(url(),user(),password())){
            clear(c);long t=System.nanoTime();
            try(Statement s=c.createStatement()){
                for(String[] p:rows){
                    String q="INSERT INTO bus_pass VALUES('"+p[0]+"','"+p[1]+"','"+p[2]+"',"+p[3]+","+p[4]+")";
                    s.executeUpdate(q);
                }
            }return (System.nanoTime()-t)/1_000_000;
        }
    }
    static long prepared(List<String[]> rows)throws SQLException{
        try(Connection c=DriverManager.getConnection(url(),user(),password())){
            clear(c);long t=System.nanoTime();
            try(PreparedStatement p=c.prepareStatement(SQL)){
                for(String[] x:rows){bind(p,x);p.executeUpdate();}
            }return (System.nanoTime()-t)/1_000_000;
        }
    }
    static long batch(List<String[]> rows)throws SQLException{
        try(Connection c=DriverManager.getConnection(url(),user(),password())){
            clear(c);c.setAutoCommit(false);long t=System.nanoTime();
            try(PreparedStatement p=c.prepareStatement(SQL)){
                int i=0;for(String[] x:rows){bind(p,x);p.addBatch();if(++i%1000==0)p.executeBatch();}
                p.executeBatch();c.commit();
            }catch(SQLException ex){c.rollback();throw ex;}
            return (System.nanoTime()-t)/1_000_000;
        }
    }
    static void bind(PreparedStatement p,String[] x)throws SQLException{
        p.setString(1,x[0]);p.setString(2,x[1]);p.setString(3,x[2]);
        p.setInt(4,Integer.parseInt(x[3]));p.setDouble(5,Double.parseDouble(x[4]));
    }
    public static void main(String[] args)throws Exception{
        for(int n:new int[]{1000,10000,100000}){
            System.out.println("Rows: "+n);
            System.out.println("Statement ms: "+statement(data(n,"S"+n+"_")));
            System.out.println("PreparedStatement ms: "+prepared(data(n,"P"+n+"_")));
            System.out.println("Prepared batch transaction ms: "+batch(data(n,"B"+n+"_")));
        }
    }
}
// For an apples-to-apples experiment use the same DB settings, empty table,
// warm-up policy and network path. This intentionally uses Statement string
// construction only to compare techniques; never use it with untrusted input.