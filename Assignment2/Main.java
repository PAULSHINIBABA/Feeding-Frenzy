/*
 * Author: Robert Tubman
 * ID: 11115713
 *
 * The Main Class which contains the entry point.
 * As well as the code to wrap all the classes together to make the game run.
 */

package Assignment2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Main extends GameEngine {
    public static void main(String[] args) {
        createGame(new Main());
    }
    public void init() {
        InitSystem();
        initMenu();
        initCheckout();
        initEnvironment();
        initPlayer();
        initItems();
        initEnemies();
    }
    public void update(double dt) {
        updateMenu(dt);
        updateCheckout();

        // While in-game, if the level is complete stop processing
        if (this.env.getIsLevelComplete()) { return; }
        updateEnvironment();

        // The environment processing for the pause is still occurring above
        if (this.env.getIsPaused()) { return; }

        updatePlayer(dt);
        updateItems(dt);
        updateEnemies(dt);
    }
    public void paintComponent() {
        drawMenu();
        drawCheckout();

        drawEnvironment();

        drawEnemies(this.enemies); // Draw enemy first
        drawItems(); // Then draw items
        drawPlayer(); // Then draw the player

        drawEnvironmentLayerTop();
    }


    //-------------------------------------------------------
    // System methods
    //-------------------------------------------------------
    // "nothing"; no game state change was clicked
    // "single_player"; single player game state change was clicked
    // "time_attack"; time attack game state change was clicked
    String gameStateString;

    // gameState = 0; Main menu
    // gameState = 1; Loading page
    // gameState = 2; Settings page
    // gameState = 3; Help page
    // gameState = 4; Environment(Single Player)
    // gameState = 5; Environment(Time Attack)
    // gameState = 6; Checkout page
    int gameState;
    boolean isSinglePlayer;
    Random randSys;
    int width;
    int height;
    int eatenPearlCount;
    int eatenStarfishCount;
    boolean hasGrownMedium;
    boolean hasGrownLarge;

    public void InitSystem() {
        // System random field
        this.randSys = new Random();

        // System window size
        this.width = 700;
        this.height = 700;

        // Set the window
        setWindowSize(this.width, this.height);

        // Game state
        this.gameState = 0;
        this.gameStateString = "nothing";

        // Single player mode default
        this.isSinglePlayer = true;

        // Player grown state
        this.hasGrownMedium = false;
        this.hasGrownLarge = false;

        // Score tracker
        this.eatenPearlCount = 0;
        this.eatenStarfishCount = 0;
    }

    public void colliderCheck() {
        // Get the player head collider x position
        double px;
        if (this.myFishFacing) { px = this.myfish.mposition_x; }
        else { px = this.myfish.mposition_x + this.myfish.myfish_w; }

        // Get the player head collider y position
        double py = this.myfish.mposition_y + this.myfish.getFishHeadColliderYOffset();

        // Get the player head collider radius
        double pr = this.myfish.getFishHeadColliderRadius();

        // Check if the enemy is at the head
        ArrayList<Enemy> removalValues = new ArrayList<Enemy>();
        for (Enemy en : this.enemies) {
            // Get enemy body position and area
            double ex = en.getXPos();
            double ey = en.getYPos();
            double el = en.getColliderBodyLength();
            double eh = en.getColliderBodyHeight();

            // Check players head to enemies body collision
            if (calcDistCircleToSquare(px, py, pr, ex, ey, el, eh)) {
                // There is a collision
                int enSize = en.getSize();
                // If the player can eat the enemy (based on size equality)
                if (this.myfish.getSize() >= enSize) {
                    // Player was equal to or greater than the enemy
                    // Get the score based on enemy size
                    int eatScore = en.getSize() + 1;
                    // Set the player score in the environment
                    this.env.setScore((this.env.getScore() + eatScore));
                    // Increase the players current progress to the level goal
                    this.env.setCurrentGoal(this.env.getCurrentGoal() + 1);
                    // Add enemy to removal list
                    removalValues.add(en);
                }
            }
        }

        // Remove enemies in the removal list
        this.removeEnemies(this.enemies, removalValues);
    }

    public void growPlayerSize() {
        // Get the players current goal state
        int currentGoal = this.env.getCurrentGoal();
        // Get the threshold for growing
        int mediumThreshold = this.env.getGrowthThresholdMedium();
        int largeThreshold = this.env.getGrowthThresholdLarge();

        // Check if the player has reached the medium size threshold
        if (!this.hasGrownMedium && (currentGoal >= mediumThreshold)) {
            this.hasGrownMedium = true;
            this.myfish.increaseSize();
        }
        // Check if the player has reached the large size threshold
        if (!this.hasGrownLarge && (currentGoal >= largeThreshold)) {
            this.hasGrownLarge = true;
            this.myfish.increaseSize();
        }
    }

    // Calculate whether there is a collision between the circle head of player/enemy against the targets body
    // Reference: https://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
    public boolean calcDistCircleToSquare(double px, double py, double pr, double ex, double ey, double el, double eh) {
        double dPlayerX = Math.abs(px - ex);
        double dPlayerY = Math.abs(py - ey);

        // Check distance outside collider box beyond outer boundary
        if (dPlayerX > ((el / 2) + pr)) { return false; }
        if (dPlayerY > ((eh / 2) + pr)) { return false; }

        // Check whether circle is in collider box
        if (dPlayerX <= ((el / 2))) { return true; }
        if (dPlayerY <= ((eh / 2))) { return true; }

        // Check the corner distance
        double dCorner = Math.pow((dPlayerX - (el / 2)), 2) + Math.pow((dPlayerY - (eh / 2)), 2);
        return (dCorner <= Math.pow(pr, 2));
    }

    public void resetGameLevel() {
        // Player grown state
        this.hasGrownMedium = false;
        this.hasGrownLarge = false;

        // Score tracker
        this.eatenPearlCount = 0;
        this.eatenStarfishCount = 0;

        // Reset the player score
        this.env.setScore(0);

        // TODO: remove all enemies from the level
        // TODO: remove all items from the level
        // TODO: reset the player position
        // TODO: reset the player size

        // TODO: reset the level time to 0
        // TODO: IF TIME ATTACK reset the current time
        // TODO: Increase next level parameters and set
    }


    //-------------------------------------------------------
    // Mouse Handler
    //-------------------------------------------------------
    public double mouseX;
    public double mouseY;
    boolean mouse1Pressed = false;

    // MousePressed Event Handler
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            // TODO: Should streamline this so the mouse handler interrupt can return quicker.
            if (gameState == 0) {
                this.gameStateString = this.sm.menuMouseClicked(e);
            } else if (this.gameState == 6) {
                System.out.println("Mouse pressed!\tgameState: " + gameState);
                this.gameStateString = this.chkpg.checkClickTarget(this.mouseX, this.mouseY);
            }
        }
    }

    // MouseReleased Event Handler
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) { this.mouse1Pressed = false; }
    }

    // MouseMoved Event Handler
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }


    //-------------------------------------------------------
    // Key Presses
    //-------------------------------------------------------
    public boolean rightKey;
    public boolean leftKey;
    public boolean upKey;
    public boolean downKey;
    public boolean escKey;
    public boolean spaceKey;

    // Called whenever a key is pressed
    public void keyPressed(KeyEvent e) {
//        //The user pressed left arrow
        if(e.getKeyCode() == KeyEvent.VK_LEFT) { this.leftKey = true; }
        // The user pressed right arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { this.rightKey = true; }
        // The user pressed up arrow
        if(e.getKeyCode() == KeyEvent.VK_UP) { this.upKey = true; }
        // The user pressed up arrow
        if(e.getKeyCode() == KeyEvent.VK_DOWN) { this.downKey = true; }
        // The user pressed ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { this.escKey = true; }
        // The user pressed spaceKey
        if(e.getKeyCode() == KeyEvent.VK_SPACE) { this.spaceKey = true; }
    }

    // Called whenever a key is released
    public void keyReleased(KeyEvent e) {
//        // The user released left arrow
        if(e.getKeyCode() == KeyEvent.VK_LEFT) { this.leftKey = false; }
        // The user released right arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { this.rightKey = false; }
        // The user released up arrow
        if(e.getKeyCode() == KeyEvent.VK_UP) { this.upKey = false; }
        // The user released up arrow
        if(e.getKeyCode() == KeyEvent.VK_DOWN) { this.downKey = false; }
        // The user released ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { this.escKey = false; }
        // The user released spaceKey
        if(e.getKeyCode() == KeyEvent.VK_SPACE) { this.spaceKey = false; }
    }


    //-------------------------------------------------------
    // Menu methods
    //-------------------------------------------------------
    public StartMenu sm;
    public LoadingPage lp;

    public void initMenu() {
        this.sm = new StartMenu(this);
        this.lp = new LoadingPage(this);
    }

    public void updateMenu(double dt) {
        if (gameState == 0) {
            if (Objects.equals(this.gameStateString, "single_player")) {
                this.gameState = 1; // Go to LoadingPage
                this.env.setIsTimeAttack(false); // Set the environment to single player mode

            } else if (Objects.equals(this.gameStateString, "time_attack")) {
                this.gameState = 1; // Go to LoadingPage
                this.env.setIsTimeAttack(true); // Set the environment to time attack mode
            }
        } else if (gameState == 1) {
            this.lp.startLoading();
            this.lp.updatePage(dt);
            // TODO: add this back in so the player can't skip the loading page
//            if (spaceKey && (this.lp.getProgress() >= 1.0)) {
            if (spaceKey) {
                // Set to the game play area instance
                this.gameState = 4;
                this.env.setBaseTime(getTime());
                this.env.setIsPaused(false);
            }
        }
    }

    public void drawMenu() {
        if (this.gameState == 0) { this.sm.drawAll(); }
        else if (gameState == 1) { this.lp.drawAll(); }
    }


    //-------------------------------------------------------
    // CheckoutPage methods
    //-------------------------------------------------------
    CheckoutPage chkpg;
    Image checkoutImage;
    Image backButtonImage;
    Image continueiButtonImage;
    public void initCheckout() {
        try {
            this.checkoutImage = loadImage("Assignment2/assets/image/background/background4.png");
            this.backButtonImage = loadImage("Assignment2/assets/image/icon/icon_return2.png");
            // Image should be reversed when displayed
            this.continueiButtonImage = loadImage("Assignment2/assets/image/icon/icon_return2.png");

        } catch(Exception e) {
            e.printStackTrace();
        }

        this.chkpg = new CheckoutPage(this,
                this.checkoutImage,
                this.backButtonImage,
                this.continueiButtonImage,
                this.width,
                this.height);
    }
    public void updateCheckout() {
        if (this.gameState == 6) {
            // Checkout page should load.
            this.chkpg.setScore(this.env.getScore());

            if (this.gameStateString == "main_menu") {
                this.gameState = 0;
                // TODO: Reset the level
                resetGameLevel();
            } else if (this.gameStateString == "next_level") {
                // TODO: reset the level and reload to state = 4
                resetGameLevel();
                nextLevel();
            } else {}
        }
    }
    public void drawCheckout() {
        if (this.gameState == 6) {
            this.chkpg.drawCheckout();
        }
    }

    //-------------------------------------------------------
    // Player methods
    //-------------------------------------------------------
    myfish myfish;
    boolean myFishFacing;
    boolean gamestart; // TODO: Replace to merge with the rest
    Image myfish_r;
    Image myfish_l;
    Image pearlimage;
    Image boomimage;
    Image starfishimage;
    Image backgoundimage;
    public void initPlayer() {
        this.myfish = new myfish();
        this.myfish.setFishLength(60);
        this.myfish.setFishHeight(40);
        this.gamestart = false;
        this.myFishFacing = false;
//        //load image
//        this.backgoundimage = loadImage("Assignment2/assets/image/background/background.png");

        // TODO: Compress the _r _l into one image and use a reflect method
        this.myfish_r = loadImage("Assignment2/assets/image/entity/entity_player1.png");
//        this.myfish_l = loadImage("Assignment2/assets/image/myfish_l.png");
    }
    public void updatePlayer(double dt) {
        if (gameState == 4) {
            // TODO: CHEAT for debug, remove when done
            if (this.spaceKey) { this.myfish.setIsAlive(true); }


            // TODO: Should update this to go to a game over screen instead
            if (!this.gamestart) { this.gamestart = true; }

            // Player is dead don't process the player
            if (!this.myfish.getIsAlive()) { return; }

            if (this.leftKey) {
                this.myFishFacing = true;
            } else if (this.rightKey) {
                this.myFishFacing = false;
            }

            if (this.myfish.getIsAlive()) {
                colliderCheck();
                growPlayerSize();
            }

            // playArea fields
            double playAreaX = this.env.getPlayAreaX();
            double playAreaY = this.env.getPlayAreaY();
            double playAreaWidth = this.env.getPlayAreaWidth();
            double playAreaHeight = this.env.getPlayAreaHeight();

            // Fish moves within the playArea bounds
            myfish.updatemyfish(dt,
                    this.upKey,
                    this.downKey,
                    this.leftKey,
                    this.rightKey,
                    playAreaX,
                    playAreaY,
                    playAreaWidth,
                    playAreaHeight);

            // Check for collision with the enemy
            // TODO: Will probably need to refactor this with the Enemy code
//            if (myfish.getmyfishRec().intersects(getenemiefishRec())) {
////                // Increase fish size
//                this.myfish.increaseSize();
////                myfish.myfish_w += 3;
////                myfish.myfish_h += 12;
//            }
        }
    }
    public void drawPlayer() {
        if (this.gameState == 4) {
            // Player is dead don't process the player
            if (!this.myfish.getIsAlive()) { return; }

            if (this.gamestart) { this.drawMyself(); }

            // TODO: Handle the game over state (need menu?)
        }

    }

    // Create a rectangle of the enemy?
    // TODO: Will need to refactor this with the Enemy code
