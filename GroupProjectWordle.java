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
import javafx.scene.image.Image; //Controls Logo
import javafx.scene.image.ImageView; //Controls Logo
import java.awt.Font;
import javax.sound.sampled.*; //Processes Audio
import java.io.File;

public class GroupProjectWordle extends Application
{
//initializing variables
   VBox guessArea;
   int guessCount = 0;
   int roundScore = 0;
   GuessRow thisGuess;
   String targetWord = "TESTW";
   boolean gameEnded = false;
   boolean paused = false;
   int sessionScore = 0;

   // Timer
   Timer timer;
   int timeLimitSec; //How much time to start (Difficulty)
   int timeLeftSec;
   Label timeDisplay;
   Label scoreLabel;
    
    //Setting time according to difficulty (5/3/1 minutes)
   int easyTime = 5 * 60 + 1;
   int mediumTime = 3 * 60 + 1;
   int hardTime = 1 * 60 + 1;
   
//scenes
   Scene playScene;
   Scene mainScene;

//Dictionary for words
   List<String> dictionary;
   List<String> potential_answers;

   // Manages pairs of button and value for easier calling in the future specifically to change color
   Map<String, Button> keyboardMap = new HashMap<>();

 

//Start Method
   public void start(Stage primaryStage)
   {
      try // a try-catch just incase the file isn't opened correctly
      {
      //gets dictionary file and reads it to get list of valid words
         dictionary = Files.readAllLines(Paths.get("valid-wordle-words.txt"));
         potential_answers = Files.readAllLines(Paths.get("valid-wordle-words.txt"));
      }
      catch(Exception e)  //added for catching the crash if txt files don't load
      {
         dictionary = new ArrayList<>();
         potential_answers = new ArrayList<>();
      }
      
   //Setting up components for playScene
      // Guess grid
      guessArea = new VBox(5);
      guessArea.setAlignment(Pos.CENTER);
   
   //Setting up guessing rows for the grid
      for(int i=0; i<6; i++)
      {
         guessArea.getChildren().add(new GuessRow(this));
      }
   
      // Logo Creation
      Image logo = new Image("file:\\Users\\tonyp\\Downloads\\Wordle_Logo.png");
      ImageView titleLogo = new ImageView(logo); 
      titleLogo.setFitWidth(350);
      titleLogo.setPreserveRatio(true);
      
      //Short Tutorial Button Creation
      Button infoBtn = new Button(" Tutorial ");
      infoBtn.setFocusTraversable(false); // makes it so game does not auto select the button, messes with pressing enter
      infoBtn.setOnAction(e -> showInfo()); //Small tutorial for players
   
      // Timer display
      timeDisplay = new Label("0:00");
   
   //Hbox wrapper adding all header items
      HBox titleBox = new HBox(10, titleLogo, infoBtn, timeDisplay);
      titleBox.setAlignment(Pos.CENTER);
   
      // Keyboard Set Up
      VBox keyboard = new VBox(10);
      keyboard.setAlignment(Pos.CENTER);
   
   //Adds all letters also last row which includes enter and delete
      keyboard.getChildren().addAll(
         createKeyboardRow("QWERTYUIOP"),
         createKeyboardRow("ASDFGHJKL"),
         createLastRow()
         );
   
      //Logo for in-game 
      ImageView titleGameplay = new ImageView(logo);
      titleGameplay.setFitWidth(90);
      titleGameplay.setFitHeight(70);
      titleGameplay.setPreserveRatio(true);
   
   
      // Buttons
      Button pauseBtn = new Button("⏸"); //these are codes for specific symbols
      Button resetBtn = new Button("⟳");
      
   //making it unselectable when clicked
      pauseBtn.setFocusTraversable(false);
      resetBtn.setFocusTraversable(false);
   
   // Controls Grouping
      HBox controlBar = new HBox(30, pauseBtn, timeDisplay, resetBtn);
      controlBar.setAlignment(Pos.CENTER);
   
   // Adding Header Together
      VBox topLayout = new VBox(20, titleGameplay, controlBar);
      topLayout.setAlignment(Pos.CENTER);
   
       // Setting borderPane
      BorderPane borderPane = new BorderPane();
     // Wrap ONLY the center (grid + keyboard)
      VBox centerContent = new VBox(10, guessArea, keyboard);
      centerContent.setAlignment(Pos.CENTER);
   
   // StackPane to have pause overlay to be on top
      StackPane centerStack = new StackPane(centerContent);
   
   // Pause label to cover screen
      Label pausedLabel = new Label("PAUSED");
      pausedLabel.setStyle("-fx-font-size: 48px;");
   //Pause VBox
      VBox pauseOverlay = new VBox(pausedLabel);
      pauseOverlay.setAlignment(Pos.CENTER);
      pauseOverlay.setVisible(false);
   
   // Add overlay to center
      centerStack.getChildren().add(pauseOverlay);
   
   // Setting layout to playScene
      borderPane.setTop(topLayout);
      borderPane.setCenter(centerStack);
      borderPane.setBottom(keyboard);
      
      //Adds borderPane playScene
      playScene = new Scene(borderPane, 440, 650);
         
      // Keyboard input event to any valid input values
      playScene.setOnKeyPressed(
         e -> {
         
            if (paused || gameEnded) //ensures pause and game ended values are false to ensure valid inputs
               return;
            if (e.getCode() == KeyCode.ENTER) submitGuess();
            else if (e.getCode() == KeyCode.BACK_SPACE) deleteLetter();
            else if (e.getText().matches("[a-zA-Z]"))
               guessLetter(e.getText().toUpperCase());
         });
   
         
   
      ComboBox<String> difficultyBox = new ComboBox<>(); // Drop Down for Difficulty Selection
      difficultyBox.getItems().addAll(
         "Easy (5 min)", 
         "Medium (3 min)", 
         "Hard (1 min)"
         );
      difficultyBox.setValue("Easy (5 min)"); //Default Setting Easy Mode
      difficultyBox.setStyle("-fx-background-color: #AFE1AF"); //Default setting color
      timeLimitSec = easyTime; //default time
      
      //Adds functions to what happens when a mode is selected
      difficultyBox.setOnAction(
         e -> {
            String choice = difficultyBox.getValue();
            if(choice.contains("Easy"))
            {
               difficultyBox.setStyle("-fx-background-color: #AFE1AF");
               timeLimitSec = easyTime;
            }
            else if(choice.contains("Medium"))
            {
               difficultyBox.setStyle("-fx-background-color: #FFDD00");
               timeLimitSec = mediumTime;
            }
            else if(choice.contains("Hard"))
            { 
               difficultyBox.setStyle("-fx-background-color: #F23D3D");
               timeLimitSec = hardTime;
            }
         });
      
      // Play Button
      Button playBtn = new Button("PLAY");
      playBtn.setFocusTraversable(false);
      playBtn.setPrefWidth(200);
      playBtn.setPrefHeight(50);
      playBtn.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-background-color: #AAFF00");
   
      //Sets the game scene and starts up game
      playBtn.setOnAction(
         e -> {
            primaryStage.setScene(playScene);
         
            playSound("StartUp.wav");
            newGame();
         });
      
      //Making Menu Container into the VBox
      VBox menuContainer = new VBox(30);
      menuContainer.setAlignment(Pos.CENTER);
      
      //Session Score Inilization 
      scoreLabel = new Label("Session Score: 0");
      scoreLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-letter-spacing: 3px;");
   
   //Adds Menu Variables
      menuContainer.getChildren().addAll(
         titleLogo,
         difficultyBox,
         playBtn,
         infoBtn,
         scoreLabel
         );
   
      mainScene = new Scene(menuContainer, 440, 650);
      
           // Pause logic
      pauseBtn.setOnAction(
         e -> {
            if(paused == false){
               paused = true;
               pauseOverlay.setVisible(true); //Hides pause screen
            
            //hide game
               guessArea.setVisible(false);
               keyboard.setVisible(false);
            } else if (paused == true){
               paused = false;
               pauseOverlay.setVisible(false); //Hides pause screen
            
            //show game
               guessArea.setVisible(true);
               keyboard.setVisible(true);
            
            }
         });
   
   
   // Reset logic
      resetBtn.setOnAction(
         e -> {
            if(timer != null){
               timer.cancel(); //cancels timer
            }
         //resets everything
            paused = false;
            pauseOverlay.setVisible(false);
            guessArea.setVisible(true);
            keyboard.setVisible(true);
         
            primaryStage.setScene(mainScene);
         });
   
      paused = false;
      pauseOverlay.setVisible(false);
   
      // Opening to main menu screen
      primaryStage.setScene(mainScene);
      primaryStage.show();
   }


