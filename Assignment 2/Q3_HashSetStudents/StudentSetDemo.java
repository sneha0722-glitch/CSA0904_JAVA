import java.util.*;

class Student {
    private final int id;
    private final String name;
    Student(int id,String name){this.id=id;this.name=name;}
    public int getId(){return id;}
    @Override public boolean equals(Object o){
        if(this==o)return true;
        if(!(o instanceof Student))return false;
        Student s=(Student)o; return id==s.id;
    }
    @Override public int hashCode(){return Integer.hashCode(id);}
    @Override public String toString(){return id+" - "+name;}
}
public class StudentSetDemo {
    public static void main(String[] args) {
        Set<Student> students=new HashSet<>();
        students.add(new Student(101,"Anu"));
        students.add(new Student(101,"Anu Duplicate"));
        students.add(new Student(102,"Ravi"));
        System.out.println("Correct equals/hashCode set: "+students);
        System.out.println("Size should be 2: "+students.size());

        Set<String> names=new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        names.add("Sneha"); names.add("SNEHA"); names.add("Ravi");
        System.out.println("TreeSet comparator treats Sneha/SNEHA as same: "+names);

        Map<Integer,Student> byId=new HashMap<>();
        for(Student s:students) byId.put(s.getId(),s);
        System.out.println("Lookup id 102: "+byId.get(102));
    }
}