//    public Rectangle getenemiefishRec(){
//        //enemies width and enemies high
//        int emfish_w = 15;
//        int emfish_h = 15;
////        return new Rectangle((int) emposition_x, (int) emposition_y, emfish_w, emfish_h);
//        return new Rectangle((int) 0, (int) 0, emfish_w, emfish_h);
//    }

    public void drawMyself() {
        // Decide which image to draw based on direction
        // TODO: Should refactor this. (use a reflect image method with one image only)
//        Image currentFishImage = this.leftKey ? myfish_l : myfish_r;
        Image currentFishImage = myfish_l;

        if (this.myFishFacing) { currentFishImage = myfish_l; }
        else { currentFishImage = myfish_r; }

        // Draw the fish at its current position
        drawImage(currentFishImage, (int)myfish.mposition_x,  (int)myfish.mposition_y, myfish.myfish_w, myfish.myfish_h);

        // Draw the collider box
        changeColor(0,255,0);
        // top
        drawLine(myfish.mposition_x,
                myfish.mposition_y,
                (myfish.mposition_x + myfish.myfish_w),
                myfish.mposition_y);
        // bottom
        drawLine(myfish.mposition_x,
                (myfish.mposition_y + myfish.myfish_h),
                (myfish.mposition_x + myfish.myfish_w),
                (myfish.mposition_y + myfish.myfish_h));
        // left
        drawLine(myfish.mposition_x,
                myfish.mposition_y,
                myfish.mposition_x,
                (myfish.mposition_y + myfish.myfish_h));
        // right
        drawLine((myfish.mposition_x + myfish.myfish_w),
                myfish.mposition_y,
                (myfish.mposition_x + myfish.myfish_w),
                (myfish.mposition_y + myfish.myfish_h));

        changeColor(255,0,0);
        double fishX;
        double fishY = this.myfish.mposition_y + this.myfish.getFishHeadColliderYOffset();
        double fishR = this.myfish.getFishHeadColliderRadius();
        if (this.myFishFacing) { fishX = this.myfish.mposition_x; }
        else { fishX = this.myfish.mposition_x + this.myfish.myfish_w; }
        drawCircle(fishX, fishY, fishR);

    }

