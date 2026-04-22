import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import java.io.*;
import java.nio.file.*;
import javax.sound.sampled.*;
import java.io.File;

public class GuessRow extends HBox
{
//Guess holder variable
   public String guess = "";
   
   //Calls the parent to call keyboard methods
   GroupProjectWordle parent;

//Constructor
   GuessRow(GroupProjectWordle parent)
   {
      this.parent = parent;
      this.setAlignment(Pos.CENTER);
   
      for(int i=0; i<5; i++)
      {
         this.getChildren().add(new LetterTile());
      }
   }

// resets rows
   public void reset()
   {
      guess = "";
      for (int i=0;i<5;i++)
      {
         LetterTile t = (LetterTile)getChildren().get(i);
         t.setLetter("");
         t.setDefault();
      }
   }
//adds selection indication
   public void nowGuessing()
   {
      ((LetterTile)getChildren().get(0)).selectLetter();
   }
//adds letter
   public void addLetterToGuess(String letter)
   {
   //returns if guess is 5
      if (guess.length() == 5) 
         return;
   //Creates a new letter tile for the selected grid location
      LetterTile t = (LetterTile)getChildren().get(guess.length());
      t.setLetter(letter);
      t.unselectLetter();
   
      guess += letter;
   
      if (guess.length() < 5)
         ((LetterTile)getChildren().get(guess.length())).selectLetter();
   }
//removes letter
   public void backspace()
   {
   //only removes letter if there are actually letters on screen
      if (guess.length() == 0) 
         return;
   
      LetterTile t;
      //gets letter tile components and sets tile to default
      if (guess.length() < 5)
      {
         t = (LetterTile)getChildren().get(guess.length());
         t.setDefault();
      }
   //changes guess number
      guess = guess.substring(0, guess.length()-1);
   //default settings
      t = (LetterTile)getChildren().get(guess.length());
      t.setLetter("");
      t.selectLetter();
   }
//Handles Guess attempts
   public boolean adjudicateGuess(String answer)
   {
      boolean[] used = new boolean[5];
   //for each letter tile adds a pause and sound effect
      for (int i=0;i<5;i++)
      {
        
         int index = i;
      //adds small paused effect
         PauseTransition pause = new PauseTransition(Duration.millis(300*i));
         pause.setOnFinished(
            e -> {
               playSound("Tick.wav"); //plays tick when tile is shown
               LetterTile tile = (LetterTile)getChildren().get(index);
               char g = guess.charAt(index); //checks the char and will see if position matches target word
            //correct char placement
               if (g == answer.charAt(index))
               {
                  tile.setCorrect();
                  parent.updateKeyboardColor(""+g,"green");
                  used[index] = true;
                  playSound("Sparkle.wav"); //Special tile location
               }
               else //char is not in correct place
               {
                  boolean found = false;
               
                  for (int j=0;j<5;j++) //ensures that the tile has not yet been matched
                  {
                     if (!used[j] && g == answer.charAt(j))
                     {
                        found = true;
                        used[j] = true;
                        break;
                     }
                  }
               
                  if (found) //char is found but not in correct location
                  {
                     tile.setMisplaced();
                     parent.updateKeyboardColor(""+g,"yellow"); //updates keyboard for misplaced
                     playSound("Sparkle.wav"); //special tile sound
                  }
                  else //char not found
                  {
                     tile.setIncorrect();
                     parent.updateKeyboardColor(""+g,"gray"); //updates keyboard for incorrect
                  }
               }
            });
      
         pause.play();
      }
   
      return guess.equals(answer);
   }
   //Sound Handler
   public void playSound(String fileName) {
      new Thread(
         () -> {
            try
            {
            //Loads audio clip
               AudioInputStream audio = AudioSystem.getAudioInputStream(new File(fileName));
               Clip clip = AudioSystem.getClip();
               clip.open(audio);
               clip.start();
            
        //Prevents premature end
               Thread.sleep(100);
            }
            catch(Exception e)
            {
               e.printStackTrace();
            }
         }).start();}

   
   
}
