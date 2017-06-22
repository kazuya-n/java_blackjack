import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.*;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.*;

public class BJLauncher extends JFrame{
  public static JTextField text;
  public static JButton btn;

  public static void main(String[] args) {
    JFrame mainFrame = new JFrame("BJLauncher");
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setSize(320, 160);
    mainFrame.setLocationRelativeTo(null);
    JPanel contentPane = (JPanel) mainFrame.getContentPane();
    JLabel north = new JLabel("Please Enter IP");
    north.setHorizontalAlignment(JLabel.CENTER);
    text = new JTextField(30);
    btn = new JButton("Launch");
    btn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ButtonPressed();
      }
    } );
    contentPane.add(north, BorderLayout.NORTH);
    contentPane.add(text, BorderLayout.CENTER);
    contentPane.add(btn, BorderLayout.SOUTH);
    mainFrame.setVisible(true);
  }

  public static void ButtonPressed(){
    String addr = text.getText();
    System.out.println(addr);
    BlackJack bj = new BlackJack(addr);
    bj.launch();
  }
}