//    public Rectangle getenemiefishRec(Enemy en){
//        return new Rectangle((int)en.getXPos(),
//                (int)en.getYPos(),
//                (int)en.getColliderBodyLength(),
//                (int)en.getHeight());
//    }


    //-------------------------------------------------------
    // Item methods
    //-------------------------------------------------------
    pearl pearl;
    boom boom;
    starfish starfish;
    public void initItems() {
        this.pearl = new pearl();
        this.boom = new boom();
        this.starfish = new starfish();

        this.pearlimage = loadImage("Assignment2/assets/image/item/item_pearl1.png");
        this.boomimage = loadImage("Assignment2/assets/image/item/item_bomb1.png");
        this.starfishimage = loadImage("Assignment2/assets/image/item/item_starfish1.png");
    }
    public void updateItems(double dt) {
        // TODO: Update the rand input values to use the playArea bounds
        // Set the pearl x,y pos
        double areaW = this.env.getPlayAreaWidth();
        double areaH = this.env.getPlayAreaHeight();
        double randStarX = rand(areaW);
        double randStarY = rand(areaH);
        double randBombX = rand(areaW);
        double randBombY = rand(areaH);
        double spawnOffsetWidth = this.env.getHUDWidth();
        double spawnOffsetHeight = this.env.getHUDHeight();

        double randSpeed = rand(100);
        if(gamestart) {
            //update pearl
            pearl.updatepearl(dt,
                    myfish.getmyfishRec(),
                    myfish.myfishspeed_x,
                    myfish.myfishspeed_y,
                    randStarX,
                    randStarY,
                    spawnOffsetWidth,
                    spawnOffsetHeight);

            //update starfish
            // TODO: update this to use the playArea bounds
            if (starfish.updatestarfish(dt,
                    myfish.getmyfishRec(),
                    randStarX,
                    randStarY,
                    spawnOffsetWidth,
                    spawnOffsetHeight,
                    randSpeed,
                    areaW,
                    areaH)) {

                // Update the environment score
                this.env.addScore(20);
            }

            // TODO: update this to use the playArea bounds
            if (boom.updateboom(dt,
                    myfish.getmyfishRec(),
                    randBombX,
                    randBombY,
                    spawnOffsetWidth,
                    spawnOffsetHeight,
                    randSpeed,
                    areaW,
                    areaH)) {

                // Set the gameStart to be false, ie. game over
                // TODO: load the game over menu state
                gamestart = false;
            }
        }

    }
    public void drawItems() {
        if (this.gameState == 4) {
            this.drawpearl();
            this.drawstarfish();
            this.drawboom();
        }
    }

    public void drawboom(){
        if (boom.isvisible()){
            drawImage(boomimage,boom.getpositionx(),boom.getpositiony(), boom.getwidth(), boom.getheight());
        }
    }
    public void drawstarfish(){
        if (starfish.isvisible()){
            drawImage(starfishimage,starfish.getpositionx(),starfish.getpositiony(),starfish.getwidth(),starfish.getheight());
        }
    }
    public void drawpearl(){
        if (pearl.isvisible()){
            changeColor(Color.white);
            drawImage(pearlimage,(int)pearl.getpositionx(),(int)pearl.getpositiony(), pearl.getwidth(), pearl.getheight());
        }
    }

    //-------------------------------------------------------
    // Enemy methods
    //-------------------------------------------------------
    int maxEnemies = 5; // Set to around 30
    ArrayList<Enemy> enemies;
    int maxShark = 5;
    ArrayList<Enemy> sharkEnemies;
    double enemySpawnTimer;
    double enemySpawnTimerPrevious;
    double enemySpawnDelay;
    Image enemyFish1;
    Image enemyFish2;
    Image enemyFish3;
    Image shark;
    public void initEnemies() {
        this.maxEnemies = this.width / 100;
        this.maxShark = this.width / 500;
        this.enemies = new ArrayList<Enemy>();
        this.sharkEnemies = new ArrayList<Enemy>();
        this.enemySpawnTimer = 0.0;
        this.enemySpawnTimerPrevious = 0.0;
        this.enemySpawnDelay = this.randSys.nextDouble(2.0) + 1.0;

        try {
            this.enemyFish1 = loadImage("Assignment2/assets/image/entity/entity_fish1.png");
            this.enemyFish2 = loadImage("Assignment2/assets/image/entity/entity_fish2.png");
            this.enemyFish3 = loadImage("Assignment2/assets/image/entity/entity_fish3.png");
            this.shark = loadImage("Assignment2/assets/image/entity/entity_shark1.png");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEnemies(double dt) {
        // Spawn enemy fish
        SpawnEnemy();

        // Process the normal enemies
        ArrayList<Enemy> removalValues = new ArrayList<Enemy>();
        if (this.enemies.size() > 0) {
            for (int i = 0; i < this.enemies.size(); i++) {
                this.updateEnemyPosition(dt, this.enemies.get(i), true);

                // Check if the player has been bit
                this.checkEnemyBitePlayer(this.enemies.get(i));

                this.checkEnemyBounds(this.enemies.get(i), removalValues);
            }
        }
        this.removeEnemies(this.enemies, removalValues);

        // Process the shark enemy
        removalValues = new ArrayList<Enemy>();
        if (this.sharkEnemies.size() > 0) {
            for (int i = 0; i < this.sharkEnemies.size(); i++) {
                this.updateEnemyPosition(dt, this.sharkEnemies.get(i), false);

                // Check if the player has been bit
                this.checkEnemyBitePlayer(this.sharkEnemies.get(i));

                this.checkEnemyBounds(this.sharkEnemies.get(i), removalValues);
            }
        }
        this.removeEnemies(this.sharkEnemies, removalValues);
    }

    public void checkEnemyBitePlayer(Enemy en) {
        // Enemy collider fields
        double ex;
        if (en.getHeadingY() == -1.0) { ex = en.getXPos() - en.getColliderHeadXOffset(); }
        else { ex = en.getXPos() + en.getColliderHeadXOffset(); }
        double ey = en.getYPos();
        double er = en.getColliderHeadRadius();

        // Player collider fields
        double px = this.myfish.mposition_x;
        double py = this.myfish.mposition_y;
        double pl = this.myfish.myfish_w;
        double ph = this.myfish.myfish_h;

        // Player loses if they are "bit"
        if (calcDistCircleToSquare(ex, ey, er, px, py, pl, ph)) {
            int enSize = en.getSize();
            if (enSize > this.myfish.getSize()) { this.myfish.setIsAlive(false); }
        }
    }

    public void updateEnemyPosition(double dt, Enemy en, boolean shouldRandomize) {
        if (shouldRandomize) { this.randomizeEnemyMovement(en); }

        double currentXPos = en.getXPos();
        double currentYPos = en.getYPos();
        double currentXHeading = en.getHeadingX();
        double currentYHeading = en.getHeadingY();
        double currentVel = en.getVelocity();

        double newXPos = currentXPos + currentXHeading * currentVel * dt;
        double newYPos = currentYPos + currentYHeading * currentVel * dt;

        en.setPos(newXPos, newYPos);
    }

    public void randomizeEnemyMovement(Enemy en) {
        // Roll each chance separately
        int changeChance = 1; // 1% chance
        // Randomize horizontal direction
        if (this.randSys.nextInt(100) < changeChance) { en.setRandomXHeading(); }
        // Randomize vertical direction
        if (this.randSys.nextInt(100) < changeChance) { en.setRandomYHeading(); }
        // Randomize velocity
        if (this.randSys.nextInt(100) < changeChance) { en.setRandomVelocity(25,100); }

        en.updateColliderHeadXOffset();
    }

    public void checkEnemyBounds(Enemy en, ArrayList<Enemy> removalValues) {
        int chanceRemoveEnemy = this.randSys.nextInt(100);
        boolean removeEnemy = (chanceRemoveEnemy < en.getChanceToLeaveEnvironment());

        int playAreaWidth = this.env.getPlayAreaWidth();
        int playAreaHeight = this.env.getPlayAreaHeight();
        double playAreaXOffset = this.env.getEnvironmentXOffset();
        double playAreaYOffset = this.env.getEnvironmentYOffset();

        double bodyLengthOffset =  en.getColliderBodyLength() / 2;
        double bodyHeightOffset =  en.getColliderBodyHeight() / 2;

        // Check horizontal bounds
        if ((en.getXPos() <= (playAreaXOffset - bodyLengthOffset)) && (en.getHeadingX() == -1.0)) {
            if (removeEnemy) { removalValues.add(en); }
            else {
                en.setXHeading(1.0);
                en.updateColliderHeadXOffset();
            }
        }
        if ((en.getXPos() >= (playAreaXOffset + playAreaWidth + bodyLengthOffset)) && (en.getHeadingX() == 1.0)) {
            if (removeEnemy) { removalValues.add(en); }
            else {
                en.setXHeading(-1.0);
                en.updateColliderHeadXOffset();
            }
        }

        // Check vertical bounds
        if (en.getYPos() <= (playAreaYOffset + bodyHeightOffset)) { en.setYHeading(1.0); }
        if (en.getYPos() >= (playAreaYOffset + playAreaHeight - bodyHeightOffset)) { en.setYHeading(-1.0); }
    }

    public void removeEnemies(ArrayList<Enemy> enemies, ArrayList<Enemy> removalValues) {
        // Remove the enemies that have been selected for removal.
        if (removalValues.size() > 0) {
            for (Enemy toRemove : removalValues) {
                enemies.remove(toRemove);
            }
        }
    }

    // The method to spawn an enemy on click
    // TODO: the enemy should spawn based on other methods, such as a timer.
    public void SpawnEnemy() {
        this.enemySpawnTimer = this.env.getCountDownTimerWOffset() - this.enemySpawnTimerPrevious;
        if (this.enemySpawnTimer > this.enemySpawnDelay) {
            this.createEnemy();
            this.enemySpawnDelay = this.randSys.nextDouble(2.0) + 1.0;
            this.enemySpawnTimerPrevious = this.enemySpawnTimer;
        }

        // TODO: consider when a shark should spawn
//        if (this.shiftKey) {
//            this.CreateShark();
//        }
    }

    public void drawEnemies(ArrayList<Enemy> enemies) {
        if (this.gameState == 4) {
            for (Enemy en : enemies) {
                this.drawEnemy(en);
                this.drawEnemyCollider(en);
                this.drawEnemyHeadCollider(en);
            }
        }
    }

    public void drawEnemy(Enemy en) {
        // TODO: Calculate the enemy image offset, to align with the collision box
        double imgX;
        double imgY = en.getImageYOffset();
        double imgLen = en.getImageLength();
        double imgHei = en.getImageHeight();
        Image enImg = en.getImage();

        // Draw the enemy image
        if (en.getHeadingX() == 1.0) {
            imgX = en.getImageXOffset();
            drawImage(enImg, imgX, imgY, imgLen, imgHei);
        } else {
            imgX = en.getImageXOffsetNegative();
            drawImage(enImg, imgX, imgY, -imgLen, imgHei);
        }

    }

    public void drawEnemyCollider(Enemy en) {
        // Retrieve the enemy collision fields
        double xPosCol = en.getColliderBodyXOffset();
        double yPosCol = en.getColliderBodyYOffset();
        double len = en.getLength();
        double hei = en.getHeight();
        double xPos = en.getXPos();
        double yPos = en.getYPos();

        // Calculate the collision offsets
        double x1 = xPos + xPosCol;
        double y1 = yPos + yPosCol;
        double x2 = xPos + xPosCol + len;
        double y2 = yPos + yPosCol + hei;

        // Draw the collision boxes
        changeColor(0, 255, 0);
        drawLine(x1,y1,x2,y1); // Collision line
        drawLine(x1,y2,x2,y2); // Collision line
        drawLine(x1,y1,x1,y2); // Collision line
        drawLine(x2,y1,x2,y2); // Collision line
        changeColor(255, 0, 0);
        drawSolidCircle(xPos, yPos, 2);
    }

    public void drawEnemyHeadCollider(Enemy en) {
        double xH = en.getColliderHeadXOffset();
        double yH = en.getColliderHeadYOffset();
        double rH = en.getColliderHeadRadius();
        double x = en.getXPos();
        double y = en.getYPos();

        double x1 = x + xH;
        double y1 = y + yH;

        changeColor(255,0,0);
        drawCircle(x1, y1, rH);
    }

    public void createEnemy() {
        if (this.enemies.size() >= this.maxEnemies) {
//            System.out.println("Max enemies reached.");
            return;
        }
//        System.out.println("Creating enemy.");

        // Randomize enemy selection
        // Should the spawn chances be modified such as:
        // - 45% small
        // - 33% medium
        // - 22% large
//        int selectSize = this.randSys.nextInt(4);
        final int enemySmall = 45;
        final int enemyMedium = 78; // 45 + 33
//        final int enemyLarge = 100;
        int selectSize = this.randSys.nextInt(100);

        int playAreaWidth = this.env.getPlayAreaWidth();
        int playAreaHeight = this.env.getPlayAreaHeight();
        double playAreaXOffset = this.env.getEnvironmentXOffset();
        double playAreaYOffset = this.env.getEnvironmentYOffset();

        Enemy en;
        if (selectSize < enemySmall) { // spawn a small fish
            en = new Enemy(this.enemyFish1, 0, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            // TODO: Match these offset values to the actual image sizes
//            en.setHeadOffset();
            en.setImageOffsets(-32,-32,64,64);
        } else if (selectSize < enemyMedium) { // spawn a medium fish
            en = new Enemy(this.enemyFish2, 1, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            // TODO: Match these offset values to the actual image sizes
            en.setImageOffsets(-32,-32,64,64);
        } else { // spawn a large fish
            en = new Enemy(this.enemyFish3, 2, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            // TODO: Match these offset values to the actual image sizes
            en.setImageOffsets(-32,-32,64,64);
        }

        this.enemies.add(en);
    }

    public void createShark() {
        if (this.sharkEnemies.size() >= this.maxShark) {
            System.out.println("Max sharks.");
            return;
        }

//        System.out.println("Create shark.");
        int playAreaWidth = env.getPlayAreaWidth();
        int playAreaHeight = env.getPlayAreaHeight();
        double playAreaXOffset = env.getEnvironmentXOffset();
        double playAreaYOffset = env.getEnvironmentYOffset();

        Enemy shk = new Enemy(this.shark, 3, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
        shk.setRandomVelocity(400, 400);
        shk.setYHeading(0.0);
        shk.setChanceToLeaveEnvironment(100);
        this.sharkEnemies.add(shk);
    }


    //-------------------------------------------------------
    // Environment methods
    //-------------------------------------------------------
    Environment env;
    int targetGoal;
    int targetGoalIncrement;
    int goalMaximum;
    double targetTime;
    double targetTimeAdjuster;
    double timeMinimum;
    boolean singlePlayerComplete;
    boolean timeAttackComplete;

    public void initEnvironment() {
        this.targetGoal = 10;
        this.targetGoalIncrement = 5;
        this.goalMaximum = 50;
        this.targetTime = 60.0;
        this.targetTimeAdjuster = 5.0;
        this.timeMinimum = 10.0;
        this.singlePlayerComplete = false;
        this.timeAttackComplete = false;

        this.env = new Environment(this, !this.isSinglePlayer);
        this.env.setEnvironmentPlayWidth(this.width);
        this.env.setEnvironmentPlayHeight(this.height);
//        this.env.
    }
    public void updateEnvironment() {
        if (gameState == 4) {
            this.env.environmentLevelCompleteCheck();

            boolean wasPaused = false;

            if (this.escKey) {
                if (this.env.getIsPaused()) {
                    this.env.setIsPaused(false);
                    wasPaused = true;
                } else {
                    this.env.setIsPaused(true);
                }
            }

            // Level is complete keep the game paused
            if (this.env.getIsLevelComplete()) {
                this.env.setIsPaused(true);
                this.gameState = 6;
            }

            UpdateTimer(wasPaused);

            // TODO: update the gameState to checkout page

        }
    }

    public void nextLevel() {
        // TODO: update the env state to be the next level
    }

    public void UpdateTimer(boolean wasPaused) {
        // return if paused
        if (this.env.getIsPaused()) { return; }


        // fields for paused timer
        double newTime = getTime();

        // Process the timer for paused case
        if (wasPaused) {
            double beforePause;
            double timeDelay;

            beforePause = this.env.getTimer();
            double afterPause = Math.round(((newTime - this.env.getBaseTime()) / 1000.0) * 100.0) / 100.0;
            timeDelay = (afterPause - beforePause);

            // Set the pausable timer
            this.env.addCountDownTimerOffset(timeDelay);
        }

        // Set the global timer
        this.env.setTimer(newTime);

        // Time attack counter processing
        if (this.env.getIsTimeAttack()) { this.env.setCountDownCurrentTimer(this.env.getCountDownTimerWOffset()); }
    }

    public void drawEnvironment() {
        if (gameState == 4) {
            this.env.drawEnvironment();
        }
    }

    public void drawEnvironmentLayerTop() {
        if (gameState == 4) {
            this.env.drawHUD();
            boolean timeAttack = this.env.getIsTimeAttack();
            this.env.drawTimer(timeAttack);
            this.env.drawScore();
            this.env.drawGrowth();
        }
    }
}
