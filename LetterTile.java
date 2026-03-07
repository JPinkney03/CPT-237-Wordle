// NOTE: Remember to build this and any other class!!

// Game: Wordle (LetterTile class)

// Anthoni Pineda
// Justice Earl Pinkney
// Zachary Ross

// CPT-237-W34
// Spring 2026

import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.geometry.*;

public class LetterTile extends Pane
{
   static double squareSize = 50.0;
   
   // Colors
   static Color unguessedColor = Color.rgb(255, 255, 255);
   static Color correctColor = Color.rgb(24, 135, 84);
   static Color incorrectColor = Color.rgb(120, 125, 125);
   static Color misplacedColor = Color.rgb(201, 180, 89);
   
   // Constructor
   LetterTile()
   {
      // Rectangle of squareSize by squareSize
      Rectangle rectangle = new Rectangle(squareSize, squareSize);
      rectangle.setFill(unguessedColor);
      rectangle.setStroke(Color.BLACK);
      this.getChildren().add(rectangle);
      
      // Letter display
      // TO DO: Set up font
      Label letterDisplay = new Label("L");
      letterDisplay.setMinWidth(squareSize);
      letterDisplay.setMinHeight(squareSize);
      letterDisplay.setAlignment(Pos.CENTER);
      this.getChildren().add(letterDisplay);
   }
}

