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
        if (this.env.GetIsLevelComplete()) { return; }
        updateEnvironment();

        // The environment processing for the pause is still occurring above
        if (this.env.GetIsPaused()) { return; }

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
        this.gameState = 0;
        this.gameStateString = "nothing";
        this.isSinglePlayer = true;
        this.hasGrownMedium = false;
        this.hasGrownLarge = false;
        this.width = 500;
        this.height = 500;
//        this.score = 0;
        this.eatenPearlCount = 0;
        this.eatenStarfishCount = 0;
        this.randSys = new Random();
        setWindowSize(this.width, this.height);
    }

    public void colliderCheck() {
        double px;
        if (this.myFishFacing) { px = this.myfish.mposition_x; }
        else { px = this.myfish.mposition_x + this.myfish.myfish_w; }

        double py = this.myfish.mposition_y + this.myfish.getFishHeadColliderYOffset();
        double pr = this.myfish.getFishHeadColliderRadius();

        ArrayList<Enemy> removalValues = new ArrayList<Enemy>();
        for (Enemy en : this.enemies) {
            double ex = en.GetXPos();
            double ey = en.GetYPos();
            double el = en.GetColliderBodyLength();
            double eh = en.GetColliderBodyHeight();

            if (calcDistCircleToSquare(px, py, pr, ex, ey, el, eh)) {
                int enSize = en.GetSize();
                if (this.myfish.getSize() >= enSize) {
                    int eatScore = en.GetSize() + 1;
                    this.env.setScore((this.env.getScore() + eatScore));
                    this.env.SetCurrentGoal(this.env.getCurrentGoal() + 1);
                    removalValues.add(en);
                }
            }
        }

        this.removeEnemies(this.enemies, removalValues);
    }

    public void growPlayerSize() {
        int currentGoal = this.env.getCurrentGoal();
        int mediumThreshold = this.env.getGrowthThresholdMedium();
        int largeThreshold = this.env.getGrowthThresholdLarge();

        if (!this.hasGrownMedium && (currentGoal >= mediumThreshold)) {
            this.hasGrownMedium = true;
            this.myfish.increaseSize();
        }
        if (!this.hasGrownLarge && (currentGoal >= largeThreshold)) {
            this.hasGrownLarge = true;
            this.myfish.increaseSize();
        }
    }

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
        // TODO: reset the level state
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
//    public boolean shiftKey;
//    public boolean enterKey;
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
//        // The user pressed shiftKey
//        if(e.getKeyCode() == KeyEvent.VK_SHIFT) { this.shiftKey = true; }
//        // The user pressed enterKey
//        if(e.getKeyCode() == KeyEvent.VK_ENTER) { this.enterKey = true; }
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
//        // The user released shiftKey
//        if(e.getKeyCode() == KeyEvent.VK_SHIFT) { this.shiftKey = false; }
//        // The user released enterKey
//        if(e.getKeyCode() == KeyEvent.VK_ENTER) { this.enterKey = false; }
        // The user released spaceKey
        if(e.getKeyCode() == KeyEvent.VK_SPACE) { this.spaceKey = false; }
    }

    // Process the keys pressed for each game element.
//    public void processKeys() {
//        if (this.leftKey) {
//        }
//        if (this.rightKey) {
//        }
//        if (this.upKey) {
//        }
//        if (this.downKey) {
//        }
//        if (this.escKey) {
//        }
//        if (this.shiftKey) {
//        }
//    }



    //-------------------------------------------------------
    // Menu methods
    //-------------------------------------------------------
    public StartMenu sm;
