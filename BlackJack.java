import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.awt.event.*;

public class BlackJack extends JFrame implements KeyListener{
  public static CardGame c;
  BlackJack(){
    addKeyListener(this);
  }
  public static void main(String[] args) {
    //起動時の初期化処理
    c = new CardGame();
    newGame();
    // ウィンドウの作成と表示
    JFrame f = new JFrame("BlackJack");
    f.getContentPane().setLayout(new FlowLayout());
    f.getContentPane().add(c);
    f.pack();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setResizable(false);
    f.setVisible(true);
    f.addKeyListener(new BlackJack());
  }
  //新規ゲーム
  public static void newGame(){
    //カードを初期化
    c.view = 0;
    c.myCards.clear();
    c.pairCards.clear();
    c.myCardNum = 1;
    c.pairCardNum = 2;
    c.deal_Card(true,1);
    c.deal_Card(false,2);
    c.myCardSum = countSum(c.myCards,c.myCardNum);
    c.pairCardSum = countSum(c.pairCards,c.pairCardNum);
  }
  public static int countSum(ArrayList<Card> cards,int cardNums){
    int sum = 0;
    for(int i=0;i<cardNums;i++){
      sum += cards.get(i).cardNum;
    }
    return sum;
  }

  //ヒット、スタンドの処理
  private void myHit(){
    if(c.myCardSum>=21) myStand();
    c.myCardNum++;
    c.deal_Card(true,1);
    c.myCardSum = countSum(c.myCards,c.myCardNum);
    //カードの合計が21以上だったらスタンド
    if(c.myCardSum>=21) myStand();
    c.repaint();
  }

  private void dealer(){
    //ディーラーは、16点以下なら必ずヒット
    //17点以上なら必ずスタンド
    while(c.pairCardSum<17){
      pairHit();
    }
    pairStand();
  }

  private void pairHit(){
    c.pairCardNum++;
    c.deal_Card(false,1);
    c.pairCardSum = countSum(c.pairCards,c.pairCardNum);
    c.repaint();
  }

  //スタンドの処理。
  private void myStand(){
    dealer();
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
      c.view=1;
      c.repaint();
    }
    //プレイ中の時
    else if(c.view==1){
      int key = e.getKeyCode();
      if(key == KeyEvent.VK_UP){
        myHit();
      }
      if(key == KeyEvent.VK_DOWN){
        myStand();
      }
    }
    //結果発表の時
    else if(c.view==2){
      //何かキーが押されたら初期化してゲーム画面へ
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
