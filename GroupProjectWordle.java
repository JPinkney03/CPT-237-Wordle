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
import javafx.scene.input.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;


public class GroupProjectWordle extends Application
{
   VBox guessArea;
   int guessCount = 0;
   GuessRow thisGuess;
   String targetWord = "TESTW";
   boolean gameEnded = false;
   
   List<String> dictionary;
   List<String> potential_answers;

   public void start(Stage primaryStage)
   {
      // Setting up dictionary
      try
      {
         dictionary = Files.readAllLines(Paths.get("valid-wordle-words.txt"));
         potential_answers = Files.readAllLines(Paths.get("wordle-answers-alphabetical.txt"));
      }
      catch(Exception openExec)
      {
         dictionary = new ArrayList<String>();
         potential_answers = new ArrayList<>(); //added for catching the crash if txt files don't load
      }
      System.out.println("Dictionary Length: " + dictionary.size());
      System.out.println("Potential Answers: " + potential_answers.size());
        
      // Setting up guessing area
      guessArea = new VBox();
      for(int i=0; i<6; i++)
      {
         guessArea.getChildren().add(new GuessRow());
      }
      
      //centered the guess grid
      guessArea.setSpacing(5);
      guessArea.setAlignment(Pos.CENTER);
      guessArea.setPadding(new Insets(10, 0, 20, 0));


      
      // Setting up title
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

      // Setting Scene for key press events
      Scene scene = new Scene(borderPane, 440, 650);

      //Row 1
      HBox row1 = new HBox(5);
      String keys1 = "QWERTYUIOP";
      for (char c : keys1.toCharArray()) {
         Button b = new Button(String.valueOf(c));
         b.getStyleClass().add("keyboard-button"); //css styling
         b.setPrefWidth(35);
         row1.getChildren().add(b);
         b.setOnAction(event -> {
            guessLetter(String.valueOf(c));
            });
         b.setFocusTraversable(false); // Prevents ENTER key activation
      }

      //Row 2
      HBox row2 = new HBox(5);
      String keys2 = "ASDFGHJKL";
      for (char c : keys2.toCharArray()) {
          Button b = new Button(String.valueOf(c));
          b.getStyleClass().add("keyboard-button"); //css styling
          b.setPrefWidth(35);
          row2.getChildren().add(b);
          b.setOnAction(event -> {
            guessLetter(String.valueOf(c));
            });
          b.setFocusTraversable(false); // Prevents ENTER key activation
      }
      
      //Row 3
      HBox row3 = new HBox(5);
      
      //ENTER button
      Button enterBtn = new Button("↵"); // Enter symbol hexcode: 21B5
      enterBtn.setPrefWidth(60);
      enterBtn.getStyleClass().addAll("keyboard-button", "keyboard-wide"); //css styling
      row3.getChildren().add(enterBtn);
      enterBtn.setOnAction(event -> submitGuess());
      
      //Middle keys
      String keys3 = "ZXCVBNM";
      for (char c : keys3.toCharArray()) {
          Button b = new Button(String.valueOf(c));
          b.getStyleClass().add("keyboard-button"); //css styling
          b.setPrefWidth(35);
          row3.getChildren().add(b);
          b.setOnAction(event -> {
            guessLetter(String.valueOf(c));
            });
          b.setFocusTraversable(false); // Prevents ENTER key activation
      }
      
      //DELETE button
      Button deleteBtn = new Button("⌫"); // Delete symbol hexcode: 232B
      deleteBtn.setPrefWidth(60);
      deleteBtn.getStyleClass().addAll("keyboard-button", "keyboard-wide"); //css styling
      row3.getChildren().add(deleteBtn);
      deleteBtn.setOnAction(event -> deleteLetter());
      deleteBtn.setFocusTraversable(false); // Prevents ENTER key activation
      
      //Add rows to keyboard
      keyboard.getChildren().addAll(row1, row2, row3);
      
      //Add keyboard to bottom of BorderPane
      borderPane.setBottom(keyboard);
      BorderPane.setAlignment(keyboard, Pos.CENTER);

      // Key input
      scene.setOnKeyPressed(event ->
         {
            if (event.getCode() == KeyCode.ENTER)
            { // Enter
               submitGuess();
               return;
            }
            if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE)
            { // Delete or Backspace
               deleteLetter();
               return;
            }
            String inputChar = event.getCode().getChar();
            if (inputChar.length() > 0 && Character.isLetter(inputChar.charAt(0)))
            {
               guessLetter(inputChar);
            }
         });
      
