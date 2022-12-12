/*
    sounds.java is used to play all of the sounds found inside of BattleBots
*/

import java.io.File;
import java.io.IOException;
 
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
 

public class sounds{
    
    
    // enum used to determine what sound to play
    public enum sound{
        crit,
        victory,
        dodgeRoll,
        flyingBat,
        lifeLeech,
        shieldBash,
        shieldBlock,
        coinFlip,
        hit,
        groundSlam,
        flamePunch,
        waterBeam, 
        vineWhip
            
    }

   /*
       playASound - Simple plays the sound based on the enum passed
   */
   public static void playASound(sound toPlay){
       
       
      int timeForPlayBack = 1000;
       
      String audioFilePath = System.getProperty("user.dir") + "/sounds/";
      
      // switch on the sound enum
      switch(toPlay){
          case crit:
          audioFilePath += "CritHit.wav";
          break;
          
          // when it's a longer audio file we add some time
          case victory:
          timeForPlayBack += 500;
          audioFilePath += "victory.wav";
          break;
          
          case dodgeRoll:
          audioFilePath += "Dodge roll.wav";
          break;
          
          case flyingBat:
          audioFilePath += "Flying bat.wav";
          break;
          
          case lifeLeech:
          audioFilePath += "Life-Leech.wav";
          break;
              
          case shieldBash:
          audioFilePath += "Shield bash.wav";
          break;
          
          case shieldBlock:
          audioFilePath += "Shield Block.wav";
          break;
          
          // when it's a longer audio file we add some time
          case coinFlip:
          timeForPlayBack += 500;
          audioFilePath += "spinning-coin.wav";
          break;
          
          case hit:
          audioFilePath += "Hit.wav";
          break;
          
          case groundSlam:
          audioFilePath += "Ground slam.wav";
          break;
          
          case flamePunch:
          audioFilePath += "flame_punch.wav";
          break;
          
          case vineWhip:
          audioFilePath += "Vine_whip.wav";
          break;
          
          case waterBeam:
          audioFilePath += "Water_beam.wav";
          break;
          
      }
      
      // link the audio file
      File audioFile = new File(audioFilePath);
      
      // set up the variables to play the audio file
      AudioInputStream audioStream = null;
      AudioFormat format = null;
      Clip audioClip = null;
      
      // try to linked the audio file
      try{
         audioStream = AudioSystem.getAudioInputStream(audioFile);
      }
      catch(Exception e){
         System.out.println(e);
      }
     
      // prime the audio file
     try{
         format = audioStream.getFormat();
      }
      catch(Exception e){
         System.out.println(e);
      }
      
      // initialize the data line
      DataLine.Info info = new DataLine.Info(Clip.class, format);
      
      // try to actuall attach the audio file to the Clip object (used to actually play it)
      try{
         audioClip = (Clip) AudioSystem.getLine(info);
         audioClip.open(audioStream);

      }
      catch(Exception e){
         System.out.println(e);
      }
      
      // play the file and sleep the thread for the play time
      audioClip.start();
      try{
         Thread.sleep(timeForPlayBack);
      }
      catch(Exception e){
         System.out.println(e);
      }
      
      // end though it will be deleted try to gracefully close the link
      try{
      
         audioStream.close();
      }
      catch(Exception e){
         System.out.println(e);   
      }
   
   }
 }