
import java.io.*;
import java.net.*;

public class JabberServer extends Thread{
  public static Socket socket[] = new Socket[2];
  private static BufferedReader in[] = new BufferedReader[2];
  private static PrintWriter out[] = new PrintWriter[2];
  public static final int PORT = 8901;
  public static void main(String[] args)
  throws IOException  {
    ServerSocket send = new ServerSocket(PORT);
    try{
      //ソケット通信受付
      socket[0] = send.accept();
      socket[1] = send.accept();
      try{
        for(int i=0; i<2; i++){
          in[i] =
          new BufferedReader(
          new InputStreamReader(
          socket[i].getInputStream()));
          out[i] =
          new PrintWriter(
          new BufferedWriter(
          new OutputStreamWriter(
          socket[i].getOutputStream())), true);
        }
        autoCommunicate();
        } finally {
          System.out.println("closing...");
          for(int i=0; i<2; i++){
            socket[i].close();
          }
        }
      } finally {
        send.close();
      }
    }
  private static void autoCommunicate(){
    ServerThread st[] = new ServerThread[2];
    for(int i = 0;i < 2;i++){
      st[i] = new ServerThread(in[i]);
    }
    Thread t0 = new Thread(st[0]);
    Thread t1 = new Thread(st[1]);
    t0.start();
    t1.start();
    String r0, r1;
    while(true){
      try{
        if((r0 = st[0].data)!=null){
          out[1].println(r0);
        }
        r1 = st[1].data;
        if((r1 = st[1].data)!=null){
          out[0].println(r1);
        }
        Thread.sleep(50);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }
}
