/*
 * summary
 * The summary object holds values associated to a match between two robots. Note this object doesn't determine what robot did what damage.
 * AKA NO VALIDATION meaning it expects other methods to keep track of what robot did what action THIS OBJECT ONLY HOLDS DATA.
 * only holds if robot 1 won since that's all that's needed
 */

public class summary {
    /*
     * Hold robot specific damage, total damage, life at end of match per robot, and the number of critical strikes per robot
     */
    // NOTE EOM means end of match
    private int robot1Damage = 0;
    private int robot2Damage = 0;
    private int robot1LifeEOM = 0;
    private int robot2LifeEOM = 0;
    private int robot1NumCrits = 0;
    private int robot2NumCrits = 0;


    // Hold boolean that can be used to determine winner
    private boolean robot1Won = false;

    public summary() {

    }

    /*
     * printSummary - Print out the summary of the calling summary object
     */
    public void printSummary(int matchNumber) {
        menu.outputCommands("Match summary of match #" + matchNumber);
        menu.outputCommands("------------------------\n");

        menu.outputCommands("Player One");
        menu.outputCommands("------------");
        if (robot1Won) {
            menu.outputCommands("Player one won the match");
        }
        menu.outputCommands("Total Damage: " + robot1Damage);
        menu.outputCommands("Total Crits: " + robot1NumCrits);
        menu.outputCommands("Total Life Left: " + robot1LifeEOM);


        menu.outputCommands("\nPlayer Two");
        menu.outputCommands("------------");
        if (!robot1Won) {
            menu.outputCommands("Player two won the match");
        }
        menu.outputCommands("Total Damage: " + robot2Damage);
        menu.outputCommands("Total Crits: " + robot2NumCrits);
        menu.outputCommands("Total Life Left: " + robot2LifeEOM);

        menu.outputCommands("\nCombined");
        menu.outputCommands("------------");
        menu.outputCommands("Total Match Damage: " + (robot1Damage + robot2Damage));
        menu.outputCommands("Total Crits: " + (robot1NumCrits + robot2NumCrits));
    }

    /*
     * Change robot1 damage by the offset value
     */
    public void offSetRobot1Damage(int offset) {
        robot1Damage += offset;

    }

    /*
     * Change robot2 damage by the offset value
     */
    public void offSetRobot2Damage(int offset) {
        robot2Damage += offset;

    }

    /*
     * Set the robot1's life at the end of the match
     */
    public void setRobot1LifeEOM(int amount) {
        robot1LifeEOM = amount;
    }

    /*
     * Set the robot2's life at the end of the match
     */
    public void setRobot2LifeEOM(int amount) {
        robot2LifeEOM = amount;
    }

    /*
     * Offset the amount of critical strikes associated to robot1
     */
    public void offsetRobot1NumCrits(int offset) {
        robot1NumCrits += offset;
    }

    /*
     * Offset the amount of critical strikes associated to robot2
     */
    public void offsetRobot2NumCrits(int offset) {
        robot2NumCrits += offset;
    }

    /*
     * Set obot1Won to if it was the winner
     */
    public void didRobot1Win(boolean win) {
        robot1Won = win;
    }

    /*
     * Get if robot1 was the winner
     */
    public boolean getRobot1Win() {
        return robot1Won;
    }

    /*
     * Get the damage dealt associated to robot1
     */
    public int getRobot1Damage() {
        return robot1Damage;
    }

    /*
     * Get the damage dealt associated to robot2
     */
    public int getRobot2Damage() {
        return robot2Damage;
    }

    /*
     * Get the totoal damage dealt (sum of both robots damage)
     */
    public int getTotalDamage() {
        return robot1Damage + robot2Damage;
    }


    /*
     * Get the life of robot1 at the end of the match
     */
    public int getRobot1LifeEOM() {
        return robot1LifeEOM;
    }

    /*
     * Get the life of robot2 at the end of the match
     */
    public int getRobot2LifeEOM() {
        return robot2LifeEOM;
    }

    /*
     * Get the number of critical strikes dealth by robot1
     */
    public int getRobot1NumCrits() {
        return robot1NumCrits;
    }

    /*
     * Get the number of critical strikes dealth by robot2
     */
    public int getRobot2NumCrits() {
        return robot2NumCrits;
    }

}