import java.io.*;
import java.nio.file.*;
public class BufferedLineCounter{
 static long countUnbuffered(Path p)throws IOException{
  long lines=0;boolean any=false;int last=-1;
  try(InputStream in=Files.newInputStream(p)){int b;while((b=in.read())!=-1){any=true;last=b;if(b=='\n')lines++;}}
  return lines+(any&&last!='\n'?1:0);
 }
 static long countBuffered(Path p)throws IOException{
  long lines=0;boolean any=false;int last=-1;byte[] buf=new byte[65536];
  try(InputStream in=new BufferedInputStream(Files.newInputStream(p),65536)){int n;while((n=in.read(buf))!=-1)
   for(int i=0;i<n;i++){any=true;last=buf[i]&255;if(last=='\n')lines++;}}
  return lines+(any&&last!='\n'?1:0);
 }
 public static void main(String[] a)throws Exception{
  if(a.length!=1){System.out.println("Usage: java BufferedLineCounter file");return;}
  Path p=Path.of(a[0]);long t=System.nanoTime(),x=countUnbuffered(p),u=System.nanoTime();
  long y=countBuffered(p),v=System.nanoTime();
  System.out.printf("Unbuffered: %d lines, %.3f ms%n",x,(u-t)/1e6);
  System.out.printf("Buffered: %d lines, %.3f ms%n",y,(v-u)/1e6);
 }
}