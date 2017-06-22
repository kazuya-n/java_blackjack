import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.applet.Applet;
import java.applet.AudioClip;


public class BlackJack extends JFrame implements KeyListener{
  public static CardGame c;
  public static JabberClient jc;
  public static boolean senkou;
  public static JFrame f;
  public static String addr;
  BlackJack(String ar){
    addKeyListener(this);
    addr = ar;
  }
  public static void launch(){
    //起動時の初期化処理
    c = new CardGame();
    jc = new JabberClient(addr);
    try{
      if((jc.receive()).equals("senkou")){
        senkou = true;
        System.out.println("senkou");
      } else {
        senkou = false;
        System.out.println("koukou");
      }
    } catch (Exception e){
      e.printStackTrace();
    }
    newGame();
    // ウィンドウの作成と表示
    f = new JFrame("BlackJack");
    f.getContentPane().setLayout(new FlowLayout());
    f.getContentPane().add(c);
    f.pack();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setResizable(false);
    f.setVisible(true);
    f.addKeyListener(new BlackJack(addr));
  }
  //新規ゲーム
  private static void newGame(){
    //カードを初期化
    c.view = 0;
    c.myCards.clear();
    c.pairCards.clear();
    c.is_Burst = false;
    c.is_BJ = false;
    c.is_Stand = false;
    c.myCardNum = 1;
    c.pairCardNum = 1;
    if(senkou){
      try{
        c.deal_Card(true,1);
        jc.send(String.valueOf(c.myCards.get(0).primaryNum));
        c.connectionCard(Integer.parseInt((jc.receive())));
      } catch (Exception e){
        e.printStackTrace();
      }
    } else {
      try{
        c.connectionCard(Integer.parseInt((jc.receive())));
        c.deal_Card(true,1);
        jc.send(String.valueOf(c.myCards.get(0).primaryNum));
      } catch (Exception e){
        e.printStackTrace();
      }
    }
    c.myCardSum = countSum(c.myCards,c.myCardNum);
    c.pairCardSum = countSum(c.pairCards,c.pairCardNum);
  }
  private static int countSum(ArrayList<Card> cards,int cardNums){
    int sum = 0;
    for(int i=0;i<cardNums;i++){
      sum += cards.get(i).cardNum;
    }
    return sum;
  }

  public static void waitPair(){

    try{
      Thread.sleep(50);
    }catch(Exception e){
      e.printStackTrace();
    }
    System.out.println("Called waitPair() : waiting...");
    String rc = new String("");
    try{
      if(!((rc=jc.receive()).equals("Stand"))){
        c.connectionCard(Integer.parseInt(rc));
      }
    } catch (Exception e){
      e.printStackTrace();
    }
    System.out.println("received...");
    c.pairCardNum++;
    c.pairCardSum = countSum(c.pairCards,c.pairCardNum);
    c.reupdate();
  }

  //ヒット、スタンドの処理
  private void myHit(){
    AudioClip audioClip = Applet.newAudioClip(BlackJack.class.getResource("./enter.wav"));
    audioClip.play();
    System.out.println("Called myHit()");
    c.myCardNum++;
    c.deal_Card(true,1);
    c.myCardSum = countSum(c.myCards,c.myCardNum);
    //カードの合計が21以上だったらスタンド
    if(c.myCardSum>=21){
      if(c.myCardSum > 21){
        System.out.println("Burst!!");
        c.is_Burst = true;
      } else {
        c.is_BJ = true;
      }
      c.repaint();
    }
    c.repaint();
  }

  private void pairHit(){
    while(true){
      String r = new String("");
      // r = jc.receive();
      if(r.equals("END")) break;
      c.pairCards.add(new Card(Integer.parseInt(r)));
      c.pairCardNum++;
    }
    c.pairCardSum = countSum(c.pairCards,c.pairCardNum);
    c.repaint();
    return;
  }

  //スタンドの処理。
  private void myStand(){
    AudioClip audioClip = Applet.newAudioClip(BlackJack.class.getResource("./back.wav"));
    audioClip.play();
    c.is_Stand = true;
    c.repaint();
    if(senkou){
      try{
        jc.send("Stand");
        if(jc.receive().equals("Stand")){
          shareCards();
        }
      }catch (Exception e){
          e.printStackTrace();
      }
    }else{
      try{
        if(jc.receive().equals("Stand")){
          jc.send("Stand");
          shareCards();
        }
      } catch (Exception e){
        e.printStackTrace();
      }
    }
  }

