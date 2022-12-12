/*
  robots.java
  This handles everything related to our robot.
*/


public class robot {

    // robots name
    private String name;

    // Total points that have been spent
    private int totalPoints = 0;

    // These are every attribute value of our robot
    private int strength = 0;
    private int defense = 0;
    private int constitution = 0;
    private int luck = 0;

    // These are the special behaviors of our robot
    private boolean shieldBlock = false;
    private boolean shieldBash = false;
    private boolean dodgeRoll = false;
    private boolean groundSlam = false;
    private boolean lifeLeech = false;

    // This holds the robots chosen sprite
    private String robotSprite = "";

    // These determine what nature the robot is
    private boolean grassType = false;
    private boolean fireType = false;
    private boolean waterType = false;


    // Constructor for robot since we default most values to 0 we only set the name here
    public robot(String botsName) {
        name = botsName;
    }

    /*
      modifyStrength changes the strength value up or down based on passed offset.
      We check to make sure we didn't go over in points or try and have a negative value.
      false means either we spent more points than we had or tried to have a negative value.
      true means we have a positive value and haven't spent too many points.
    */
    public boolean modifyStrength(int strOffset) {

        // Get a copy of the current points spent and current strength value
        int curStr = strength;
        int curPoints = totalPoints;
        boolean noErrors = true;

        // Apply the offset to both the strength value and total points
        strength += strOffset;
        totalPoints += strOffset;

        // Check to see if we made the value negative if we did reset and set our flag
        if (strength < 0) {
            noErrors = false;
            strength = curStr;
            totalPoints = curPoints;
        }
        // Check to see if we went over on our points if we did set our flag
        if (totalPoints > 50) {
            noErrors = false;
            strength = curStr;
            totalPoints = curPoints;
        }

        // Return our flag indicating if the modify was successful or not
        return noErrors;

    }

    /*
      modifyDefense changes the strength value up or down based on passed offset.
      We check to make sure we didn't go over in points or try and have a negative value.
      false means either we spent more points than we had or tried to have a negative value.
      true means we have a positive value and haven't spent too many points.
    */
    public boolean modifyDefense(int defOffset) {

        // Get a copy of the current points spent and current defense value
        int curDef = defense;
        int curPoints = totalPoints;
        boolean noErrors = true;

        // Apply the offset to both the defense value and total points
        defense += defOffset;
        totalPoints += defOffset;

        // Check to see if we made the value negative if we did reset and set our flag
        if (defense < 0) {
            noErrors = false;
            defense = curDef;
            totalPoints = curPoints;
        }
        // Check to see if we went over on our points if we did set our flag
        if (totalPoints > 50) {
            noErrors = false;
            defense = curDef;
            totalPoints = curPoints;
        }

        // Return our flag indicating if the modify was successful or not
        return noErrors;

    }

    /*
      modifyConstitution changes the strength value up or down based on passed offset.
      We check to make sure we didn't go over in points or try and have a negative value.
      false means either we spent more points than we had or tried to have a negative value.
      true means we have a positive value and haven't spent too many points.
    */
    public boolean modifyConstitution(int conOffset) {

        // Get a copy of the current points spent and current constitution value
        int curCon = constitution;
        int curPoints = totalPoints;
        boolean noErrors = true;

        // Apply the offset to both the constitution value and total points
        constitution += conOffset;
        totalPoints += conOffset;

        // Check to see if we made the value negative if we did reset and set our flag
        if (constitution < 0) {
            noErrors = false;
            constitution = curCon;
            totalPoints = curPoints;
        }
        // Check to see if we went over on our points if we did set our flag
        if (totalPoints > 50) {
            noErrors = false;
            constitution = curCon;
            totalPoints = curPoints;
        }

        // Return our flag indicating if the modify was successful or not
        return noErrors;

    }

