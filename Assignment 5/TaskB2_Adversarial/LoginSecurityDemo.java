import java.sql.*;
public class LoginSecurityDemo{
 public static void main(String[] a)throws Exception{
  String url=System.getenv().getOrDefault("TEST_DB_URL","jdbc:mysql://localhost:3306/persistence_lab");
  String user=System.getenv().getOrDefault("TEST_DB_USER","root"),pw=System.getenv().getOrDefault("TEST_DB_PASSWORD","");
  try(Connection c=DriverManager.getConnection(url,user,pw);Statement s=c.createStatement()){
   s.executeUpdate("CREATE TABLE IF NOT EXISTS demo_users(id VARCHAR(80) PRIMARY KEY,pwd VARCHAR(80) NOT NULL)");
   s.executeUpdate("DELETE FROM demo_users");s.executeUpdate("INSERT INTO demo_users VALUES('admin','correct-secret')");
   String id="admin' -- ",bad="wrong-password";
   try(ResultSet r=s.executeQuery("SELECT id FROM demo_users WHERE id='"+id+"' AND pwd='"+bad+"'")){
    System.out.println("Unsafe accepts crafted input: "+r.next());}
   try(PreparedStatement p=c.prepareStatement("SELECT id FROM demo_users WHERE id=? AND pwd=?")){
    p.setString(1,id);p.setString(2,bad);try(ResultSet r=p.executeQuery()){
     System.out.println("PreparedStatement accepts crafted input: "+r.next());}}
  }
 }
}
// Run only on a disposable local test schema. Expected MySQL result: unsafe true; safe false.