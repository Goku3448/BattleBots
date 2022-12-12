/*
 * battle is a java class that implements the entire match between two robots of BattleBots
 * This file contains everything needed to calculate and implement the fighting between two given robots
 */

import java.lang.Thread;
import java.text.*;

public class battle {
    
    
    /*
        soundRunnable handles playing sounds without halting the game (another thread)
    */
    public class soundRunnable implements Runnable {
        
        sounds.sound toPlay;
        
        public soundRunnable(sounds.sound soundToPlay){
            toPlay = soundToPlay;
        }
        
        
        public void run() {
            sounds.playASound(toPlay);
        }
    }
    
    
    // Copy of the robots so we can access it's attributes
    private robot robotA;
    private robot robotB;

    // Robot base health and additonal from constitution
    private int robotAHealth = 0;
    private int robotBHealth = 0;

    // Robot damage max range (base plus 2 for every strength)
    private int robotAMaxDmgRange = 0;
    private int robotBMaxDmgRange = 0;

    // Robot defense multiplier that's applied to each attack
    private double robotADefenseMulti = 0;
    private double robotBDefenseMulti = 0;

    // Robot luck multiplier that determines how likely a critical strike is to occur and the scaling of it
    private double robotALuckMulti = 0;
    private double robotBLuckMulti = 0;

    // Robot current status (is a integer that represents the current status effect on them with 0 being NONE)
    private int robotAStatus = 0;
    private int robotBStatus = 0;

    // Holds the robots special behavior
    private String robotASpecialBehavior = "";
    private String robotBSpecialBehavior = "";
    
    // Holds the robots nature
    private String robotAElementType = "";
    private String robotBElementType = "";

    // variables use to determine if a robots was confused form ground slam and should have their attack skipped
    private boolean robotAGroundSlamSkipAttack = false;
    private boolean robotBGroundSlamSkipAttack = false;

    /*
     * getRobotHealth gets the health of a robot at the start of a match scales with constitution
     */
    private int getRobotHealth(robot myRobot) {
        return 200 + (myRobot.getConstitution() * 20);
    }

    /*
     * getRobotMaxDmgRange gets the top of a range of robots max damage. Scales with strength
     */
    private int getRobotMaxDmgRange(robot myRobot) {
        return 30 + (myRobot.getStrength() * 2);
    }

    /*
     * getRobotDefenseMulti gets the multiplier based on defense that negates a percentage of an incoming attack
     */
    private double getRobotDefenseMulti(robot myRobot) {
        int defense = myRobot.getDefense();

        if (defense < 5) {
            return (.05 + (defense * .0012));
        } else if (defense < 10) {
            return (0.08 + ((defense - 5) * .0019));
        } else if (defense < 20) {
            return (0.12 + ((defense - 10) * .0024));
        } else if (defense < 40) {
            return (0.25 + ((defense - 20) * 0.0032));
        } else if (defense < 50) {
            return 0.45;
        } else {
            return 0.75;
        }
    }

    /*
     * getRobotLuckMulti gets the multipler for luck which determines how likely a critical strikes occurs and
     * how much it scales
     */
    private double getRobotLuckMulti(robot myRobot) {

        return .05 + (myRobot.getLuck() * 0.01);

    }
    
    /*
        ApplyElementStatEffects changes the player 1 and player 2's stats based on their opponent's nature
    */
    public void applyElementStatEffects(robot curRobot, boolean isPlayer1){
        if(isPlayer1){
            switch(robotAElementType){
                case "Grass Type":
                    robotBDefenseMulti = robotBDefenseMulti - (robotBDefenseMulti * 0.10);
                    break;
                
                case "Water Type":
                    robotBMaxDmgRange = robotBMaxDmgRange - (robotB.getStrength());
                    break;
                
                case "Fire Type":
                    robotBHealth = robotBHealth - (robotB.getConstitution() * 5);
                    break;
            }
        }
        else{
            switch(robotBElementType){
                case "Grass Type":
                    robotADefenseMulti = robotADefenseMulti - (robotADefenseMulti * 0.10);
                    break;
                
                case "Water Type":
                    robotAMaxDmgRange = robotAMaxDmgRange - (robotA.getStrength());
                    break;
                
                case "Fire Type":
                    robotAHealth = robotAHealth - (robotA.getConstitution() * 5);
                    break;
            }
        }
    }


    /*
     * battle is the constructor for the battle object
     * it initializes all of the attributes of the battle object
     */
    public battle(robot robot1, robot robot2) {
        robotA = robot1;
        robotB = robot2;
        robotAHealth = getRobotHealth(robot1);
        robotBHealth = getRobotHealth(robot2);
        robotAMaxDmgRange = getRobotMaxDmgRange(robot1);
        robotBMaxDmgRange = getRobotMaxDmgRange(robot2);
        robotADefenseMulti = getRobotDefenseMulti(robot1);
        robotBDefenseMulti = getRobotDefenseMulti(robot2);
        robotALuckMulti = getRobotLuckMulti(robot1);
        robotBLuckMulti = getRobotLuckMulti(robot2);
        robotASpecialBehavior = robotA.getSpecialBehavior();
        robotBSpecialBehavior = robotB.getSpecialBehavior();
        robotAElementType = robotA.getElementType();
        robotBElementType = robotB.getElementType();
        
        applyElementStatEffects(robotA, true);
        applyElementStatEffects(robotB, false);
        
        
        
    }

    /*
     * printMatchHeader prints the header for a given match
     * Added as it's likely to greatly grow with complexity of the fighting system
     */
    public void printMatchHeader(int matchNumber) {
        DecimalFormat formatted = new DecimalFormat("#.#");
        menu.outputCommands("MATCH: " + matchNumber + " " + robotA.getRobotName() + " VS " + robotB.getRobotName() + "\n");
        menu.outputCommands("Health: " + robotAHealth + "   " + robotBHealth);
        menu.outputCommands("Max hit: " + robotAMaxDmgRange + "    " + robotBMaxDmgRange);
        menu.outputCommands("Defense: " + formatted.format(100 * robotADefenseMulti) + "%    " + formatted.format(100 * robotBDefenseMulti) + "%");
        menu.outputCommands("Luck: " + formatted.format((100 * robotALuckMulti)) + "%    " + formatted.format((100 * robotBLuckMulti)) + "%\n\n");

    }