  private void shareCards(){
    if(senkou){
      try{
        StringBuilder sendStr = new StringBuilder();
        sendStr.append(String.valueOf(c.myCardNum));
        for(int i = 0;i<c.myCardNum;i++){
          sendStr.append(",");
          sendStr.append(String.valueOf(c.myCards.get(i).primaryNum));
        }
        jc.send(sendStr.toString() );
        System.out.println("sent String ..." + sendStr);

        String receiveStr = jc.receive();
        System.out.println("receive String ..." + receiveStr);
        String[] query = receiveStr.split(",", 0);
        c.pairCardNum = Integer.parseInt(query[0]);
        System.out.println("Koukou Card Number : " + c.pairCardNum);
        for(int i = 2; i <= c.pairCardNum; i++){
          c.connectionCard(Integer.parseInt(query[i]));
          System.out.println("received card num ..." + query[i]);
        }
      } catch (Exception e){
        e.printStackTrace();
      }
    } else {
      try{
        String receiveStr = jc.receive();
        System.out.println("receive String ..." + receiveStr);
        String[] query = receiveStr.split(",", 0);
        c.pairCardNum = Integer.parseInt(query[0]);
        System.out.println("Koukou Card Number : " + c.pairCardNum);
        for(int i = 2; i <= c.pairCardNum; i++){
          c.connectionCard(Integer.parseInt(query[i]));
          System.out.println("received card num ..." + query[i]);
        }

        StringBuilder sendStr = new StringBuilder();
        sendStr.append(String.valueOf(c.myCardNum));
        for(int i = 0;i<c.myCardNum;i++){
          sendStr.append(",");
          sendStr.append(String.valueOf(c.myCards.get(i).primaryNum));
        }
        jc.send(sendStr.toString() );
        System.out.println("sent String ..." + sendStr);

      } catch (Exception e){
        e.printStackTrace();
      }
    }
    c.pairCardSum = countSum(c.pairCards,c.pairCardNum);
    result();
  }

  private void pairStand(){
    result();
  }

  //結果
  private void result(){
    c.view=2;
    //勝敗判定
    //まず、21超えたほうが負け
    if(c.myCardSum>21 && c.pairCardSum<=21){
      c.isWin = -1;
      c.repaint();
    }
    else if(c.pairCardSum>21 && c.myCardSum<=21){
      c.isWin = 1;
      c.repaint();
    }
    //21に近いほうが勝ち
    else if(Math.abs(c.myCardSum-21)>Math.abs(c.pairCardSum-21)){
      c.isWin = -1;
      c.repaint();
    }else if(Math.abs(c.pairCardSum-21)>Math.abs(c.myCardSum-21)){
      c.isWin = 1;
      c.repaint();
    }else{
      c.isWin = 0;
      c.repaint();
    }
  }
  //KeyListenerをimplement
  //連打対策で、keyreleasedで処理。
  public void keyReleased(KeyEvent e){
    //スタート画面の時
    if(c.view==0){
      //何かキーが押されたらゲーム画面へ
      AudioClip audioClip = Applet.newAudioClip(BlackJack.class.getResource("./enter.wav"));
      audioClip.play();
      c.view=1;
      c.repaint();
    }
    //プレイ中の時
    else if(c.view==1){
      int key = e.getKeyCode();

      if(key == KeyEvent.VK_UP){
        if(c.myCardSum>=21){
          myStand();
        } else {
          myHit();
        }
      }
      if(key == KeyEvent.VK_DOWN){
        myStand();
      }
    }
    //結果発表の時
    else if(c.view==2){
      //何かキーが押されたら初期化してゲーム画面へ
      AudioClip audioClip = Applet.newAudioClip(BlackJack.class.getResource("./enter.wav"));
      audioClip.play();
      newGame();
      c.view=1;
      c.repaint();
    }
  }
  public void keyPressed(KeyEvent e){

  }
  public void keyTyped(KeyEvent e){

  }
}