   // KEYBOARD Frame positioning and creation

   private HBox createKeyboardRow(String letters)
   {
      HBox row = new HBox(5);
      row.setAlignment(Pos.CENTER);
   
      for(char c : letters.toCharArray())
      {
      //letter button creation and placing
         Button b = new Button(String.valueOf(c));
         b.setFocusTraversable(false);
         b.setPrefWidth(35);
      
      //adds button to map and also string of state
         keyboardMap.put(String.valueOf(c), b);
         
      //when button is pressed guessletter called
         b.setOnAction(e -> guessLetter(String.valueOf(c)));
         row.getChildren().add(b);
      }
   
      return row;
   }
//adds button to keyboard
   private HBox createLastRow()
   {
     //letter button creation and placing
      HBox row = new HBox(5);
      row.setAlignment(Pos.CENTER);
      
         //when enter button is pressed, submit answer
      Button enter = new Button("ENTER");
      enter.setOnAction(
         e -> {
            if (paused || gameEnded)  //makes sure game state is valid
               return;
            submitGuess();
         });
   
    //creates delete button with a special icon
      Button delete = new Button("⌫");
      delete.setFocusTraversable(false);
      
    //when backspace button is pressed, delete lever
      delete.setOnAction(
         e -> {
            if (paused || gameEnded) 
               return;
            deleteLetter();
         });
   
   //Adding more letter buttons (final row)
      for(char c : "ZXCVBNM".toCharArray())
      {
         Button b = new Button(String.valueOf(c));
         b.setFocusTraversable(false);
         b.setPrefWidth(35);
      
         keyboardMap.put(String.valueOf(c), b);
      
         b.setOnAction(e -> guessLetter(String.valueOf(c)));
         row.getChildren().add(b);
      }
      
      //Adding special buttons 
      row.getChildren().add(enter);
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
      
   //changes button style based on guess state
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
   //game values default values
      guessCount = 0;
      roundScore = 0;
      gameEnded = false;
     
   // Timer setup
      timeLeftSec = timeLimitSec;
      timer = new Timer();
      
      //timer that runs till 0 or cancelled
      timer.scheduleAtFixedRate(
         new TimerTask(){
            @Override
            public void run() {
               Platform.runLater(
                  () -> {
                     if(!paused){ //Not paused then continues to count down
                        timeLeftSec--;
                     }
                     //if time left is less than 10 seconds adjusts the text label to accurately display time
                     if(timeLeftSec%60 < 10) {
                        timeDisplay.setText(String.valueOf(timeLeftSec/60) + ":0" + String.valueOf(timeLeftSec%60));
                     }
                     else {
                        timeDisplay.setText(String.valueOf(timeLeftSec/60) + ":" + String.valueOf(timeLeftSec%60));
                     }
                     //When timer hits 0 the game ends
                     if (timeLeftSec == 0) {
                        gameEnded = true;
                        roundScore = 0; //no score on timer ending
                        playSound("LoseSound.wav");
                        endScreen(false);
                     }
                  });
            }
         }, 0, 1000); // 1000ms = 1sec (fixed rate)
         
   //reset keyboard styling
      keyboardMap.values().forEach(b -> b.setStyle(""));
   
   //resets all areas
      for (Node n : guessArea.getChildren())
         ((GuessRow)n).reset();
         
   //Changes selected tile to first position
      thisGuess = (GuessRow)guessArea.getChildren().get(0);
      thisGuess.nowGuessing();
   
   //finds random word to set as goal
      targetWord = potential_answers.get((int)(Math.random()*potential_answers.size())).toUpperCase();
      
      //  !!!!!!!! TEST PURPOSE ONLY, VIEW WORD IN PRINT, REMOVE FOR FINAL !!!!!!!!
      System.out.println(targetWord);
      ///  !!!!!!!! TEST PURPOSE ONLY, VIEW WORD IN PRINT, REMOVE FOR FINAL !!!!!!!!
      
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
   //Guess is the correct word
      if (thisGuess.adjudicateGuess(targetWord))
      {
         playSound("Cheering.wav");
         gameEnded = true;
      //adds points to session score
         int points = scoreMaker(guessCount + 1);
         sessionScore += points;
         roundScore = points; //sets round score
         scoreLabel.setText("Session Score: " + sessionScore);
         
         endScreen(true);
         return;
      }
   //increases guess count
      guessCount++;
   //If max guess count is reached end game
      if (guessCount > 5)
      {
         gameEnded = true;
         playSound("LoseSound.wav");
         endScreen(false);
         roundScore = 0; //no score on max guesses
         return;
      }
      
   //gets which tile is the user is one and makes that the selected tile
      thisGuess = (GuessRow)guessArea.getChildren().get(guessCount);
      thisGuess.nowGuessing();
   }

//End Screen Pop Up
   private void endScreen(boolean won)
   {
      timer.cancel();
      
      //Adds small pop up to tell the user whether they won or lost
      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setHeaderText(won ? "You Won!" : "You Lost!");
      alert.setContentText("Word: " + targetWord + " | Points: +"+roundScore);
   
      alert.showAndWait();
     
      paused = false;
   //gets the playScene and sets it back on screen
      Stage stage = (Stage) playScene.getWindow();
      stage.setScene(mainScene);
   }

//small tutorial
   private void showInfo()
   {
      Alert a = new Alert(Alert.AlertType.INFORMATION);
      a.setHeaderText("How to Play");
      a.setContentText(
         "Guess the 5-letter word in 6 guesses within the TIME LIMIT!"
         );
      a.showAndWait();
   }

