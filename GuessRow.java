// NOTE: Remember to build this and any other class!!

// Game: Wordle (GuessRow class)

// Anthoni Pineda
// Justice Earl Pinkney
// Zachary Ross

// CPT-237-W34
// Spring 2026

import javafx.geometry.*;
import javafx.scene.layout.*;

public class GuessRow extends HBox
{
   Insets letterMargins = new Insets(5.0, 5.0, 5.0, 5.0);
   
   public String guess = "";
   
   // Constructors
   GuessRow()
   {
      this.setAlignment(Pos.CENTER);
      
      // Adding 5 LetterTile as children
      for(int i=0; i<5; i++)
      {
         LetterTile newTile = new LetterTile();
         this.getChildren().add(newTile);
         this.setMargin(newTile, letterMargins);
      }
   }
   
   public void reset()
   {
      guess = "";
      for(int i=0; i<5; i++)
      {
         LetterTile selectedTile = (LetterTile)this.getChildren().get(i);
         selectedTile.unselectLetter();
         selectedTile.setLetter("");
      }
   }
   
   public void nowGuessing()
   {
      // first tile is selected
      LetterTile selectedTile = (LetterTile)this.getChildren().get(0);
      selectedTile.selectLetter();
   }
   
   public void addLetterToGuess(String letter)
   {
      if (guess.length() == 5) return; // If guess is full, do nothing.
      
      // Updating tile the letter is going in.
      LetterTile selectedTile = (LetterTile)this.getChildren().get(guess.length());
      selectedTile.setLetter(letter);
      selectedTile.unselectLetter();
      
      guess += letter; // Add the letter to guess
      
      if (guess.length() == 5) return; // If guess is full, stop.
      
      // Guess isn't full, highlight next letter tile
      selectedTile = (LetterTile)this.getChildren().get(guess.length());
      selectedTile.selectLetter();
   }
   
   public void backspace()
   {
      if (guess.length() == 0) return; // If guess is already empty, do nothing.
      
      LetterTile selectedTile;
      if (guess.length() < 5)
      {
         // Reversing select
         selectedTile = (LetterTile)this.getChildren().get(guess.length());
         selectedTile.unselectLetter();
      }
      
      guess = guess.substring(0, guess.length() - 1); // Truncate last letter
      
      // Highlighting previous letter tile
      selectedTile = (LetterTile)this.getChildren().get(guess.length());
      selectedTile.setLetter("");
      selectedTile.selectLetter();
   }
   
   public boolean adjudicateGuess(String answer)
   {
      for(int i=0; i<5; i++)
      {
         LetterTile thisTile = (LetterTile)this.getChildren().get(i);
         
         // Same letter in same spot
         if (answer.charAt(i) == guess.charAt(i))
         {
            thisTile.setCorrect();
         }
         // Letter in word, in a different spot
         else if (answer.indexOf(guess.charAt(i)) != -1)
         {
            thisTile.setMisplaced();
         }
         else
         {
            thisTile.setIncorrect();
         }
         
      }
      
      // Win condition
      if (answer.equals(guess)) return true;
      
      return false;
   }
}