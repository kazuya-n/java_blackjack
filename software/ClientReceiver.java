public class ClientReceiver{
  public static void main(String[] args){
    JabberClient c = new JabberClient();
    try{
      System.out.println(c.receive());
    }catch(Exception e){
    }
  }
}