   public static void main(String[] args)
   {
      launch(args);
   }
   
   //Makes score
   private int scoreMaker(int guessesUsed)
   {
      int score;
   
   // Score depending on guess count
      switch(guessesUsed)
      {
         case 1: score = 50; 
            break;
         case 2: score = 40; 
            break;
         case 3: score = 30; 
            break;
         case 4: score = 20; 
            break;
         case 5: score = 10; 
            break;
         default: score = 10; 
            break;
      }
   
   // Difficulty multiplier
      double multiplier = 1.0;
      
   //Timelimitsec is set to a way to detect difficulty
      if(timeLimitSec == easyTime){
         multiplier = 1.0;
      }
      else if(timeLimitSec == mediumTime){
         multiplier = 1.5;
      }
      else if(timeLimitSec == hardTime){
         multiplier = 2.0;
      }
   
      return (int)(score * multiplier);
   }
   
   //Handles all sound
   public void playSound(String fileName) {
      new Thread( //Thread created to allow it to run smoothly
         () -> {
            try
            {
            //Creates a clip to play audio
               AudioInputStream audio = AudioSystem.getAudioInputStream(new File(fileName)); //reads raw audio info
               Clip clip = AudioSystem.getClip(); //handles playback of short audio
               clip.open(audio);
               clip.start();
            
            // Debounce so it doesn't end prematurely
               Thread.sleep(100);
            }
            catch(Exception e)
            {
            //if error occures prints error
               e.printStackTrace();
            }
         }).start();}
   
}