    /*
      modifyLuck changes the strength value up or down based on passed offset.
      We check to make sure we didn't go over in points or try and have a negative value.
      false means either we spent more points than we had or tried to have a negative value.
      true means we have a positive value and haven't spent too many points.
    */
    public boolean modifyLuck(int luckOffset) {

        // Get a copy of the current points spent and current luck value
        int curLuck = luck;
        int curPoints = totalPoints;
        boolean noErrors = true;

        // Apply the offset to both the luck value and total points
        luck += luckOffset;
        totalPoints += luckOffset;

        // Check to see if we made the value negative if we did reset and set our flag
        if (luck < 0) {
            noErrors = false;
            luck = curLuck;
            totalPoints = curPoints;
        }
        // Check to see if we went over on our points if we did set our flag
        if (totalPoints > 50) {
            noErrors = false;
            luck = curLuck;
            totalPoints = curPoints;
        }

        // Return our flag indicating if the modify was successful or not
        return noErrors;

    }

    /*
      setSpecialBehavior sets the special behavior according to the selection output in createRobot
      This behavior allows easy addition of new special behaviors
    */
    public void setSpecialBehavior(int select) {
        switch (select) {
            // If 1 then user wants shieldBlock
            case 1:
                shieldBlock = true;
                break;
                // if 2 then user wants shieldBash
            case 2:
                shieldBash = true;
                break;

                // If 3 then user wants dodgeRoll
            case 3:
                dodgeRoll = true;
                break;

                // If 4 then user wants groundSlam
            case 4:
                groundSlam = true;
                break;

                // If 5 then user wants lifeLeech
            case 5:
                lifeLeech = true;
                break;


            default:
                menu.outputCommands("Incorrect value passed to setSpecialBehavior");
                System.exit(0);
        }

    }

    /*
        setRobotSprite sets the robots sprite to be the sprites name
    */
    public void setRobotSprite(String spriteName) {
        robotSprite = spriteName;
    }


    /*
        setElementType sets the robots element type to what was requested
    */
    public void setElementType(int select) {
        switch (select) {
            case 1:
                grassType = true;
                break;

            case 2:
                waterType = true;
                break;

            case 3:
                fireType = true;
                break;

            default:
                System.out.println("Invalid value passed to setElementType");
                System.exit(0);
        }
    }

    /*
      getTotalPoints simply gets the robots points that have been spent and returns it
    */
    public int getTotalPoints() {
        return totalPoints;
    }

    /*
      getStregnth gets our robots strength value and returns it
    */
    public int getStrength() {
        return strength;
    }

    /*
      getDefense gets our robots defense value and returns it
    */
    public int getDefense() {
        return defense;
    }

    /*
      getConstitution gets our robots constitution value and returns it
    */
    public int getConstitution() {
        return constitution;
    }

    /*
      getLuck gets our robots luck value and returns it
    */
    public int getLuck() {
        return luck;
    }

    /*
      getRobotName will return the name given to the robot as a string
    */
    public String getRobotName() {
        return name;
    }

    /*
     getRobotSprite will return the sprite associated to this robot
    */
    public String getRobotSprite() {
        return robotSprite;
    }

    /*
      get getSpecialBehavior returns as a string what special behavior the robot has
      Useful for quick outputs in the future
    */
    public String getSpecialBehavior() {
        if (shieldBlock) {
            return "Shield block";
        } else if (shieldBash) {
            return "Shield bash";
        } else if (dodgeRoll) {
            return "Dodge roll";
        } else if (groundSlam) {
            return "Ground slam";
        } else if (lifeLeech) {
            return "Life leech";
        } else
            return null;
    }

    /*
        getElementType returns the robots element type as a string
    */
    public String getElementType() {
        if (grassType) {
            return "Grass Type";
        } else if (waterType) {
            return "Water Type";
        } else if (fireType) {
            return "Fire Type";
        } else {
            // TODO change to be null after gui support for robot natures is pushed
            return "Grass Type";
        }
    }

    //resets all special behaviors
    public void resetSpecialBehavior() {

        shieldBlock = false;
        shieldBash = false;
        dodgeRoll = false;
        groundSlam = false;
        lifeLeech = false;

    }

}
