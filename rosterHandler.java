import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;
import java.util.*;
import java.io.*;


abstract class rosterHandler {


    /*
      Takes in the name of the current roster getting created, and checks the rosters directorty to see if the name exists.
      If found we return true if not we return false.
   */
    public static boolean isNameTaken(String currentRosterName) {

        boolean isRosterInDirectory = false;

        Path rosterPath = Paths.get(getDirectoryOfRosters() + "/" + currentRosterName + ".txt");

        if (Files.exists(rosterPath))
            isRosterInDirectory = true;

        return isRosterInDirectory;
    }


    // Creates a roster with saved robots as directed by user
    public static void createRoster(String name, String[] files) {

        roster myroster = new roster(5);

        robot myrobot1 = new robot(files[0]);
        robot myrobot2 = new robot(files[1]);
        robot myrobot3 = new robot(files[2]);
        robot myrobot4 = new robot(files[3]);
        robot myrobot5 = new robot(files[4]);

        myroster.setRosterName(name);
        myroster.addRobot(myrobot1);
        myroster.addRobot(myrobot2);
        myroster.addRobot(myrobot3);
        myroster.addRobot(myrobot4);
        myroster.addRobot(myrobot5);

        saveRoster(myroster);

    } //end createRoster


    // Outputs 'botinfo' for all robots in roster
    public static void printRoster(roster R) {
        // print to let user know what is going to be printed
        menu.outputCommands("Listing all robots in this roster:");

        // iterate through the roster and print the 'botinfo' for each robot saved in the roster
        for (int i = 0; i < R.getRosterSize(); i++) {
            robotHandler.botInfo(R.getRobot(i));
        }

    }

    /* Gets the directory where the program is being run from, and adds '/rosters' to the end
     so that we will be able to return the full path of the folder where the roster
     will be saved or is saved. If any exceptions are found, a null String is returned
     */
    public static String getDirectoryOfRosters() {

        String rosterDirectory = "";

        // Try and get the path our rosters are saved in
        try {
            rosterDirectory = System.getProperty("user.dir");
            rosterDirectory += "/rosters";
        }

        // Catch SecurityException
        catch (SecurityException E) {
            menu.outputCommands("SecurityException occurs in getDirectoryOfRosters");
        }

        // Catch NullPointerException
        catch (NullPointerException E) {
            menu.outputCommands("NullPointerException occured in getDirectoryOfRosters");
        }

        // catch IllegalArgumentException
        catch (IllegalArgumentException E) {
            menu.outputCommands("IllegalArgumentException occured in getDirectoryOfRosters");
        }

        // If we failed then rosterDirectory will be 0 in length so if it isn't we succeeded
        if (rosterDirectory.length() != 0) {
            return rosterDirectory;
        }

        // If we failed then nothings been added to the string so we return null
        else {
            return null;
        }
    }

    /* Saves a roster under the name specified by user inside of the 'rosters' folder.
       The file is saved as "specifiedname.txt" and follows the following style:
       - The first line is the name of the roster as specified by the user
       - the second line gives the rosterSize
       - the third line gives the number of wins of the roster
       - the fourth line gives the number of loses of the roster
       - the fifth line gives the total number of matches of the roster
       - the sixth line says "------Robots In Roster Listed Below-------"
       - the seventh and all following lines contain just the name of each robot that is
         a part of the roster.
     */
    public static Boolean saveRoster(roster R) {

        // flag that will flag a failure in saving the roster
        Boolean didRosterSave = false;

        // 'try' to save the roster into a text file with the name "rostername.txt"
        try {
            FileWriter currentRoster = new FileWriter(new File(getDirectoryOfRosters(), R.getRosterName() + ".txt"));
            PrintWriter printToFile = new PrintWriter(currentRoster);

            // Saves all the roster attributes to a .txt
            printToFile.printf(R.getRosterName() + "\n");
            printToFile.printf("Roster size: " + R.getRosterSize() + "\n");
            printToFile.printf("Number of wins: " + R.getWins() + "\n");
            printToFile.printf("Number of loses " + R.getLoses() + "\n");
            printToFile.printf("Number of  matches: " + R.getNumMatches() + "\n");
            printToFile.printf("------Robots In Roster Listed Below-------\n");
            // Saves all names of robots in the roster to the same .txt
            for (int i = 0; i < R.getRosterSize(); i++) {
                printToFile.printf("%s\n", R.getRobot(i).getRobotName());
            }
            printToFile.close();
            didRosterSave = true;
        }

        // catch any error that could occur
        catch (Exception e) {
            menu.outputCommands("An error occurred when saving");
        }

        // returns true if save was successful, and false if it failed
        return didRosterSave;
    }

