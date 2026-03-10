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
      for(int i=0; i<6; i++)
      {
         guessArea.getChildren().add(new GuessRow());
      }
      
      //centered the guess grid
      guessArea.setSpacing(5);
      guessArea.setAlignment(Pos.CENTER);
      guessArea.setPadding(new Insets(10, 0, 20, 0));


      
      // Setting up title
      // TO DO: Set up title
      Label title = new Label("WORDLE");
      title.getStyleClass().add("title-label"); //css styling
      title.setAlignment(Pos.CENTER);
      

      
      //Hbox wrapper
      HBox titleBox = new HBox(title);
      titleBox.setAlignment(Pos.CENTER);
      titleBox.setPadding(new Insets(10));


      
      // Setting up borderPane
      BorderPane borderPane = new BorderPane();
      borderPane.setTop(titleBox);
      borderPane.setCenter(guessArea);
      borderPane.setAlignment(guessArea, Pos.CENTER);
      
      //Setting up on-screen keyboard
      VBox keyboard = new VBox(10);
      keyboard.setAlignment(Pos.CENTER);
      keyboard.setPadding(new Insets(10, 20, 20, 20)); 



      //Row 1
      HBox row1 = new HBox(5);
      String keys1 = "QWERTYUIOP";
      for (char c : keys1.toCharArray()) {
         Button b = new Button(String.valueOf(c));
         b.getStyleClass().add("keyboard-button"); //css styling
         b.setPrefWidth(35);
         row1.getChildren().add(b);
      }

      //Row 2
      HBox row2 = new HBox(5);
      String keys2 = "ASDFGHJKL";
      for (char c : keys2.toCharArray()) {
          Button b = new Button(String.valueOf(c));
          b.getStyleClass().add("keyboard-button"); //css styling
          b.setPrefWidth(35);
          row2.getChildren().add(b);
      }
      
      //Row 3
      HBox row3 = new HBox(5);
      
      //ENTER button
      Button enterBtn = new Button("ENTER");
      enterBtn.setPrefWidth(60);
      enterBtn.getStyleClass().addAll("keyboard-button", "keyboard-wide"); //css styling
      row3.getChildren().add(enterBtn);
      
      //Middle keys
      String keys3 = "ZXCVBNM";
      for (char c : keys3.toCharArray()) {
          Button b = new Button(String.valueOf(c));
          b.getStyleClass().add("keyboard-button"); //css styling
          b.setPrefWidth(35);
          row3.getChildren().add(b);
      }
      
      //DELETE button
      Button deleteBtn = new Button("DEL");
      deleteBtn.setPrefWidth(60);
      deleteBtn.getStyleClass().addAll("keyboard-button", "keyboard-wide"); //css styling
      row3.getChildren().add(deleteBtn);
      
      //Add rows to keyboard
      keyboard.getChildren().addAll(row1, row2, row3);
      
      //Add keyboard to bottom of BorderPane
      borderPane.setBottom(keyboard);
      BorderPane.setAlignment(keyboard, Pos.CENTER);


      
      // Setting up scene and stage
      Scene scene = new Scene(borderPane, 400, 650);
      scene.getStylesheets().add("wordle.css"); //load the style sheet
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