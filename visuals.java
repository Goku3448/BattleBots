/*
   visuals.java is a class that contains the animations used in the fightGUI
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import java.lang.Thread;
import javafx.geometry.Insets;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.image.*;



// TODO REMOVE
import java.io.*;

public class visuals extends menu{
     
    // borders holds all the borders that might be used
    enum borders {
        defaultBorder
    }
        
    /*
        getBorderPath returns the path to the choosen border
    */
    public static String getBorderPath(borders border){
        String borderPath = "";
        borderPath += System.getProperty("user.dir");
        borderPath += "/images/";
        switch(border){
            case defaultBorder:
                borderPath += "defaultBorder.png";
                break;
            default:
                System.out.println("MISSING CASE FOR BORDER EXITING");
                System.exit(0);
                break;
        }
        return borderPath;
    }
    

   /* Returns background object */
   public static Background setBackground(Image currentImage){

      BackgroundImage bImg = new BackgroundImage(currentImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                                                               BackgroundPosition.CENTER,  BackgroundSize.DEFAULT);
      Background bGround = new Background(bImg);
      return bGround;
   }
   
   private static robot robotA;
   private static robot robotB;
     
   public static void saveRobot(robot A, robot B){
     robotA = A;
     robotB = B;
     visuals.robotIdle_A(1);
     visuals.robotIdle_A(2);
   }
   
   // animations holds all animations that might be used
    enum animations {
         critAttack,
         shieldBlock,
         shieldBash,
         lifeLeech_Bat,
         lifeLeech_Heart,
         groundSlam_Meteor,
         groundSlam_MeteorSlam,
         coinFlip,
         victory
    }
      
    /*
        getAnimationsPath returns the path to the choosen animation sprite
    */
    public static String getAnimationsPath(animations ani){
        String animationPath = "";
        animationPath += System.getProperty("user.dir");
        animationPath += "/images/";
        switch(ani){

            case critAttack:
                animationPath += "crit.png";
                break;
            case shieldBlock:
                animationPath += "ShieldBlock.png";
                break;
            case shieldBash:
                animationPath += "ShieldBash.png";
                break;           
            case lifeLeech_Bat:
                animationPath += "lifeLech_bat.png";
                break;
            case lifeLeech_Heart:
                animationPath += "heart.png";
                break; 
            case groundSlam_Meteor:
                animationPath += "meteor.png";
                break;
            case groundSlam_MeteorSlam:
                animationPath += "meteorSlam.png";
                break;     
            case coinFlip:
                animationPath += "victory.png";
                break;
            case victory:
                animationPath += "victory.png";
                break;
            default:
                System.out.println("MISSING CASE FOR ANIMTATION EXITING");
                System.exit(0);
                break;
        }
        
        return animationPath;
    }

   /*
        getRobotSpritePath returns the path to the robots choosen sprite
   */
   public static String getRobotSpritePath(robot curRobot){
      String robotSpriteLocation = "";
      robotSpriteLocation += System.getProperty("user.dir");
      robotSpriteLocation += "/robot_sprites/";
      robotSpriteLocation += curRobot.getRobotSprite() + "_idle.png";
      return robotSpriteLocation;
   }
    
   /*
        getRobotDamagedSpritePath returns the path to the robots damaged sprite version
   */
   public static String getRobotDamagedSpritePath(robot curRobot){
      String robotSpriteLocation = "";
      robotSpriteLocation += System.getProperty("user.dir");
      robotSpriteLocation += "/robot_sprites/";
      robotSpriteLocation += curRobot.getRobotSprite() + "_Damaged.png";
      return robotSpriteLocation;
    }
   
   /* 
    * Resets selected robot pose and location 
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Resets the robot.
   */
   public static void robotIdle_A(int player){
      Thread idleThread = new Thread(new Runnable(){
         public void run(){
            
            // loads the correct data for player 1 or player 2
            if(player == 1){
               Image idle = new Image("file:" + getRobotSpritePath(robotA));
               menu.getPlayer1().setTranslateX(80);
               menu.getPlayer1().setTranslateY(0);
               menu.getPlayer1().setImage(idle);
               
               menu.getp1SpecialBehavior().setTranslateX(80);
               menu.getp1SpecialBehavior().setTranslateY(0);  
            }
            else if(player == 2){
               Image idle = new Image("file:" + getRobotSpritePath(robotB));
               menu.getPlayer2().setTranslateX(20);
               menu.getPlayer2().setTranslateY(-350);
               menu.getPlayer2().setImage(idle);
               
               menu.getp2SpecialBehavior().setTranslateX(80);
               menu.getp2SpecialBehavior().setTranslateY(-300);  
            }
         }
      });
      //Plays animation
      idleThread.start();
   }


   /* 
    * Animation for a normal attack. 
    * The method takes in a number that represents the robot that will do the action.
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Plays animation of a robot doing a normal attack.
   */
   public static void robotAttackNormal_A(int player){
      // Creates new thread for attack animation
      Thread attackThread = new Thread(new Runnable() {
         public void run() {
         
            // loads the correct data for player 1 or player 2
            if(player == 1){
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getPlayer1());
               translate.setDuration(Duration.millis(50));
               translate.setByX(300);
               PauseTransition pt = new PauseTransition(Duration.millis(300));
               TranslateTransition translate1 = new TranslateTransition();
               translate1.setNode(menu.getPlayer1());
               translate1.setDuration(Duration.millis(10));
               translate1.setByX(-300);
               SequentialTransition seqT = new SequentialTransition (translate, pt, translate1);
               seqT.setOnFinished(e -> { robotIdle_A(1); });
               seqT.play();
            }
            else if(player == 2){
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getPlayer2());
               translate.setDuration(Duration.millis(50));
               translate.setByX(-300);
               PauseTransition pt = new PauseTransition(Duration.millis(300));
               TranslateTransition translate1 = new TranslateTransition();
               translate1.setNode(menu.getPlayer2());
               translate1.setDuration(Duration.millis(10));
               translate1.setByX(300);
               SequentialTransition seqT = new SequentialTransition (translate, pt, translate1);
               seqT.setOnFinished(e -> { robotIdle_A(2); });
               seqT.play();
            }
            
           }
      });
      //Plays animation
      attackThread.start();
   }
   
   /* 
    * Animation for a crit attack. 
    * The method takes in a number that represents the robot that will do the action.
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Plays animation of a robot doing a normal attack. 
   */
   public static void  critHit_A (int player){
      // Creates new thread for attack animation
      Thread attackCritThread = new Thread(new Runnable() {
         public void run() {
            Image crit = new Image("file:" +  getAnimationsPath(animations.critAttack));

            // loads the correct data for player 1 or player 2
            if(player == 1){           
               PauseTransition pt = new PauseTransition(Duration.millis(10));
               pt.setOnFinished(e -> { 
                  menu.getp1Effects().setImage(crit); 
                  menu.getp1Effects().setVisible(true);
               });
               PauseTransition pt2 = new PauseTransition(Duration.millis(300));
               pt2.setOnFinished(e -> { 
                  menu.getp1Effects().setVisible(false);
               });       
               SequentialTransition seqT = new SequentialTransition (pt,pt2);  
               seqT.play();
               
            }
            else if(player == 2){           
               PauseTransition pt = new PauseTransition(Duration.millis(10));
               pt.setOnFinished(e -> { 
                  menu.getp2Effects().setImage(crit); 
                  menu.getp2Effects().setVisible(true);
               });
               PauseTransition pt2 = new PauseTransition(Duration.millis(300));
               pt2.setOnFinished(e -> { 
                  menu.getp2Effects().setVisible(false);
               });       
               SequentialTransition seqT = new SequentialTransition (pt,pt2);  
               seqT.play();
           } 
          }
      });
      //Plays animation
      attackCritThread.start();
   }


    /* 
    * Animation for damage taken. 
    * The method takes in a number that represents the robot that will do the action.
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Plays animation of a robot taking damage
   */
   public static void robotDamaged_A(int player){
      // Creates new thread for damage animation
      Thread damagedThread = new Thread(new Runnable() {
         public void run() {
             
            // loads the correct data for player 1 or player 2
            if(player == 1){
               Image damaged = new Image("file:" + getRobotDamagedSpritePath(robotA));       
               menu.getPlayer1().setImage(damaged);
               PauseTransition pt = new PauseTransition(Duration.millis(250));
               pt.setOnFinished(e -> { robotIdle_A(1); });
               pt.play();
               
               }
            if(player == 2){
               Image damaged = new Image("file:" + getRobotDamagedSpritePath(robotB));  
               menu.getPlayer2().setImage(damaged);
               PauseTransition pt = new PauseTransition(Duration.millis(250));
               pt.setOnFinished(e -> { robotIdle_A(2); });
               pt.play();
            }
          }
      });
      //Plays animation
      damagedThread.start();
   }
   
   /* 
    * Resets selected robot pose and location 
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Resets the robot.    
   */
   public static void sb_ShieldBlock_A (int player){
      Thread shieldBlockThread = new Thread(new Runnable(){
         public void run(){
            Image shieldBlock = new Image("file:" + getAnimationsPath(animations.shieldBlock));
            
            // loads the correct data for player 1 or player 2
            if(player == 1){
               menu.getp1SpecialBehavior().setImage(shieldBlock);                  
               menu.getp1SpecialBehavior().setVisible(true);
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getPlayer1());
               translate.setDuration(Duration.millis(1));
               translate.setByX(-30);
               PauseTransition pt = new PauseTransition(Duration.millis(300));
               pt.setOnFinished(e -> { 
                  robotIdle_A(1);
                  menu.getp1SpecialBehavior().setVisible(false);
                  });
               SequentialTransition seqT = new SequentialTransition (translate, pt);
               seqT.play();
            }
            if(player == 2){
               menu.getp2SpecialBehavior().setImage(shieldBlock);  
               menu.getp2SpecialBehavior().setVisible(true);  
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getPlayer2());
               translate.setDuration(Duration.millis(1));
               translate.setByX(30);
               PauseTransition pt = new PauseTransition(Duration.millis(300));
               pt.setOnFinished(e -> { 
                  robotIdle_A(2);
                  menu.getp2SpecialBehavior().setVisible(false);
                  });
               SequentialTransition seqT = new SequentialTransition (translate, pt);
               seqT.play();
            }
         }
      });
      //Plays animation
      shieldBlockThread.start();
   }
   
   /* 
    * Animtion for the special Behavior Shield Bash
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Plays Animation for Shield Bash   
   */ 
   public static void sb_ShieldBash_A (int player){
      Thread shieldBashThread = new Thread(new Runnable(){
         public void run(){
           Image shieldAttack = new Image("file:" + getAnimationsPath(animations.shieldBash));
           
           // loads the correct data for player 1 or player 2
           if(player == 1){               
               menu.getp1SpecialBehavior().setVisible(true);
               menu.getp1SpecialBehavior().setImage(shieldAttack);    

               
               PauseTransition pt = new PauseTransition(Duration.millis(50));
               pt.setOnFinished(e -> { 
                  robotAttackNormal_A(1); 
               });
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getp1SpecialBehavior());
               translate.setDuration(Duration.millis(50));
               translate.setByX(300);
               PauseTransition pt2 = new PauseTransition(Duration.millis(300));
               SequentialTransition seqT = new SequentialTransition (pt,translate,pt2);
               seqT.setOnFinished(A -> { 
                  menu.getp1SpecialBehavior().setVisible(false); 
                  robotIdle_A(1);
               });
               seqT.play();
            }
           if(player == 2){                 
               menu.getp2SpecialBehavior().setVisible(true);           
               menu.getp2SpecialBehavior().setImage(shieldAttack);  
               
               PauseTransition pt = new PauseTransition(Duration.millis(50));
               pt.setOnFinished(e -> { 
                  robotAttackNormal_A(2);
                });
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getp2SpecialBehavior());
               translate.setDuration(Duration.millis(50));
               translate.setByX(-300);
               PauseTransition pt2 = new PauseTransition(Duration.millis(300));
               SequentialTransition seqT = new SequentialTransition (pt,translate,pt2);
               seqT.setOnFinished(A -> { 
                  menu.getp2SpecialBehavior().setVisible(false); 
                  robotIdle_A(2);
               });
               seqT.play();
            }
         }
      });
      //Plays animation
      shieldBashThread.start();
   }

   /* 
    * Animtion for the special Behavior Dodge
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Plays Animation for Dodge 
   */
   public static void sb_Dodge_A(int player){
      Thread dodgeThread = new Thread(new Runnable(){
         public void run(){
         
            // loads the correct data for player 1 or player 2
            if(player == 1){
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getPlayer1());
               translate.setDuration(Duration.millis(1));
               translate.setByX(-50);
               PauseTransition pt = new PauseTransition(Duration.millis(300));
               pt.setOnFinished(e -> { robotIdle_A(1); });
               SequentialTransition seqT = new SequentialTransition (translate, pt);
               seqT.play();
            }
            if(player == 2){
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getPlayer2());
               translate.setDuration(Duration.millis(1));
               translate.setByX(50);
               PauseTransition pt = new PauseTransition(Duration.millis(300));
               pt.setOnFinished(e -> { robotIdle_A(2);});
               SequentialTransition seqT = new SequentialTransition (translate, pt);
               seqT.play();
            }
         }
      });
      //Plays animation
      dodgeThread.start();
   }
   
   /* 
    * Animtion for the special Behavior Life Leech
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Plays Animation for Life Leech 
   */
   public static void sb_LifeLeech_A (int player){
      Thread lifeLeechThread = new Thread(new Runnable(){
         public void run(){
            Image leech = new Image("file:" + getAnimationsPath(animations.lifeLeech_Bat));
            Image heart = new Image("file:" + getAnimationsPath(animations.lifeLeech_Heart));
            
            // loads the correct data for player 1 or player 2
            if(player == 1){ 
               menu.getp1SpecialBehavior().setImage(leech);                
               menu.getp1Effects().setImage(heart);   
                              
               menu.getp1SpecialBehavior().setVisible(true);
              
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getp1SpecialBehavior());
               translate.setDuration(Duration.millis(250));
               translate.setByX(450);
               PauseTransition pt2 = new PauseTransition(Duration.millis(200));
               translate.setOnFinished(A -> {    
                  menu.getp1SpecialBehavior().setVisible(false); 
                  menu.getp1Effects().setVisible(true);
               });
               PauseTransition pt3 = new PauseTransition(Duration.millis(400));
               
               SequentialTransition seqT = new SequentialTransition (translate,pt2,pt3);
               seqT.setOnFinished(A -> { 
                  menu.getp1Effects().setVisible(false); 
                   robotIdle_A(1);
                  });
               seqT.play();
            }
            if(player == 2){
               menu.getp2SpecialBehavior().setImage(leech);                
               menu.getp2Effects().setImage(heart); 
                                
               menu.getp2SpecialBehavior().setVisible(true);
              
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getp2SpecialBehavior());
               translate.setDuration(Duration.millis(250));
               translate.setByX(-400);
               PauseTransition pt2 = new PauseTransition(Duration.millis(200));
               translate.setOnFinished(A -> {    
                  menu.getp2SpecialBehavior().setVisible(false); 
                  menu.getp2Effects().setVisible(true);
               });
               PauseTransition pt3 = new PauseTransition(Duration.millis(400));
               
               SequentialTransition seqT = new SequentialTransition (translate,pt2,pt3);
               seqT.setOnFinished(A -> { 
                  menu.getp2Effects().setVisible(false); 
                  robotIdle_A(2);
               });
               seqT.play();
            }
         }
      });
      //Plays animation
      lifeLeechThread.start();
   }
   
   /* 
    * Animtion for the special Behavior Ground Slam 
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Plays Animation for Ground Slam sb_GroundSlam_A
   */
   public static void sb_GroundSlam_A (int player){
      Thread groundSlamThread = new Thread(new Runnable(){
         public void run(){
            Image meteor = new Image("file:" + getAnimationsPath(animations.groundSlam_Meteor));
            Image GroundSlam = new Image("file:" + getAnimationsPath(animations.groundSlam_MeteorSlam));
  
            // loads the correct data for player 1 or player 2
            if(player == 1){
               menu.getp1SpecialBehavior().setTranslateY(-350);
               menu.getp1SpecialBehavior().setImage(meteor);                 
               menu.getp1SpecialBehavior().setVisible(true);  
                
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getp1SpecialBehavior());
               translate.setDuration(Duration.millis(100));
               translate.setByX(450);
               translate.setByY(350);
               translate.setOnFinished(A -> { 
                  menu.getp1SpecialBehavior().setImage(GroundSlam); 
                  
               });

               PauseTransition pt2 = new PauseTransition(Duration.millis(400));
                
               SequentialTransition seqT = new SequentialTransition (translate,pt2);
                  seqT.setOnFinished(A -> {    
                  menu.getp1SpecialBehavior().setVisible(false); 
               });
               seqT.play();
            }
            if(player == 2){
               menu.getp2SpecialBehavior().setTranslateY(-650);
               menu.getp2SpecialBehavior().setImage(meteor);                 
               menu.getp2SpecialBehavior().setVisible(true);  
                
               TranslateTransition translate = new TranslateTransition();
               translate.setNode(menu.getp2SpecialBehavior());
               translate.setDuration(Duration.millis(100));
               translate.setByX(-450);
               translate.setByY(350);
               translate.setOnFinished(A -> { 
                  menu.getp2SpecialBehavior().setImage(GroundSlam); 
                  
               });
               PauseTransition pt2 = new PauseTransition(Duration.millis(400));

               SequentialTransition seqT = new SequentialTransition (translate,pt2);
               seqT.setOnFinished(A -> {    
                  menu.getp2SpecialBehavior().setVisible(false); 
               });
               seqT.play();
            }
         }
      });
      //Plays animation
      groundSlamThread.start();
   } 
       
   /* 
    * Animtion for the special Behavior vine whip
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Plays Animation for vine whip 
   */
   public static void sb_VineWhip_A ( int player ){
      Thread VineWhiphThread = new Thread(new Runnable(){
         public void run(){

            try{
               if(player == 1){
                  menu.getp1BigAnimations().setVisible(true);
                  int currentImage = 1;
                  while(currentImage != 5){
                     Image vineWhip = new Image("file:" + getDirectoryOfImages() + "/animations/vineWhip_"+ currentImage + ".png");
                     menu.getp1BigAnimations().setImage(vineWhip); 
                     Thread.sleep(50);
                     currentImage++;
                  }
                  Thread.sleep(300);
                  menu.getp1BigAnimations().setVisible(false);
               }
               if(player == 2){
                  menu.getp2BigAnimations().setVisible(true);
                  int currentImage = 1;
                  while(currentImage != 5){
                     Image vineWhip = new Image("file:" + getDirectoryOfImages() + "/animations/vineWhip_"+ currentImage + ".png");
                     menu.getp2BigAnimations().setImage(vineWhip); 
                     Thread.sleep(50);
                     currentImage++;
                  }
                  Thread.sleep(300);
                  menu.getp2BigAnimations().setVisible(false);
               }
            }
            catch(InterruptedException e){ Thread.currentThread().interrupt(); }
         }
      });
      //Plays animation
      VineWhiphThread.start();
   }
   
   /* 
    * Animtion for the special Behavior water beam
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Plays Animation for water beam sb_waterBeam_A
   */
   public static void sb_waterBeam_A (int player ){
      Thread waterBeamThread = new Thread(new Runnable(){
         public void run(){
            try{
               if(player == 1){
                  menu.getp1BigAnimations().setVisible(true);
                  int currentImage = 1;
                  while(currentImage != 6){
                     Image waterBeam = new Image("file:" + getDirectoryOfImages() + "/animations/waterBeam_"+ currentImage + ".png");
                     menu.getp1BigAnimations().setImage(waterBeam); 
                     Thread.sleep(50);
                     currentImage++;
                  }
                  menu.getp1BigAnimations().setVisible(false);
               }
               if(player == 2){
                  menu.getp2BigAnimations().setVisible(true);
                  int currentImage = 1;
                  while(currentImage != 6){
                     Image waterBeam = new Image("file:" + getDirectoryOfImages() + "/animations/waterBeam_"+ currentImage + ".png");
                     menu.getp2BigAnimations().setImage(waterBeam); 
                     Thread.sleep(50);
                     currentImage++;
                  }
                  menu.getp2BigAnimations().setVisible(false);
               }
            }
            catch(InterruptedException e){ Thread.currentThread().interrupt(); }
         }
      });
      //Plays animation
      waterBeamThread.start();
   }

   /* 
    * Animtion for the special Behavior fire punch
    * Pre: Takes an int 1 is for player 1 <left robot>, 2 is for player 2 <right robot>
    * Post: Plays Animation for water beam  sb_firePunch_A
   */
   public static void sb_firePunch_A(int player ){
      Thread firePunchThread = new Thread(new Runnable(){
         public void run(){
            try{
               if(player == 1){
                  menu.getp1BigAnimations().setVisible(true);
                  int currentImage = 1;
                  while(currentImage != 4){
                     Image firePunch = new Image("file:" + getDirectoryOfImages() + "/animations/firePunch_"+ currentImage + ".png");
                     menu.getp1BigAnimations().setImage(firePunch); 
                     Thread.sleep(50);
                     currentImage++;
                  }

                  menu.getp1BigAnimations().setVisible(false);
               }
               if(player == 2){
                  menu.getp2BigAnimations().setVisible(true);
                  int currentImage = 1;
                  while(currentImage != 4){
                     Image firePunch = new Image("file:" + getDirectoryOfImages() + "/animations/firePunch_"+ currentImage + ".png");
                     menu.getp2BigAnimations().setImage(firePunch); 
                     Thread.sleep(50);
                     currentImage++;
                  }

                  menu.getp2BigAnimations().setVisible(false);
               }
            }
            catch(InterruptedException e){ Thread.currentThread().interrupt(); }
         }
      });
      //Plays animation
      firePunchThread.start();
   }
 
   /* 
    * Animtion for the special Behavior victory
    * Pre: Takes an int 0 for cpu -- 1 is for player 1 <left robot> -- 2 is for player 2 <right robot>
    * Post: Plays Animation for victory  
   */
   public static void sb_victory_A ( int player ){
      Thread victoryThread = new Thread(new Runnable(){
         public void run(){
            Image playerWon = null;
            
            try{
               if(player == 0){
                  menu.getvictory().setVisible(true);
                  playerWon = new Image("file:" + getDirectoryOfImages() + "/cpu_won"+ ".png");
                  menu.getvictory().setImage(playerWon); 

                  Thread.sleep(10000);
                  menu.getvictory().setVisible(false);
               }
               if(player == 1){
                  menu.getvictory().setVisible(true);
                  playerWon = new Image("file:" + getDirectoryOfImages() + "/player1_won"+ ".png");    
                  menu.getvictory().setImage(playerWon);               
                  Thread.sleep(10000);
                  menu.getvictory().setVisible(false);
               }
               if(player == 2){
                  menu.getvictory().setVisible(true);
                  playerWon = new Image("file:" + getDirectoryOfImages() + "/player2_won"+ ".png");
                  menu.getvictory().setImage(playerWon); 
                  Thread.sleep(10000);
                  menu.getvictory().setVisible(false);
               }
            }
            catch(InterruptedException e){ Thread.currentThread().interrupt(); }
         }
      });
      //Plays animation
      victoryThread.start();
   }

   
   /* 
    * Animation for a coin flip
    * Pre: N/A
    * Post: Plays Animation for coin flipping 
   */
   public static void coinflip_A (){
      Thread coinThread = new Thread(new Runnable(){
         public void run(){
            try{
            menu.getCoinFlip().setVisible(true);
            int times = 0;
            while(times != 5){
               int image = 1;
               while(image != 6 ){
                  Image coin = new Image("file:" + getDirectoryOfImages() + "/animations/coinFlip_"+ image + ".png");
                  menu.getCoinFlip().setImage(coin); 
                  Thread.sleep(60);
                  image++;
             }
               
               times++;
            }
            }
            catch(InterruptedException e){ Thread.currentThread().interrupt(); }
         }
      });
      //Plays animation
      coinThread.start();
   }
   
    /* 
    * Animation for a coin flip 
    * Pre: player that wins
    * Post: Shows the player that won
   */
   public static void coinPick_A (int player){
      Thread coinThread = new Thread(new Runnable(){
         public void run(){
            try{
               // loads the correct data for player 1 or player 2
               if(player == 1){
                  Image coin = new Image("file:" + getDirectoryOfImages() + "/animations/coinFlip_" + "one.png");
                   menu.getCoinFlip().setImage(coin);
               }
               if(player == 2){
                  Image coin = new Image("file:" + getDirectoryOfImages() + "/animations/coinFlip_"+"two.png");
                   menu.getCoinFlip().setImage(coin);
               }
               Thread.sleep(1200);
               menu.getCoinFlip().setVisible(false);
            }
            catch(InterruptedException e){ Thread.currentThread().interrupt(); }
 
         }
      });
      //Plays animation
      coinThread.start();
   }


   /*
    * Gets the directory of the images
   */
   public static String getDirectoryOfImages(){
      String rosterDirectory = "";
      // Try and get the path our rosters are saved in
      try{
         rosterDirectory = System.getProperty("user.dir");
         rosterDirectory += "/images";
      }
      // Catch NullPointerException
      catch(NullPointerException E){
         System.out.println("NullPointerException occured in getDirectoryOfRosters");
      }
      return rosterDirectory;
   }
}
