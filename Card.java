import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

public class Card{

  Image cardImg[] = new Image[2]; //カードの画像を保持
  public int cardNum; // 柄を無視したカードの番号(0~12)
  public int primaryNum; //柄のデータ付きのカードの番号(0~51)
  public boolean front; //カードが表かどうか

  public Card(int num){
    primaryNum = num;
    cardNum = (primaryNum % 13) + 1 ;
    loadImage();
    front = true;
  }

  private void loadImage() {
    try {
        System.out.println("./png/t000"+primaryNum+".png");
        //0が表面、1が裏面
        cardImg[0] = ImageIO.read(new File("./png/t000"+primaryNum+".png"));
        cardImg[1] = ImageIO.read(new File("./png/t00055.png"));
    } catch (Exception e) {
        System.out.println(e);
        System.exit(0);
    }
  }
}
