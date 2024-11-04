package com.mycompany.rpggame;

import com.mycompany.rpggame.entity.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    
    //Screen Settings
    
    final int originalTileSize = 16; //16x16 tile
    
    final int scale = 3 ;
    
    public final int tileSize = originalTileSize * scale; //48*48
    
    final int maxScreenCol = 16;
    
    final int maxScreenRow = 12;
    
    final int screenWidth = maxScreenCol*tileSize;//768 pixels
    
    final int screenHeight = maxScreenRow*tileSize;//576 pixels
    
    //FPS 
    
    int FPS = 60;
    
    KeyHandler keyH = new KeyHandler();
    
    Thread gameThread;
    
    Player player = new Player(this,keyH);
    
    //Set Player default position
    
    int playerX = 100;
    
    int playerY = 100;
    
    int playerSpeed = 4 ;
    
    public GamePanel(){
        
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));//Set the size of the game panel
        
        this.setBackground(Color.black);
        
        this.setDoubleBuffered(true);//if true all the drawing from this component will be drawn in offsceen Paint buffer
        
        this.addKeyListener(keyH);
        
        this.setFocusable(true); // Game panel will be focused to receive keys
    
    }
    
    public void startGameThread(){
        
        gameThread = new Thread(this);
        
        gameThread.start();
    }

    @Override
    public void run() {    
            
        double drawInterval = 1000000000/FPS; // 0.01666667
            
        double nextDrawTime = System.nanoTime() + drawInterval;
        
        long lastTime = System.nanoTime();
        
        long timer = 0;
        
        int drawCount = 0;
        
        while(gameThread != null){
            
            long currentTime = System.nanoTime();
            
            timer += (currentTime - lastTime);
            
            lastTime = currentTime;
            //update the imformation suchn as character position
            update();
            
            //draw the screen
            
            repaint();
            
            
            drawCount++;
            
            
            try{
                
                double remainingTime = nextDrawTime - System.nanoTime();
                
                remainingTime = remainingTime/1000000;
                
                if(remainingTime<0){
                    
                    remainingTime = 0;
                }
                
                Thread.sleep((long) remainingTime);
                
                nextDrawTime += drawInterval;
                
            }catch(InterruptedException e){
                
                e.printStackTrace();
                
            }
            
            if(timer >=1000000000){
                
                System.out.println("FPS: "+drawCount);
                
                drawCount = 0;
                
                timer = 0;
            }
            
            
        }
        
    }
    
    public void update(){
        
        player.update();
        
    }
    
    public void paintComponent(Graphics g){
        
        
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D)g; //Graphics 2d extends Graphics class for better control so we change g from Graphics to Graphics2D clas
        
        player.draw(g2);
        
        g2.dispose(); // release any system resources  
        
    }
    
}
