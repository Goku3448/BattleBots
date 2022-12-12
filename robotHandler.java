/*
  robotHandler.java handes all usage of the robot object.
  This includes creation, saving, and loading
*/

import java.util.Scanner;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.*;
import java.io.*;

abstract class robotHandler {

    /*
      createRobot handles ALL the initialization and values associated to the creation of our robot
      Asks the user to get all details associated to their robot.
      Making sure they spend their maximum points and don't have invalid values.

    */
    public static void createRobot(String botName, int strengthV, int defenseV, int constitutionV, int luckV, int behavior, int nature, String sprite) {

        // Gets name from GUI that user wants to use for the robot
        robot myRobot = new robot(botName);

        // Gets values for attribute from user
        myRobot.modifyStrength(strengthV);
        myRobot.modifyDefense(defenseV);
        myRobot.modifyConstitution(constitutionV);
        myRobot.modifyLuck(luckV);
        myRobot.setSpecialBehavior(behavior + 1);
        myRobot.setElementType(nature + 1);
        myRobot.setRobotSprite(sprite);

        if (exportRobot(myRobot))
            menu.outputCommands("successfully saved " + myRobot.getRobotName());
        else
            menu.outputCommands("Something went wrong. Did not save " + myRobot.getRobotName());
    }


    // Prints out the summary of the robot's attributes
    public static void botInfo(robot Robot) {
        // Print some padding on the top
        menu.outputCommands("");

        // Print the header border
        menu.outputCommands("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * ");

        // Print the robots name with border
        menu.outputCommands("* Name - " + Robot.getRobotName());

        // Print out the robots sprite
        menu.outputCommands("* Robot sprite: " + Robot.getRobotSprite());
        
        // Print out the robots Nature
        menu.outputCommands("* Robot Nature: " + Robot.getElementType());

        // Print border to seperate name and other stats
        menu.outputCommands("*---------------");

        // Print all the stats of the robot
        menu.outputCommands("*  Strength - " + Robot.getStrength());
        menu.outputCommands("*  Defense - " + Robot.getDefense());
        menu.outputCommands("*  Constitution - " + Robot.getConstitution());
        menu.outputCommands("*  Luck - " + Robot.getLuck());
        menu.outputCommands("*  Special behavior - " + Robot.getSpecialBehavior());

        // Print the bottom border
        menu.outputCommands("*  *  *  *  *  *  *  *  *  *  *  *  *  *  *  * ");

        menu.outputCommands("");
    }


    /*
      getDirectoryOfRobots gets the directory that the program was initialize in and adds /robots to it
      Meaning we return the full path that our robots are saved in
      If we encountered any exception then we return a null String
    */
    public static String getDirectoryOfRobots() {

        String robotDirectory = "";

        // Try and get the path our robots are saved in
        try {
            robotDirectory = System.getProperty("user.dir");
            robotDirectory += "/robots";
        }

        // Catch SecurityException
        catch (SecurityException E) {
            menu.outputCommands("SecurityException occurs in getDirectoryOfRobots");
        }

        // Catch NullPointerException
        catch (NullPointerException E) {
            menu.outputCommands("NullPointerException occured in getDirectoryOfRobots");
        }

        // catch IllegalArgumentException
        catch (IllegalArgumentException E) {
            menu.outputCommands("IllegalArgumentException occured in getDirectoryOfRobots");
        }

        // If we failed then robotDirectory will be 0 in length so if it isn't we succeeded
        if (robotDirectory.length() != 0) {
            return robotDirectory;
        }

        // If we failed then nothings been added to the string so we return null
        else {
            return null;
        }
    }

    /*
       Takes in the name of the current robot getting created, and checks the robots directorty to see if the name exists.
       If found we return true if not we return false.
    */
    public static boolean isNameTaken(String currentRobotName) {

        boolean isRobotInDirectory = false;

        Path robotPath = Paths.get(getDirectoryOfRobots() + "/" + currentRobotName + ".txt");

        if (Files.exists(robotPath))
            isRobotInDirectory = true;

        return isRobotInDirectory;
    }

    /*
       Takes in a robot object and saves all attributes into a "robotName.txt". This will then be saved into in a
       dedicated folder called "robots".
    */
    public static Boolean exportRobot(robot Robot) {

        // Flag to notify the user of failure if failure occurs
        Boolean didItSave = false;

        // Try and save the robot into a file called "robotname.txt"
        try {
            FileWriter currentRobot = new FileWriter(new File(getDirectoryOfRobots(), Robot.getRobotName() + ".txt"));
            PrintWriter printToFile = new PrintWriter(currentRobot);

            //Saves all attributes to a .txt
            printToFile.printf(Robot.getRobotName() + "\n");
            printToFile.printf("Robot's sprite: " + Robot.getRobotSprite() + "\n");
            printToFile.printf("Robot's Nature: " + Robot.getElementType() + "\n");
            printToFile.printf("TotalPoints: " + Robot.getTotalPoints() + "\n");
            printToFile.printf("Strength: " + Robot.getStrength() + "\n");
            printToFile.printf("Defense: " + Robot.getDefense() + "\n");
            printToFile.printf("Constitution: " + Robot.getConstitution() + "\n");
            printToFile.printf("Luck: " + Robot.getLuck() + "\n");
            printToFile.printf("SpecialBehavior: " + Robot.getSpecialBehavior() + "\n");
            printToFile.close();
            didItSave = true;
        }

        //If any thing goes wrong it will throw an error
        catch (Exception e) {
            menu.outputCommands("An error occurred when saving");
        }
        // Return our flag to signify success or failure
        return didItSave;
    }

