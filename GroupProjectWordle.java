// Game: Wordle

// Anthoni Pineda
// Justice Earl Pinkney
// Zachary Ross

// CPT-237-W34
// Spring 2026
  
//Importing
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.stage.*;
import javafx.geometry.*;
import java.util.*;
import java.io.*;
import java.nio.file.*;

public class GroupProjectWordle extends Application
{
//initializing variables
   VBox guessArea;
   int guessCount = 0;
   GuessRow thisGuess;
   String targetWord = "TESTW";
   boolean gameEnded = false;
   int timeLimitSec;
   int timeLeftSec;
   
//scenes
   Scene playScene;
   Scene settingsScene;

//Dictionary for words
   List<String> dictionary;
   List<String> potential_answers;

   // Manages pairs of button and value for easier calling in the future
   Map<String, Button> keyboardMap = new HashMap<>();

   public void start(Stage primaryStage)
   {
      try
      {
         dictionary = Files.readAllLines(Paths.get("valid-wordle-words.txt"));
         potential_answers = Files.readAllLines(Paths.get("valid-wordle-words.txt"));
      }
      catch(Exception e)
      {
         dictionary = new ArrayList<>();
         potential_answers = new ArrayList<>(); //added for catching the crash if txt files don't load
      }
  
  //Setting up components for playScene
      // Guess grid
      guessArea = new VBox(5);
      guessArea.setAlignment(Pos.CENTER);
   
   //Setting up guessing area
      for(int i=0; i<6; i++)
      {
         guessArea.getChildren().add(new GuessRow(this));
      }
   
      // Title + Info button
      Label title = new Label("WORDLE");
      title.getStyleClass().add("title-label"); //css styling
      title.setAlignment(Pos.CENTER);
      Button infoBtn = new Button("ℹ");
      infoBtn.setOnAction(e -> showInfo()); //Small tutorial for players
   
   //Hbox wrapper
      HBox titleBox = new HBox(10, title, infoBtn);
      titleBox.setAlignment(Pos.CENTER);
   
      // Keyboard Set Up
      VBox keyboard = new VBox(10);
      keyboard.setAlignment(Pos.CENTER);
   
      keyboard.getChildren().addAll(
         createKeyboardRow("QWERTYUIOP"),
         createKeyboardRow("ASDFGHJKL"),
         createLastRow()
         );
   
      BorderPane borderPane = new BorderPane();
      borderPane.setTop(titleBox);
      borderPane.setCenter(guessArea);
      borderPane.setBottom(keyboard);
   
      playScene = new Scene(borderPane, 440, 650);
   
      // Keyboard input event
      playScene.setOnKeyPressed(
         e -> {
            if (e.getCode() == KeyCode.ENTER) submitGuess();
            else if (e.getCode() == KeyCode.BACK_SPACE) deleteLetter();
            else if (e.getText().matches("[a-zA-Z]"))
               guessLetter(e.getText().toUpperCase());
         });
   
      // Setting up settingsScene
      Pane settingsPane = new Pane();
      
      // Start button
      Button startButton = new Button("Start");
      settingsPane.getChildren().add(startButton);
      startButton.setOnAction(e -> { // Start game action
         primaryStage.setScene(playScene);
         newGame();
      });
      
      // Difficulty buttons
      ToggleGroup group = new ToggleGroup();
      
      RadioButton easy = new RadioButton("5 minutes");
      easy.setToggleGroup(group);
      easy.setOnAction(e -> timeLimitSec = 5 * 60);
      
      RadioButton medium = new RadioButton("3 minutes");
      medium.setToggleGroup(group);
      medium.setOnAction(e -> timeLimitSec = 3 * 60);
      
      RadioButton hard = new RadioButton("1 minute");
      hard.setToggleGroup(group);
      hard.setOnAction(e -> timeLimitSec = 1 * 60);
      
      easy.setSelected(true); // Easy as default
      
      HBox difficultyHBox = new HBox();
      difficultyHBox.getChildren().addAll(hard, medium, easy);
      
      //!!Temp!! VBox
      VBox vBox = new VBox();
      vBox.getChildren().add(difficultyHBox);
      vBox.getChildren().add(startButton);
   
      // Opening to settings screen
      settingsScene = new Scene(vBox, 440, 650);
      primaryStage.setScene(settingsScene);
      primaryStage.setTitle("Wordle");
      primaryStage.show();
   }


