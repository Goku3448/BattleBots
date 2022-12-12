/*
  roster is a object that holds a robot array, the roster's size, it's wins, losses, and number of matches.
  This object will be used to streamline future mechanics.

*/

public class roster {

    // The roster is held as an array of robot
    private robot[] array;
    // The size of the roster (since it can be 3 or 5 it's not initialized)
    private int rosterSize;
    // The number of wins this roster is associated to (isn't volatile)
    private int wins = 0;
    // The number of loses this roster is associated to (isn't volatile)
    private int loses = 0;
    // The number of matches this roster is associated to (isn't volatile)
    private int numMatches = 0;
    // The name of the roster
    private String name = "";

    // Constructor will initialize our robot array and set it's length to the give size
    public roster(int size) {
        rosterSize = size;
        array = new robot[rosterSize];

        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
    }

    /*
      addRobot adds passed robots into the next free (null) slot
      If no null slot exists than we return false indicating it's full (incorrect behavior occured)
    */
    public boolean addRobot(robot toAdd) {
        boolean flag = false;

        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                array[i] = toAdd;
                flag = true;
                break;
            }
        }

        return flag;
    }

    // getWins returns the wins assosciated to the roster
    public int getWins() {
        return wins;
    }

    // getLoses returns the loses assosciated to the roster
    public int getLoses() {
        return loses;
    }

    // getNumMatches returns the number of matches assosciated to the roster
    public int getNumMatches() {
        return numMatches;
    }

    // getRosterSize returns the size of the associated roster
    public int getRosterSize() {
        return rosterSize;
    }

    // getRosterName returns the name given to the roster by the user
    public String getRosterName() {
        return name;
    }
    // offSetWins recieves a given offset and applies it to the wins
    public void offSetWins(int offSet) {
        wins = wins + offSet;
    }

    // offSetLoses recieves a given offset and applies it to the loses
    public void offSetLoses(int offSet) {
        loses = loses + offSet;
    }

    // offSetMatches recieves a given offset and applies it to the numMatches
    public void offSetMatches(int offSet) {
        numMatches = numMatches + offSet;
    }

    // setRosterName sets the name of the roster to that given by the user
    public void setRosterName(String rosterName) {
        name = rosterName;
    }


    // getRobot returns the robot at a given index (terminates program on incorrect indexes)
    public robot getRobot(int robotIndex) {
        if (robotIndex >= 0 && robotIndex <= 4) {
            return array[robotIndex];
        } else {
            menu.outputCommands("incorrect index requested!");
            System.exit(0);
        }
        return null;
    }
}