    /*
     * printPlayer1CurStats - will print the current robots standings to the correct text output
     */
    public void printPlayer1CurStats() {
        String toPrint = "";

        toPrint += "\t\t\t\t\t\t\tPlayer 1 \n";
        toPrint += "\tRobot: " + robotA.getRobotName() + "\n";
        toPrint += "\tHealth: " + robotAHealth + "\n";

        switch (robotAStatus) {
            case 0:
                toPrint += "\tStatus: NONE\n";
                break;
                
            case 1:
                toPrint += "\tStatus: Confused\n";
                break;
        }

        menu.outputCommandsFight(toPrint, 1);
    }

    /*
     * printPlayer2CurStats - will print the current robots standings to the correct text output
     */
    public void printPlayer2CurStats() {
        String toPrint = "";

        toPrint += "\t\t\t\t\t\t\tPlayer 2 \n";
        toPrint += "\tRobot: " + robotB.getRobotName() + "\n";
        toPrint += "\tHealth: " + robotBHealth + "\n";

        switch (robotBStatus) {
            case 0:
                toPrint += "\tStatus: NONE\n";
                break;
            case 1:
                toPrint += "\tStatus: Confused\n";
                break;
        }

        menu.outputCommandsFight(toPrint, 2);
    }

    /*
     * prints the header information (in the fight GUI)
     */
    public void printMatchInfoHeader(int matchNumber, String stringToPrint) {
        String toPrint = "";

        toPrint += "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t   Match #" + matchNumber + "\n";
        menu.outputCommandsFight(toPrint + "\t" + stringToPrint, 0);
    }

    /*
     * flipCoin - flips a coin with a given probability
     */
    public boolean flipCoin(double probability) {
        return (Math.random() * (1 - 0) + 0) <= probability;

    }
    
    /*
        getDamageToDeal returns a integer value representing the damage that the robot will do this tick
    */
    public int getDamageToDeal(boolean isPlayer1, boolean isCrit){
        int damage = 0;
        
        if(isPlayer1){
            damage = (int)((Math.random() * (robotAMaxDmgRange - 20 + 1) + 20));
            if(isCrit){
                damage = (int)(damage + (damage * robotALuckMulti));
            }
        }
        else{
            damage = (int)((Math.random() * (robotBMaxDmgRange - 20 + 1) + 20));
            if(isCrit){
                damage = (int)(damage + (damage * robotBLuckMulti));
            }
        }
        
        return damage;
    }
    
    
    /*
        getRobotDamageBlocked returns a integer representing the damaged blocked
    */
    public int getRobotDamageBlocked(int damage, boolean isPlayer1){
        int dmgBlocked = 0;
        if(isPlayer1){
            dmgBlocked = ((int)(damage * robotBDefenseMulti));
        }
        else{
            dmgBlocked =((int)(damage * robotADefenseMulti));
        }
        if(dmgBlocked > damage){
            return damage - 20;
        }
        else{
            return dmgBlocked;
        }
    }
    
