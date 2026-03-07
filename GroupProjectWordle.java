
// Game: Wordle

// Anthoni Pineda
// Justice Earl Pinkney
// Zachary Ross

// CPT-237-W34
// Spring 2026

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.geometry.*;


public class GroupProjectWordle extends Application
{
   public void start(Stage primaryStage)
   {
      // Setting up guessing area
      VBox guessArea = new VBox();
      for(int i=0; i<5; i++)
      {
         guessArea.getChildren().add(new GuessRow());
      }
      
      // Setting up title
      // TO DO: Set up title
      Label title = new Label("WORDLE");
      title.setAlignment(Pos.CENTER);
      
      // Setting up borderPane
      BorderPane borderPane = new BorderPane();
      borderPane.setTop(title);
      borderPane.setCenter(guessArea);
      borderPane.setAlignment(guessArea, Pos.CENTER);
      
      // Setting up scene and stage
      Scene scene = new Scene(borderPane, 400, 550);
      primaryStage.setTitle("Wordle Group Project CPT-237-W34");
      primaryStage.setResizable(false);
      primaryStage.setScene(scene);
      primaryStage.show();
   }
   
   public static void main(String[] args)
   {
      launch(args);
   }
}


