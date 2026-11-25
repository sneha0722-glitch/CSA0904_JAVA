import java.io.*;
import java.nio.file.*;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
public class PassStore {
 private final Path file;
 PassStore(Path file){this.file=file;}
 public void add(String passId,String regNo,String name,int route,double amount)throws IOException{
  try(BufferedWriter w=Files.newBufferedWriter(file,StandardOpenOption.CREATE,StandardOpenOption.APPEND)){
   w.write(String.join(",",passId,regNo,name,String.valueOf(route),String.valueOf(amount)));w.newLine();}
 }
 public String[] find(String passId)throws IOException{
  if(Files.notExists(file))return null;
  try(BufferedReader r=Files.newBufferedReader(file)){
   String line;while((line=r.readLine())!=null){
    String[] f=line.split(",",-1);if(f.length!=5||f[0].isBlank()||f[1].isBlank()||f[2].isBlank())continue;
    try{int route=Integer.parseInt(f[3]);double amount=Double.parseDouble(f[4]);if(route<0||!Double.isFinite(amount)||amount<0)continue;}
    catch(NumberFormatException ex){continue;}
    if(f[0].equals(passId))return f;
   }
  }return null;
 }
 public List<String[]> listByRoute(int wantedRoute)throws IOException{
  List<String[]> matches=new ArrayList<>();
  if(Files.notExists(file))return matches;
  try(BufferedReader r=Files.newBufferedReader(file)){
   String line;while((line=r.readLine())!=null){
    String[] f=line.split(",",-1);
    if(f.length!=5||f[0].isBlank()||f[1].isBlank()||f[2].isBlank())continue;
    try{int route=Integer.parseInt(f[3]);double amount=Double.parseDouble(f[4]);
     if(route<0||!Double.isFinite(amount)||amount<0)continue;
     if(route==wantedRoute)matches.add(f);
    }catch(NumberFormatException ex){ /* skip damaged record */ }
   }
  }return matches;
 }
 public static void main(String[] args)throws IOException{
  Path f=Files.createTempFile("passes",".csv");PassStore s=new PassStore(f);
  s.add("P1001","192311001","Arun",12,4500);
  Files.writeString(f,"P1002,DAMAGED LINE\n",StandardOpenOption.APPEND);
  s.add("P1003","192311003","Divya",7,3800);
  System.out.println(Arrays.toString(s.find("P1003")));
  System.out.println(Arrays.toString(s.find("P1002")));Files.delete(f);
 }
}
// Expected: [P1003, 192311003, Divya, 7, 3800.0] then null.
// Damaged P1002 row has only two fields, so it is skipped and scanning continues.
// This simple comma format assumes fields contain no commas; use a CSV parser otherwise.