import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.util.Random;
import java.util.ArrayList;

public class CardGame extends JPanel{
  //それぞれのカードの枚数
  public int myCardNum;
  public int pairCardNum;
  //それぞれのカードの数字の和
  public int myCardSum;
  public int pairCardSum;

  //-1だったら負け、0だったら引き分け、1だったら勝ち
  public int isWin;

  //画面遷移用
  //0...起動画面
  //1...プレイ画面
  //2...結果画面
  public int view;

  public ArrayList<Card> myCards = new ArrayList<Card>();
  public ArrayList<Card> pairCards = new ArrayList<Card>();
  // コンストラクタ（初期化処理）
  public CardGame() {
    setPreferredSize(new Dimension(900,700));
    myCardNum = 0;
    pairCardNum = 0;
    view=0;
  }
  public void deal_Card(boolean is_mine,int nums){
    Random rnd = new Random();

    if(is_mine){
      for(int i=0;i<nums;i++){
        myCards.add(new Card(rnd.nextInt(51)));
      }
    }else{
      for(int i=0;i<nums;i++){
        pairCards.add(new Card(rnd.nextInt(51)));
        pairCards.get(i).front = false;
      }
      //相手側の最後のカードだけ表面に
      pairCards.get(nums-1).front = true;
    }
  }

  // 画面描画
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setFont(new Font("Arial",Font.PLAIN, 18));
    Color bg = new Color(15, 91, 11);
    Color cha = Color.white;
    //画面遷移の表現
    switch (view){
      case 0:
        startView(g,bg,cha);
        break;
      case 1:
        gameView(g,bg,cha);
        break;
      case 2:
        resultView(g,bg,cha);
        break;
      default:
        break;
    }
  }

  //各画面ごとの処理
  private void startView(Graphics g,Color bg, Color cha){
    g.setColor(bg);
    g.fillRect(0,0,getWidth(), getHeight());
    g.setColor(cha);
    drawStringCenter(g,"BlackJack!!",getWidth()/2,getHeight()/2);
    drawStringCenter(g,"press any key to start",getWidth()/2,getHeight()/2+15);
  }
  private void gameView(Graphics g,Color bg, Color cha){
    g.setColor(bg);
    g.fillRect(0,0,getWidth(), getHeight());
    g.setColor(cha);
    g.drawString("現在の合計 : " + myCardSum, 200, 170);
    g.drawString("Press Up to HIT.",200,190);
    g.drawString("Press Down to Stand.",200,210);
    drawTrump(g);
  }
  private void resultView(Graphics g,Color bg, Color cha){
    //ペアのカードも全部表向きに
    for (int i = 0; i < pairCardNum; i++) {
      pairCards.get(i).front = true;
    }
    g.setColor(bg);
    g.fillRect(0,0,getWidth(), getHeight());
    g.setColor(cha);
    drawTrump(g);
    drawStringCenter(g,""+myCardSum + "vs" + pairCardSum+"", getWidth()/2,getHeight()/2-15);
    switch(isWin){
      case -1:
      drawStringCenter(g,"YOU LOSE...", getWidth()/2,getHeight()/2);
      break;
      case 0:
      drawStringCenter(g,"DRAW", getWidth()/2,getHeight()/2);
      break;
      case 1:
      drawStringCenter(g,"YOU WIN!!", getWidth()/2,getHeight()/2);
      break;
      default:
      break;
    }
    drawStringCenter(g,"Press any key to continue...", getWidth()/2,getHeight()/2 +15);
  }
  //再描画用
  public void update(){
    repaint();
  }

  private void drawTrump(Graphics g){
    int i;
    int x = 0;
    //自分のカード
    for (i = 0; i < myCardNum; i++) {
      g.drawImage(myCards.get(i).cardImg[0], x,700-300, null);
      x += 100;
    }
    //相手のカード
    x = 900-200;
    for (i = 0; i < pairCardNum; i++) {
    if(pairCards.get(i).front){
      g.drawImage(pairCards.get(i).cardImg[0], x,0, null);
        x -= 100;
      }else{
        g.drawImage(pairCards.get(i).cardImg[1], x,0, null);
        x -= 100;
      }
    }
    //g.drawImage(img[1], x,0, null);
  }

  private void drawStringCenter(Graphics g,String text,int x,int y){
    FontMetrics fm = g.getFontMetrics();
    Rectangle rectText = fm.getStringBounds(text, g).getBounds();
    x=x-rectText.width/2;
    y=y-rectText.height/2+fm.getMaxAscent();
    g.drawString(text, x, y);
	}
}
