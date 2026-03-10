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
}