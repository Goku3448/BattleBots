/*
    menu.java - handles ALL of the gui or javafx specific stuff besides the animations (visuals.java)
*/

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.application.Platform;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import java.lang.Thread;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.image.*;
import javafx.scene.transform.Rotate;

public class menu extends Application {

   // Create a TextArea to printout the commandline
   static TextArea output = new TextArea();

  
   // Wraps rosterFight in a Thread and runnable
   public class rosterFightRunnable implements Runnable {

       // parameters for the roster fight
       private roster one;
       private roster two;

       // constructor for the roster fight
       public rosterFightRunnable(roster one, roster two){
           this.one = one;
           this.two = two;
       }

       // the thread run that actually runs the fight
       public void run(){
          rosterHandler.rosterFight(one, two);
          

       }
   }

    // launch the application
    public void start(Stage s)
    {

        // set title for the stage
        s.setTitle("Battle Bots");

        // create a button
        Button a = new Button("Create Robot");
        Button c = new Button("Robot Summary");
        Button d = new Button("Exit");
        Button e = new Button("Fight");
        Button f = new Button("Delete Robots");
        Button h = new Button("Create Roster");
        Button i = new Button("Roster Summary");
        Button l = new Button("Delete Roster");

        // create a label
        //Label spacer = new Label(" ");
        //Label spacer1 = new Label(" ");

        // create a stack pane
        GridPane r = new GridPane();

        GridPane root = new GridPane();
        root.setHgap(8);
        root.setVgap(8);
        root.setPadding(new Insets(5));

        // set spacing in between buttons in stack pane
        r.setVgap(50);
        r.setHgap(50);

        // action event: create Robot
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                // Calls a new window
                creationGUI(s);
            }
        };

        // Action event: robot summary
        EventHandler<ActionEvent> event2 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
               // Creates a thread with the method call botInfo
               //robotSummaryThread robotSummaryThread = new robotSummaryThread();
               //robotSummaryThread.start();

               robotSummaryGUI(s);
            }
        };

        // action event: exit program
        EventHandler<ActionEvent> event3 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                System.exit(0);
            }
        };

        // action event: have a popup window that gets all the information for the fight and then starts it
        EventHandler<ActionEvent> event4 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
               // Notify user on how to operate this window 
               outputCommands("\nFight - Select two rosters and have them battle!");
               outputCommands("During the fight each robot will fight 1 other robot (according to roster positions)");
               outputCommands("for a total of 5 matches the player with 3 win or more wins WINS THE MATCH");
               
               outputCommands("Game rules are simple the robots will fight following the attributes, special behaviors, and natures you picked");
               outputCommands("The first robot to be hit to less than or equal to 0 hit points gets destroyed and loses that match");
               
               outputCommands("Which robot starts the match will be determined with a coin flip!!!!");
                
               // get all the rosters as a string array for the drop down menu
               String[] allRosters = rosterHandler.getAllRostersHelper();

               // set up the grid for all the elements
               GridPane fightGrid = new GridPane();
               fightGrid.setPadding(new Insets(20, 20, 20, 20));
               fightGrid.setVgap(8);
               fightGrid.setHgap(10);

               // the label for player 1 roster header (to inform the user what the drop down is for)
               Label labelPlayer1 = new Label("Player 1 roster");
               GridPane.setConstraints(labelPlayer1, 0, 0);

               // set up the drop down menu for player 1 roster selection
               ChoiceBox<String> player1 = new ChoiceBox<>();
               player1.setValue(allRosters[0]);
               player1.getItems().addAll(allRosters);
               GridPane.setConstraints(player1, 0, 1);

               // the label for player 2 roster header (to inform the user what the drop down is for)
               Label labelPlayer2 = new Label("Player 2 roster");
               GridPane.setConstraints(labelPlayer2, 1, 0);

               // set up the drop down menu for player 2 roster selection
               ChoiceBox<String> player2 = new ChoiceBox<>();
               player2.setValue(allRosters[0]);
               player2.getItems().addAll(allRosters);
               GridPane.setConstraints(player2, 1, 1);

               // set up the fight button
               Button startFightButton = new Button("FIGHT");
               GridPane.setConstraints(startFightButton, 1, 2);
               
               // set up the exit button
               Button exitButton = new Button("Exit");
               GridPane.setConstraints(exitButton, 0, 2);
               
               // declare and initialize the stage
               Stage fightWindow = new Stage();
               // attach the GridPane to the stage
               Scene secondScene = new Scene(fightGrid, 350, 200);
               fightWindow.setTitle("Fight");
               fightWindow.setScene(secondScene);

               // set the fight buttons action
               startFightButton.setOnAction(event -> startFight(player1, player2, fightWindow));

               // attach the elements to the GridPane
               fightGrid.getChildren().addAll(startFightButton, player1, player2, labelPlayer1, labelPlayer2, exitButton);
               
               // set exit button to close the fight gui window
               exitButton.setOnAction(actionEvent -> fightWindow.close());
               
               // show the fight window
               fightWindow.show();
               

			}

         // startFightButtons action that takes everything including hte stage so it can close the window after running Fight
         private void startFight(ChoiceBox<String> player1, ChoiceBox<String> player2, Stage fightWindow){
             // get the player 1 and 2 roster selection
             roster one = rosterHandler.loadSpecificRoster(player1.getValue());
             roster two = rosterHandler.loadSpecificRoster(player2.getValue());

             // start up the match
             Thread rosterFightThread = new Thread(new rosterFightRunnable(one, two));
             rosterFightThread.start();
             fightGUI(fightWindow);
             fightWindow.close();

         }
        };

        /*// Action event: delete robots
        EventHandler<ActionEvent> event5 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent f) {
               // Creates a thread with the method call deleteRobots
               deleteRobotThread deleteRobotThread = new deleteRobotThread();
               deleteRobotThread.start();
            }
        };*/


        // Action event: delete robots
        EventHandler<ActionEvent> event5 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent f) {
                // Notify user on how to operate this window 
               outputCommands("\nDelete Robot - Select a robot and it will be deleted (can't be undone)");
               //get all bots in an array
               String[] robots = robotHandler.getAllRobotsHelper();

               for(int ind = 0; ind < robots.length; ind++){
                  robots[ind] = robots[ind].replace(".txt","");
               }


               //layout
               GridPane layout = new GridPane();
               layout.setVgap(8);
               layout.setHgap(8);
               layout.setPadding(new Insets(20,20,20,20));

               // Creates scene
               Scene secondScene = new Scene(layout, 300, 200);

               // New Window
               Stage createStage = new Stage();
               createStage.setTitle("Robot Deletion");
               createStage.setScene(secondScene);

               // Shows window
		         createStage.show();

               //buttons
               Button deleteRobot = new Button("DELETE");
               Button exit = new Button("Exit");


               //choice boxes
               Label rN = new Label("Robot to Delete:");
               ChoiceBox<String> robotName = new ChoiceBox<>();
               robotName.setValue(robots[0]);
               robotName.getItems().addAll(robots);


               //add objects to window
               layout.getChildren().add(rN);
               layout.getChildren().add(robotName);
               layout.getChildren().add(deleteRobot);
               layout.getChildren().add(exit);

               //placement
               layout.setConstraints(rN,1,0);
               layout.setConstraints(robotName,1,1,2,1);
               layout.setConstraints(deleteRobot,5,11);
               layout.setConstraints(exit,7, 11);


                EventHandler<ActionEvent> delete = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {


                robotHandler.deleteRobots(robotName.getSelectionModel().getSelectedItem());
                createStage.close();

               }
            };


               //actions
               exit.setOnAction(actionEvent -> createStage.close());
               deleteRobot.setOnAction(delete);



            }
        };

         // action event: create roster
        EventHandler<ActionEvent> event7 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent h) {
                // Notify user on how to operate this window 
               outputCommands("\nCreate Roster - Select 5 different robots (can have repeats) to make into a roster");
               
               //get all bots in a array
               String[] robots = robotHandler.getAllRobotsHelper();
               String[] fileNames = new String[5];

               for(int ind = 0; ind < robots.length; ind++){
                  robots[ind] = robots[ind].replace(".txt","");
               }

               //layout
               GridPane layout = new GridPane();
               layout.setVgap(8);
               layout.setHgap(8);
               layout.setPadding(new Insets(20,20,20,20));

               // Creates scene
               Scene secondScene = new Scene(layout, 500, 500);
               //secondScene.setFill(Color.GREY);

               // New Window
               Stage createStage = new Stage();
               createStage.setTitle("Roster Creation");
               createStage.setScene(secondScene);

               // Shows window
		         createStage.show();

               //buttons
               Button createRoster = new Button("Create Roster");
               Button exit = new Button("Exit");


               // Creating text fields with prompt text
               Label rN = new Label("Roster Name:");
               TextField rosterName = new TextField();
               //String name = rosterName.getText();

               //choice boxes
               Label robo1 = new Label("Robot Selection 1");
               ChoiceBox<String> r1 = new ChoiceBox<>();
               r1.setValue(robots[0]);
               r1.getItems().addAll(robots);
               //listener
               int selection1 = r1.getSelectionModel().getSelectedIndex();


               Label robo2 = new Label("Robot Selection 2");
               ChoiceBox<String> r2 = new ChoiceBox<>();
               r2.setValue(robots[0]);
               r2.getItems().addAll(robots);
               //listener
               int selection2 = r2.getSelectionModel().getSelectedIndex();


               Label robo3 = new Label("Robot Selection 3");
               ChoiceBox<String> r3 = new ChoiceBox<>();
               r3.setValue(robots[0]);
               r3.getItems().addAll(robots);
               //listener
               int selection3 = r3.getSelectionModel().getSelectedIndex();


               Label robo4 = new Label("Robot Selection 4");
               ChoiceBox<String> r4 = new ChoiceBox<>();
               r4.setValue(robots[0]);
               r4.getItems().addAll(robots);
               //listener
               int selection4 = r4.getSelectionModel().getSelectedIndex();


               Label robo5 = new Label("Robot Selection 5");
               ChoiceBox<String> r5 = new ChoiceBox<>();
               r5.setValue(robots[0]);
               r5.getItems().addAll(robots);
               //listener
               int selection5 = r5.getSelectionModel().getSelectedIndex();


               //add objects to window
               layout.getChildren().add(rN);
               layout.getChildren().add(rosterName);
               layout.getChildren().add(robo1);
               layout.getChildren().add(r1);
               layout.getChildren().add(robo2);
               layout.getChildren().add(r2);
               layout.getChildren().add(robo3);
               layout.getChildren().add(r3);
               layout.getChildren().add(robo4);
               layout.getChildren().add(r4);
               layout.getChildren().add(robo5);
               layout.getChildren().add(r5);
               layout.getChildren().add(createRoster);
               layout.getChildren().add(exit);


               //placement
               layout.setConstraints(rN,1,0);
               layout.setConstraints(rosterName,1,1,2,1);
               layout.setConstraints(robo1,1,5);
               layout.setConstraints(r1,1,6,6,1);
               layout.setConstraints(robo2,1,9);
               layout.setConstraints(r2,1,10,10,1);
               layout.setConstraints(robo3,1,13);
               layout.setConstraints(r3,1,14,14,1);
               layout.setConstraints(robo4,1,17);
               layout.setConstraints(r4,1,18,18,1);
               layout.setConstraints(robo5,1,21);
               layout.setConstraints(r5,1,22,22,1);
               layout.setConstraints(createRoster,15,23);
               layout.setConstraints(exit,16, 23);

            EventHandler<ActionEvent> createAction = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                if(rosterHandler.isNameTaken(rosterName.getText())){
                   menu.outputCommands("The name entered is already taken. Input another one.");
                }
                else{
                fileNames[0] = r1.getSelectionModel().getSelectedItem().replace(".txt","");
                fileNames[1] = r2.getSelectionModel().getSelectedItem().replace(".txt","");
                fileNames[2] = r3.getSelectionModel().getSelectedItem().replace(".txt","");
                fileNames[3] = r4.getSelectionModel().getSelectedItem().replace(".txt","");
                fileNames[4] = r5.getSelectionModel().getSelectedItem().replace(".txt","");
                
                rosterHandler.createRoster(rosterName.getText(), fileNames);
                createStage.close();

                }


               }
            };

         //actions
         exit.setOnAction(actionEvent -> createStage.close());
         createRoster.setOnAction(createAction);



            }
        };


        // action event: roster summary
        EventHandler<ActionEvent> event8 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent i) {
               // Creates a thread with the method call printRoster
               //rosterSummaryThread rosterSummaryThread = new rosterSummaryThread();
               //rosterSummaryThread.start();

               rosterSummaryGUI(s);

            }
        };

        // action event: delete roster
        EventHandler<ActionEvent> event11 = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent l) {
                // Notify user on how to operate this window 
                outputCommands("\nDelete Roster - Select a roster and it will be deleted (can't be undone)");
                
               //get all bots in an array
               String[] rosters = rosterHandler.getAllRostersHelper();

               for(int ind = 0; ind < rosters.length; ind++){
                  rosters[ind] = rosters[ind].replace(".txt","");
               }


               //layout
               GridPane layout = new GridPane();
               layout.setVgap(8);
               layout.setHgap(8);
               layout.setPadding(new Insets(20,20,20,20));

               // Creates scene
               Scene secondScene = new Scene(layout, 300, 200);

               // New Window
               Stage createStage = new Stage();
               createStage.setTitle("Roster Deletion");
               createStage.setScene(secondScene);

               // Shows window
		         createStage.show();

               //buttons
               Button deleteRoster = new Button("DELETE");
               Button exit = new Button("Exit");


               //choice boxes
               Label rN = new Label("Roster to Delete:");
               ChoiceBox<String> rosterName = new ChoiceBox<>();
               rosterName.setValue(rosters[0]);
               rosterName.getItems().addAll(rosters);

               //add objects to window
               layout.getChildren().add(rN);
               layout.getChildren().add(rosterName);
               layout.getChildren().add(deleteRoster);
               layout.getChildren().add(exit);

               //placement
               layout.setConstraints(rN,1,0);
               layout.setConstraints(rosterName,1,1,2,1);
               layout.setConstraints(deleteRoster,5,11);
               layout.setConstraints(exit,7, 11);


                EventHandler<ActionEvent> delete = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {


                rosterHandler.deleteRosters(rosterName.getSelectionModel().getSelectedItem());
                createStage.close();

               }
            };


               //actions
               exit.setOnAction(actionEvent -> createStage.close());
               deleteRoster.setOnAction(delete);

            }
        };


        // when button is pressed event will happen
        a.setOnAction(event);
        c.setOnAction(event2);
        d.setOnAction(event3);
        e.setOnAction(event4);
        f.setOnAction(event5);
        h.setOnAction(event7);
        i.setOnAction(event8);
        l.setOnAction(event11);

        // addition of buttons (order matters in this case, they will print out in the order they are below)
        r.getChildren().add(e);
        r.getChildren().add(a);
        r.getChildren().add(c);
        r.getChildren().add(l);
        r.getChildren().add(f);
        r.getChildren().add(h);
        r.getChildren().add(i);
        r.getChildren().add(d);




        //added
        root.add(output,0,1);
        root.add(r,0,3);


        r.getColumnConstraints().add(new ColumnConstraints(0));   // column 0 is 100 wide
        r.getColumnConstraints().add(new ColumnConstraints(110)); // column 1 is 100 wide
        r.getColumnConstraints().add(new ColumnConstraints(110)); // column 2 is 100 wide
        r.getColumnConstraints().add(new ColumnConstraints(110)); // column 3 is 100 wide
        r.getColumnConstraints().add(new ColumnConstraints(110)); // column 4 is 100 wide
        r.getColumnConstraints().add(new ColumnConstraints(0));   // column 5 is 100 wide

        //button positionint (label, column, row)
        r.setConstraints(e, 1, 1);
        r.setConstraints(a, 2, 1);
        r.setConstraints(c, 3, 1);
        r.setConstraints(l, 4, 1);
        r.setConstraints(f, 1, 2);
        r.setConstraints(h, 2, 2);
        r.setConstraints(i, 3, 2);
        r.setConstraints(d, 4, 4);

        //button sizing (length, height)
        a.setMaxSize(150, 100);
        c.setMaxSize(150, 100);
        d.setMaxSize(150, 100);
        e.setMaxSize(150, 100);
        f.setMaxSize(150, 100);
        h.setMaxSize(150, 100);
        i.setMaxSize(150, 100);
        l.setMaxSize(150, 100);

        //Sets the size of the textArea
        output.setPrefHeight(300);
        output.setPrefWidth(75);

        
        // create a scene, changes the size of the pane that was created
        Scene sc = new Scene(root, 700, 650);

        // set the scene
        s.setScene(sc);

        // makes scene visible
        s.show();
        
        // Notify the user of basic information regarding BattleBots usage
        outputCommands("WELCOME");
        outputCommands("If at any point you're confused helpful messages will appear here!!!!");
        outputCommands("");
        
        outputCommands("Fight - here you will select two rosters and have them battle");
        outputCommands("Create Robot - here you will create your robot with it's associated attributes, special behaviors, and nature");
        outputCommands("Robot Summary - Select a robot and get a summary printed to this window of it's attributes, special behavior, and nature");
        outputCommands("Delete Roster - Select a roster and it will be deleted (can't be undone)");
        outputCommands("Delete Robots - Select a robot and it will be deleted (can't be undone)");
        outputCommands("Create Roster - Select 5 different robots (can have repeats) to make into a roster");
        outputCommands("Roster Summary - Select a roster and all the robots inside will have their summary printed to this window");
    }

    /*
        creationGUI - Makes the entire window for the creation of a robot and handles all of the associated actions
    */
    public static void creationGUI(Stage s){
        
      outputCommands("\n");
      // Notify the user of how creation of a robot works and information regarding it
      outputCommands("Welcome to the robot creator\n");
        
      // Explain briefly all information regarding robots
      outputCommands("Please select attributes that sum to 50, a special behavior and nature");
      
      outputCommands("Attributes:");
      outputCommands("Strength - scales damage");
      outputCommands("Defense - scales damage blocked");
      outputCommands("Constitution - scales max health points");
      outputCommands("Luck - scales how likely you will critical strike");
      outputCommands("");
      
      outputCommands("Special behaviors:");
      
      outputCommands("Shield block: Have a constant chance to block critical strikes but ONLY attacks that are critical.");
      outputCommands("Shield bash: Have a chance to have an additional multiplier attached to your critical strike (crits deal more).");
      outputCommands("Dodge roll: Randomly, an incoming attack will miss.");
      outputCommands("Ground slam: Buffed crit and chance for your critical strike to prevent the opponent’s next attack.");
      outputCommands("Life leech: Whenever you deal damage a certain amount of that damage is returned as health back to you.");
      outputCommands("");
      
      outputCommands("Natures:");
      
      outputCommands("Grass:");
      outputCommands("Vine whip - Smack the enemy robot with a mechanical whip that's overgrown with vegetation." + 
          "\n(randomly increase a normal attack but more so if opponents nature is weak to Grass)");
      outputCommands("Root grip - Reduce the enemies ability to defend themselves by constantly having roots grab them. \n(reduce their defend)");
      
      outputCommands("Water:");
      outputCommands("Water beam - Shoot a pressurized beam of water at the enemy robot." + 
          "\n(randomly increase a normal attack but more so if opponents nature is weak to Water)");
      outputCommands("Slippy ground - Reduce the enemies ability to attack by making them slip. (reduce their strength)");
      
      outputCommands("Fire:");
      outputCommands("Fire punch - Punch the enemy robot with a flame fist." + 
          "\n(randomly increase a normal attack but more so if opponents nature is weak to Fire)");
      outputCommands("Burns - Reduce the enemies constitution by covering them in burns. (reduce their constitution)");
      
      outputCommands("\n");
      

      GridPane secondaryLayout = new GridPane();
      secondaryLayout.setHgap(8);
      secondaryLayout.setVgap(8);
      secondaryLayout.setPadding(new Insets(5));

      // Creates scene
      Scene secondScene = new Scene(secondaryLayout, 700, 650);

      // New Window
      Stage createStage = new Stage();
      createStage.setTitle("Robot Creation");
      createStage.setScene(secondScene);

      // Shows window
		createStage.show();

      // Creating buttons
      Button closeWindow = new Button("Exit");
      Button createSave = new Button("Create/Save");

      // Creating text fields with prompt text
      Label rN = new Label("Robot Name:");
      TextField robotName = new TextField();

      Label sV = new Label("Strength Value:");
      TextField strengthValue = new TextField();

      Label dV = new Label("Defense Value:");
      TextField defenseValue = new TextField();

      Label cV = new Label("Constitution Value:");
      TextField constitutionValue = new TextField();

      Label lV = new Label("Luck Value:");
      TextField luckValue = new TextField();

      TextField infoArea = new TextField();
      infoArea.setPromptText("Messages: ");

      // Creating drop-down boxes
      Label cB = new Label("Special Behavior");
      ChoiceBox<String> choiceBox = new ChoiceBox();
      // Set a default value for drop-down menu
      choiceBox.setValue("Select");
      
      Label natureCB = new Label("Nature");
      ChoiceBox<String> natureBox = new ChoiceBox();
      // Set a default value for drop-down menu
      natureBox.setValue("Select");
      
      // Gets robot sprites into an array to populate dropdown box 
      String[] spriteList = robotHandler.getAllSpritesHelper();
      // removes .png from sprite names in list
      for(int i = 0; i < spriteList.length; i++){
         spriteList[i] = spriteList[i].replace(".png","");
      }
      
      Label imageB = new Label("Robot selection");
      ChoiceBox<String> imageBox = new ChoiceBox();
      //Set a default value for drop-down menu
      imageBox.setValue("Select");
      imageBox.getItems().addAll(spriteList);
      
      // Creating the image view
      ImageView currentBot = new ImageView();
      currentBot.setFitWidth(200);
      currentBot.setFitHeight(200);
      currentBot.setTranslateX(0);  
      currentBot.setTranslateY(-10);        

      // Shows corresponding image when its picked from dropdown menu
      imageBox.setOnAction(d->{
         Image robotImage = null;
         String robotSpriteLocation = "";
         robotSpriteLocation += System.getProperty("user.dir");
         robotSpriteLocation += "/robot_sprites_GUI/";
         switch (spriteList[imageBox.getSelectionModel().getSelectedIndex()]){
            case "MonitorHead": robotImage = new Image("file:" + robotSpriteLocation + "/MonitorHead.png");
                    break;
            case "robotSprite": robotImage = new Image("file:" + robotSpriteLocation + "/robotSprite.png");
                    break;
            case "robotSprite1": robotImage = new Image("file:" + robotSpriteLocation + "/robotSprite1.png");
                    break;
            case "robotSprite2": robotImage = new Image("file:" + robotSpriteLocation + "/robotSprite2.png");
                    break;
            case "robotSprite3": robotImage = new Image("file:" + robotSpriteLocation + "/robotSprite3.png");
                    break;
            case "Tina": robotImage = new Image("file:" + robotSpriteLocation + "/Tina.png");
                    break;
            case "VegetaBot": robotImage = new Image("file:" + robotSpriteLocation + "/VegetaBot.png");
                    break;
            case "Watt": robotImage = new Image("file:" + robotSpriteLocation + "/Watt.png");
                    break;
                    
         }
         currentBot.setImage(robotImage);
         
      });
      
      // Robot Name text field event
      EventHandler<ActionEvent> createRobot = new EventHandler<ActionEvent>() {
         public void handle(ActionEvent e) {

            // Checks if the textfields are not empty, when empty it will tell user
            if((robotName.getText() != null && !robotName.getText().isEmpty()) && (strengthValue.getText() != null && !strengthValue.getText().isEmpty()) &&
               (defenseValue.getText() != null && !defenseValue.getText().isEmpty()) && (constitutionValue.getText() != null && !constitutionValue.getText().isEmpty())
               && (luckValue.getText() != null && !luckValue.getText().isEmpty())){

               // Setting values from TextField to new variables
               String name = robotName.getText();
               int strengthV = Integer.parseInt(strengthValue.getText());
               int defenseV = Integer.parseInt(defenseValue.getText());
               int constitutionV = Integer.parseInt(constitutionValue.getText());
               int luck = Integer.parseInt(luckValue.getText());

               // Getting total points of attributes
               int total = strengthV + defenseV + constitutionV + luck;

               // Index selection from dropdown boxes
               int specialBehavior = choiceBox.getSelectionModel().getSelectedIndex();
               int nature = natureBox.getSelectionModel().getSelectedIndex();
               
               // Sprite selection
               int sprite = imageBox.getSelectionModel().getSelectedIndex();
               String selectedSprite = imageBox.getSelectionModel().getSelectedItem();

               if((specialBehavior == -1) || (nature == -1) || (sprite == -1)){
               infoArea.setText("Please select a special behavior");
               }
               else{

                  // Calls "isNameTaken" if taken we ask for a differen robot name
                  if(robotHandler.isNameTaken(name)){
                     infoArea.setText("Sorry, name is already taken!!!");
                  }
                  else{
                     // Checks that total of attributes is 50
                     if(total != 50){
                        infoArea.setText("Total of attributes is not equal to 50.");
                     }
                     else{
                        // Sends values to "createRobot"
                        robotHandler.createRobot(name, strengthV, defenseV, constitutionV, luck, specialBehavior, nature, selectedSprite);

                        // Closes current window
                        createStage.close();
                     }// end of inner else
                  }// end of outer else
               }// end of outer if
             }
            else{
               infoArea.setText("A field is empty!");
            }

         }
      };

      EventHandler<ActionEvent> closeWindowEvent = new EventHandler<ActionEvent>() {
         public void handle(ActionEvent e) {
            // Closes the second window
            createStage.close();
         }
      };

      // Links button to event
      createSave.setOnAction(createRobot);
      closeWindow.setOnAction(closeWindowEvent);

      // getItems returns the OvservableList object which you can add items to
      choiceBox.getItems().add("Shield block");
      choiceBox.getItems().add("Shield bash");
	  choiceBox.getItems().add("Dodge roll");
      choiceBox.getItems().add("Ground slam");
      choiceBox.getItems().add("Life Leech");
      
      // ObservableList object for Gen 2 Natures
      natureBox.getItems().add("Grass");
      natureBox.getItems().add("Water");
      natureBox.getItems().add("Fire");

      // Adds objects to window
      secondaryLayout.getChildren().add(rN);
      secondaryLayout.getChildren().add(robotName);
      secondaryLayout.getChildren().add(sV);
      secondaryLayout.getChildren().add(strengthValue);
      secondaryLayout.getChildren().add(dV);
      secondaryLayout.getChildren().add(defenseValue);
      secondaryLayout.getChildren().add(cV);
      secondaryLayout.getChildren().add(constitutionValue);
      secondaryLayout.getChildren().add(lV);
      secondaryLayout.getChildren().add(luckValue);
      secondaryLayout.getChildren().add(cB);
      secondaryLayout.getChildren().add(choiceBox);
      secondaryLayout.getChildren().add(infoArea);
      secondaryLayout.getChildren().add(createSave);
      secondaryLayout.getChildren().add(closeWindow);
      secondaryLayout.getChildren().add(natureCB);
      secondaryLayout.getChildren().add(natureBox);
      secondaryLayout.getChildren().add(imageB);
      secondaryLayout.getChildren().add(imageBox);
      secondaryLayout.getChildren().add(currentBot);

      // Objects placement (columnIndex, rowIndex, columnSpan, rowSpan)
      secondaryLayout.setConstraints(rN,1,0);
      secondaryLayout.setConstraints(robotName, 1, 1, 2, 1);
      secondaryLayout.setConstraints(sV,1,8);
      secondaryLayout.setConstraints(strengthValue, 1, 9);
      secondaryLayout.setConstraints(dV,1,10);
      secondaryLayout.setConstraints(defenseValue, 1, 11);
      secondaryLayout.setConstraints(cV, 1, 12);
      secondaryLayout.setConstraints(constitutionValue, 1, 13);
      secondaryLayout.setConstraints(lV,1,14);
      secondaryLayout.setConstraints(luckValue, 1,15);
      secondaryLayout.setConstraints(cB, 4, 0);
      secondaryLayout.setConstraints(choiceBox, 4, 1, 2, 1);
      secondaryLayout.setConstraints(infoArea, 4, 11, 2, 3);
      secondaryLayout.setConstraints(createSave, 4, 15);
      secondaryLayout.setConstraints(closeWindow, 5, 15);
      secondaryLayout.setConstraints(natureCB, 4, 2);
      secondaryLayout.setConstraints(natureBox, 4, 3, 2, 1);
      secondaryLayout.setConstraints(imageB, 1,2);
      secondaryLayout.setConstraints(imageBox, 1, 3, 2, 1);
      secondaryLayout.setConstraints(currentBot, 1, 5, 1, 3);


      // Object size in window
      robotName.setMaxSize(Double.MAX_VALUE, 50);
      choiceBox.setMaxSize(Double.MAX_VALUE, 50);
      natureBox.setMaxSize(Double.MAX_VALUE, 50);
      imageBox.setMaxSize(Double.MAX_VALUE, 50);
      infoArea.setMaxSize(Double.MAX_VALUE, 80);
      closeWindow.setMaxSize(100, 30);
      createSave.setMaxSize(100, 30);

      // Constrains on columns
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(30));   // column 0 is 30 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(150));  // column 1 is 150 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(50));   // column 2 is 50 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(100));  // column 3 is 100 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(100));  // column 4 is 100 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(160));  // column 5 is 160 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(0));    // column 6 is 0 wide


      //Constrains on rows
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 0 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(50));   // row 1 is 50 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 2 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(50));   // row 3 is 50 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 4 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(50));   // row 5 is 50 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(50));   // row 6 is 50 lengt
      secondaryLayout.getRowConstraints().add(new RowConstraints(50));   // row 7 is 50 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 8 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(25));   // row 9 is 25 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 10 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(25));   // row 11 is 25 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 12 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(25));   // row 13 is 25 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 14 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(25));   // row 15 is 25 length


    }

    // Prints out summary for selected robot
     public static void robotSummaryGUI(Stage s){
      

      GridPane secondaryLayout = new GridPane();
      secondaryLayout.setHgap(8);
      secondaryLayout.setVgap(8);
      secondaryLayout.setPadding(new Insets(5));
      
      // Notify user on how to operate this window 
      outputCommands("\nRobot Summary - Select a robot and get a summary printed to the main window of it's attributes, special behavior, and nature");

      // Creates scene
      Scene secondScene = new Scene(secondaryLayout, 500, 400);

      // New Window
      Stage createStage = new Stage();
      createStage.setTitle("Robot Summary");
      createStage.setScene(secondScene);

      // Shows window
      createStage.show();

      // Creating buttons
      Button closeWindow = new Button("Exit");
      Button summary = new Button("Summary");

      // Gets created robots into an array
      String[] robotList = robotHandler.getAllRobotsHelper();
      // removes .txt from robot names in list
      for(int i = 0; i < robotList.length; i++){
         robotList[i] = robotList[i].replace(".txt","");
      }

      // Creates choice box that has all robots created
      Label selection = new Label("Select Robot");
      ChoiceBox<String> robotSelection = new ChoiceBox<>();
      robotSelection.setValue("Select");
      robotSelection.getItems().addAll(robotList);

      // Adds objects to window
      secondaryLayout.getChildren().add(closeWindow);
      secondaryLayout.getChildren().add(summary);
      secondaryLayout.getChildren().add(robotSelection);
      secondaryLayout.getChildren().add(selection);

      // Objects placement (columnIndex, rowIndex, columnSpan, rowSpan)
      secondaryLayout.setConstraints(closeWindow,3,4);
      secondaryLayout.setConstraints(summary,2,4);
      secondaryLayout.setConstraints(selection,1,1);
      secondaryLayout.setConstraints(robotSelection,1,2);

      // Object size in window
      robotSelection.setMaxSize(150,30);
      closeWindow.setMaxSize(100, 30);
      summary.setMaxSize(100, 30);

      // Constrains on columns
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(30));   // column 0 is 30 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(200));  // column 1 is 150 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(100));  // column 2 is 50 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(100));  // column 3 is 50 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(700));   // column 4 is 50 wide

      //Constrains on rows
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 0 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 1 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(40));   // row 2 is 50 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(235));   // row 3 is 50 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(50));   // row 4 is 50 length

      // Event handler that will print out summary of selected robot
      EventHandler<ActionEvent> roboSummary = new EventHandler<ActionEvent>(){
         public void handle(ActionEvent e){
            int selectedRobot = robotSelection.getSelectionModel().getSelectedIndex();
            robotHandler.botInfo(robotHandler.loadRobot(selectedRobot));
            createStage.close();
         }
      };

      // Event handler to close window with exit button
      EventHandler<ActionEvent> closeWindowEvent = new EventHandler<ActionEvent>() {
         public void handle(ActionEvent e) {
            // Closes the second window
            createStage.close();
         }
      };

      // Links button to event
      summary.setOnAction(roboSummary);
      closeWindow.setOnAction(closeWindowEvent);

    }

    // Prints out the summaries of robots in a selected roster
    public static void rosterSummaryGUI(Stage s){
        
      // Notify user on how to operate this window 
      outputCommands("\nRoster Summary - Select a roster and all the robots inside will have their summary printed to this window");
      GridPane secondaryLayout = new GridPane();
      secondaryLayout.setHgap(8);
      secondaryLayout.setVgap(8);
      secondaryLayout.setPadding(new Insets(5));

      // Creates scene
      Scene secondScene = new Scene(secondaryLayout, 500, 400);

      // New Window
      Stage createStage = new Stage();
      createStage.setTitle("Roster Summary");
      createStage.setScene(secondScene);

      // Shows window
      createStage.show();

      // Creating buttons
      Button closeWindow = new Button("Exit");
      Button summary = new Button("Summary");

      // Gets created rosters into an array
      String[] rosterList = rosterHandler.getAllRostersHelper();
      // removes .txt from roster names in list
      for(int i = 0; i < rosterList.length; i++){
         rosterList[i] = rosterList[i].replace(".txt","");
      }

      // Creates choice box that has all rosters created
      Label selection = new Label("Select Roster");
      ChoiceBox<String> rosterSelection = new ChoiceBox<>();
      rosterSelection.setValue("Select");
      rosterSelection.getItems().addAll(rosterList);

      // Adds objects to window
      secondaryLayout.getChildren().add(closeWindow);
      secondaryLayout.getChildren().add(summary);
      secondaryLayout.getChildren().add(rosterSelection);
      secondaryLayout.getChildren().add(selection);

      // Objects placement (columnIndex, rowIndex, columnSpan, rowSpan)
      secondaryLayout.setConstraints(closeWindow,3,4);
      secondaryLayout.setConstraints(summary,2,4);
      secondaryLayout.setConstraints(selection,1,1);
      secondaryLayout.setConstraints(rosterSelection,1,2);

      // Object size in window
      rosterSelection.setMaxSize(150,30);
      closeWindow.setMaxSize(100, 30);
      summary.setMaxSize(100, 30);

      // Constrains on columns
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(30));   // column 0 is 30 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(200));  // column 1 is 150 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(100));  // column 2 is 50 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(100));  // column 3 is 50 wide
      secondaryLayout.getColumnConstraints().add(new ColumnConstraints(700));  // column 4 is 50 wide

      //Constrains on rows
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 0 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(15));   // row 1 is 15 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(40));   // row 2 is 50 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(235));  // row 3 is 50 length
      secondaryLayout.getRowConstraints().add(new RowConstraints(50));   // row 4 is 50 length

      // Event handler that will print out summary of selected roster
      EventHandler<ActionEvent> roboSummary = new EventHandler<ActionEvent>(){
         public void handle(ActionEvent e){
            int selectedRoster = rosterSelection.getSelectionModel().getSelectedIndex();
            rosterHandler.printRoster(rosterHandler.loadRoster(selectedRoster));
            createStage.close();
         }
      };

      // Event handler to close window with exit button
      EventHandler<ActionEvent> closeWindowEvent = new EventHandler<ActionEvent>() {
         public void handle(ActionEvent e) {
            // Closes the second window
            createStage.close();
         }
      };

      // Links button to event
      summary.setOnAction(roboSummary);
      closeWindow.setOnAction(closeWindowEvent);

    }




    public static void launchGUI(){
        launch();
    }


    /*
     * This method takes in a string of text and prints it into the gui.
     * For this method to work all System.out.println has been replaced with this method call.
     * Platform.runLater gives the program time before running its contents
    */
    public static void outputCommands(String text){
      // Waits for the program to be ready before printing on gui
      Platform.runLater(() -> {
         output.appendText(text);
         output.appendText("\n");
      });
    }

    

    /* Global TextArea */
   private static TextArea battleInfo = new TextArea();
   private static TextArea bot1Info = new TextArea();
   private static TextArea bot2Info = new TextArea();

   /* Global ImageView */
   private static ImageView player1View = new ImageView();
   private static ImageView player2View = new ImageView();
   private static ImageView p1SpecialBehavior = new ImageView();
   private static ImageView p2SpecialBehavior = new ImageView();
   private static ImageView p1Effect = new ImageView();
   private static ImageView p2Effect = new ImageView();
   private static ImageView coinFlip = new ImageView();
   private static ImageView victory = new ImageView();
   private static ImageView p1BigAnimations = new ImageView();
   private static ImageView p2BigAnimations = new ImageView();


   /*
   * Creates Second window that will show the robots fight
  */
  public static void fightGUI( Stage mainStage ){

     Stage fightStage = new Stage();
     fightStage.setTitle("Fight");

     // Locks Window Size
     fightStage.setResizable(false);
     
     //sets image    
     player2View = new ImageView(); 
     p2SpecialBehavior = new ImageView();
     p2Effect = new ImageView();
     
     /* Setting up robots image view pos and size*/
     // Setting up player1
     //player1View.setImage(robotImage);
     player1View.setFitWidth(350);
     player1View.setFitHeight(350);
     player1View.setTranslateX(80);  
     player1View.setTranslateY(0);  
     
     // Setting up player2
     //player2View.setImage(robotImage);
     player2View.setFitWidth(350);
     player2View.setFitHeight(350);
     player2View.setTranslateX(20);
     player2View.setTranslateY(-350);
     
     // Mirrors image for player 2
     Rotate flipImage = new Rotate(180, Rotate.X_AXIS);
     player2View.getTransforms().add(flipImage);
     player2View.setRotate(180);   
     
     /* Setting up Special Behavior image view pos and size*/
     
     // ImageView for p1 Special Behavior 
     p1SpecialBehavior.setFitWidth(300);
     p1SpecialBehavior.setFitHeight(300);
     p1SpecialBehavior.setTranslateX(80);  
     p1SpecialBehavior.setTranslateY(0);  

     // ImageView for p2 Special Behavior   
     p2SpecialBehavior.setFitWidth(300);
     p2SpecialBehavior.setFitHeight(300);
     p2SpecialBehavior.setTranslateX(80); 
     p2SpecialBehavior.setTranslateY(-300);
     
     // Mirrors image for player 2
     p2SpecialBehavior.getTransforms().add(flipImage);
     p2SpecialBehavior.setRotate(180);     
     
     /* Setting up Effects image view pos and size*/
     
     // ImageView for p1 Effect 
     p1Effect.setFitWidth(350);
     p1Effect.setFitHeight(350);
     p1Effect.setTranslateX(80);
     p1Effect.setTranslateY(0);
     
    
     // ImageView for p2 Effect
     p2Effect.setFitWidth(350);
     p2Effect.setFitHeight(350);
     p2Effect.setTranslateX(20);
     p2Effect.setTranslateY(-350);
     
     // Mirrors image for player 2
     p2Effect.getTransforms().add(flipImage);
     p2Effect.setRotate(180);
    
     // ImageView for coinflip
     coinFlip.setFitWidth(125);
     coinFlip.setFitHeight(125);
     coinFlip.setTranslateX(385);
     coinFlip.setTranslateY(200);
     
     // ImageView for animations1
     p1BigAnimations.setFitWidth(900);
     p1BigAnimations.setFitHeight(900);
     p1BigAnimations.setTranslateX(0);
     p1BigAnimations.setTranslateY(-30);
     
     // ImageView for animations2
     p2BigAnimations.setFitWidth(900);
     p2BigAnimations.setFitHeight(900);
     p2BigAnimations.setTranslateX(20);
     p2BigAnimations.setTranslateY(-940);
     
     p2BigAnimations.getTransforms().add(flipImage);
     p2BigAnimations.setRotate(180);
        
     // ImageView for victory
     victory.setFitWidth(900);
     victory.setFitHeight(900);
     victory.setTranslateX(0);
     victory.setTranslateY(20);
     victory.setVisible(false);
     
     /* Create GridPanes */
     GridPane root = new GridPane();
     GridPane foreground = new GridPane();

     //(NO TOUCH)
     GridPane sceneBlocking = new GridPane();
     sceneBlocking.setVgap(10);

     //(NO TOUCH)
     GridPane TextBlocking = new GridPane();
     TextBlocking.setHgap(10);

     GridPane playersBlocking = new GridPane();

     /* Debuging for Panes */
     root.setGridLinesVisible(true);
     foreground.setGridLinesVisible(true);
     sceneBlocking.setGridLinesVisible(true);
     TextBlocking.setGridLinesVisible(true);
     playersBlocking.setGridLinesVisible(false);


     /* Sizing all GridPanes */
     // root
     root.getColumnConstraints().add(new ColumnConstraints(900));
     root.getRowConstraints().add(new RowConstraints(900));

     // forground
     foreground.getColumnConstraints().add(new ColumnConstraints(900));
     foreground.getRowConstraints().add(new RowConstraints(900));

     // spacing (NO TOUCH)
     sceneBlocking.getColumnConstraints().add(new ColumnConstraints(890));
     sceneBlocking.getRowConstraints().add(new RowConstraints(510));
     sceneBlocking.getRowConstraints().add(new RowConstraints(70));
     sceneBlocking.getRowConstraints().add(new RowConstraints(300));

     // sceneTextBlocking (NO TOUCH)
     TextBlocking.getColumnConstraints().add(new ColumnConstraints(440));
     TextBlocking.getRowConstraints().add(new RowConstraints(290));
     TextBlocking.getColumnConstraints().add(new ColumnConstraints(440));

     // playersBlocking
     playersBlocking.getColumnConstraints().add(new ColumnConstraints(450));
     playersBlocking.getRowConstraints().add(new RowConstraints(510));
     playersBlocking.getColumnConstraints().add(new ColumnConstraints(450));

     /* Creating backgrounds images*/
     Image genericBorder = new Image("file:" + getDirectoryOfImages() + "/temp_border.png");
     Image genericBackground = new Image("file:" + getDirectoryOfImages() + "/borderAndBackground.png");


     /* Setting backgrounds */
     foreground.setBackground(visuals.setBackground(genericBorder));
     root.setBackground(visuals.setBackground(genericBackground));

     // Setting up playersBlocking
     playersBlocking.add(p1Effect,0,0);
     playersBlocking.add(p2Effect,1,0);
     playersBlocking.add(player1View,0,0);
     playersBlocking.add(player2View,1,0);
     playersBlocking.add(p1SpecialBehavior,0,0);
     playersBlocking.add(p2SpecialBehavior,1,0);

   
     // Setting up sceneTextBlocking
     TextBlocking.add(bot1Info, 0, 0);
     TextBlocking.add(bot2Info, 1, 0);

     // Setting up sceneBlocking
     sceneBlocking.add(playersBlocking,0,0);
     sceneBlocking.add(coinFlip,0,0);
     sceneBlocking.add(TextBlocking,0,2);
     sceneBlocking.add(battleInfo, 0,1);


     /******************************
     Anything the mouse interacts with not work if something is displayed before the "SceneBlocking"
     ******************************/
     // Setting up root
     root.add(sceneBlocking, 0, 0); //Will Display under Front
     root.add(foreground, 0, 0);  //Will Display in front
     root.add(p1BigAnimations, 0, 0);
     root.add(p2BigAnimations, 0, 0);
     root.add(victory, 0, 0);

     Scene sc = new Scene(root, 900, 900);
     fightStage.setScene(sc);
     fightStage.show();

  }
   /* Gets imageView of robot one */
  public static ImageView getPlayer1(){ return player1View; }

  /* Gets imageView of robot two */
  public static ImageView getPlayer2(){ return player2View; } 

  /* Gets imageView of robot one Special Behavior */
  public static ImageView getp1SpecialBehavior(){ return p1SpecialBehavior; }
  
  /* Gets imageView of robot two Special Behavior */
  public static ImageView getp2SpecialBehavior(){ return p2SpecialBehavior; } 
  
  /* Gets imageView of robot one Effects */
  public static ImageView getp1Effects(){ return p1Effect; }
  
  /* Gets imageView of robot two Effects */
  public static ImageView getp2Effects(){ return p2Effect; }
  
  /* Gets imageView of coin */
  public static ImageView getCoinFlip(){ return coinFlip; }  
    
  /* Gets imageView of p1BigAnimations */
  public static ImageView getp1BigAnimations(){ return p1BigAnimations; }  

  /* Gets imageView of p2BigAnimations */
  public static ImageView getp2BigAnimations(){ return p2BigAnimations; }    
  
  /* Gets imageView of victory */
  public static ImageView getvictory(){ return victory; }     
   
  /*
   * Gets the directory of the images
  */
  public static String getDirectoryOfImages(){
     String rosterDirectory = "";
     // Try and get the path our rosters are saved in
     try{
        rosterDirectory = System.getProperty("user.dir");
        rosterDirectory += "/images";
     }
     // Catch NullPointerException
     catch(NullPointerException E){
        System.out.println("NullPointerException occured in getDirectoryOfRosters");
     }
     return rosterDirectory;
  }

  /*
    * Takes in a string and a integer for <0> - battleInfo, <1> - bot1Info, <2> - bot2Info.
    * The integer will pick the Textarea to print to and the string will display on gui
   */
   public static void outputCommandsFight(String text, int currentOutput){

      // Waits for the program to be ready before displaying on gui
      Platform.runLater(() -> {
         switch(currentOutput){
            case 0:
               battleInfo.clear();
               battleInfo.appendText("\t" + text);
               battleInfo.appendText("\n");
               break;

            case 1:
              bot1Info.clear();
               bot1Info.appendText("\t" + text);
               bot1Info.appendText("\n");
               break;

            case 2:
               bot2Info.clear();
               bot2Info.appendText("\t" + text);
               bot2Info.appendText("\n");
               break;

            default: System.out.print("Error there is no textarea with that name!");
         }
      });
   }

}
