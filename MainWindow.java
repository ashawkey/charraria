import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public static int DEBUG=1;
    public static void main(String[] args) {
        MainWindow m=new MainWindow();
    }
    private MainPanel P=new MainPanel();
    //915-615
    private static final int WIN_WIDTH=1200;
    private static final int WIN_HEIGHT=700;

    public static int getWinWidth() { return WIN_WIDTH;}

    public static int getWinHeight(){
        return WIN_HEIGHT;
    }

    private MainWindow(){
        setSize(WIN_WIDTH,WIN_HEIGHT);
        setResizable(false);
        setTitle("Charraria");
        this.getContentPane().setBackground(Color.WHITE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(P, BorderLayout.CENTER);

        setVisible(true);
    }


}
