import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class LetterTile extends StackPane
{
   Label letterDisplay;
   
//Creates a letter tile, constructor
   LetterTile()
   {
      letterDisplay = new Label("");
      letterDisplay.getStyleClass().add("tile");
      letterDisplay.setMinSize(50,50);
      letterDisplay.setAlignment(Pos.CENTER);
      letterDisplay.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
   
      getChildren().add(letterDisplay);
   }
   
//changes letterdisplay to the letter typed
   public void setLetter(String letter)
   {
      letterDisplay.setText(letter);
   }
   
//shows which lettertile is selected
   public void selectLetter()
   {
      setStyle("-fx-border-color: blue;");
   }
   
//unselects the lettertile
   public void unselectLetter()
   {
      setStyle("-fx-border-color: black;");
   }

//resets lettertile to default
   public void setDefault()
   {
      setStyle("-fx-background-color: white; -fx-border-color: black;");
   }

//The following modifies tile based on guess state
   public void setCorrect()
   {
      setStyle("-fx-background-color: #188754;");
   }

   public void setMisplaced()
   {
      setStyle("-fx-background-color: #c9b459;");
   }

   public void setIncorrect()
   {
      setStyle("-fx-background-color: #787d7d;");
   }
}