//    public int menuState;
//    public LoadingPage lp;
    public LoadingPage lp;

    public void initMenu() {
        this.sm = new StartMenu(this);
//        this.sm.init();

        this.lp = new LoadingPage(this);
    }

    public void updateMenu(double dt) {
        if (gameState == 0) {
            if (Objects.equals(this.gameStateString, "single_player")) {
                this.gameState = 1; // Go to LoadingPage
                this.env.SetIsTimeAttack(false); // Set the environment to single player mode

            } else if (Objects.equals(this.gameStateString, "time_attack")) {
                this.gameState = 1; // Go to LoadingPage
                this.env.SetIsTimeAttack(true); // Set the environment to time attack mode
            }
        } else if (gameState == 1) {
            this.lp.startLoading();
            this.lp.updatePage(dt);
//            if (spaceKey && (this.lp.getProgress() >= 1.0)) {
            if (spaceKey) {
                // Set to the game play area instance
                this.gameState = 4;
                this.env.SetBaseTime(getTime());
                this.env.SetIsPaused(false);
            }
        }
    }

    public void drawMenu() {
        if (this.gameState == 0) {
            this.sm.drawAll();
        } else if (gameState == 1) {
            this.lp.drawAll();
        }
    }


    //-------------------------------------------------------
    // CheckoutPage methods
    //-------------------------------------------------------
    CheckoutPage chkpg;
    Image checkoutImage;
    public void initCheckout() {
        try {
            this.checkoutImage = loadImage("Assignment2/assets/image/B2.png");

        } catch(Exception e) {
            e.printStackTrace();

        }

        this.chkpg = new CheckoutPage(this, this.checkoutImage, this.width, this.height);
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
//    double score;
    boolean gamestart; // TODO: Replace to merge with the rest
    Image myfish_r,myfish_l, pearlimage,boomimage,starfishimage,backgoundimage;
    public void initPlayer() {
        this.myfish = new myfish();
        this.myfish.setFishLength(60);
        this.myfish.setFishHeight(40);
        this.gamestart = false;
        this.myFishFacing = false;

        //load image
        this.backgoundimage = loadImage("Assignment2/assets/image/background.png");

        // TODO: Compress the _r _l into one image and use a reflect method
        this.myfish_r = loadImage("Assignment2/assets/image/myfish_r.png");
        this.myfish_l = loadImage("Assignment2/assets/image/myfish_l.png");
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
            double playAreaX = this.env.GetPlayAreaX();
            double playAreaY = this.env.GetPlayAreaY();
            double playAreaWidth = this.env.GetPlayAreaWidth();
            double playAreaHeight = this.env.GetPlayAreaHeight();

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
//        return new Rectangle((int)en.GetXPos(),
//                (int)en.GetYPos(),
//                (int)en.GetColliderBodyLength(),
//                (int)en.GetHeight());
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

        this.pearlimage = loadImage("Assignment2/assets/image/Pearl.png");
        this.boomimage = loadImage("Assignment2/assets/image/boom.png");
        this.starfishimage = loadImage("Assignment2/assets/image/starfish.png");
    }
    public void updateItems(double dt) {
        // TODO: Update the rand input values to use the playArea bounds
        // Set the pearl x,y pos
        double areaW = this.env.GetPlayAreaWidth();
        double areaH = this.env.GetPlayAreaHeight();
        double randStarX = rand(areaW);
        double randStarY = rand(areaH);
        double randBombX = rand(areaW);
        double randBombY = rand(areaH);
        double spawnOffsetWidth = this.env.GetHUDWidth();
        double spawnOffsetHeight = this.env.GetHUDHeight();

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
            this.enemyFish1 = loadImage("Assignment2/assets/image/fish1r.png");
            this.enemyFish2 = loadImage("Assignment2/assets/image/fish2r.png");
            this.enemyFish3 = loadImage("Assignment2/assets/image/fish3r.png");
            this.shark = loadImage("Assignment2/assets/image/sharkr.png");
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
        if (en.GetHeadingY() == -1.0) { ex = en.GetXPos() - en.GetColliderHeadXOffset(); }
        else { ex = en.GetXPos() + en.GetColliderHeadXOffset(); }
        double ey = en.GetYPos();
        double er = en.GetColliderHeadRadius();;

        // Player collider fields
        double px = this.myfish.mposition_x;
        double py = this.myfish.mposition_y;
        double pl = this.myfish.myfish_w;
        double ph = this.myfish.myfish_h;

        // Player loses if they are "bit"
        if (calcDistCircleToSquare(ex, ey, er, px, py, pl, ph)) {
            int enSize = en.GetSize();
            if (enSize > this.myfish.getSize()) { this.myfish.setIsAlive(false); }
        }
    }

    public void updateEnemyPosition(double dt, Enemy en, boolean shouldRandomize) {
        if (shouldRandomize) { this.randomizeEnemyMovement(en); }

        double currentXPos = en.GetXPos();
        double currentYPos = en.GetYPos();
        double currentXHeading = en.GetHeadingX();
        double currentYHeading = en.GetHeadingY();
        double currentVel = en.GetVelocity();

        double newXPos = currentXPos + currentXHeading * currentVel * dt;
        double newYPos = currentYPos + currentYHeading * currentVel * dt;

        en.SetPos(newXPos, newYPos);
    }

    public void randomizeEnemyMovement(Enemy en) {
        // Roll each chance separately
        int changeChance = 1; // 1% chance
        // Randomize horizontal direction
        if (this.randSys.nextInt(100) < changeChance) { en.SetRandomXHeading(); }
        // Randomize vertical direction
        if (this.randSys.nextInt(100) < changeChance) { en.SetRandomYHeading(); }
        // Randomize velocity
        if (this.randSys.nextInt(100) < changeChance) { en.SetRandomVelocity(25,100); }

        en.UpdateColliderHeadXOffset();
    }

    public void checkEnemyBounds(Enemy en, ArrayList<Enemy> removalValues) {
        int chanceRemoveEnemy = this.randSys.nextInt(100);
        boolean removeEnemy = (chanceRemoveEnemy < en.GetChanceToLeaveEnvironment());

        int playAreaWidth = this.env.GetPlayAreaWidth();
        int playAreaHeight = this.env.GetPlayAreaHeight();
        double playAreaXOffset = this.env.GetEnvironmentXOffset();
        double playAreaYOffset = this.env.GetEnvironmentYOffset();

        double bodyLengthOffset =  en.GetColliderBodyLength() / 2;
        double bodyHeightOffset =  en.GetColliderBodyHeight() / 2;

        // Check horizontal bounds
        if ((en.GetXPos() <= (playAreaXOffset - bodyLengthOffset)) && (en.GetHeadingX() == -1.0)) {
            if (removeEnemy) { removalValues.add(en); }
            else {
                en.SetXHeading(1.0);
                en.UpdateColliderHeadXOffset();
            }
        }
        if ((en.GetXPos() >= (playAreaXOffset + playAreaWidth + bodyLengthOffset)) && (en.GetHeadingX() == 1.0)) {
            if (removeEnemy) { removalValues.add(en); }
            else {
                en.SetXHeading(-1.0);
                en.UpdateColliderHeadXOffset();
            }
        }

        // Check vertical bounds
        if (en.GetYPos() <= (playAreaYOffset + bodyHeightOffset)) { en.SetYHeading(1.0); }
        if (en.GetYPos() >= (playAreaYOffset + playAreaHeight - bodyHeightOffset)) { en.SetYHeading(-1.0); }
    }

    public void removeEnemies(ArrayList<Enemy> enemies, ArrayList<Enemy> removalValues) {
        // Remove the enemies that have been selected for removal.
        if (removalValues.size() > 0) {
//            System.out.println("Removing shark");
            for (Enemy toRemove : removalValues) {
                enemies.remove(toRemove);
            }
        }
    }

    // The method to spawn an enemy on click
    // TODO: the enemy should spawn based on other methods, such as a timer.
    public void SpawnEnemy() {
        this.enemySpawnTimer = this.env.GetCountDownTimerWOffset() - this.enemySpawnTimerPrevious;
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
        double imgY = en.GetImageYOffset();
        double imgLen = en.GetImageLength();
        double imgHei = en.GetImageHeight();
        Image enImg = en.GetImage();

        // Draw the enemy image
        if (en.GetHeadingX() == 1.0) {
            imgX = en.GetImageXOffset();
            drawImage(enImg, imgX, imgY, imgLen, imgHei);
        } else {
            imgX = en.GetImageXOffsetNegative();
            drawImage(enImg, imgX, imgY, -imgLen, imgHei);
        }

    }

    public void drawEnemyCollider(Enemy en) {
        // Retrieve the enemy collision fields
        double xPosCol = en.GetColliderBodyXOffset();
        double yPosCol = en.GetColliderBodyYOffset();
        double len = en.GetLength();
        double hei = en.GetHeight();
        double xPos = en.GetXPos();
        double yPos = en.GetYPos();

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
        double xH = en.GetColliderHeadXOffset();
        double yH = en.GetColliderHeadYOffset();
        double rH = en.GetColliderHeadRadius();
        double x = en.GetXPos();
        double y = en.GetYPos();

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

        int playAreaWidth = this.env.GetPlayAreaWidth();
        int playAreaHeight = this.env.GetPlayAreaHeight();
        double playAreaXOffset = this.env.GetEnvironmentXOffset();
        double playAreaYOffset = this.env.GetEnvironmentYOffset();

        Enemy en;
        if (selectSize < enemySmall) { // spawn a small fish
            en = new Enemy(this.enemyFish1, 0, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            // TODO: Match these offset values to the actual image sizes
//            en.SetHeadOffset();
            en.SetImageOffsets(-32,-32,64,64);
        } else if (selectSize < enemyMedium) { // spawn a medium fish
            en = new Enemy(this.enemyFish2, 1, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            // TODO: Match these offset values to the actual image sizes
            en.SetImageOffsets(-32,-32,64,64);
        } else { // spawn a large fish
            en = new Enemy(this.enemyFish3, 2, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            // TODO: Match these offset values to the actual image sizes
            en.SetImageOffsets(-32,-32,64,64);
        }

        this.enemies.add(en);
    }

    public void createShark() {
        if (this.sharkEnemies.size() >= this.maxShark) {
            System.out.println("Max sharks.");
            return;
        }

//        System.out.println("Create shark.");
        int playAreaWidth = env.GetPlayAreaWidth();
        int playAreaHeight = env.GetPlayAreaHeight();
        double playAreaXOffset = env.GetEnvironmentXOffset();
        double playAreaYOffset = env.GetEnvironmentYOffset();

        Enemy shk = new Enemy(this.shark, 3, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
        shk.SetRandomVelocity(400, 400);
        shk.SetYHeading(0.0);
        shk.SetChanceToLeaveEnvironment(100);
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
            this.env.EnvironmentLevelCompleteCheck();

            boolean wasPaused = false;

            if (this.escKey) {
                if (this.env.GetIsPaused()) {
                    this.env.SetIsPaused(false);
                    wasPaused = true;
                } else {
                    this.env.SetIsPaused(true);
                }
            }

            // Level is complete keep the game paused
            if (this.env.GetIsLevelComplete()) {
                this.env.SetIsPaused(true);
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
        if (this.env.GetIsPaused()) { return; }


        // fields for paused timer
        double newTime = getTime();

        // Process the timer for paused case
        if (wasPaused) {
            double beforePause;
            double timeDelay;

            beforePause = this.env.GetTimer();
            double afterPause = Math.round(((newTime - this.env.GetBaseTime()) / 1000.0) * 100.0) / 100.0;
            timeDelay = (afterPause - beforePause);

            // Set the pausable timer
            this.env.addCountDownTimerOffset(timeDelay);
        }

        // Set the global timer
        this.env.SetTimer(newTime);

        // Time attack counter processing
        if (this.env.GetIsTimeAttack()) { this.env.SetCountDownCurrentTimer(this.env.GetCountDownTimerWOffset()); }
    }

    public void drawEnvironment() {
        if (gameState == 4) {
            this.env.drawEnvironment();
        }
    }

    public void drawEnvironmentLayerTop() {
        if (gameState == 4) {
            this.env.drawHUD();
            boolean timeAttack = this.env.GetIsTimeAttack();
            this.env.drawTimer(timeAttack);
            this.env.drawScore();
            this.env.drawGrowth();
        }
    }
}