    /*
        doAVineWhipTick does a tick according to the specifications of a vine whip (buff attack especially against a weak nature robot)
    */
    public void doAVineWhipTick(boolean isRobotA, summary matchSummary, int matchNumber) throws InterruptedException {

        // variables to handle the robot tick
        int damageBlocked = 0;
        int robotACurHitDmg = 0;
        int robotBCurHitDmg = 0;

        // variables to handle match pacing
        int sleepTime = 500;
        int sleepTimeCoinFlip = 250;
        int sleepTimeForAnimations = 500;
        int betweenAnimationsSleep = 250;

        if (isRobotA) {
            // get the damage to deal and to block
            robotACurHitDmg = getDamageToDeal(true, false);
            if(robotBElementType == "Water Type"){
                robotACurHitDmg = robotACurHitDmg + (int)(robotACurHitDmg * 0.30);
            }
            else{
                robotACurHitDmg = robotACurHitDmg + (int)(robotACurHitDmg * 0.10);
            }
            
            damageBlocked = getRobotDamageBlocked(robotACurHitDmg, true);
            robotACurHitDmg = robotACurHitDmg - damageBlocked;

            // output the information regarding the current tick and then do the attack and damaged animation
            printMatchInfoHeader(matchNumber, "Player 1 " + robotA.getRobotName() + " has landed a vine whip of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");

            menu.outputCommands("Player 1 " + robotA.getRobotName() + " has landed a vine whip of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
            // play sound
            new Thread(new soundRunnable(sounds.sound.vineWhip)).start();
            
            // print the updated player robot stats
            robotBHealth = robotBHealth - robotACurHitDmg;
            printPlayer2CurStats();

            // do the logic for water beam
            visuals.sb_VineWhip_A(1);
            Thread.sleep(betweenAnimationsSleep);
            visuals.robotDamaged_A(2);
            Thread.sleep(sleepTimeForAnimations);

            // Keep track of the tick by updating the summary object
            matchSummary.offSetRobot1Damage(robotACurHitDmg);
            matchSummary.offsetRobot1NumCrits(1);
            Thread.sleep(sleepTime);

        } else {

            // get the damage to deal and to block
            robotBCurHitDmg = getDamageToDeal(false, false);
            if(robotAElementType == "Water Type"){
                robotBCurHitDmg = robotBCurHitDmg + (int)(robotBCurHitDmg * 0.30);
            }
            else{
                robotBCurHitDmg = robotBCurHitDmg + (int)(robotBCurHitDmg * 0.10);
            }
            
            damageBlocked = getRobotDamageBlocked(robotACurHitDmg, false);
            robotBCurHitDmg = robotBCurHitDmg - damageBlocked;
            
            // output the information regarding the current tick and then do the attack and damaged animation
            printMatchInfoHeader(matchNumber, "Player 2 " + robotB.getRobotName() + " has landed a vine whip of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
    
            menu.outputCommands("Player 2 " + robotB.getRobotName() + " has landed a vine whip of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
            // play sound
            new Thread(new soundRunnable(sounds.sound.vineWhip)).start();
            
            // print the updated player robot stats
            robotAHealth = robotAHealth - robotBCurHitDmg;
            printPlayer1CurStats();

            // do the logic for vine whip
            visuals.sb_VineWhip_A(2);
            Thread.sleep(betweenAnimationsSleep);
            visuals.robotDamaged_A(1);
            Thread.sleep(sleepTimeForAnimations);

            // Keep track of the tick by updating the summary object
            matchSummary.offSetRobot2Damage(robotBCurHitDmg);
            matchSummary.offsetRobot2NumCrits(1);
            Thread.sleep(sleepTime);
        }
    }
    
    /*
        doAWaterBeamTick does a tick according to the specifications of a water beam (buff attack especially against a weak nature robot)
    */
    public void doAWaterBeamTick(boolean isRobotA, summary matchSummary, int matchNumber) throws InterruptedException {

        // variables to handle the robot tick
        int damageBlocked = 0;
        int robotACurHitDmg = 0;
        int robotBCurHitDmg = 0;

        // variables to handle match pacing
        int sleepTime = 500;
        int sleepTimeCoinFlip = 250;
        int sleepTimeForAnimations = 500;
        int betweenAnimationsSleep = 250;

        
        if (isRobotA) {
            // get the damage to deal and to block
            robotACurHitDmg = getDamageToDeal(true, false);
            if(robotBElementType == "Fire Type"){
                robotACurHitDmg = robotACurHitDmg + (int)(robotACurHitDmg * 0.30);
            }
            else{
                robotACurHitDmg = robotACurHitDmg + (int)(robotACurHitDmg * 0.10);
            }
            
            damageBlocked = getRobotDamageBlocked(robotACurHitDmg, true);
            robotACurHitDmg = robotACurHitDmg - damageBlocked;

            // output the information regarding the current tick and then do the attack and damaged animation
            printMatchInfoHeader(matchNumber, "Player 1 " + robotA.getRobotName() + " has landed a water beam of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");

            menu.outputCommands("Player 1 " + robotA.getRobotName() + " has landed a water beam of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
            // play sound
            new Thread(new soundRunnable(sounds.sound.waterBeam)).start();
            
            // print the updated player robot stats
            robotBHealth = robotBHealth - robotACurHitDmg;
            printPlayer2CurStats();

            // do the logic for water beam
            visuals.sb_waterBeam_A(1);
            Thread.sleep(betweenAnimationsSleep);
            visuals.robotDamaged_A(2);
            Thread.sleep(sleepTimeForAnimations);

            // Keep track of the tick by updating the summary object
            matchSummary.offSetRobot1Damage(robotACurHitDmg);
            matchSummary.offsetRobot1NumCrits(1);
            Thread.sleep(sleepTime);

        } else {
            // get the damage to deal and to block
            robotBCurHitDmg = getDamageToDeal(false, false);
            if(robotAElementType == "Fire Type"){
                robotBCurHitDmg = robotBCurHitDmg + (int)(robotBCurHitDmg * 0.30);
            }
            else{
                robotBCurHitDmg = robotBCurHitDmg + (int)(robotBCurHitDmg * 0.10);
            }
            
            damageBlocked = getRobotDamageBlocked(robotACurHitDmg, false);
            robotBCurHitDmg = robotBCurHitDmg - damageBlocked;
            
            // output the information regarding the current tick and then do the attack and damaged animation
            printMatchInfoHeader(matchNumber, "Player 2 " + robotB.getRobotName() + " has landed a water beam of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");

            menu.outputCommands("Player 2 " + robotB.getRobotName() + " has landed a water beam of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
            // play sound
            new Thread(new soundRunnable(sounds.sound.waterBeam)).start();
            
            // print the updated player robot stats
            robotAHealth = robotAHealth - robotBCurHitDmg;
            printPlayer1CurStats();

            // do the logic for water beam
            visuals.sb_waterBeam_A(2);
            Thread.sleep(betweenAnimationsSleep);
            visuals.robotDamaged_A(1);
            Thread.sleep(sleepTimeForAnimations);


            // Keep track of the tick by updating the summary object
            matchSummary.offSetRobot2Damage(robotBCurHitDmg);
            matchSummary.offsetRobot2NumCrits(1);
            Thread.sleep(sleepTime);
        }
    }
    
    
    /*
        doAFirePunchTick does a tick according to the specifications of a water beam (buff attack especially against a weak nature robot)
    */
    public void doAFirePunchTick(boolean isRobotA, summary matchSummary, int matchNumber) throws InterruptedException {

        // variables to handle the robot tick
        int damageBlocked = 0;
        int robotACurHitDmg = 0;
        int robotBCurHitDmg = 0;

        // variables to handle match pacing
        int sleepTime = 500;
        int sleepTimeCoinFlip = 250;
        int sleepTimeForAnimations = 500;
        int betweenAnimationsSleep = 250;


        if (isRobotA) {
            // get the damage to deal and to block
            robotACurHitDmg = getDamageToDeal(true, false);
            if(robotBElementType == "Grass Type"){
                robotACurHitDmg = robotACurHitDmg + (int)(robotACurHitDmg * 0.30);
            }
            else{
                robotACurHitDmg = robotACurHitDmg + (int)(robotACurHitDmg * 0.10);
            }
            
            damageBlocked = getRobotDamageBlocked(robotACurHitDmg, true);
            robotACurHitDmg = robotACurHitDmg - damageBlocked;

            // output the information regarding the current tick and then do the attack and damaged animation
            printMatchInfoHeader(matchNumber, "Player 1 " + robotA.getRobotName() + " has landed a fire punch of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
    
            menu.outputCommands("Player 1 " + robotA.getRobotName() + " has landed a fire punch of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
            // play sound
            new Thread(new soundRunnable(sounds.sound.flamePunch)).start();
            
            // print the updated player robot stats
            robotBHealth = robotBHealth - robotACurHitDmg;
            printPlayer2CurStats();

            // do the logic for fire punch
            visuals.sb_firePunch_A(1);
            Thread.sleep(betweenAnimationsSleep);
            visuals.robotDamaged_A(2);
            Thread.sleep(sleepTimeForAnimations);

            // Keep track of the tick by updating the summary object
            matchSummary.offSetRobot1Damage(robotACurHitDmg);
            matchSummary.offsetRobot1NumCrits(1);
            Thread.sleep(sleepTime);

        } else {
            // get the damage to deal and to block
            robotBCurHitDmg = getDamageToDeal(false, false);
            if(robotAElementType == "Grass Type"){
                robotBCurHitDmg = robotBCurHitDmg + (int)(robotBCurHitDmg * 0.30);
            }
            else{
                robotBCurHitDmg = robotBCurHitDmg + (int)(robotBCurHitDmg * 0.10);
            }
            
            damageBlocked = getRobotDamageBlocked(robotACurHitDmg, false);
            robotBCurHitDmg = robotBCurHitDmg - damageBlocked;
            
            
            // output the information regarding the current tick and then do the attack and damaged animation
            printMatchInfoHeader(matchNumber, "Player 2 " + robotB.getRobotName() + " has landed a fire punch of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
  
            menu.outputCommands("Player 2 " + robotB.getRobotName() + " has landed a fire punch of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
            // play sound
            new Thread(new soundRunnable(sounds.sound.flamePunch)).start();
            
            // print the updated player robot stats
            robotAHealth = robotAHealth - robotBCurHitDmg;
            printPlayer1CurStats();

            // do the logic for fire punch
            visuals.sb_firePunch_A(2);
            Thread.sleep(betweenAnimationsSleep);
            visuals.robotDamaged_A(1);
            Thread.sleep(sleepTimeForAnimations);


            // Keep track of the tick by updating the summary object
            matchSummary.offSetRobot2Damage(robotBCurHitDmg);
            matchSummary.offsetRobot2NumCrits(1);
            Thread.sleep(sleepTime);
        }
    }

        
    /*
        doAShieldBashTick - Does all the logic behind a robot doing a shield bash
    */
    public void doAShieldBashTick(boolean isRobotA, summary matchSummary, int matchNumber) throws InterruptedException {

        // variables to handle the robot tick
        int damageBlocked = 0;
        int robotACurHitDmg = 0;
        int robotBCurHitDmg = 0;

        // variables to handle match pacing
        int sleepTime = 500;
        int sleepTimeCoinFlip = 250;
        int sleepTimeForAnimations = 500;
        int betweenAnimationsSleep = 250;

        
        if (isRobotA) {
            // get the damage to deal and to block
            robotACurHitDmg = getDamageToDeal(true, true);
            damageBlocked = getRobotDamageBlocked(robotACurHitDmg, true);
            robotACurHitDmg = robotACurHitDmg - damageBlocked;

            // output the information regarding the current tick and then do the attack and damaged animation
            printMatchInfoHeader(matchNumber, "Player 1 " + robotA.getRobotName() + " has landed a shield bash critical strike of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");

            menu.outputCommands("Player 1 " + robotA.getRobotName() + " has landed a shield bash critical strike of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
            // play sound
            new Thread(new soundRunnable(sounds.sound.shieldBash)).start();
            
            // print the updated player robot stats
            robotBHealth = robotBHealth - robotACurHitDmg;
            printPlayer2CurStats();

            robotALuckMulti = getRobotLuckMulti(robotA);
            
            // Do the actual shield bash logic
            visuals.sb_ShieldBash_A(1);
            Thread.sleep(betweenAnimationsSleep);
            visuals.critHit_A(2);
            visuals.robotDamaged_A(2);
            Thread.sleep(sleepTimeForAnimations);

            // Keep track of the tick by updating the summary object
            matchSummary.offSetRobot1Damage(robotACurHitDmg);
            matchSummary.offsetRobot1NumCrits(1);
            Thread.sleep(sleepTime);

        } else {

            robotBCurHitDmg = getDamageToDeal(false, true);
            damageBlocked = getRobotDamageBlocked(robotACurHitDmg, false);
            robotBCurHitDmg = robotBCurHitDmg - damageBlocked;

            // output the information regarding the current tick and then do the attack and damaged animation
            printMatchInfoHeader(matchNumber, "Player 2 " + robotB.getRobotName() + " has landed a shield bash critical strike of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");

            menu.outputCommands("Player 2 " + robotB.getRobotName() + " has landed a shield bash critical strike of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
            // play sound
            new Thread(new soundRunnable(sounds.sound.shieldBash)).start();
            
            
            // print the updated player robot stats
            robotAHealth = robotAHealth - robotBCurHitDmg;
            printPlayer1CurStats();

            robotBLuckMulti = getRobotLuckMulti(robotB);

            // Do the actual shield bash logic

            visuals.sb_ShieldBash_A(2);
            Thread.sleep(betweenAnimationsSleep);
            visuals.critHit_A(1);
            visuals.robotDamaged_A(1);
            Thread.sleep(sleepTimeForAnimations);

            // Keep track of the tick by updating the summary object
            matchSummary.offSetRobot2Damage(robotBCurHitDmg);
            matchSummary.offsetRobot2NumCrits(1);
            Thread.sleep(sleepTime);
        }
    }

    /*
     * doAShieldBlockTick - simply states that the player has blocked the other players critical attack
     */
    public void doAShieldBlockTick(boolean isRobotA, summary matchSummary, int matchNumber) throws InterruptedException {
        // variables to handle match pacing
        int sleepTime = 500;
        int sleepTimeForAnimations = 1000;

        // play sound
        new Thread(new soundRunnable(sounds.sound.shieldBlock)).start();
        
        // do the logic behind a shield block
        if (isRobotA) {
            visuals.robotAttackNormal_A(1);
            printMatchInfoHeader(matchNumber, "Player 2 has activated shield block! and blocked player 1's critical attack!");
            menu.outputCommands("Player 2 has activated shield block! and blocked player 1's critical attack!");
            visuals.sb_ShieldBlock_A(2);
            Thread.sleep(sleepTimeForAnimations);
        } else {
            visuals.robotAttackNormal_A(2);
            printMatchInfoHeader(matchNumber, "Player 1 has activated shield block! and blocked player 2's critical attack!");
            menu.outputCommands("Player 1 has activated shield block! and blocked player 2's critical attack!");
            visuals.sb_ShieldBlock_A(1);
            Thread.sleep(sleepTimeForAnimations);
        }
        Thread.sleep(sleepTime);
    }

    /*
     * doADodgeRollTick - simply states that the attack was missed because the robot dodge rolled
     */
    public void doADodgeRollTick(boolean isRobotA, summary matchSummary, int matchNumber) throws InterruptedException {
        // variables to handle match pacing
        int sleepTime = 500;
        int sleepTimeForAnimations = 1000;

        // play sound
        new Thread(new soundRunnable(sounds.sound.dodgeRoll)).start();
        
        // do the logic beheind the dodge roll
        if (isRobotA) {
            visuals.robotAttackNormal_A(1);
            printMatchInfoHeader(matchNumber, "Player 2 has activated dodge roll! and dodged player 1's attack!");
            menu.outputCommands("Player 2 has activated dodge roll! and dodged player 1's attack!");
            visuals.sb_Dodge_A(2);
            Thread.sleep(sleepTimeForAnimations);
        } else {
            visuals.robotAttackNormal_A(2);
            printMatchInfoHeader(matchNumber, "Player 1 has activated dodge roll! and dodged player 2's attack!");
            menu.outputCommands("Player 1 has activated dodge roll! and dodged player 2's attack!");
            visuals.sb_Dodge_A(1);
            Thread.sleep(sleepTimeForAnimations);
        }
        Thread.sleep(sleepTime);

    }


    /*
     * doAGroundSlamTick - simply states that the critical strike was a ground slam and if the enemies next attack will be prevented
     */
    public void doAGroundSlamTick(boolean isRobotA, summary matchSummary, int matchNumber) throws InterruptedException {

        // variables to handle the robot tick
        int damageBlocked = 0;
        int robotACurHitDmg = 0;
        int robotBCurHitDmg = 0;

        // variables to handle match pacing
        int sleepTime = 500;
        int sleepTimeCoinFlip = 250;
        int sleepTimeForAnimations = 1000;
        int betweenAnimationsSleep = 250;

        if (isRobotA) {
            // get the damage to deal and to block
            robotALuckMulti = robotALuckMulti * 0.05;
            robotACurHitDmg = getDamageToDeal(true, true);
            damageBlocked = getRobotDamageBlocked(robotACurHitDmg, true);
            robotACurHitDmg = robotACurHitDmg - damageBlocked;

            // output the information regarding the current tick and then do the attack and damaged animation
            printMatchInfoHeader(matchNumber, "Player 1 " + robotA.getRobotName() + " has landed a ground slam critical strike of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");

            menu.outputCommands("Player 1 " + robotA.getRobotName() + " has landed a ground slam critical strike of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
            // play sound
            new Thread(new soundRunnable(sounds.sound.groundSlam)).start();
            
            // print the updated player robot stats

            robotBHealth = robotBHealth - robotACurHitDmg;
            printPlayer2CurStats();

            // reset the critical scalar 
            robotALuckMulti = getRobotLuckMulti(robotA);

            // Do the actual ground slam logic
            visuals.sb_GroundSlam_A(1);
            Thread.sleep(betweenAnimationsSleep);
            visuals.critHit_A(2);
            visuals.robotDamaged_A(2);
            Thread.sleep(sleepTimeForAnimations);

            // check if they stunned the enemy
            if (flipCoin(.25)) {
                robotBStatus = 1;
                robotBGroundSlamSkipAttack = true;
                printMatchInfoHeader(matchNumber, "Player 1's ground slam has confused player 2 (their next attack will miss)");
                menu.outputCommands("Player 1's ground slam has confused player 2 (their next attack will miss)");
                printPlayer2CurStats();
            }

            // Keep track of the tick by updating the summary object
            matchSummary.offSetRobot1Damage(robotACurHitDmg);
            matchSummary.offsetRobot1NumCrits(1);
            Thread.sleep(sleepTime);
        } else {
            // get the damage to deal and to block
            robotBLuckMulti = robotBLuckMulti * 0.05;
            robotBCurHitDmg = getDamageToDeal(false, true);
            damageBlocked = getRobotDamageBlocked(robotACurHitDmg, false);
            robotBCurHitDmg = robotBCurHitDmg - damageBlocked;

            // output the information regarding the current tick and then do the attack and damaged animation
            printMatchInfoHeader(matchNumber, "Player 2 " + robotB.getRobotName() + " has landed a ground slam critical strike of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked)+ " - blocked " + damageBlocked + ")");

            menu.outputCommands("Player 2 " + robotB.getRobotName() + " has landed a ground slam critical strike of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
            // play sound
            new Thread(new soundRunnable(sounds.sound.groundSlam)).start();
            
            // print the updated player robot stats
            robotAHealth = robotAHealth - robotBCurHitDmg;
            printPlayer1CurStats();

            // reset the critical scalar 
            robotBLuckMulti = getRobotLuckMulti(robotB);
            
            // Do the actual ground slam logic
            visuals.sb_GroundSlam_A(2);
            Thread.sleep(betweenAnimationsSleep);
            visuals.critHit_A(1);
            visuals.robotDamaged_A(1);
            Thread.sleep(sleepTimeForAnimations);

            // check if they stunned the enemy
            if (flipCoin(.25)) {
                robotAGroundSlamSkipAttack = true;
                robotAStatus = 1;
                printMatchInfoHeader(matchNumber, "Player 2's ground slam has confused player 1 (their next attack will miss)");
                menu.outputCommands("Player 2's ground slam has confused player 1 (their next attack will miss)");
                printPlayer1CurStats();
            }

            // Keep track of the tick by updating the summary object
            matchSummary.offSetRobot2Damage(robotBCurHitDmg);
            matchSummary.offsetRobot2NumCrits(1);
            Thread.sleep(sleepTime);
        }
    }
    /*
        doALifeLeechTick does a life leech tick which is to attack the enemy and regain some of that damage as health
    */
    public void doALifeLeechTick(boolean isRobotA, summary matchSummary, int matchNumber) throws InterruptedException {

        // variables to handle the robot tick
        int damageBlocked = 0;
        int robotACurHitDmg = 0;
        int robotBCurHitDmg = 0;
        int healthHealed = 0;

        // variables to handle match pacing
        int sleepTime = 500;
        int sleepTimeCoinFlip = 250;
        int sleepTimeForAnimations = 1000;
        int betweenAnimationsSleep = 250;

        if (isRobotA) {
            // Flip for critical srike and then do tick based on that result
            if (flipCoin(robotALuckMulti)) {
                // get the damage to deal and to block
                robotACurHitDmg = getDamageToDeal(true, true);
                damageBlocked = getRobotDamageBlocked(robotACurHitDmg, true);
                robotACurHitDmg = robotACurHitDmg - damageBlocked;

                healthHealed = (int)(robotACurHitDmg * .75);

                // output the information regarding the current tick and then do the attack and damaged animation
                printMatchInfoHeader(matchNumber, "Player 1 " + robotA.getRobotName() + " has landed a critical life leech strike of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")\n\t" + "Player 1 has healed " + healthHealed + " health from life leech");

                menu.outputCommands("Player 1 " + robotA.getRobotName() + " has landed a critical life leech strike of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked)  + " - blocked " + damageBlocked + ")\n" + "Player 1 has healed " + healthHealed + " health from life leech");
                // play sound
                new Thread(new soundRunnable(sounds.sound.lifeLeech)).start();
                
                // print the updated player robot stats
                robotBHealth = robotBHealth - robotACurHitDmg;

                // gain the health from the life leech attack
                robotAHealth += healthHealed;

                printPlayer1CurStats();
                printPlayer2CurStats();
                
                // do a life leach tick
                visuals.sb_LifeLeech_A(1);
                Thread.sleep(betweenAnimationsSleep);
                visuals.critHit_A(2);
                visuals.robotDamaged_A(2);
                Thread.sleep(sleepTimeForAnimations);

                // Keep track of the tick by updating the summary object
                matchSummary.offSetRobot1Damage(robotACurHitDmg);
                matchSummary.offsetRobot1NumCrits(1);
                Thread.sleep(sleepTime);
            } else {
                // get the damage to deal and to block
                robotACurHitDmg = getDamageToDeal(true, false);
                damageBlocked = getRobotDamageBlocked(robotACurHitDmg, true);
                robotACurHitDmg = robotACurHitDmg - damageBlocked;

                healthHealed = (int)(robotACurHitDmg * .75);

                // output the information regarding the current tick and then do the attack and damaged animation
                printMatchInfoHeader(matchNumber, "Player 1 " + robotA.getRobotName() + " has landed a life leech hit of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")\n\t" + "Player 1 has healed " + healthHealed + " health from life leech");

                menu.outputCommands("Player 1 " + robotA.getRobotName() + " has landed a life leech hit of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")\n" + "Player 1 has healed " + healthHealed + " health from life leech");
                // play sound
                new Thread(new soundRunnable(sounds.sound.lifeLeech)).start();
                
                
                // print the updated player robot stats
                robotBHealth = robotBHealth - robotACurHitDmg;

                // gain the health from the life leech attack
                robotAHealth += healthHealed;

                // do a life leach tick
                printPlayer1CurStats();
                printPlayer2CurStats();
                visuals.sb_LifeLeech_A(1);
                Thread.sleep(betweenAnimationsSleep);
                visuals.robotDamaged_A(2);
                Thread.sleep(sleepTimeForAnimations);

                // Keep track of the tick by updating the summary object
                matchSummary.offSetRobot1Damage(robotACurHitDmg);
                Thread.sleep(sleepTime);
            }
        } else {

            if (flipCoin(robotBLuckMulti)) {
                // get the damage to deal and to block
                robotBCurHitDmg = getDamageToDeal(false, true);
                damageBlocked = getRobotDamageBlocked(robotBCurHitDmg, false);
                robotBCurHitDmg = robotBCurHitDmg - damageBlocked;

                healthHealed = (int)(robotBCurHitDmg * .75);

                // output the information regarding the current tick and then do the attack and damaged animation
                printMatchInfoHeader(matchNumber, "Player 2 " + robotB.getRobotName() + " has landed a critical life leech strike of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")\n\t" + "Player 2 has healed " + healthHealed + " health from life leech");
 
                menu.outputCommands("Player 2 " + robotB.getRobotName() + " has landed a critical life leech strike of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")\n" + "Player 2 has healed " + healthHealed + " health from life leech");
                // play sound
                new Thread(new soundRunnable(sounds.sound.lifeLeech)).start();
                
                // print the updated player robot stats
                robotAHealth = robotAHealth - robotBCurHitDmg;

                // gain the health from the life leech attack
                robotBHealth += healthHealed;

                // do a life leach tick
                printPlayer1CurStats();
                printPlayer2CurStats();
                visuals.sb_LifeLeech_A(2);
                Thread.sleep(betweenAnimationsSleep);
                visuals.critHit_A(1);
                visuals.robotDamaged_A(1);
                Thread.sleep(sleepTimeForAnimations);

                // Keep track of the tick by updating the summary object
                matchSummary.offSetRobot2Damage(robotBCurHitDmg);
                matchSummary.offsetRobot2NumCrits(1);
                Thread.sleep(sleepTime);
            } else {
                // get the damage to deal and to block
                robotBCurHitDmg = getDamageToDeal(false, false);
                damageBlocked = getRobotDamageBlocked(robotBCurHitDmg, false);
                robotBCurHitDmg = robotBCurHitDmg - damageBlocked;

                healthHealed = (int)(robotBCurHitDmg * .75);

                // output the information regarding the current tick and then do the attack and damaged animation
                printMatchInfoHeader(matchNumber, "Player 2 " + robotB.getRobotName() + " has landed a life leech hit of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")\n\t" + "Player 2 has healed " + healthHealed + " health from life leech");

                menu.outputCommands("Player 2 " + robotB.getRobotName() + " has landed a life leech hit of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")\n" + "Player 2 has healed " + healthHealed + " health from life leech");
                // play sound
                new Thread(new soundRunnable(sounds.sound.lifeLeech)).start();
                
                // print the updated player robot stats
                robotAHealth = robotAHealth - robotBCurHitDmg;

                // gain the health from the life leech attack
                robotBHealth += healthHealed;

                // do a life leach tick
                printPlayer1CurStats();
                printPlayer2CurStats();
                visuals.sb_LifeLeech_A(2);
                Thread.sleep(betweenAnimationsSleep);
                visuals.robotDamaged_A(1);
                Thread.sleep(sleepTimeForAnimations);


                // Keep track of the tick by updating the summary object
                matchSummary.offSetRobot2Damage(robotBCurHitDmg);
                Thread.sleep(sleepTime);
            }
        }
    }

    /*
     * doATick - Does the entire logic behind all fights. 
     */
    public void doATick(boolean isRobotA, summary matchSummary, int matchNumber) throws InterruptedException {

        // variables to handle the robot tick
        int damageBlocked = 0;
        int robotACurHitDmg = 0;
        int robotBCurHitDmg = 0;

        // variables to handle match pacing
        int sleepTime = 500;
        int sleepTimeCoinFlip = 250;
        int sleepTimeForAnimations = 1000;
        int betweenAnimationsSleep = 250;

        if (isRobotA) {
            // check if robotB was confused (skip a turn)
            if (robotAGroundSlamSkipAttack) {
                robotAStatus = 0;
                robotAGroundSlamSkipAttack = false;
                return;
            }

            // Flip for critical srike and then do tick based on that result
            if (flipCoin(robotALuckMulti)) {

                // if robotA has shield block roll for blocking the crit
                if (robotBSpecialBehavior.equals("Shield block")) {
                    if (flipCoin(.75)) {
                        doAShieldBlockTick(isRobotA, matchSummary, matchNumber);
                        return;

                    }
                    // roll for shield bash
                } else if (robotASpecialBehavior.equals("Shield bash")) {
                    if (flipCoin(.5)) {
                        robotALuckMulti = .30 + robotA.getLuck() * 0.03;
                        doAShieldBashTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                    // roll for dodge roll
                } else if (robotBSpecialBehavior.equals("Dodge roll")) {
                    if (flipCoin(.1)) {
                        doADodgeRollTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                    // roll for ground slam
                } else if (robotASpecialBehavior == "Ground slam") {
                    robotALuckMulti = .20 + robotA.getLuck() * 0.02;
                    doAGroundSlamTick(isRobotA, matchSummary, matchNumber);
                    return;
                }
                // roll for life leech
                if (robotASpecialBehavior == "Life leech") {
                    if(flipCoin(.3)){
                        doALifeLeechTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }

                // get the damage to deal and to block
                robotACurHitDmg = getDamageToDeal(true, true);
                damageBlocked = getRobotDamageBlocked(robotACurHitDmg, true);
                robotACurHitDmg = robotACurHitDmg - damageBlocked;

                // output the information regarding the current tick and then do the attack and damaged animation
                printMatchInfoHeader(matchNumber, "Player 1 " + robotA.getRobotName() + " has landed a critical strike of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");

                menu.outputCommands("Player 1 " + robotA.getRobotName() + " has landed a critical strike of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
                // play sound
                new Thread(new soundRunnable(sounds.sound.crit)).start();
                
                // print the updated player robot stats
                robotBHealth = robotBHealth - robotACurHitDmg;
                printPlayer2CurStats();
                
                // do a critical normal attack
                visuals.robotAttackNormal_A(1);
                Thread.sleep(betweenAnimationsSleep);
                visuals.critHit_A(2);
                visuals.robotDamaged_A(2);
                Thread.sleep(sleepTimeForAnimations);

                // Keep track of the tick by updating the summary object
                matchSummary.offSetRobot1Damage(robotACurHitDmg);
                matchSummary.offsetRobot1NumCrits(1);
                Thread.sleep(sleepTime);
            } else {
                // roll for dodge roll
                if (robotBSpecialBehavior.equals("Dodge roll")) {
                    if (flipCoin(.1)) {
                        doADodgeRollTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                // roll for vine whip
                if(robotAElementType == "Grass Type"){
                    if(flipCoin(.2)){
                        doAVineWhipTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                // roll for water beam
                if(robotAElementType == "Water Type"){
                    if(flipCoin(.2)){
                        doAWaterBeamTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                // roll for fire punch
                if(robotAElementType == "Fire Type"){
                    if(flipCoin(.2)){
                        doAFirePunchTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                // roll for life leech
                if (robotASpecialBehavior == "Life leech") {
                    if(flipCoin(.3)){
                        doALifeLeechTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                
                // get the damage to deal and to block
                robotACurHitDmg = getDamageToDeal(true, false);
                damageBlocked = getRobotDamageBlocked(robotACurHitDmg, true);
                robotACurHitDmg = robotACurHitDmg - damageBlocked;

                // output the information regarding the current tick and then do the attack and damaged animation
                printMatchInfoHeader(matchNumber, "Player 1 " + robotA.getRobotName() + " has landed a hit of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked)  + " - blocked " + damageBlocked + ")");
   
                menu.outputCommands("Player 1 " + robotA.getRobotName() + " has landed a hit of " + robotACurHitDmg + " (dmg " + (robotACurHitDmg + damageBlocked)  + " - blocked " + damageBlocked + ")");
                // play sound
                new Thread(new soundRunnable(sounds.sound.hit)).start();
                
                // print the updated player robot stats
                robotBHealth = robotBHealth - robotACurHitDmg;
                printPlayer2CurStats();
                visuals.robotAttackNormal_A(1);
                Thread.sleep(betweenAnimationsSleep);
                visuals.robotDamaged_A(2);
                Thread.sleep(sleepTimeForAnimations);

                // Keep track of the tick by updating the summary object
                matchSummary.offSetRobot1Damage(robotACurHitDmg);
                Thread.sleep(sleepTime);
            }
        } else {
            // check if robotB was confused (skip a turn)
            if (robotBGroundSlamSkipAttack) {
                robotBStatus = 0;
                robotBGroundSlamSkipAttack = false;
                return;
            }

            if (flipCoin(robotBLuckMulti)) {

                // if robotA has shield block roll for blocking the crit
                if (robotASpecialBehavior.equals("Shield block")) {
                    if (flipCoin(.75)) {
                        doAShieldBlockTick(isRobotA, matchSummary, matchNumber);
                        return;

                    }
                    // roll for shield bash
                } else if (robotBSpecialBehavior.equals("Shield bash")) {
                    if (flipCoin(.5)) {
                        robotALuckMulti = .30 + robotA.getLuck() * 0.03;
                        doAShieldBashTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                    // roll for dodge roll
                } else if (robotASpecialBehavior.equals("Dodge roll")) {
                    if (flipCoin(.1)) {
                        doADodgeRollTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                    // roll for ground slam
                } else if (robotBSpecialBehavior == "Ground slam") {
                    robotALuckMulti = .20 + robotA.getLuck() * 0.02;
                    doAGroundSlamTick(isRobotA, matchSummary, matchNumber);
                    return;
                }
                // roll for life leech
                if (robotBSpecialBehavior == "Life leech") {
                    if(flipCoin(.3)){
                        doALifeLeechTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                // get the damage to deal and to block
                robotBCurHitDmg = getDamageToDeal(false, true);
                damageBlocked = getRobotDamageBlocked(robotBCurHitDmg, false);
                robotBCurHitDmg = robotBCurHitDmg - damageBlocked;

                // output the information regarding the current tick and then do the attack and damaged animation
                printMatchInfoHeader(matchNumber, "Player 2 " + robotB.getRobotName() + " has landed a critical strike of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");

                menu.outputCommands("Player 2 " + robotB.getRobotName() + " has landed a critical strike of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
                // play sound
                new Thread(new soundRunnable(sounds.sound.crit)).start();
                
                // print the updated player robot stats
                robotAHealth = robotAHealth - robotBCurHitDmg;
                printPlayer1CurStats();
                visuals.robotAttackNormal_A(2);
                Thread.sleep(betweenAnimationsSleep);
                visuals.critHit_A(1);
                visuals.robotDamaged_A(1);
                Thread.sleep(sleepTimeForAnimations);


                // Keep track of the tick by updating the summary object
                matchSummary.offSetRobot2Damage(robotBCurHitDmg);
                matchSummary.offsetRobot2NumCrits(1);
                Thread.sleep(sleepTime);
            } else {

                // roll for dodge roll
                if (robotASpecialBehavior.equals("Dodge roll")) {
                    if (flipCoin(.1)) {
                        doADodgeRollTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                // roll for vine whip
                if(robotBElementType == "Grass Type"){
                    if(flipCoin(.2)){
                        doAVineWhipTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                // roll for water beam
                if(robotBElementType == "Water Type"){
                    if(flipCoin(.2)){
                        doAWaterBeamTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                // roll for fire punch
                if(robotBElementType == "Fire Type"){
                    if(flipCoin(.2)){
                        doAFirePunchTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                // roll for life leech
                if (robotBSpecialBehavior == "Life leech") {
                    if(flipCoin(.3)){
                        doALifeLeechTick(isRobotA, matchSummary, matchNumber);
                        return;
                    }
                }
                
                // get the damage to deal and to block
                robotBCurHitDmg = getDamageToDeal(false, false);
                damageBlocked = getRobotDamageBlocked(robotBCurHitDmg, false);
                robotBCurHitDmg = robotBCurHitDmg - damageBlocked;

                // output the information regarding the current tick and then do the attack and damaged animation
                printMatchInfoHeader(matchNumber, "Player 2 " + robotB.getRobotName() + " has landed a hit of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");

                menu.outputCommands("Player 2 " + robotB.getRobotName() + " has landed a hit of " + robotBCurHitDmg + " (dmg " + (robotBCurHitDmg + damageBlocked) + " - blocked " + damageBlocked + ")");
                // play sound
                new Thread(new soundRunnable(sounds.sound.hit)).start();
                
                // print the updated player robot stats
                robotAHealth = robotAHealth - robotBCurHitDmg;
                printPlayer1CurStats();
                visuals.robotAttackNormal_A(2);
                Thread.sleep(betweenAnimationsSleep);
                visuals.robotDamaged_A(1);
                Thread.sleep(sleepTimeForAnimations);

                // Keep track of the tick by updating the summary object
                matchSummary.offSetRobot2Damage(robotBCurHitDmg);
                Thread.sleep(sleepTime);
            }
        }
    }


    /*
     * doAMatch completes an entire match cycle for 2 given robots
     */
    public summary doAMatch(int matchNumber) throws InterruptedException {
        // Declare all of our variables used for the match and our summary object
        summary matchSummary = new summary();

        int debug = 0;
        int sleepTime = 500;

        // reset the robots health for the current match
        robotAHealth = getRobotHealth(robotA);
        robotBHealth = getRobotHealth(robotB);

        // Prints the match header
        printMatchHeader(matchNumber);

        // print the first output of the robot and match information into their corresponding window
        printMatchInfoHeader(matchNumber, "");
        printPlayer1CurStats();
        printPlayer2CurStats();
        Thread.sleep(sleepTime);
        
        // set up the robots 
        visuals.saveRobot(robotA, robotB);

        // Notifies user that the coin flip is about to begin
        Thread.sleep(1000);
        printMatchInfoHeader(matchNumber, "COIN FLIP TO DETERMINE WHICH ROBOT HITS FIRST");
        //menu.outputCommandsFight("COIN FLIP TO DETERMINE WHICH ROBOT HITS FIRST", 0);
        menu.outputCommands("COIN FLIP TO DETERMINE WHICH ROBOT HITS FIRST\n");
        // play sound
        new Thread(new soundRunnable(sounds.sound.coinFlip)).start();

        //playes coin animation
        visuals.coinflip_A();
        Thread.sleep(3000);

        if (flipCoin(0.5)) {
            // coin lands on player one
            visuals.coinPick_A(1);

            // Notify the user of the coin flip result
            printMatchInfoHeader(matchNumber, "Player 1 has won the coin flip and will go first!");
            //menu.outputCommandsFight("Player 1 has won the coin flip and will go first!", 0);
            menu.outputCommands("Player 1 has won the coin flip and will go first!\n\n");
            Thread.sleep(2000);

            doATick(true, matchSummary, matchNumber);
            // Check if robotB has been eliminated
            if (!(robotBHealth <= 0)) {
                doATick(false, matchSummary, matchNumber);
            }
        } else {
            // coin lands on player one
            visuals.coinPick_A(2);
            // Notify the user of the coin flip result
            printMatchInfoHeader(matchNumber, "Player 2 has won the coin flip and will go first!");
            //menu.outputCommandsFight("Player 2 has won the coin flip and will go first!", 0);
            menu.outputCommands("Player 2 has won the coin flip and will go first!\n\n");
            Thread.sleep(2000);

            doATick(false, matchSummary, matchNumber);
        }

        while (robotAHealth > 0 && robotBHealth > 0) {
            // Print tick header
            menu.outputCommands("\n" + robotA.getRobotName() + " VS " + robotB.getRobotName());
            menu.outputCommands("Health: " + robotAHealth + "   " + robotBHealth + "\n");
            Thread.sleep(sleepTime);

            // do a normal tick
            doATick(true, matchSummary, matchNumber);

            // Check if robotB has been eliminated
            if (robotBHealth <= 0) {
                break;
            }
            // do a normal tick
            doATick(false, matchSummary, matchNumber);

        }

        // Finalize the summary object
        matchSummary.setRobot1LifeEOM(robotAHealth);
        matchSummary.setRobot2LifeEOM(robotBHealth);
        matchSummary.didRobot1Win((robotBHealth <= 0));

        // Print a final ouutput standing which robot won
        if (matchSummary.getRobot1Win()) {
            printMatchInfoHeader(matchNumber, "Player 1 HAS WON\n");
            // play sound
            new Thread(new soundRunnable(sounds.sound.victory)).start();
            menu.outputCommands("Player 1" + " HAS WON\n");
        } else {
            printMatchInfoHeader(matchNumber, "Player 2 HAS WON\n");
            // play sound
            new Thread(new soundRunnable(sounds.sound.victory)).start();
            menu.outputCommands("Player 2" + " HAS WON\n");
        }
        Thread.sleep(2000);

        menu.outputCommands("final health standings are ");
        menu.outputCommands(robotA.getRobotName() + " VS " + robotB.getRobotName());
        menu.outputCommands("Health: " + robotAHealth + "   " + robotBHealth + "\n");

        // Return the matchSummary
        return matchSummary;
    }
}