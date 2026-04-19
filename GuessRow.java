import javafx.geometry.*;
import javafx.scene.layout.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

public class GuessRow extends HBox
{
   public String guess = "";
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
      if (guess.length() == 5) return;

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
      if (guess.length() == 0) return;

      LetterTile t;
      
      if (guess.length() < 5)
      {
         t = (LetterTile)getChildren().get(guess.length());
         t.setDefault();
      }

      guess = guess.substring(0, guess.length()-1);

      t = (LetterTile)getChildren().get(guess.length());
      t.setLetter("");
      t.selectLetter();
   }

   public boolean adjudicateGuess(String answer)
   {
      boolean[] used = new boolean[5];

      for (int i=0;i<5;i++)
      {
         int index = i;
//adds small effect
         PauseTransition pause = new PauseTransition(Duration.millis(300*i));
         pause.setOnFinished(e -> {

            LetterTile tile = (LetterTile)getChildren().get(index);
            char g = guess.charAt(index);

            if (g == answer.charAt(index))
            {
               tile.setCorrect();
               parent.updateKeyboardColor(""+g,"green");
               used[index] = true;
            }
            else
            {
               boolean found = false;

               for (int j=0;j<5;j++)
               {
                  if (!used[j] && g == answer.charAt(j))
                  {
                     found = true;
                     used[j] = true;
                     break;
                  }
               }

               if (found)
               {
                  tile.setMisplaced();
                  parent.updateKeyboardColor(""+g,"yellow"); //updates keyboard for misplaced
               }
               else
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
}
