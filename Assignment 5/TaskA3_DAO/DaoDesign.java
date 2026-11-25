import java.util.*;
import java.sql.*;
interface Dao<T,K> {
 void insert(T x)throws Exception; Optional<T> findById(K id)throws Exception;
 List<T> findAll()throws Exception; void update(T x)throws Exception; void delete(K id)throws Exception;
}
record Student(long id,String registerNo,String name,String email){}
final class StudentDao implements Dao<Student,Long>{
 private final Connection c; StudentDao(Connection c){this.c=c;}
 public void insert(Student s)throws SQLException{
  try(PreparedStatement p=c.prepareStatement("INSERT INTO student(id,register_no,name,email) VALUES(?,?,?,?)")){
   p.setLong(1,s.id());p.setString(2,s.registerNo());p.setString(3,s.name());p.setString(4,s.email());p.executeUpdate();}
 }
 public Optional<Student> findById(Long id)throws SQLException{
  try(PreparedStatement p=c.prepareStatement("SELECT id,register_no,name,email FROM student WHERE id=?")){
   p.setLong(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(new Student(r.getLong(1),r.getString(2),r.getString(3),r.getString(4))):Optional.empty();}}
 }
 public List<Student> findAll()throws SQLException{
  List<Student> a=new ArrayList<>();try(Statement s=c.createStatement();ResultSet r=s.executeQuery("SELECT id,register_no,name,email FROM student")){
   while(r.next())a.add(new Student(r.getLong(1),r.getString(2),r.getString(3),r.getString(4)));}return a;
 }
 public void update(Student s)throws SQLException{
  try(PreparedStatement p=c.prepareStatement("UPDATE student SET register_no=?,name=?,email=? WHERE id=?")){
   p.setString(1,s.registerNo());p.setString(2,s.name());p.setString(3,s.email());p.setLong(4,s.id());p.executeUpdate();}
 }
 public void delete(Long id)throws SQLException{try(PreparedStatement p=c.prepareStatement("DELETE FROM student WHERE id=?")){p.setLong(1,id);p.executeUpdate();}}
}
// Common steps: connection, SQL, bind, execute, map ResultSet, close resources.
// Student.id -> student.id PRIMARY KEY; registerNo -> register_no; name -> name; email -> email.
// DAO centralises schema knowledge: a renamed column is usually fixed in one DAO.
// SQL in GUI/service scatters changes. Runtime/space per DB operation is essentially
// the same; DAO adds negligible abstraction overhead.