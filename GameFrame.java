import javax.swing.JFrame;

public class GameFrame extends JFrame{
    GameFrame(){
        this.add(new GamePanel()); //creating a instance of GamePanel with a shortcut 
        this.setTitle("Snake in Java"); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        this.setResizable(false); 
        this.pack(); 
        this.setVisible(true);
        this.setLocationRelativeTo(null); // to make the window appear middle of computer 
    }
}