    /*
        loads a roster as choosen by the user
        Fully initializing all the indexes of the roster and validating all of the robots attributes and values
        then returns this loaded roster
    */
    public static roster loadRoster(int ind) {
        // Delcare our roster's size, name, and object along with the robot name since their needed outside certain scopes
        int rosterSize = 0;
        String rosterName = "";
        String robotName = "";
        roster toRet = null;

        // Get the folder path and use it to create a file object
        String folderPath = getDirectoryOfRosters();
        File f = new File(folderPath);

        // Place scanned file names into a string array
        String[] rosters = f.list();

        // file read in variable
        String curLine = "";

        try {
            // Creating a new scanner to scan through the robot file chosen by the player
            Scanner rosterScan = new Scanner(new File("" + folderPath + File.separator + rosters[ind]));
            // Get the rosters name
            curLine = rosterScan.nextLine();
            curLine = curLine.replace("\n", "");
            rosterName = curLine;

            // Get the rosters size
            curLine = rosterScan.nextLine();
            rosterSize = Integer.parseInt(curLine.substring(curLine.lastIndexOf(" ")).replace(" ", ""));
            toRet = new roster(rosterSize);

            // Get and set the rosters wins
            curLine = rosterScan.nextLine();
            toRet.offSetWins(Integer.parseInt(curLine.substring(curLine.lastIndexOf(" ")).replace(" ", "")));

            // Get and set the rosters loses
            curLine = rosterScan.nextLine();
            toRet.offSetLoses(Integer.parseInt(curLine.substring(curLine.lastIndexOf(" ")).replace(" ", "")));

            // Get and set the rosters matches played
            curLine = rosterScan.nextLine();
            toRet.offSetMatches(Integer.parseInt(curLine.substring(curLine.lastIndexOf(" ")).replace(" ", "")));

            // Set the rosters name
            toRet.setRosterName(rosterName);

            rosterScan.nextLine();

            // Load each robot into the roster
            for (int i = 0; i < rosterSize; i++) {
                // Get the name of the robot from the roster file
                robotName = rosterScan.nextLine().replace("\n", "");
                try {
                    // Declare needed variables to read in the robot file
                    int[] attributes = new int[5];
                    robot myRobot = new robot(robotName);

                    // Creating a new scanner to scan through the robot file chosen by the player
                    Scanner robotScan = new Scanner(new File("" + robotHandler.getDirectoryOfRobots() + File.separator + robotName + ".txt"));
                    robotScan.nextLine();
                    
                    curLine = robotScan.nextLine();
                    
                    // get the robots sprite selection
                    int locationOfSep = curLine.indexOf(":");
                    curLine = curLine.substring(locationOfSep + 2);
                    myRobot.setRobotSprite(curLine);
            
                    // get the robots nature selection
                    curLine = robotScan.nextLine();
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
                    for (int j = 0; j < 5; j++) {
                        curLine = robotScan.nextLine();
                        attributeNum = Integer.parseInt(curLine.substring(curLine.length() - 2).replace(" ", ""));
                        attributes[j] = attributeNum;
                    }

                    // Read the special behavior from the saved robot
                    specialBehav = robotScan.nextLine().substring(17);

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
                    toRet.addRobot(myRobot);
                }
                // Catch a robot file read in failure and then barf
                catch (FileNotFoundException ex) {
                    menu.outputCommands("That robot file doesn't exist!");
                    System.exit(0);
                }
            }

        }
        // Catch the roster file read in failure and then barf
        catch (FileNotFoundException ex) {
            menu.outputCommands("That roster file doesn't exist!");
            System.exit(0);
        }

        // Return the fully initialized roaster
        return toRet;
    }


    public static void deleteRosters(String name) {

        String[] rosterFiles = new File(getDirectoryOfRosters()).list();
        // If directory is empty return
        if (rosterFiles.length == 0) {
            System.out.print("The Directory is empty.");
        }

        // Saves the robot into a file
        File file = new File(getDirectoryOfRosters() + "/" + name + ".txt");

        // Attempts to delete the robot
        try {
            file.delete();
            menu.outputCommands("deleted.");

        } catch (Exception e) {
            menu.outputCommands("Error occured in deleting.");

        }

    } // end deleteRobots


    /* Loads in two rosters and has them fight printing the match information after the entire 5 matches.
     */
    public static void rosterFight(roster one, roster two) {
        // print a header line and the information of the 2 rosters that are going to battle
        menu.outputCommands("Welcome to roster fighting! Below are the two rosters that will battle: ");
        menu.outputCommands("\nRoster #1:");
        printRoster(one);
        menu.outputCommands("\nRoster #2:");
        printRoster(two);

        // create a variable to hold the smallest roster's size
        int lowestRosterSize = 0;

        // check to see which of the 2 rosters is smaller and set 'lowestRosterSize'
        if (one.getRosterSize() == two.getRosterSize()) {
            lowestRosterSize = one.getRosterSize();
        } else if (one.getRosterSize() > two.getRosterSize()) {
            lowestRosterSize = one.getRosterSize();
        } else {
            lowestRosterSize = two.getRosterSize();
        }

        // create a summary array to hold the summaries of the 5 battles for each index, this will
        // also be used to create the finalReports
        summary[] sumArray = new summary[5];

        // create the 5 battle objects for the 5 battles that will occur at each index
        battle match;

        // iterate through the rosters and perform 5 battles at each corresponding index in both rosters
        for (int i = 0; i < lowestRosterSize; i++) {
            match = new battle(one.getRobot(i), two.getRobot(i));

            // once all 5 battles are created, do the match and store the results in the sumArray at the
            // corresponding match index. EG: 'match1' summary will be held in sumArray[0] and so on.
            try {
                sumArray[i] = match.doAMatch(i + 1);


            } catch (InterruptedException E) {
                menu.outputCommands("Process was interrupted during a sleep operation in \"match.doAMatch\"");
            }

        }
        int numPlayer1Won = 0;
        for(int i = 0; i < 5; i++){
            if(sumArray[i].getRobot1Win()){
                numPlayer1Won++;
            }
        }
        if(numPlayer1Won >= 3){
            visuals.sb_victory_A(1);
        }
        else{
            visuals.sb_victory_A(2);
        }

        // print out all finalReports generated from the 5 battles done at each index
        menu.outputCommands("Printing all resulting match summaries \n");
        for (int i = 0; i < sumArray.length; i++) {
            sumArray[i].printSummary(i + 1);
        }
        menu.outputCommands("\nEND OF FIGHTING");
    }

