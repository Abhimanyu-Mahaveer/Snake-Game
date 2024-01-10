import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements ActionListener{
   
    static final int SCREEN_WIDTH =600;
    static final int SCREEN_HEIGHT=600; 
    static final int UNIT_SIZE=25 ;
    static final int GAME_UNITS=(SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE; 
    static final int DELAY=75; 
    final int x[]=new int[GAME_UNITS];
    final int y[]=new int [GAME_UNITS];
    int bodyParts=6; 
    int applesEaten; 
    int appleX,appleY; 
    char direction='R'; //default direction
    boolean running =false;
    Timer timer ;
    Random random ;
    Clip clip;
    Color headColor; //should have been named bodyColor but it is what it is 
    //Image headImage; //image variable  
    //private boolean isSoundPlaying = false;
    GamePanel(){
        random =new Random(); 
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black); 
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter()); 
  /*              try {
                    headImage = new ImageIcon(getClass().getResource("src/supro cut.png")).getImage();
                } catch (Exception e) {
                    e.printStackTrace();
                } */
        startGame();

    }
    public void startGame() {
        newApple();
        running=true; 
        timer=new Timer(DELAY,this); 
        timer.start(); 
        
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g); 
    }
    public void draw (Graphics g){
        if(running){
        for(int i=0;i< SCREEN_HEIGHT/UNIT_SIZE;i++){
            g.drawLine(i*UNIT_SIZE,0, i*UNIT_SIZE, SCREEN_HEIGHT); 
            g.drawLine(0,i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); 
        } 
        // drawing the apple 
        g.setColor(Color.red); 
        g.fillOval(appleX,appleY,UNIT_SIZE,UNIT_SIZE); 
        // Drawing the snake's head using the image
        // g.drawImage(headImage, x[0], y[0], UNIT_SIZE, UNIT_SIZE, this);//drawing the snake 
        
      for(int i =0;i<bodyParts;i++)
        {
            if(i==0)
            {
                g.setColor(Color.green);
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
            else 
            {
                g.setColor(new Color(45,100,0));//original code
                g.setColor(headColor); //for changing the body color whenever the snake eats a apple
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); 
            }
        }
        g.setColor(new Color(255, 0, 0, 135)); // 128 is the alpha value, ranging from 0 (fully transparent) to 255 (fully opaque)
            g.setFont(new Font("Minecrafter",Font.BOLD,30)); 
            FontMetrics metrics=getFontMetrics(g.getFont()); 
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
    }
    else
    {
        gameOver(g); 
    }
    } 
    public void newApple(){
        // random placement of apple
        appleX= random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;    
        appleY= random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;  
    } 
    public void move (){ 
        for(int i=bodyParts;i>0;i--)
        {
            x[i]=x[i-1]; 
            y[i]=y[i-1];
            
        }
        // to  move the snake up down left right 
        switch(direction){
            case 'U': 
            y[0]=y[0]-UNIT_SIZE;
            break;
            case 'D': 
            y[0]=y[0]+UNIT_SIZE;
            break;
            case 'L': 
            x[0]=x[0]-UNIT_SIZE;
            break;
            case 'R': 
            x[0]=x[0]+UNIT_SIZE;
            break;
            
            
        }
       
    } 
    public void checkApple() {
        
        if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
            headColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));//to randomly change the body color
            
            playBackgroundMusic("src/snake-eats.wav");
		}
    } 
    public void checkCollisions() {
        // check if head toched any body part 
        for (int i=bodyParts;i>0;i--)
        {
            if((x[0]==x[i])&&(y[0]==y[i])) {
                playBackgroundMusic("src/snake-hiss.wav");
                running=false; 
            }

        }
       // check if head touches right border
        if (x[0] >= SCREEN_WIDTH) {
            running = false;
        }

        // check if head touches bottom border
        if (y[0] >= SCREEN_HEIGHT) {
            running = false;
        }
        // check if head touches left border
        if (x[0] < 0) {
            running = false;
        }

        // check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
		
		if(!running) {
            playBackgroundMusic("src/snake-hiss.wav");
			timer.stop();
		}

    }
    public void gameOver(Graphics g){
            //GAME OVER 
            g.setColor((Color.red)); // 128 is the alpha value, ranging from 0 (fully transparent) to 255 (fully opaque)
            g.setFont(new Font("Minecrafter",Font.BOLD,30)); 
            FontMetrics metrics1=getFontMetrics(g.getFont()); 
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
            g.setColor(Color.red);
            g.setFont(new Font("Minecrafter",Font.BOLD,65)); 
            FontMetrics metrics2=getFontMetrics(g.getFont()); 
            g.drawString("GAME OVER", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
            playBackgroundMusic("src/game-over.wav");
    } 

    @Override
    public void actionPerformed (ActionEvent e){
        if(running){
            move() ;
            checkApple();
            checkCollisions();

        }
        repaint();
        
    }
    // for music 
    public void playBackgroundMusic(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);

            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        event.getLine().close();
                    }
                }});

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    
    public class MyKeyAdapter extends KeyAdapter{
        @Override
        //which key pressed will do what ?
        public void keyPressed (KeyEvent e) {
            //only 90 degree turns allowed 
			switch(e.getKeyCode()) { 
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
        }
    }
 }
}