    /*
        Reads in all the information of the robot from the correct file. Includes a list and selection
        (reading all the file names inside of robots and parsing just the robots name). Validate and set the correct
        values for the robot object. Returns a robot object type. Make sure to use getDirectoryOfRobots to find the path
        to the robots folder.
    */
    public static robot loadRobot(int ind) {

        // Get the folder path and use it to create a file object
        String folderPath = getDirectoryOfRobots();
        File f = new File(folderPath);

        // Place scanned file names into a string array
        String[] robots = f.list();

        // Create a new robot object to return to user with the name of the robot chosen by the player
        robot myRobot = new robot(robots[ind].replace(".txt", ""));
        int[] attributes = new int[5];

        // Create curLine String to hold the current line from file as a string to be parsed
        String curLine = "";
        try {
            // Creating a new scanner to scan through the robot file chosen by the player
            Scanner fileScan = new Scanner(new File("" + folderPath + File.separator + robots[ind]));
            fileScan.nextLine();
            curLine = fileScan.nextLine();
            
            // get the robots sprite selection
            int locationOfSep = curLine.indexOf(":");
            curLine = curLine.substring(locationOfSep + 2);
            myRobot.setRobotSprite(curLine);
            
            // get the robots nature selection
            curLine = fileScan.nextLine();
            curLine = curLine.substring(locationOfSep + 2);
            switch(curLine){
                case "Grass Type":
                    myRobot.setElementType(1);
                    break;
                
                case "Water Type":
                    myRobot.setElementType(2);
                    break;
                
                case "Fire Type":
                    myRobot.setElementType(3);
                    break;
            }
            
            
            int attributeNum = 0;
            String specialBehav = "";

            // Go through the text file and scan the integers(attribute points)
            // from the text file into the attributes[] array
            for (int i = 0; i < 5; i++) {
                curLine = fileScan.nextLine();
                attributeNum = Integer.parseInt(curLine.substring(curLine.length() - 2).replace(" ", ""));
                attributes[i] = attributeNum;
            }

            // Read the special behavior from the saved robot
            specialBehav = fileScan.nextLine().substring(17);

            // Set the special behavior based on what was read from the "robot".txt file
            switch (specialBehav) {
                case "Shield block":
                    myRobot.setSpecialBehavior(1);
                    break;
                case "Shield bash":
                    myRobot.setSpecialBehavior(2);
                    break;
                case "Dodge roll":
                    myRobot.setSpecialBehavior(3);
                    break;
                case "Ground slam":
                    myRobot.setSpecialBehavior(4);
                    break;
                case "Life leech":
                    myRobot.setSpecialBehavior(5);
                    break;
                default:
                    System.exit(0);
                    break;
            }

            // Set myRobot attributes
            myRobot.modifyStrength(attributes[1]);
            myRobot.modifyDefense(attributes[2]);
            myRobot.modifyConstitution(attributes[3]);
            myRobot.modifyLuck(attributes[4]);

            // make sure the total attribute points equals that from the text file and that a special behavior was set
            // if they aren't, the robot has loaded in wrong, therfore barf and exit
            if ((myRobot.getTotalPoints() != attributes[0]) || (myRobot.getSpecialBehavior() == null)) {
                menu.outputCommands("Invalid load of robot!\n");
                System.exit(0);
            }
        }
        // Catch a filure and then barf
        catch (FileNotFoundException ex) {
            menu.outputCommands("That file doesn't exist!");
            System.exit(0);
        }

        // Return the robot selected by the user
        return myRobot;
    }

    public static void deleteRobots(String name) {

        String[] robotFiles = new File(getDirectoryOfRobots()).list();
        // If directory is empty return
        if (robotFiles.length == 0) {
            System.out.print("The Directory is empty.");
        }

        // Saves the robot into a file
        File file = new File(getDirectoryOfRobots() + "/" + name + ".txt");

        // Attempts to delete the robot
        try {
            file.delete();
            menu.outputCommands("deleted.");

        } catch (Exception e) {
            menu.outputCommands("Error occured in deleting.");

        }

    } // end deleteRobots


    /*
         Very simple helper method that just gets all the robots as an array string so that
         they can be listed in our drop down menu
     */
    public static String[] getAllRobotsHelper() {
        // Get the folder path and use it to create a file object
        String folderPath = getDirectoryOfRobots();
        File f = new File(folderPath);

        // Place scanned file names into a string array
        String[] robots = f.list();
        return robots;
    }
    
    public static String getDirectoryOfSprites() {

        String spriteDirectory = "";

        // Try and get the path our robots are saved in
        try {
            spriteDirectory = System.getProperty("user.dir");
            spriteDirectory += "/robot_sprites_GUI";
        }

        // Catch SecurityException
        catch (SecurityException E) {
            menu.outputCommands("SecurityException occurs in getDirectoryOfSprites");
        }

        // Catch NullPointerException
        catch (NullPointerException E) {
            menu.outputCommands("NullPointerException occured in getDirectoryOfSprites");
        }

        // catch IllegalArgumentException
        catch (IllegalArgumentException E) {
            menu.outputCommands("IllegalArgumentException occured in getDirectoryOfSprites");
        }

        // If we failed then spriteDirectory will be 0 in length so if it isn't we succeeded
        if (spriteDirectory.length() != 0) {
            return spriteDirectory;
        }

        // If we failed then nothings been added to the string so we return null
        else {
            return null;
        }
    }
    
    
    /*
         Very simple helper method that just gets all the robots sprites
     */
    public static String[] getAllSpritesHelper() {
        // Get the folder path and use it to create a file object
        String folderPath = getDirectoryOfSprites();
        File f = new File(folderPath);

        // Place scanned file names into a string array
        String[] sprites = f.list();
        return sprites;
    }
}