      // Setting up scene and stage
      scene.getStylesheets().add("wordle.css"); //load the style sheet
      primaryStage.setTitle("Wordle Group Project CPT-237-W34");
      primaryStage.setResizable(false);
      primaryStage.setScene(scene);
      primaryStage.show();
      
      // Starting a new game.
      newGame();
   }
   
   public void newGame()
   {
      // Reset variable
      gameEnded = false;
      guessCount = 0;
      
      // Reset guesses
      for(int i=0; i<6; i++)
      {
         GuessRow g = (GuessRow)guessArea.getChildren().get(i);
         g.reset();
      }
      thisGuess = (GuessRow)guessArea.getChildren().get(0);
      thisGuess.nowGuessing();
      
      // Next target word
      targetWord = potential_answers.get((int)(Math.random() * potential_answers.size())).toUpperCase();
      //System.out.println("Target: " + targetWord);
   }
   
   public void guessLetter(String letter)
   {
      System.out.println("Inputted: " + letter);
      thisGuess.addLetterToGuess(letter);
   }
   
   public void deleteLetter()
   {
      if (gameEnded)
      {
         newGame();
      };
      
      thisGuess.backspace();
   }
   
   public boolean wordInDictionary(String test)
   {
      test = test.toLowerCase();
      for (String s : dictionary)
      {
         if (test.equals(s)) return true;
      }
      return false;
   }
   
   public void submitGuess()
   {
      System.out.println("Guessed: " + thisGuess.guess);
      
      // Check for enough letters
      if (thisGuess.guess.length() != 5)
      {
         return;
      }
      
      if (wordInDictionary(thisGuess.guess) == false)
      {
         return;
      }
      
      // TODO: Check for dictionary
      
      if (thisGuess.adjudicateGuess(targetWord))
      {
         // Correct guess
         gameEnded = true;
         endScreen(true);
         return;
      }
      
      // Next guess
      guessCount += 1;
      
      if (guessCount > 5)
      {
         // Failure condition
         gameEnded = true;
         endScreen(false);
         return;
      }
      
      thisGuess = (GuessRow)guessArea.getChildren().get(guessCount);
      thisGuess.nowGuessing();
   }
   
   // Game over screen for if the player guesses the correct word (win) or runs out of guesses (lose)
   private void endScreen(boolean won)
{
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Game Over");

    if (won)
    {
        alert.setHeaderText("You Won!");
        alert.setContentText("Great job! The word was: " + targetWord); // Win screen congratulates player and tells the target word
    }
    else
    {
        alert.setHeaderText("You Lost!");
        alert.setContentText("Sorry, The word was: " + targetWord); // Win screen says sorry to player and tells the target word
    }
    
    ButtonType playAgainBtn = new ButtonType("Play Again");
    ButtonType quitBtn = new ButtonType("Quit");
    
    alert.getButtonTypes().setAll(playAgainBtn, quitBtn);
    
    ButtonType result = alert.showAndWait().orElse(quitBtn); // Win screen will stay up until a button is selected or the X is pressed

    if (result == playAgainBtn) // Reset game if play again is selected
    {
        newGame();
    }
    else
    {
        System.exit(0); //Exit program if quit is selected
    }


}


  
   
   public static void main(String[] args)
   {
      launch(args);
   }
}