    /*
        Very simple helper method that just gets all the rosters as an array string so that
        they can be listed in our drop down menu
    */
    public static String[] getAllRostersHelper() {
        // Get the folder path and use it to create a file object
        String folderPath = getDirectoryOfRosters();
        File f = new File(folderPath);

        // Place scanned file names into a string array
        String[] rosters = f.list();
        return rosters;
    }

    /*
        loads the specific roster thats specified from the string rostersName
        Returns this roster after loading it
    */
    public static roster loadSpecificRoster(String rostersName) {

        // get all the pre-requisite variables declared
        String folderPath = getDirectoryOfRosters();
        String rosterName;
        int rosterSize;
        roster specificRoster = null;
        String robotName;

        // set up a check to make sure the roster is loaded
        File rosterExists = new File(folderPath + File.separator + rostersName);
        if (rosterExists.exists()) {

            // file read in variable
            String curLine = "";

            try {
                // Creating a new scanner to scan through the robot file chosen by the player
                Scanner rosterScan = new Scanner(new File("" + folderPath + File.separator + rostersName));
                // Get the rosters name
                curLine = rosterScan.nextLine();
                curLine = curLine.replace("\n", "");
                rosterName = curLine;

                // Get the rosters size
                curLine = rosterScan.nextLine();
                rosterSize = Integer.parseInt(curLine.substring(curLine.lastIndexOf(" ")).replace(" ", ""));
                specificRoster = new roster(rosterSize);

                // Get and set the rosters wins
                curLine = rosterScan.nextLine();
                specificRoster.offSetWins(Integer.parseInt(curLine.substring(curLine.lastIndexOf(" ")).replace(" ", "")));

                // Get and set the rosters loses
                curLine = rosterScan.nextLine();
                specificRoster.offSetLoses(Integer.parseInt(curLine.substring(curLine.lastIndexOf(" ")).replace(" ", "")));

                // Get and set the rosters matches played
                curLine = rosterScan.nextLine();
                specificRoster.offSetMatches(Integer.parseInt(curLine.substring(curLine.lastIndexOf(" ")).replace(" ", "")));

                // Set the rosters name
                specificRoster.setRosterName(rosterName);

                rosterScan.nextLine();

                // Load each robot into the roster
                for (int i = 0; i < rosterSize; i++) {
                    // Get the name of the robot from the roster file
                    robotName = rosterScan.nextLine().replace("\n", "");
                    try {
                        // Declare needed variables to read in the robot file
                        int[] attributes = new int[5];
                        robot myRobot = new robot(robotName);

                        // Creating a new scanner to scan through the robot file chosen by the player
                        Scanner robotScan = new Scanner(new File("" + robotHandler.getDirectoryOfRobots() + File.separator + robotName + ".txt"));
                        robotScan.nextLine();
                        
                        // get robots sprite selection
                        curLine = robotScan.nextLine();
                        int locationOfSep = curLine.indexOf(":");
                        curLine = curLine.substring(locationOfSep + 2);
                        myRobot.setRobotSprite(curLine);
                        
                        // get the robots nature selection
                        curLine = robotScan.nextLine();
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
                        for (int j = 0; j < 5; j++) {
                            curLine = robotScan.nextLine();
                            attributeNum = Integer.parseInt(curLine.substring(curLine.length() - 2).replace(" ", ""));
                            attributes[j] = attributeNum;
                        }

                        // Read the special behavior from the saved robot
                        specialBehav = robotScan.nextLine().substring(17);

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
                        specificRoster.addRobot(myRobot);
                    }
                    // Catch a robot file read in failure and then barf
                    catch (FileNotFoundException ex) {
                        menu.outputCommands("That robot file doesn't exist!");
                        System.exit(0);
                    }
                }

            }
            // Catch the roster file read in failure and then barf
            catch (FileNotFoundException ex) {
                menu.outputCommands("That roster file doesn't exist!");
                System.exit(0);
            }
        }
        // if the roster doesn't exists that means we have a uncorrectable error and must barf and exit
        else {
            menu.outputCommands(rostersName + " doesn't exist exiting");
            System.exit(0);
        }
        // Return the fully initialized roaster
        return specificRoster;
    }

}