import java.io.*;
import java.net.*;

public class ServerThread implements Runnable{
  public String data;
  public BufferedReader in;
  ServerThread(BufferedReader i){
    this.in = i;
  }
  public void run(){
    String buf;
    while(true){
      try{
        if((buf = in.readLine())!=null){
          System.out.println(buf);
          data=buf;
        }
        Thread.sleep(20);
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }
  }
}
