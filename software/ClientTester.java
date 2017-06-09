public class ClientTester{
  public static void main(String args[]){
    JabberClient c = new JabberClient();
    try{
      c.send("おはよう");
    }catch(Exception e){
      e.printStackTrace();
    }

  }
}
