import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class LetterTile extends StackPane
{
   Label letterDisplay;

   LetterTile()
   {
      letterDisplay = new Label("");
      letterDisplay.getStyleClass().add("tile");
      letterDisplay.setMinSize(50,50);
      letterDisplay.setAlignment(Pos.CENTER);
  letterDisplay.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
  
      getChildren().add(letterDisplay);
   }

   public void setLetter(String letter)
   {
      letterDisplay.setText(letter);
   }

   public void selectLetter()
   {
      setStyle("-fx-border-color: blue;");
   }

   public void unselectLetter()
   {
      setStyle("-fx-border-color: black;");
   }

   public void setDefault()
   {
      setStyle("-fx-background-color: white; -fx-border-color: black;");
   }

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