   // KEYBOARD Frame positioning

   private HBox createKeyboardRow(String letters)
   {
      HBox row = new HBox(5);
      row.setAlignment(Pos.CENTER);
   
      for(char c : letters.toCharArray())
      {
         Button b = new Button(String.valueOf(c));
         b.setPrefWidth(35);
      
         keyboardMap.put(String.valueOf(c), b);
      
         b.setOnAction(e -> guessLetter(String.valueOf(c)));
         row.getChildren().add(b);
      }
   
      return row;
   }
//adds button to keyboard
   private HBox createLastRow()
   {
      HBox row = new HBox(5);
      row.setAlignment(Pos.CENTER);
   
      Button enter = new Button("ENTER");
      enter.setOnAction(e -> submitGuess());
   
      Button delete = new Button("⌫");
      delete.setOnAction(e -> deleteLetter());
   
      row.getChildren().add(enter);
   
      for(char c : "ZXCVBNM".toCharArray())
      {
         Button b = new Button(String.valueOf(c));
         b.setPrefWidth(35);
      
         keyboardMap.put(String.valueOf(c), b);
      
         b.setOnAction(e -> guessLetter(String.valueOf(c)));
         row.getChildren().add(b);
      }
   
      row.getChildren().add(delete);
      return row;
   }
//Updates keyboard when a letter is guessed
   public void updateKeyboardColor(String letter, String state)
   {
      Button b = keyboardMap.get(letter);
      if (b == null) 
         return;
   
      String style = b.getStyle();
   
      if (state.equals("green"))
         b.setStyle("-fx-background-color: #188754;");
      else if (state.equals("yellow") && !style.contains("#188754"))
         b.setStyle("-fx-background-color: #c9b459;");
      else if (state.equals("gray") && style.isEmpty())
         b.setStyle("-fx-background-color: #787d7d;");
   }

//logic

   public void newGame()
   {
   //game values
      guessCount = 0;
      gameEnded = false;
      timeLeftSec = timeLimitSec;
   
      keyboardMap.values().forEach(b -> b.setStyle(""));
   
   //resets all areas
      for (Node n : guessArea.getChildren())
         ((GuessRow)n).reset();
   
      thisGuess = (GuessRow)guessArea.getChildren().get(0);
      thisGuess.nowGuessing();
   
   //finds random word to set as goal
      targetWord = potential_answers.get((int)(Math.random()*potential_answers.size())).toUpperCase();
   }

//guessing letter
   public void guessLetter(String letter)
   {
      thisGuess.addLetterToGuess(letter);
   }

//removing letter
   public void deleteLetter()
   {
      thisGuess.backspace();
   }
//finds word in dictionary
   public boolean wordInDictionary(String word)
   {
      return dictionary.contains(word.toLowerCase());
   }

   public void submitGuess()
   {
   //Makes sure game is continuing and guess is valid
      if (thisGuess.guess.length() != 5) 
         return;
      if (!wordInDictionary(thisGuess.guess)) 
         return;
   
      if (thisGuess.adjudicateGuess(targetWord))
      {
         gameEnded = true;
         endScreen(true);
         return;
      }
   //increases guess count
      guessCount++;
   
      if (guessCount > 5)
      {
         gameEnded = true;
         endScreen(false);
         return;
      }
   
      thisGuess = (GuessRow)guessArea.getChildren().get(guessCount);
      thisGuess.nowGuessing();
   }

//End Screen Pop Up
   private void endScreen(boolean won)
   {
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setHeaderText(won ? "You Won!" : "You Lost!");
      alert.setContentText("Word: " + targetWord);
   
      alert.showAndWait();
      newGame();
   }

//small tutorial
   private void showInfo()
   {
      Alert a = new Alert(Alert.AlertType.INFORMATION);
      a.setHeaderText("How to Play");
      a.setContentText(
         "Guess the 5-letter word in 6 guesses!"
         );
      a.showAndWait();
   }

   public static void main(String[] args)
   {
      launch(args);
   }
}
