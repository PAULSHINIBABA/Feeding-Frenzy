/*
 * Author: Robert Tubman
 * ID: 11115713
 *
 * Co-Author: Lucass
 * ID:
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
        if (env.getIsLevelComplete()) { return; }
        updateEnvironment();

        // The environment processing for the pause is still occurring above
        if (env.getIsPaused()) { return; }

        updatePlayer(dt);
        updateItems(dt);
        updateEnemies(dt);
    }
    public void paintComponent() {
        drawMenu();
        drawCheckout();

        drawEnvironment();

        drawEnemies(enemies); // Draw enemy first
        drawEnemies(sharkEnemies); // Draw shark enemy next
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
    final String assetPathImage = "Assignment2/assets/image/";
    final String assetPathAudio = "Assignment2/assets/audio/";

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

    public void InitSystem() {
        // System random field
        randSys = new Random();

        // System window size
        width = 1200;
        height = 900;
//        width = 500;
//        height = 500;

        // Set the window
        setWindowSize(width, height);

        // Game state
        gameState = 0;
        gameStateString = "nothing";

        // Single player mode default
        isSinglePlayer = true;
    }

    public void colliderCheck() {
        // Get the player head x collider
        double px;
        if (myfish.getFacingLeft()) { px = myfish.getXPos(); }
        else { px = myfish.getXPos() + myfish.getWidth(); }

        // Check if the enemy is at the head
        ArrayList<Enemy> removalValues = new ArrayList<Enemy>();
        for (Enemy en : enemies) {
            // Check players head to enemies body collision
            if (calcDistCircleToSquare(px,
                    (myfish.getYPos() + myfish.getFishHeadColliderYOffset()),
                    myfish.getFishHeadColliderRadius(),
                    en.getXPos(),
                    en.getYPos(),
                    en.getColliderBodyLength(),
                    en.getColliderBodyHeight())) {

                // There is a collision
                int enSize = en.getSize();
                // If the player can eat the enemy (based on size equality)
                if (myfish.getSize() >= enSize) {
                    // Player was equal to or greater than the enemy
                    // Get the score based on enemy size
                    int eatScore = enSize + 1;
                    env.setEatEnemyBySize(enSize);

                    // Set the player score in the environment
                    env.setScore((env.getScore() + eatScore));

                    // Increase the players current progress to the level goal
                    env.setCurrentGoal(env.getCurrentGoal() + 1);

                    // Add enemy to removal list
                    removalValues.add(en);
                }
            }
        }

        // Remove enemies in the removal list
        removeEnemies(enemies, removalValues);
    }

    public void growPlayerSize() {
        // Get the players current goal state
        int currentGoal = env.getCurrentGoal();

        // Check if the player has reached the medium size threshold
        if (myfish.getSize() == 0 && (currentGoal >= env.getGrowthThresholdMedium())) {
            myfish.increaseSize();
        }
        // Check if the player has reached the large size threshold
        if (myfish.getSize() == 1 && (currentGoal >= env.getGrowthThresholdLarge())) {
            myfish.increaseSize();
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




    //-------------------------------------------------------
    // Mouse Handler
    //-------------------------------------------------------
    public double mouseX;
    public double mouseY;
    boolean mouse1Pressed = false;

    // MousePressed Event Handler
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            if (gameState == 0) {
                gameStateString = sm.menuMouseClicked(e);

            } else if (gameState == 6) {
                gameStateString = chkpg.checkClickTarget(mouseX, mouseY);

            }
        }
    }

    // MouseReleased Event Handler
    public void mouseReleased(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) { mouse1Pressed = false; }
    }

    // MouseMoved Event Handler
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
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
        if(e.getKeyCode() == KeyEvent.VK_LEFT) { leftKey = true; }
        // The user pressed right arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { rightKey = true; }
        // The user pressed up arrow
        if(e.getKeyCode() == KeyEvent.VK_UP) { upKey = true; }
        // The user pressed up arrow
        if(e.getKeyCode() == KeyEvent.VK_DOWN) { downKey = true; }
        // The user pressed ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { escKey = true; }
        // The user pressed spaceKey
        if(e.getKeyCode() == KeyEvent.VK_SPACE) { spaceKey = true; }
    }

    // Called whenever a key is released
    public void keyReleased(KeyEvent e) {
//        // The user released left arrow
        if(e.getKeyCode() == KeyEvent.VK_LEFT) { leftKey = false; }
        // The user released right arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { rightKey = false; }
        // The user released up arrow
        if(e.getKeyCode() == KeyEvent.VK_UP) { upKey = false; }
        // The user released up arrow
        if(e.getKeyCode() == KeyEvent.VK_DOWN) { downKey = false; }
        // The user released ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { escKey = false; }
        // The user released spaceKey
        if(e.getKeyCode() == KeyEvent.VK_SPACE) { spaceKey = false; }
    }


    //-------------------------------------------------------
    // Menu methods
    //-------------------------------------------------------
    public StartMenu sm;
    public LoadingPage lp;
    Image startMenuBackground;
    Image startTitle;
    int titleWidth;
    int titleHeight;
    final int START_BUTTONS_LENGTH = 5;
    Image[] buttonIcons = new Image[START_BUTTONS_LENGTH];
    Image musicButton;
    Image loadingImage;
    final int LOADING_TIPS_LENGTH = 3;
    Image[] loadingTips = new Image[LOADING_TIPS_LENGTH];

    public void initMenu() {
        titleWidth = 300;
        titleHeight = 150;

        startMenuBackground = loadImage(assetPathImage + "background/background1.png");
        startTitle = loadImage(assetPathImage + "icon/icon_title1.png");
        buttonIcons[0] = loadImage(assetPathImage + "icon/icon_start2.png");
        buttonIcons[1] = loadImage(assetPathImage + "icon/icon_time_attack2.png");
        buttonIcons[2] = loadImage(assetPathImage + "icon/icon_settings1.png");
        buttonIcons[3] = loadImage(assetPathImage + "icon/icon_quit1.png");
        buttonIcons[4] = loadImage(assetPathImage + "icon/icon_help2.png");
        musicButton = loadImage(assetPathImage + "icon/icon_music1.png");

        sm = new StartMenu(this,
                startMenuBackground,
                startTitle,
                titleWidth,
                titleHeight,
                buttonIcons,
                START_BUTTONS_LENGTH,
                musicButton,
                width,
                height);

        sm.initMusic(assetPathAudio + "music/track_light_positive_modern.wav");
        sm.playMusic();

        loadingImage = loadImage(assetPathImage + "background/background2.png");
        loadingTips[0] = loadImage(assetPathImage + "tip/icon_tip1.png");
        loadingTips[1] = loadImage(assetPathImage + "tip/icon_tip2.png");
        loadingTips[2] = loadImage(assetPathImage + "tip/icon_tip3.png");

        lp = new LoadingPage(this,
                loadingImage,
                startTitle,
                titleWidth,
                titleHeight,
                loadingTips,
                LOADING_TIPS_LENGTH,
                width,
                height);
    }

    public void updateMenu(double dt) {
        if (gameState == 0) {
            if (Objects.equals(gameStateString, "single_player")) {
                gameState = 1; // Go to LoadingPage
                env.setIsTimeAttack(false); // Set the environment to single player mode

            } else if (Objects.equals(gameStateString, "time_attack")) {
                gameState = 1; // Go to LoadingPage
                env.setIsTimeAttack(true); // Set the environment to time attack mode
            }
        } else if (gameState == 1) {
            lp.startLoading();
            lp.updatePage(dt);
            // TODO: add this back in so the player can't skip the loading page
//            if (spaceKey && (lp.getProgress() >= 1.0)) {
            if (spaceKey) {
                // Set to the game play area instance
                gameState = 4;
                env.setBaseTime(getTime());
                env.setIsPaused(false);
            }
        }
    }

    public void drawMenu() {
        if (gameState == 0) { sm.drawAll(); }
        else if (gameState == 1) { lp.drawAll(); }
    }


    //-------------------------------------------------------
    // CheckoutPage methods
    //-------------------------------------------------------
    CheckoutPage chkpg;
    Image checkoutImage;
    Image backButtonImage;
    Image continueiButtonImage;
    boolean setCheckoutPage;
    public void initCheckout() {
        try {
            checkoutImage = loadImage(assetPathImage + "background/background4.png");
            backButtonImage = loadImage(assetPathImage + "icon/icon_return2.png");
            // Image should be reversed when displayed
            continueiButtonImage = loadImage(assetPathImage + "icon/icon_return2.png");

        } catch(Exception e) {
            e.printStackTrace();
        }

        chkpg = new CheckoutPage(this,
                checkoutImage,
                backButtonImage,
                continueiButtonImage,
                width,
                height);

        setCheckoutPage = false;
    }
    public void updateCheckout() {
        if (gameState == 6) {
            if (!setCheckoutPage) {
                // Checkout page should load.
                chkpg.setScore(env.getScore());
                chkpg.setPearlsEaten(pearl.getTimesEaten());
                chkpg.setStarfishEaten(starfish.getTimesEaten());
                chkpg.setEnemiesEaten(env.getEnemiesEaten());
                if (env.getRestartLevel()) { chkpg.setRestartButton(); }

                setCheckoutPage = true;
            }

            if (gameStateString == "main_menu") {
                System.out.println("Main menu");
                finalReset();
                env.setRestartLevel(false);
                gameState = 0;

            } else if (gameStateString == "next_level") {
                System.out.println("Next level");
                finalReset();
                if (!env.getRestartLevel()) { nextLevel(); }
                env.setRestartLevel(false);
                gameState = 4;

            } else if (gameStateString == "restart") {
                System.out.println("Restart");
                finalReset();
                env.setRestartLevel(false);
                gameState = 4;

            }
            gameStateString = "nothing";
            resetGameLevel();
        }
    }
    public void drawCheckout() {
        if (gameState == 6) {
            Image[] enImages = {enemyFish[0], enemyFish[1], enemyFish[2]};
            chkpg.drawCheckout(enImages);
            chkpg.setEnemyBaseSize(enemyWidth,enemyHeight / 2);
        }
    }

    //-------------------------------------------------------
    // Player methods
    //-------------------------------------------------------
    myfish myfish;
    Image myFishImage;
    Image pearlimage;
    Image boomimage;
    Image starfishimage;
    public void initPlayer() {
        myfish = new myfish( (width / 2.0), (height / 2.0) );
        myfish.setFishLength( 60 );
        myfish.setFishHeight( 40 );

        myFishImage = loadImage(assetPathImage + "entity/entity_player1.png");
    }
    public void updatePlayer(double dt) {
        if (gameState == 4) {
//            if (spaceKey) { myfish.setIsAlive(true); } // TODO: CHEAT for debug, remove when done

            // Player is dead don't process the player
            if (!myfish.getIsAlive()) { return; }

            if (leftKey) { myfish.setFacingLeft(true); }
            else if (rightKey) { myfish.setFacingLeft(false); }

            if (myfish.getIsAlive()) {
                colliderCheck();
                growPlayerSize();
            }

            // playArea fields
            double playAreaX = env.getPlayAreaX();
            double playAreaY = env.getPlayAreaY();
            double playAreaWidth = env.getPlayAreaWidth();
            double playAreaHeight = env.getPlayAreaHeight();

            // Fish moves within the playArea bounds
            myfish.updatemyfish(dt,
                    upKey,
                    downKey,
                    leftKey,
                    rightKey,
                    playAreaX,
                    playAreaY,
                    playAreaWidth,
                    playAreaHeight);
        }
    }
    public void drawPlayer() {
        if (gameState == 4) {
            // Draw player if they are alive
            if (myfish.getIsAlive()) { drawMyself(); }
        }
    }

    public void drawMyself() {
        // Decide which image to draw based on direction
        Image currentFishImage = myFishImage;
        if (myfish.getFacingLeft()) {
            drawImage(currentFishImage, myfish.getXPos() + myfish.getWidth(), myfish.getYPos(), -myfish.getWidth(), myfish.getHeight());
        } else {
            drawImage(currentFishImage, myfish.getXPos(),  myfish.getYPos(), myfish.getWidth(), myfish.getHeight());
        }

        drawPlayerCollider(); // TODO: Remove before submitting
    }

    public void drawPlayerCollider() {
        // Draw the collider box
        changeColor(0,255,0);
        // top
        drawLine(myfish.getXPos(),
                myfish.getYPos(),
                (myfish.getXPos() + myfish.getWidth()),
                myfish.getYPos());
        // bottom
        drawLine(myfish.getXPos(),
                (myfish.getYPos() + myfish.getHeight()),
                (myfish.getXPos() + myfish.getWidth()),
                (myfish.getYPos() + myfish.getHeight()));
        // left
        drawLine(myfish.getXPos(),
                myfish.getYPos(),
                myfish.getXPos(),
                (myfish.getYPos() + myfish.getHeight()));
        // right
        drawLine((myfish.getXPos() + myfish.getWidth()),
                myfish.getYPos(),
                (myfish.getXPos() + myfish.getWidth()),
                (myfish.getYPos() + myfish.getHeight()));

        changeColor(255,0,0);
        double fishX;
        double fishY = myfish.getYPos() + myfish.getFishHeadColliderYOffset();
        double fishR = myfish.getFishHeadColliderRadius();
        if (myfish.getFacingLeft()) { fishX = myfish.getXPos(); }
        else { fishX = myfish.getXPos() + myfish.getWidth(); }
        drawCircle(fishX, fishY, fishR);
    }



    //-------------------------------------------------------
    // Item methods
    //-------------------------------------------------------
    pearl pearl;
    boom boom;
    starfish starfish;
    public void initItems() {
        pearl = new pearl();
        boom = new boom();
        starfish = new starfish();

        pearlimage = loadImage(assetPathImage + "item/item_pearl1.png");
        boomimage = loadImage(assetPathImage + "item/item_bomb1.png");
        starfishimage = loadImage(assetPathImage + "item/item_starfish1.png");
    }
    public void updateItems(double dt) {
        // Set the pearl x,y pos
        double areaW = env.getPlayAreaWidth();
        double areaH = env.getPlayAreaHeight();
        double spawnOffsetWidth = env.getHUDWidth();
        double spawnOffsetHeight = env.getHUDHeight();

        double randSpeed = rand(100);
        if(!env.getIsLevelComplete()) {
            //update pearl
            pearl.updatepearl(dt,
                    myfish.getmyfishRec(),
                    myfish.getXVel(),
                    myfish.getYVel(),
                    rand(areaW),
                    rand(areaH),
                    spawnOffsetWidth,
                    spawnOffsetHeight);

            //update starfish
            if (starfish.updatestarfish(dt,
                    myfish.getmyfishRec(),
                    rand(areaW),
                    rand(areaH),
                    spawnOffsetWidth,
                    spawnOffsetHeight,
                    randSpeed,
                    areaW,
                    areaH)) {

                // Update the environment score
                env.addScore(20);
            }

            if (boom.updateboom(dt,
                    myfish.getmyfishRec(),
                    rand(areaW),
                    rand(areaH),
                    spawnOffsetWidth,
                    spawnOffsetHeight,
                    randSpeed,
                    areaW,
                    areaH)) {

                myfish.setIsAlive(false);
                env.setEndLevel();
            }
        }

    }
    public void drawItems() {
        if (gameState == 4) {
            drawpearl();
            drawstarfish();
            drawboom();
        }
    }

    public void drawboom(){
        if (boom.isvisible()){
            drawImage(boomimage,
                    boom.getpositionx(),
                    boom.getpositiony(),
                    boom.getwidth(),
                    boom.getheight());
        }
    }
    public void drawstarfish(){
        if (starfish.isvisible()){
            drawImage(starfishimage,
                    starfish.getpositionx(),
                    starfish.getpositiony(),
                    starfish.getwidth(),
                    starfish.getheight());
        }
    }
    public void drawpearl(){
        if (pearl.isvisible()){
            changeColor(Color.white);
            drawImage(pearlimage,
                    pearl.getpositionx(),
                    pearl.getpositiony(),
                    pearl.getwidth(),
                    pearl.getheight());
        }
    }

    //-------------------------------------------------------
    // Enemy methods
    //-------------------------------------------------------
    int maxEnemies; // Set to around 30
    ArrayList<Enemy> enemies;
    int maxShark;
    ArrayList<Enemy> sharkEnemies;
    double enemySpawnTimer;
    double enemySpawnTimerPrevious;
    double enemySpawnDelay;
    Image[] enemyFish;
    Image shark;
    int enemyTypes;
    int[] enemyEaten;
    int enemyWidth;
    int enemyHeight;
    public void initEnemies() {
        // Define the maximum enemy spawns based on screen dimensions
        maxEnemies = width / 100;
        maxShark = width / 500;

        // Instantiate the enemy arraylists
        enemies = new ArrayList<Enemy>();
        sharkEnemies = new ArrayList<Enemy>();

        // Set the default enemy spawn timer fields
        enemySpawnTimer = 0.0;
        enemySpawnTimerPrevious = 0.0;
        enemySpawnDelay = randSys.nextDouble(2.0) + 1.0;

        // Set the enemy types
        enemyTypes = 3;

        // Instantiate the array for enemies eaten
        enemyEaten = new int[enemyTypes];

        // Default base enemy dimensions
        enemyWidth = 32;
        enemyHeight = 32;

        try {
            enemyFish = new Image[enemyTypes];
            enemyFish[0] = loadImage(assetPathImage + "entity/entity_fish1.png");
            enemyFish[1] = loadImage(assetPathImage + "entity/entity_fish2.png");
            enemyFish[2] = loadImage(assetPathImage + "entity/entity_fish3.png");
            shark = loadImage(assetPathImage + "entity/entity_shark1.png");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updateEnemies(double dt) {
        // Spawn enemy fish
        SpawnEnemy();

        // Process the normal enemies
        ArrayList<Enemy> removalValues = new ArrayList<Enemy>();
        if (enemies.size() > 0) {
            for (Enemy en : enemies) {
                // Update the enemy positions
                updateEnemyPosition(dt, en, true);

                // Check if the player has been bitten
                checkEnemyBitePlayer(en);

                // Check if enemy moves out of bounds
                checkEnemyBounds(en, removalValues);
            }
        }
        // Remove the enemies marked for removal
        removeEnemies(enemies, removalValues);

        // Process the shark enemy
        removalValues = new ArrayList<Enemy>();
        if (sharkEnemies.size() > 0) {
            for (Enemy sen : sharkEnemies) {
                // Update the shark enemy positions
                updateEnemyPosition(dt, sen, false);

                // Check if the player has been bitten
                checkEnemyBitePlayer(sen);

                // Check if shark enemy moves out of bounds
                checkEnemyBounds(sen, removalValues);
            }
        }
        // Remove the shark enemies marked for removal
        removeEnemies(sharkEnemies, removalValues);
    }

    public void checkEnemyBitePlayer(Enemy en) {
        // Enemy collider fields
        double ex;
        if (en.getHeadingY() == -1.0) { ex = en.getXPos() - en.getColliderHeadXOffset(); }
        else { ex = en.getXPos() + en.getColliderHeadXOffset(); }

        // Player loses if they are "bitten"
        if (calcDistCircleToSquare(ex,
                en.getYPos(),
                en.getColliderHeadRadius(),
                myfish.getXPos(),
                myfish.getYPos(),
                myfish.getWidth(),
                myfish.getHeight())) {

            if (en.getSize() > myfish.getSize()) {
                myfish.setIsAlive(false);
                env.setEndLevel();
            }
        }
    }

    public void updateEnemyPosition(double dt, Enemy en, boolean shouldRandomize) {
        if (shouldRandomize) { randomizeEnemyMovement(en); }

        double currentVel = en.getVelocity();
        double newXPos = en.getXPos() + en.getHeadingX() * currentVel * dt;
        double newYPos = en.getYPos() + en.getHeadingY() * currentVel * dt;

        en.setPos(newXPos, newYPos);
    }

    public void randomizeEnemyMovement(Enemy en) {
        // Roll each chance separately
        int changeChance = 1; // 1% chance
        // Randomize horizontal direction
        if (randSys.nextInt(100) < changeChance) { en.setRandomXHeading(); }
        // Randomize vertical direction
        if (randSys.nextInt(100) < changeChance) { en.setRandomYHeading(); }
        // Randomize velocity
        if (randSys.nextInt(100) < changeChance) { en.setRandomVelocity(en.getDefaultVelocity(),en.getDefaultVelocityRange()); }

        en.updateColliderHeadXOffset();
    }

    public void checkEnemyBounds(Enemy en, ArrayList<Enemy> removalValues) {
        int chanceRemoveEnemy = randSys.nextInt(100);
        boolean removeEnemy = (chanceRemoveEnemy < en.getChanceToLeaveEnvironment());

        double playAreaXOffset = env.getEnvironmentXOffset();
        double playAreaYOffset = env.getEnvironmentYOffset();

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
        if ((en.getXPos() >= (playAreaXOffset + env.getPlayAreaWidth() + bodyLengthOffset)) && (en.getHeadingX() == 1.0)) {
            if (removeEnemy) { removalValues.add(en); }
            else {
                en.setXHeading(-1.0);
                en.updateColliderHeadXOffset();
            }
        }

        // Check vertical bounds
        if (en.getYPos() <= (playAreaYOffset + bodyHeightOffset)) { en.setYHeading(1.0); }
        if (en.getYPos() >= (playAreaYOffset + env.getPlayAreaHeight() - bodyHeightOffset)) { en.setYHeading(-1.0); }
    }

    public void removeEnemies(ArrayList<Enemy> enemies, ArrayList<Enemy> removalValues) {
        // Remove the enemies that have been selected for removal.
        if (removalValues.size() > 0) {
            for (Enemy toRemove : removalValues) {
                enemies.remove(toRemove);
            }
        }
    }

    public void removeAllEnemies(ArrayList<Enemy> enemies) { enemies.clear(); }

    // The method to spawn an enemy on click
    public void SpawnEnemy() {
        enemySpawnTimer = env.getCountDownTimerWOffset() - enemySpawnTimerPrevious;
        if (enemySpawnTimer > enemySpawnDelay) {
            createEnemy();
            enemySpawnDelay = randSys.nextDouble(2.0) + 1.0;
            enemySpawnTimerPrevious = enemySpawnTimer;
        }

        if (env.getHardMode()) {
            if (env.getCountDownCurrentTimerPercentage() >= 0.75 || myfish.getSize() >= 2) {
                double roll = randSys.nextInt(100);
                double chance = 33;
                if (roll < chance) {
                    createShark();
                }
            }
        }
    }

    public void drawEnemies(ArrayList<Enemy> enemies) {
        if (gameState == 4) {
            for (Enemy en : enemies) {
                drawEnemy(en);
                drawEnemyCollider(en);
                drawEnemyHeadCollider(en);
            }
        }
    }

    public void drawEnemy(Enemy en) {
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

    // Debug: Used to show the enemy body colliders
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

    // Debug: Used to show the enemy head colliders
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
        if (enemies.size() >= maxEnemies) { return; }

        // Randomize enemy selection
        // Should the spawn chances be modified such as:
        // - 45% small
        // - 33% medium
        // - 22% large
        final int enemySmall = 45;
        final int enemyMedium = 78; // 45 + 33
        int selectSize = randSys.nextInt(100);

        int playAreaWidth = env.getPlayAreaWidth();
        int playAreaHeight = env.getPlayAreaHeight();
        double playAreaXOffset = env.getEnvironmentXOffset();
        double playAreaYOffset = env.getEnvironmentYOffset();

        Enemy en;
        int size;
        if (selectSize < enemySmall) { // spawn a small fish
            size = 0;
            en = new Enemy(enemyFish[0], size, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            en.setImageOffsets(-enemyWidth,-enemyHeight,enemyWidth * 2,enemyHeight * 2);
        } else if (selectSize < enemyMedium) { // spawn a medium fish
            size = 1;
            en = new Enemy(enemyFish[1], size, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            en.setImageOffsets(-enemyWidth,-enemyHeight,enemyWidth * 2,enemyHeight * 2);
        } else { // spawn a large fish
            size = 2;
            en = new Enemy(enemyFish[2], size, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            en.setImageOffsets(-enemyWidth,-enemyHeight,enemyWidth * 2,enemyHeight * 2);
        }
        enemies.add(en);
    }

    public void createShark() {
        if (sharkEnemies.size() >= maxShark) { return; }

        // The shark should be the largest size
        Enemy shk = new Enemy(shark, 3, env.getEnvironmentXOffset(), env.getEnvironmentYOffset(), env.getPlayAreaWidth(), env.getPlayAreaHeight());

        // The shark should be very fast, ie (400 + range(400))
        shk.setRandomVelocity(400, 400);
//        shk.setRandomVelocity(50, 50);
        shk.setYHeading(0.0); // Don't move vertically (unless spawning close to the top or bottom)
        shk.setChanceToLeaveEnvironment(50); // The shark will always leave the environment
        sharkEnemies.add(shk);
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
    double defaultTime;
    boolean singlePlayerComplete;
    boolean timeAttackComplete;
    int growthThresholdM;
    int growthThresholdL;
    int level = 0;

    public void initEnvironment() {
        goalMaximum = 50;
        timeMinimum = 10.0;
        defaultTime = 60.0;
        targetGoalIncrement = 5;
        targetTimeAdjuster = 5.0;
        singlePlayerComplete = false;
        timeAttackComplete = false;
        
        targetGoal = 10 + (targetGoalIncrement * level);
        if (targetGoal > goalMaximum) { targetGoal = goalMaximum; }
        targetTime = 10.0 - (targetTimeAdjuster * level); // TODO: Make this start at 60.0
        if (targetTime < timeMinimum) { targetTime = timeMinimum; }

        growthThresholdM = (int)(targetGoal / 3.0);
        growthThresholdL = (int)((targetGoal / 3.0) * 2.0);

        env = new Environment(this, !isSinglePlayer);
        env.setEnvironmentPlayWidth(width);
        env.setEnvironmentPlayHeight(height);
        env.setTargetGoal(targetGoal);
        env.setCountDownTimer(targetTime);
        env.setGrowthThresholdLarge(growthThresholdL);
        env.setGrowthThresholdMedium(growthThresholdM);
        env.setHardMode(false);
    }
    public void updateEnvironment() {
        if (gameState == 4) {
            // Check if the level is complete
            env.environmentLevelCompleteCheck();

            // Check for the pausing of the game
            boolean wasPaused = false;
            if (escKey) {
                if (env.getIsPaused()) {
                    env.setIsPaused(false);
                    wasPaused = true;
                } else {
                    env.setIsPaused(true);
                }
            }

            // Level is complete keep the game paused
            if (env.getIsLevelComplete() || env.getEndLevel()) {
                env.setIsPaused(true);
                gameState = 6; // Go to CheckoutPage
            }

            UpdateTimer(wasPaused);
        }
    }

    public void resetGameLevel() {

        // Remove all enemies from the level
        removeAllEnemies(enemies);

        // Reset the spawn timer fields
        enemySpawnTimer = 0.0;
        enemySpawnTimerPrevious = 0.0;
        enemySpawnDelay = randSys.nextDouble(2.0) + 1.0;

        // Remove all items from the level
        // Reset the pearl
        pearl.setvisible(false);
        pearl.resettimevis();

        // Reset the bomb
        boom.setvisible(false);
        boom.resettimevis();

        // Reset the starfish
        starfish.setvisible(false);
        starfish.resettimevis();
        starfish.setSpeedX(0.0);
        starfish.setSpeedY(0.0);

        // Reset the player
        myfish.setXPos(width / 2.0);
        myfish.setYPos(height / 2.0);
        myfish.setXVel(0);
        myfish.setYVel(0);
        myfish.setIsAlive(true);
        myfish.setSize(0);

        // Reset the in game display timer
        env.setBaseTime(getTime());
        env.setCurrentGoal(0);
        if (env.getIsTimeAttack()) {
            env.setCountDownCurrentTimer(0.0);
        }
//        env.setEnemyEatenCounter();
        env.setIsLevelComplete(false);
        env.setEndLevel(false);
        env.setIsPaused(false);
        chkpg.setRestartButton(false);

        setCheckoutPage = false;
    }

    public void finalReset() {
        env.setScore(0);
        env.setEnemyEatenCounter();
        pearl.setTimesEaten(0);
        starfish.setTimesEaten(0);
    }

    public void nextLevel() {
        // Increment level
        env.setCurrentLevel(env.getCurrentLevel() + 1);

        // Increment the target goal for the level
        targetGoal = targetGoal + (targetGoalIncrement * env.getCurrentLevel());
        if (targetGoal >= goalMaximum * 0.66) { env.setHardMode(true); }
        if (targetGoal > goalMaximum) {
            // If the goal exceeds the maximum then set to the maximum
            targetGoal = goalMaximum;

            // If the game is not time attack, then single player is complete
            if (!env.getIsTimeAttack()) { singlePlayerComplete = true; }
        }
        // Set the target goal for the level
        env.setTargetGoal(targetGoal);

        if (env.getIsTimeAttack()) {
            // Reset the time attack timer
            env.setCountDownCurrentTimer(0.0);

            // If the game is time attack decrement the target time
            targetTime -= targetTimeAdjuster;
            if (targetTime < defaultTime * 0.33) {
                env.setHardMode(true);
            }
            // If the target time goes below the minimum, then time attack is complete
            if (targetTime < timeMinimum) {
                targetTime = timeMinimum;
                timeAttackComplete = true;
            }

            // Set the target time otherwise
            env.setCountDownTimer(targetTime);
        }
    }

    public void UpdateTimer(boolean wasPaused) {
        // return if paused
        if (env.getIsPaused()) { return; }


        // fields for paused timer
        double newTime = getTime();

        // Process the timer for paused case
        if (wasPaused) {
            double beforePause;
            double timeDelay;

            beforePause = env.getTimer();
            double afterPause = Math.round(((newTime - env.getBaseTime()) / 1000.0) * 100.0) / 100.0;
            timeDelay = (afterPause - beforePause);

            // Set the pausable timer
            env.addCountDownTimerOffset(timeDelay);
        }

        // Set the global timer
        env.setTimer(newTime);

        // Time attack counter processing
        if (env.getIsTimeAttack()) { env.setCountDownCurrentTimer(env.getCountDownTimerWOffset()); }
    }

    public void drawEnvironment() {
        if (gameState == 4) {
            env.drawEnvironment();
        }
    }

    public void drawEnvironmentLayerTop() {
        if (gameState == 4) {
            env.drawHUD();
            boolean timeAttack = env.getIsTimeAttack();
            env.drawTimer(timeAttack);
            env.drawScore();
            env.drawCurrentToTargetGoal();
            env.drawGrowth();
        }
    }
}
