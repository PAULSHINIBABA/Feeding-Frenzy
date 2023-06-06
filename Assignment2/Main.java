/*
 * Author: Robert Tubman (Merging, debugging, and refactoring)
 * ID: 11115713
 *
 * The Main Class which contains the entry point.
 * As well as the code to wrap all the classes together to make the game run.
 *
 * Reference for Explosion sound: * https://mixkit.co/free-sound-effects/explosion/
 */

package Assignment2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Main extends GameEngine {

    // Main game instance
    public static void main(String[] args) {
        createGame(new Main());
    }

    // Initialize the game parameters
    public void init() {
        InitSystem();
        initMenu();
        initInGameMenu();
        initCheckout();
        initHelpPage();
        initEnvironment();
        initPlayer();
        initItems();
        initEnemies();
    }

    // Update the main processing loop
    public void update(double dt) {
        updateSystem(dt);
        updateMenu(dt);
        updateCheckout(dt);
        updateInGameMenu(dt);
        updateHelpPage();

        // While in-game, if the level is complete stop processing
        if (env.getIsLevelComplete()) { return; }
        updateEnvironment(dt);

        // Don't process game events if paused
        if (env.getIsPaused()) { return; }

        updatePlayer(dt);
        updateItems(dt);
        updateEnemies(dt);
    }

    // Paint the components
    public void paintComponent() {
        changeBackgroundColor(0,0,0);
        // The bottom most layer
        drawMenu();
        drawCheckout();
        drawHelpPage();

        drawEnvironment();

        drawEnemies(enemies); // Draw enemy first
        drawEnemies(sharkEnemies); // Draw shark enemy next
        drawItems(); // Then draw items
        drawPlayer(); // Then draw the player

        drawEnvironmentLayerTop();

        // The top most layer
        drawInGameMenu();
    }


    //-------------------------------------------------------
    // System methods
    //-------------------------------------------------------
    // "nothing"; no game state change was clicked
    // "single_player"; single player game state change was clicked
    // "time_attack"; time attack game state change was clicked
    // "settings"; settings button was clicked
    // "quit"; quit button was clicked
    // "help"; help button was clicked
    // "main_menu"; go back to main menu was clicked
    // "next_level"; go to next level was clicked
    // "restart"; restart level was clicked
    String gameStateString;
    final String assetPathImage = "Assignment2/assets/image/"; // Asset paths
    final String assetPathAudio = "Assignment2/assets/audio/"; // Asset paths

    // gameState = 0; Main menu
    // gameState = 1; Loading page
    // gameState = 2; Settings page
    // gameState = 3; Help page
    // gameState = 4; Environment(Single Player)
    // gameState = 5; Environment(Time Attack)
    // gameState = 6; Checkout page
    int gameState;
    boolean isSinglePlayer; // State for single player mode
    Random randSys; // Random number generator
    int width; // Screen width
    int height; // Screen height
    AudioHandler ah; // Audio handler class, to handle all the audio
    int audioMusicCap; // Field to set the max music capacity
    int audioSFXCap; // Field to set the max sound effects (sfx) capacity
    String[] audioMusicPaths; // Paths for the music assets
    String[] audioSFXPaths; // Paths for the sfx assets
    float overAllVolume; // Base volume for most audio files
    float inGameVolume; // Adjusted volume for a particularly loud audio file
    Image transparentWhiteBackground;

    // Initialize the system fields
    public void InitSystem() {
        // Handle the audio I/O
        audioMusicCap = 3;
        audioSFXCap = 11;
        audioMusicPaths = new String[audioMusicCap];
        audioSFXPaths = new String[audioSFXCap];
        overAllVolume = 0.9f;
        inGameVolume = 0.5f;

        audioMusicPaths[0] = assetPathAudio + "music/track_light_positive_modern_T2.wav";
        audioMusicPaths[1] = assetPathAudio + "music/track_light_positive_disco_T2_Loop.wav";
        audioMusicPaths[2] = assetPathAudio + "music/track_ambience_underwater_T.wav";

        audioSFXPaths[0] = assetPathAudio + "sfx/sfx_bite1_T.wav";
        audioSFXPaths[1] = assetPathAudio + "sfx/sfx_bite2_T.wav";
        audioSFXPaths[2] = assetPathAudio + "sfx/sfx_click1_T.wav";
        audioSFXPaths[3] = assetPathAudio + "sfx/sfx_click2_T.wav";
        audioSFXPaths[4] = assetPathAudio + "sfx/sfx_click3_T.wav";
        audioSFXPaths[5] = assetPathAudio + "sfx/sfx_jingle_up_long1_T.wav";
        audioSFXPaths[6] = assetPathAudio + "sfx/sfx_jingle_up_short1_T.wav";
        audioSFXPaths[7] = assetPathAudio + "sfx/sfx_pop1_T.wav";
        audioSFXPaths[8] = assetPathAudio + "sfx/sfx_splash1_T.wav";
        audioSFXPaths[9] = assetPathAudio + "sfx/sfx_trumpet_down1_T.wav";
        audioSFXPaths[10] = assetPathAudio + "sfx/sfx_explosion_T.wav";

        ah = new AudioHandler(this, audioMusicCap, audioSFXCap);
        ah.setAudioMusicClips(audioMusicPaths);
        ah.setAudioSFXClips(audioSFXPaths);

        ah.setCurrentMusic(0);
        ah.setVolume(1.0f);
        ah.startCurrentAudioMusic();

        // System random field
        randSys = new Random();

        // System window size
        width = 1200;
        height = 900;

        // Set the window
        setWindowSize(width, height);

        // Game state
        gameState = 0;
        gameStateString = "nothing";

        // Single player mode default
        isSinglePlayer = true;

        transparentWhiteBackground = loadImage(assetPathImage + "background/background_w.png");
    }

    // Process the mouse handler in the system update
    public void updateSystem(double dt) {
        mouseHandler();
    }

    // Check if there was a collider between the players head and an enemy
    public void colliderCheck() {
        // Get the player head x collider
        double px;
        if (myfish.getFacingLeft()) { px = myfish.getXPos() - myfish.getFishHeadColliderXOffset(); }
        else { px = myfish.getXPos() + myfish.getFishHeadColliderXOffset(); }

        // Check if the enemy is at the head
        ArrayList<Enemy> removalValues = new ArrayList<Enemy>();
        for (Enemy en : enemies) {
            // Check players head to enemies body collisionF
            if (calcDistPointToSquare(px,
                    (myfish.getYPos() + myfish.getFishHeadColliderYOffset()),
                    (en.getXPos() - en.getColliderBodyXOffset()),
                    (en.getYPos() - en.getColliderBodyYOffset()),
                    en.getColliderBodyLength(),
                    en.getColliderBodyHeight())) {

                // There is a collision
                int enSize = en.getSize();
                // If the player can eat the enemy (based on size equality)
                if (myfish.getSize() >= enSize) {
                    // Player was equal to or greater than the enemy
                    // Play bite sfx
                    playAudioSFX(0, 1.0f);
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

    // Set the volume to play sfx
    public void playAudioSFX(int index, float volume) {
        ah.setVolume(volume);
        ah.playAudioSFX(index);
    }

    // Set the volume to restart music
    public void restartMusicLoop(int index, float volume) {
        ah.stopCurrentAudioMusic();
        ah.setVolume(volume);
        ah.setCurrentMusic(index);
        ah.startCurrentAudioMusic();
    }

    // Grow the player size (to allow eating more enemy types)
    public void growPlayerSize() {
        // Get the players current goal state
        int currentGoal = env.getCurrentGoal();

        // Check if the player has reached the medium size threshold
        if (myfish.getSize() == 0 && (currentGoal >= env.getGrowthThresholdMedium())) {
            myfish.setSize(1);
            myfish.updateSize();
        }
        // Check if the player has reached the large size threshold
        if (myfish.getSize() == 1 && (currentGoal >= env.getGrowthThresholdLarge())) {
            myfish.setSize(2);
            myfish.updateSize();
        }
    }

    // Calculate the distance between the point and square
    public boolean calcDistPointToSquare(double px, double py, double ex, double ey, double el, double eh) {
        if ((px >= ex) && (px <= (ex + el)) && (py >= ey) && (py <= (ey + eh))) { return true; }
        return false;
    }


    //-------------------------------------------------------
    // Mouse Handler
    //-------------------------------------------------------
    public double mouseX;
    public double mouseY;
    boolean mouse1Pressed = false;
    int mousePressedCount = 0;

    // MousePressed Event Handler
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) { mouse1Pressed = true; }
    }

    // Process the mouse pressed events
    public void mouseHandler() {
        if (mouse1Pressed) {
            if (mousePressedCount == 0) {
                // Play the mouse click sfx
                playAudioSFX(7, 1.2f);

                // Process mouse state
                if (gameState == 0) { gameStateString = sm.menuMouseClicked(mouseX, mouseY); }
                else if (gameState == 2) { gameStateString = inGameMenuFromMain.inGameMenuMouseClicked(mouseX, mouseY); }
                else if (gameState == 3) { gameStateString = hp.inGameMenuMouseClicked(mouseX, mouseY); }
                else if (gameState == 4) {
                    if (env.getIsPaused()) { gameStateString = inGameMenu.inGameMenuMouseClicked(mouseX, mouseY); }
                } else if (gameState == 6) { gameStateString = chkpg.checkClickTarget(mouseX, mouseY); }
            }
            mousePressedCount += 1;
        } else { mousePressedCount = 0; }
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
        //The user pressed left arrow
        if(e.getKeyCode() == KeyEvent.VK_LEFT) { leftKey = true; }
        // The user pressed right arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { rightKey = true; }
        // The user pressed up arrow
        if(e.getKeyCode() == KeyEvent.VK_UP) { upKey = true; }
        // The user pressed up arrow
        if(e.getKeyCode() == KeyEvent.VK_DOWN) { downKey = true; }
        // The user pressed ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { escKey = true; }
//        // The user pressed spaceKey
//        if(e.getKeyCode() == KeyEvent.VK_SPACE) { spaceKey = true; }
    }

    // Called whenever a key is released
    public void keyReleased(KeyEvent e) {
        // The user released left arrow
        if(e.getKeyCode() == KeyEvent.VK_LEFT) { leftKey = false; }
        // The user released right arrow
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) { rightKey = false; }
        // The user released up arrow
        if(e.getKeyCode() == KeyEvent.VK_UP) { upKey = false; }
        // The user released up arrow
        if(e.getKeyCode() == KeyEvent.VK_DOWN) { downKey = false; }
        // The user released ESC
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE) { escKey = false; }
//        // The user released spaceKey
//        if(e.getKeyCode() == KeyEvent.VK_SPACE) { spaceKey = false; }
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

    // Initialize the main menu fields
    public void initMenu() {
        titleWidth = 500;
        titleHeight = (int)(titleWidth * 0.3);

        startMenuBackground = loadImage(assetPathImage + "background/background1.png");
        startTitle = loadImage(assetPathImage + "icon/icon_title_x2.png");
        buttonIcons[0] = loadImage(assetPathImage + "icon/icon_start_x1.png");
        buttonIcons[1] = loadImage(assetPathImage + "icon/icon_time_attack_x1.png");
        buttonIcons[2] = loadImage(assetPathImage + "icon/icon_settings_x1.png");
        buttonIcons[3] = loadImage(assetPathImage + "icon/icon_quit_x1.png");
        buttonIcons[4] = loadImage(assetPathImage + "icon/icon_help_x1.png");
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

    // Process the main menu
    public void updateMenu(double dt) {
        if (gameState == 0) {
            if (Objects.equals(gameStateString, "single_player")) {
                // Reset the music
                restartMusicLoop(2, 1.0f);

                gameState = 1; // Go to LoadingPage
                env.setIsTimeAttack(false); // Set the environment to single player mode

            } else if (Objects.equals(gameStateString, "time_attack")) {
                // Reset the music
                restartMusicLoop(2, 1.0f);

                gameState = 1; // Go to LoadingPage
                env.setIsTimeAttack(true); // Set the environment to time attack mode

            } else if (Objects.equals(gameStateString, "settings")) {
                gameState = 2; // Go to Settings page

            } else if (Objects.equals(gameStateString, "help_page")) {
                gameState = 3; // Go to help page

            }
        } else if (gameState == 1) {
            lp.startLoading();
            lp.updatePage(dt);

            if (spaceKey && (lp.getProgress() >= 1.0)) {
                // Play the splash sound sfx
                playAudioSFX(8, 1.0f);

                // Reset the music
                restartMusicLoop(1, inGameVolume);

                // Set to the game play area instance
                gameState = 4;
                env.setBaseTime(getTime());
                env.setIsPaused(false);
            }
        }
    }

    // draw the main menu or the loading page
    public void drawMenu() {
        if (gameState == 0) { sm.drawAll(); }
        else if (gameState == 1) { lp.drawAll(); }
    }


    //-------------------------------------------------------
    // Help page methods
    //-------------------------------------------------------
    helppage hp;
    Image helpPageImage;

    // Initialize the help page fields
    public void initHelpPage() {
        helpPageImage = loadImage(assetPathImage + "background/background4.png");
        hp = new helppage(this, helpPageImage, startTitle, backButtonImage, titleWidth, titleHeight, width, height);
        hp.setWhiteBackgroundImage(transparentWhiteBackground);
    }

    // Update the help page
    public void updateHelpPage() {
        if (gameState == 3) {
            if (Objects.equals(gameStateString, "main_menu")) { gameState = 0; }
            gameStateString = "nothing";
        }
    }

    // Draw the help page
    public void drawHelpPage() {
        if (gameState == 3) {
            hp.drawall();
        }
    }


    //-------------------------------------------------------
    // In Game Menu methods
    //-------------------------------------------------------
    INGAMEMENU inGameMenu;
    INGAMEMENU inGameMenuFromMain;
    Image inGameMenuBackground;
    Image inGameMenuBackIcon;
    Image inGameMenuMusicButton;
    Image inGameMenuHardModeButton;
    Image inGameMenuMainMenu;
    Image inGameMenuTimeAttackTime;
    Image inGameMenuGrowthTarget;
    double backgroundX;
    double backgroundY;
    double backGroundWidth;
    double backGroundHeight;

    // Initialize the in game settings menu
    public void initInGameMenu() {
        inGameMenuBackground = loadImage(assetPathImage + "background/background4.png");
        inGameMenuBackIcon = loadImage(assetPathImage + "icon/icon_return2.png");
        inGameMenuMusicButton = loadImage(assetPathImage + "icon/icon_music1.png");
        inGameMenuHardModeButton = loadImage(assetPathImage + "icon/icon_shark_x2.png");
        inGameMenuMainMenu = loadImage(assetPathImage + "icon/icon_main_menu_x1.png");
        inGameMenuTimeAttackTime = loadImage(assetPathImage + "icon/icon_time_attack_time_x2.png");
        inGameMenuGrowthTarget = loadImage(assetPathImage + "icon/icon_growth_target_x2.png");

        backGroundWidth = width / 3.0;
        backGroundHeight = height / 2.0;
        backgroundX = ((width/2.0) - (backGroundWidth/2.0));
        backgroundY = ((height/2.0) - (backGroundHeight/2.0));

        inGameMenu = new INGAMEMENU(this, backGroundWidth, backGroundHeight, false);
        inGameMenu.setImages(inGameMenuBackground,
                inGameMenuBackIcon,
                inGameMenuMusicButton,
                inGameMenuHardModeButton,
                inGameMenuMainMenu,
                inGameMenuTimeAttackTime,
                inGameMenuGrowthTarget);
        inGameMenu.setBackgroundParameters(backgroundX, backgroundY, backGroundWidth, backGroundHeight);
        int buttonOffset = 50;
        inGameMenu.setButtonSize(buttonOffset);
        inGameMenu.setButtonOffset(buttonOffset);
        inGameMenu.setBackIconParameters();
        inGameMenu.setMusicButtonParameters();
        inGameMenu.setHardModeParameters();
        inGameMenu.setToMainMenuParameters();

        inGameMenuFromMain = new INGAMEMENU(this, width, height, true);
        inGameMenuFromMain.setImages(inGameMenuBackground,
                inGameMenuBackIcon,
                inGameMenuMusicButton,
                inGameMenuHardModeButton,
                inGameMenuMainMenu,
                inGameMenuTimeAttackTime,
                inGameMenuGrowthTarget);
        inGameMenuFromMain.setBackgroundParameters(0, 0, width, height);
        inGameMenuFromMain.setBackIconParameters();
        inGameMenuFromMain.setMusicButtonParameters();
    }

    // Update the settings menu
    public void updateInGameMenu(double dt) {
        if (gameState == 2) { processInGameMenu(); }
        if (gameState == 4) {
            if (env.getIsPaused()) { processInGameMenu(); }
        }
    }

    // Process the settings menu
    public void processInGameMenu() {
        switch(gameStateString) {
            case "main_menu":
                // Go back to main menu
                if (gameState == 4) { restartMusicLoop(0, overAllVolume); }

                // Reset game state
                finalReset();
                env.setRestartLevel(false);
                gameState = 0;
                resetGameLevel();
                resetGameTargets();
                break;

            case "toggle_music":
                if (!ah.getPauseMusic()) {
                    ah.stopCurrentAudioMusic();
                    ah.setPauseMusic(true);
                } else {
                    ah.setPauseMusic(false);
                    ah.startCurrentAudioMusic();
                }
                break;

            // Should add other menu adjustments here
            case "back_to_game":
                byPassUnpause = true;
                break;

            // Should add other menu adjustments here
            case "hard_mode":
                env.setHardMode(!env.getHardMode());
                inGameMenu.setIsHardMode(env.getHardMode());
                inGameMenuFromMain.setIsHardMode(env.getHardMode());
                break;

            // Should add other menu adjustments here
            case "growth_target":
                currentGoalRangeIndex += 1;
                if (currentGoalRangeIndex >= targetGoalRanges.length) { currentGoalRangeIndex = 0; }

                goalMaximum = targetGoalRanges[currentGoalRangeIndex];
                env.setMaxLevel(goalMaximum);

                targetGoal = defaultTarget + (targetGoalIncrement * level);
                env.setTargetGoal(targetGoal);

                growthThresholdM = (int)(targetGoal / 3.0);
                growthThresholdL = (int)((targetGoal / 3.0) * 2.0);
                env.setGrowthThresholdLarge(growthThresholdL);
                env.setGrowthThresholdMedium(growthThresholdM);

                env.setCurrentRequiredGrowth(goalMaximum);
                inGameMenuFromMain.setGrowthTargetValue(goalMaximum);
                break;

            // Should add other menu adjustments here
            case "time_attack_time":
                env.setCurrentTimeAttackLevel(targetTime - timeMinimum);
                currentTargetTimeRangeIndex += 1;
                if (currentTargetTimeRangeIndex >= targetTimeRanges.length) { currentTargetTimeRangeIndex = 0; }

                defaultTime = targetTimeRanges[currentTargetTimeRangeIndex];
                targetTime = defaultTime - (targetTimeAdjuster * env.getCurrentLevel());
                env.setCountDownTimer(targetTime);

                env.setDefaultTimeAttackLevel(defaultTime);
                inGameMenuFromMain.setTimeAttackTimeValue(defaultTime);
                break;

            default:
                break;

        }
        gameStateString = "nothing";
    }

    // Draw the settings menu
    public void drawInGameMenu() {
        if (gameState == 2) { inGameMenuFromMain.drawInGameMenu(); }
        if (gameState == 4) {
            if (env.getIsPaused()) { inGameMenu.drawInGameMenu(); }
        }
    }

    //-------------------------------------------------------
    // CheckoutPage methods
    //-------------------------------------------------------
    CheckoutPage chkpg;
    Image checkoutImage;
    Image backButtonImage;
    Image continueiButtonImage;
    boolean setCheckoutPage;

    // Initialize the checkout fields
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

    // Update the checkout page
    public void updateCheckout(double dt) {
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

            // Process the button click result
            if (gameStateString == "main_menu") {
                // Reset the music
                restartMusicLoop(0, overAllVolume);

                // Reset the game state
                finalReset();
                env.setRestartLevel(false);
                resetGameTargets();
                gameState = 0;

            } else if (gameStateString == "next_level") {
                // Play splash sfx
                playAudioSFX(8, 1.0f);
                // Reset the music
                restartMusicLoop(1, inGameVolume);

                // Reset the game state
                finalReset();
                if (!env.getRestartLevel()) { nextLevel(); }
                env.setRestartLevel(false);
                gameState = 4;

            } else if (gameStateString == "restart") {
                // Play splash sfx
                playAudioSFX(8, 1.0f);
                // Reset the music
                restartMusicLoop(1, inGameVolume);

                // Reset the game state
                finalReset();
                env.setRestartLevel(false);
                gameState = 4;

            }
            gameStateString = "nothing";
            resetGameLevel();
        }
    }

    // Draw the checkout page
    public void drawCheckout() {
        if (gameState == 6) {
            Image[] enImages = {enemyFish[0], enemyFish[1], enemyFish[2]};
            chkpg.drawCheckout(enImages);
            chkpg.setEnemyBaseSize(enemyWidth / 2,enemyHeight / 2);
        }
    }

    //-------------------------------------------------------
    // Player methods
    //-------------------------------------------------------
    myfish myfish;
    Image myFishImage;

    // Initialize the player fields
    public void initPlayer() {
        double fishW = 60;
        double fishH = 40;
        double fishX = (width / 2.0); // starting x
        double fishY = fishH * 3; // starting y
        myfish = new myfish(fishX, fishY, fishW, fishH);
        myfish.setYVel(0);
        double fishImageW = fishW * 1.5;
        double fishImageH = fishH * 1.5;
        myfish.setImageParameters((fishImageW / 2.0),
                (fishImageH / 2.0),
                fishImageW,
                fishImageH);
        myfish.setFishHeadColliderXOffset((fishW / 2.0) -12);
        myfish.setFishHeadColliderYOffset(8);

        myFishImage = loadImage(assetPathImage + "entity/entity_player1.png");
    }

    // Update the player
    public void updatePlayer(double dt) {
        if (gameState == 4) {
            // Player is dead don't process the player
            if (!myfish.getIsAlive()) { return; }

            if (leftKey) { myfish.setFacingLeft(true); }
            else if (rightKey) { myfish.setFacingLeft(false); }

            // If the player is still alive, check the collider for collisions
            // and grow the player if the size has changed
            if (myfish.getIsAlive()) {
                colliderCheck();
                growPlayerSize();
            }

            // Process the player
            double ox = env.getGlobalCOMX();
            double oy = env.getGlobalCOMY();
            double w = env.getGlobalWidth();
            double h = env.getGlobalHeight();
            myfish.updatemyfish(dt,
                    upKey,
                    downKey,
                    leftKey,
                    rightKey,
                    ox,
                    oy,
                    w,
                    h);
        }
    }

    // Draw the player (processing loop)
    public void drawPlayer() {
        if (gameState == 4) {
            // Draw player if they are alive
            if (myfish.getIsAlive()) { drawMyself(); }
        }
    }

    // Draw the player
    public void drawMyself() {
        // Decide which image to draw based on direction
        Image currentFishImage = myFishImage;
        if (myfish.getFacingLeft()) { // Facing left
            drawImage(currentFishImage,
                    (env.getVisibleAreaCOMX() + myfish.getImageOffsetX()),
                    (env.getVisibleAreaCOMY() - myfish.getImageOffsetY()),
                    -myfish.getImageWidth(),
                    myfish.getImageHeight());
        } else { // Facing right
            drawImage(currentFishImage,
                    (env.getVisibleAreaCOMX() - myfish.getImageOffsetX()),
                    (env.getVisibleAreaCOMY() - myfish.getImageOffsetY()),
                    myfish.getImageWidth(),
                    myfish.getImageHeight());
        }

        // Debug: draw the player collider for debugging purposes
//        drawPlayerCollider();
    }

    // Debug: Draw the player collider for debugging purposes
    public void drawPlayerCollider() {
        // Draw the collider box
        changeColor(0,255,0);
        // top
        drawLine((env.getVisibleAreaCOMX() - myfish.getOffsetX()),
                (env.getVisibleAreaCOMY() - myfish.getOffsetY()),
                (env.getVisibleAreaCOMX() + myfish.getOffsetX()),
                (env.getVisibleAreaCOMY() - myfish.getOffsetY()));
        // bottom
        drawLine((env.getVisibleAreaCOMX() - myfish.getOffsetX()),
                (env.getVisibleAreaCOMY() + myfish.getOffsetY()),
                (env.getVisibleAreaCOMX() + myfish.getOffsetX()),
                (env.getVisibleAreaCOMY() + myfish.getOffsetY()));
        // left
        drawLine((env.getVisibleAreaCOMX() - myfish.getOffsetX()),
                (env.getVisibleAreaCOMY() - myfish.getOffsetY()),
                (env.getVisibleAreaCOMX() - myfish.getOffsetX()),
                (env.getVisibleAreaCOMY() + myfish.getOffsetY()));
        // right
        drawLine((env.getVisibleAreaCOMX() + myfish.getOffsetX()),
                (env.getVisibleAreaCOMY() - myfish.getOffsetY()),
                (env.getVisibleAreaCOMX() + myfish.getOffsetX()),
                (env.getVisibleAreaCOMY() + myfish.getOffsetY()));

        // Draw the head collider
        changeColor(255,0,0);
        double fishX;
        double fishY = env.getVisibleAreaCOMY() + myfish.getFishHeadColliderYOffset();
        if (myfish.getFacingLeft()) { fishX = env.getVisibleAreaCOMX() - myfish.getFishHeadColliderXOffset(); }
        else { fishX = env.getVisibleAreaCOMX() + myfish.getFishHeadColliderXOffset(); }
        drawCircle(fishX, fishY, 2);
    }



    //-------------------------------------------------------
    // Item methods
    //-------------------------------------------------------
    pearl pearl;
    boom boom;
    starfish starfish;
    Image pearlimage;
    Image boomimage;
    Image starfishimage;

    // Initialize the item fields
    public void initItems() {
        pearlimage = loadImage(assetPathImage + "item/item_pearl1.png");
        boomimage = loadImage(assetPathImage + "item/item_bomb1.png");
        starfishimage = loadImage(assetPathImage + "item/item_starfish1.png");
        pearl = new pearl(this, pearlimage);
        boom = new boom(this, boomimage);
        starfish = new starfish(this, starfishimage);
    }

    // Update and handle all the item processing
    public void updateItems(double dt) {
        if(!env.getIsLevelComplete()) {
            // Make sure the game bounds are up before spawning items
            double rangeX = env.getBoundaryX2() - env.getBoundaryX1();
            double rangeY = env.getBoundaryY2() - env.getBoundaryY1();
            // Make sure the ranges are positive
            if (rangeX < 0) { rangeX *= -1.0; }
            if (rangeY < 0) { rangeY *= -1.0; }

            if (rangeX > 0 && rangeY > 0) {
                pearl.setPlayAreaCOM(env.getVisibleAreaCOMX(), env.getVisibleAreaCOMY());
                pearl.setWindowToGlobalCOMOffset(myfish.getXPos(), myfish.getYPos());

                // Update the pearl
                if (pearl.updatepearl(dt,
                        env.getGlobalWidth(),
                        env.getGlobalHeight(),
                        (2 * myfish.getOffsetX()),
                        (2 * myfish.getOffsetY()))) {

                    // Play bite sfx
                    playAudioSFX(1, 1.0f);
                    // Play pop sfx
                    playAudioSFX(7, 1.0f);

                    double speedVal = 25.0;
                    myfish.incrementMaxSpeed(speedVal);
                    myfish.incrementAccelerationSpeed(speedVal);
                }

                starfish.setPlayAreaCOM(env.getVisibleAreaCOMX(), env.getVisibleAreaCOMY());
                starfish.setWindowToGlobalCOMOffset(myfish.getXPos(), myfish.getYPos());

                // Update the starfish
                if (starfish.updatestarfish(dt,
                        env.getGlobalWidth(),
                        env.getGlobalHeight(),
                        (2 * myfish.getOffsetX()),
                        (2 * myfish.getOffsetY()))) {

                    // Play bite sound
                    playAudioSFX(1, 1.0f);
                    // Play power up sfx
                    playAudioSFX(6, 1.0f);

                    // Update the environment score
                    env.addScore(20);
                }

                boom.setPlayAreaCOM(env.getVisibleAreaCOMX(), env.getVisibleAreaCOMY());
                boom.setWindowToGlobalCOMOffset(myfish.getXPos(), myfish.getYPos());

                // Update the bomb
                if (boom.updateboom(dt,
                        env.getGlobalWidth(),
                        env.getGlobalHeight(),
                        (2 * myfish.getOffsetX()),
                        (2 * myfish.getOffsetY()))) {

                    // Play explosion sfx
                    playAudioSFX(10, 1.2f);

                    myfish.setIsAlive(false);
                    env.setEndLevel();
                }
            }
        }
    }

    // Draw all the items
    public void drawItems() {
        if (gameState == 4) {
            drawpearl();
            drawstarfish();
            drawboom();

            // Debug: to draw the item collider
//            pearl.drawItemSpawnPoint();
//            starfish.drawItemSpawnPoint();
//            bomb.drawItemSpawnPoint();
        }
    }

    // Draw the bomb item
    public void drawboom(){
        if (boom.getIsVisible()) {
            boom.drawItem();
            boom.drawItemColliders();
        }
    }

    // Draw the starfish item
    public void drawstarfish() {
        if (starfish.getIsVisible()) {
            starfish.drawItem();
            starfish.drawItemColliders();
        }
    }

    // Draw the pearl item
    public void drawpearl() {
        if (pearl.getIsVisible()) {
            pearl.drawItem();
            pearl.drawItemColliders();
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

    // Initialize the enemy fields
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
        enemyWidth = 64;
        enemyHeight = 32;

        try {
            enemyFish = new Image[enemyTypes];
            enemyFish[0] = loadImage(assetPathImage + "entity/entity_fish1_x1.png");
            enemyFish[1] = loadImage(assetPathImage + "entity/entity_fish2_x1.png");
            enemyFish[2] = loadImage(assetPathImage + "entity/entity_fish3.png");
            shark = loadImage(assetPathImage + "entity/entity_shark1.png");

        } catch(Exception e) {
            System.out.println("Could not find enemy images. Skipping loading the images.");
        }
    }

    // Update enemies (main update loop)
    public void updateEnemies(double dt) {
        if (gameState == 4) {
            // Spawn enemy fish
            SpawnEnemy();

            // Process the normal enemies
            ArrayList<Enemy> removalValues = new ArrayList<Enemy>();
            if (enemies.size() > 0) { updateAllEnemies(dt, enemies, removalValues); }
            // Remove the enemies marked for removal
            removeEnemies(enemies, removalValues);

            // Process the shark enemy
            removalValues = new ArrayList<Enemy>();
            if (sharkEnemies.size() > 0) { updateAllEnemies(dt, sharkEnemies, removalValues); }
            // Remove the shark enemies marked for removal
            removeEnemies(sharkEnemies, removalValues);
        }
    }

    // Update all the enemies that currently exist
    public void updateAllEnemies(double dt, ArrayList<Enemy> ens, ArrayList<Enemy> removalValues) {
        for (Enemy en : ens) {
            en.setPlayAreaCOM(env.getVisibleAreaCOMX(), env.getVisibleAreaCOMY());
            en.setWindowToGlobalCOMOffset(myfish.getXPos(), myfish.getYPos());
            // Update the enemy positions
            en.randomizeEnemyMovement();
            en.updateEnemyPosition(dt, true);
            // Check if the player has been bitten
            if (en.checkEnemyBitePlayer(myfish)) {
                if (en.getSize() > myfish.getSize()) {
                    // Play bite sfx
                    if (myfish.getIsAlive()) { playAudioSFX(0, 1.0f); }

                    myfish.setIsAlive(false);
                    env.setEndLevel();
                }
            }
            // Check if enemy moves out of bounds
            en.checkEnemyBounds(env, myfish, removalValues);
        }
    }

    // Remove specific enemies
    public void removeEnemies(ArrayList<Enemy> enemies, ArrayList<Enemy> removalValues) {
        // Remove the enemies that have been selected for removal.
        if (removalValues.size() > 0) {
            for (Enemy toRemove : removalValues) {
                enemies.remove(toRemove);
            }
        }
    }

    // Remove all the enemies
    public void removeAllEnemies(ArrayList<Enemy> enemies, ArrayList<Enemy> sharkEnemies) {
        enemies.clear();
        sharkEnemies.clear();
    }

    // Spawn the enemy based on the enemy fields
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

    // Draw all the enemies
    public void drawEnemies(ArrayList<Enemy> enemies) {
        if (gameState == 4) {
            for (Enemy en : enemies) { en.drawAll(); }
        }
    }

    // Create the regular enemies
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

        // Retrieve the game boundaries
        double[] envBound = new double[4];
        envBound[0] = env.getVisibleAreaCOMX() + env.getWindowToGlobalOriginOffsetX(); // Left edge
        envBound[1] = env.getVisibleAreaCOMX() + env.getWindowToGlobalOriginOffsetX() + env.getGlobalWidth(); // Right edge
        envBound[2] = env.getVisibleAreaCOMY() + env.getWindowToGlobalOriginOffsetY(); // Top edge
        envBound[3] = env.getVisibleAreaCOMY() + env.getWindowToGlobalOriginOffsetY() + env.getGlobalHeight(); // Bottom edge

        Enemy en;
        int size;
        int headOffsetTemp;
        Image tempImage;
        if (selectSize < enemySmall) { // spawn a small fish
            size = 0;
            headOffsetTemp = 10;
            tempImage = enemyFish[0];
        } else if (selectSize < enemyMedium) { // spawn a medium fish
            size = 1;
            headOffsetTemp = 0;
            tempImage = enemyFish[1];
        } else { // spawn a large fish
            size = 2;
            headOffsetTemp = 0;
            tempImage = enemyFish[2];
        }
        en = new Enemy(this,
                tempImage,
                size,
                env.getGlobalWidth(),
                env.getGlobalHeight(),
                env.getVisibleAreaCOMX(),
                env.getVisibleAreaCOMY(),
                env.getWindowToGlobalOriginOffsetX(),
                env.getWindowToGlobalOriginOffsetY());

        en.setHeadYOffset(headOffsetTemp);
        enemies.add(en);
    }

    // Create the "shark" enemy (A lionfish png was selected but the concept remains)
    public void createShark() {
        if (sharkEnemies.size() >= maxShark) { return; }

        // Retrieve the game boundaries
        double[] envBound = new double[4];
        envBound[0] = env.getVisibleAreaCOMX() + env.getWindowToGlobalOriginOffsetX(); // Left edge
        envBound[1] = env.getVisibleAreaCOMX() + env.getWindowToGlobalOriginOffsetX() + env.getGlobalWidth(); // Right edge
        envBound[2] = env.getVisibleAreaCOMY() + env.getWindowToGlobalOriginOffsetY(); // Top edge
        envBound[3] = env.getVisibleAreaCOMY() + env.getWindowToGlobalOriginOffsetY() + env.getGlobalHeight(); // Bottom edge

        // The shark should be the largest size
        Enemy shk = new Enemy(this,
                shark,
                3,
                env.getGlobalWidth(),
                env.getGlobalHeight(),
                env.getVisibleAreaCOMX(),
                env.getVisibleAreaCOMY(),
                env.getWindowToGlobalOriginOffsetX(),
                env.getWindowToGlobalOriginOffsetY());

        // The shark should be very fast, ie (400 + range(400))
        shk.setRandomVelocity(400, 400);
        shk.setChanceToLeaveEnvironment(100); // The shark will always leave the environment
        shk.setHeadYOffset(10);
        sharkEnemies.add(shk);
    }


    //-------------------------------------------------------
    // Environment methods
    //-------------------------------------------------------
    Environment env;
    int imagesCap;
    Image[] envImages;
    int targetGoal;
    int targetGoalIncrement;
    int defaultTarget;
    int goalMaximum;
    int currentGoalRangeIndex;
    int[] targetGoalRanges;
    int currentTargetTimeRangeIndex;
    double[] targetTimeRanges;
    double targetTime;
    double targetTimeAdjuster;
    double timeMinimum;
    double defaultTime;
    boolean singlePlayerComplete;
    boolean timeAttackComplete;
    int growthThresholdM;
    int growthThresholdL;
    int level = 0;
    int escKeyCount;
    boolean byPassUnpause;

    // Initialize the initial environment fields
    public void initEnvironment() {
        imagesCap = 3;
        envImages = new Image[imagesCap];
        envImages[0] = loadImage(assetPathImage + "background/background3.png");
        envImages[1] = loadImage(assetPathImage + "background/background5.png");
        envImages[2] = loadImage(assetPathImage + "background/background6.png");

        targetGoalRanges = new int[6];
        targetGoalRanges[0] = 50;
        targetGoalRanges[1] = 60;
        targetGoalRanges[2] = 70;
        targetGoalRanges[3] = 80;
        targetGoalRanges[4] = 90;
        targetGoalRanges[5] = 100;
        currentGoalRangeIndex = 0;

        targetTimeRanges = new double[7];
        targetTimeRanges[0] = 30.0;
        targetTimeRanges[1] = 45.0;
        targetTimeRanges[2] = 60.0;
        targetTimeRanges[3] = 75.0;
        targetTimeRanges[4] = 90.0;
        targetTimeRanges[5] = 105.0;
        targetTimeRanges[6] = 120.0;
        currentTargetTimeRangeIndex = 2;

        goalMaximum = targetGoalRanges[currentGoalRangeIndex]; // Defaults at 50 points max
        timeMinimum = 15.0;
        defaultTime = targetTimeRanges[currentTargetTimeRangeIndex]; // Defaults at 60.o seconds
        defaultTarget = 10;
        targetGoalIncrement = 5;
        targetTimeAdjuster = 5.0;
        singlePlayerComplete = false;
        timeAttackComplete = false;
        escKeyCount = 0;
        byPassUnpause = false;

        env = new Environment(this, envImages, !isSinglePlayer);
        env.setEnvironmentPlayWidth(width);
        env.setEnvironmentPlayHeight(height);
        env.setCurrentLevel(0);

        targetGoal = defaultTarget + (targetGoalIncrement * env.getCurrentLevel());
        targetTime = defaultTime - (targetTimeAdjuster * env.getCurrentLevel());
        growthThresholdM = (int)(targetGoal / 3.0);
        growthThresholdL = (int)((targetGoal / 3.0) * 2.0);

        env.setTargetGoal(targetGoal);
        env.setCountDownTimer(targetTime);
        env.setGrowthThresholdLarge(growthThresholdL);
        env.setGrowthThresholdMedium(growthThresholdM);
        env.setHardMode(false);
        env.setMaxLevel(goalMaximum);
        env.setMinTimeAttackLevel(timeMinimum);
        env.setCurrentTimeAttackLevel(targetTime - timeMinimum);
        env.setDefaultTimeAttackLevel(defaultTime);

        // The inGameMenu has been set before this, so can set the target values to show in the settings page
        inGameMenu.setIsHardMode(env.getHardMode());
        inGameMenuFromMain.setIsHardMode(env.getHardMode());

        // The inGameMenuFromMain has been set before this, so can set the target values to show in the settings page
        inGameMenuFromMain.setGrowthTargetValue(goalMaximum);
        inGameMenuFromMain.setTimeAttackTimeValue(defaultTime);
    }

    // Update the environment every game tick
    public void updateEnvironment(double dt) {
        if (gameState == 4) {
            // Check if the level is complete
            env.environmentLevelCompleteCheck();

            // Update some moving boundaries
            env.updateEnvironment(dt);

            // Check for the pausing of the game
            boolean wasPaused = false;
            // Only use the bypass unpause in specific situations. Such as from the overlay settings menu
            if (escKey || byPassUnpause) {
                // Trigger only once per press
                if (escKeyCount == 0) {
                    // Handle paused case
                    if (env.getIsPaused()) {
                        env.setIsPaused(false);
                        wasPaused = true;
                    } else {
                        env.setIsPaused(true);
                        drawInGameMenu();
                    }
                }
                escKeyCount += 1;
                byPassUnpause = false;

            } else { escKeyCount = 0; }

            // Level is complete keep the game paused
            if (env.getIsLevelComplete() || env.getEndLevel()) {
                env.setIsPaused(true);

                // Reset the music
                restartMusicLoop(2, overAllVolume);
                // Play winner audio sfx
                if (myfish.getIsAlive()) { playAudioSFX(5, 1.0f); }
                // Play loser audio sfx
                else { playAudioSFX(9, 1.0f); }

                gameState = 6; // Go to CheckoutPage
            }
            UpdateTimer(wasPaused);
            if (!env.getIsPaused()) { env.setWindowToGlobalCOMOffsetX(myfish.getXPos(), myfish.getYPos()); }
        }
    }

    // Reset the game level fields
    // There is more than 1 reset method so the resetting from different states can be accommodated for
    public void resetGameLevel() {
        // Remove all enemies from the level
        removeAllEnemies(enemies, sharkEnemies);

        // Reset the spawn timer fields
        enemySpawnTimer = 0.0;
        enemySpawnTimerPrevious = 0.0;
        enemySpawnDelay = randSys.nextDouble(2.0) + 1.0;

        // Remove all items from the level
        // Reset the pearl
        pearl.setVisible(false);
        pearl.resetTimeVisible();

        // Reset the bomb
        boom.setVisible(false);
        boom.resetTimeVisible();

        // Reset the starfish
        starfish.setVisible(false);
        starfish.resetTimeVisible();
        starfish.setSpeed(0.0, 0.0);

        // Reset the player
        myfish.setXPos(width / 2.0);
        myfish.setYPos(myfish.getHeight() * 3);
        myfish.setXVel(0);
        myfish.setYVel(0);
        myfish.setIsAlive(true);
        myfish.setSize(0);
        myfish.resetSpeed();

        // Reset the in game display timer
        env.setBaseTime(getTime());
        env.setCurrentGoal(0);
        if (env.getIsTimeAttack()) {
            env.setCountDownCurrentTimer(0.0);
        }
        env.setIsLevelComplete(false);
        env.setEndLevel(false);
        env.setIsPaused(false);
        chkpg.setRestartButton(false);

        setCheckoutPage = false;
    }

    // Reset some fields for a new game
    // There is more than 1 reset method so the resetting from different states can be accommodated for
    public void finalReset() {
        env.setScore(0);
        env.setEnemyEatenCounter();
        pearl.setTimesEaten(0);
        starfish.setTimesEaten(0);
        myfish.setSize(0);
        myfish.updateSize();
        myfish.setYVel(0);
    }

    // Reset the targets for the level
    // There is more than 1 reset method so the resetting from different states can be accommodated for
    public void resetGameTargets() {
        // Reset the game level
        env.setCurrentLevel(0);

        // Reset the target goals
        targetGoal = defaultTarget + (targetGoalIncrement * env.getCurrentLevel());
        if (targetGoal > goalMaximum) { targetGoal = goalMaximum; }
        growthThresholdM = (int)(targetGoal / 3.0);
        growthThresholdL = (int)((targetGoal / 3.0) * 2.0);
        env.setTargetGoal(targetGoal);
        env.setCurrentRequiredGrowth(targetGoal);
        env.setGrowthThresholdMedium(growthThresholdM);
        env.setGrowthThresholdLarge(growthThresholdL);

        // Reset the target time
        targetTime = defaultTime - (targetTimeAdjuster * env.getCurrentLevel());
        if (targetTime < timeMinimum) { targetTime = timeMinimum; }
        env.setCountDownTimer(targetTime);
        env.setCurrentTimeAttackLevel(targetTime - timeMinimum);
    }

    // Proceed to the next level, set the next level fields
    public void nextLevel() {
        // Increment level
        env.setCurrentLevel(env.getCurrentLevel() + 1);

        // Increment the target goal for the level
        targetGoal = defaultTarget + (targetGoalIncrement * env.getCurrentLevel());
        if (targetGoal > goalMaximum) {
            // If the goal exceeds the maximum then set to the maximum
            targetGoal = goalMaximum;
            // If the game is not time attack, then single player is complete
            if (!env.getIsTimeAttack()) { singlePlayerComplete = true; }
        }
        // Update the growth thresholds
        growthThresholdM = (int)(targetGoal / 3.0);
        growthThresholdL = (int)((targetGoal / 3.0) * 2.0);

        // Set the target goal for the level
        env.setTargetGoal(targetGoal);
        env.setCurrentRequiredGrowth(targetGoal);
        env.setGrowthThresholdLarge(growthThresholdL);
        env.setGrowthThresholdMedium(growthThresholdM);

        if (env.getIsTimeAttack()) {
            // Change the time attack level
            env.setCurrentTimeAttackLevel(targetTime - timeMinimum);
            // Reset the time attack timer
            env.setCountDownCurrentTimer(0.0);
            // If the game is time attack decrement the target time
            targetTime = defaultTime - (targetTimeAdjuster * env.getCurrentLevel());
            // If the target time goes below the minimum, then time attack is complete
            if (targetTime < timeMinimum) {
                targetTime = timeMinimum;
                timeAttackComplete = true;
            }
            // Set the target time otherwise
            env.setCountDownTimer(targetTime);
        }
    }

    // Update the timer for the level
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

    // Draw the environment
    public void drawEnvironment() {
        if (gameState == 4) { env.drawEnvironment(); }
    }

    // Draw the top most layer for the environment
    public void drawEnvironmentLayerTop() {
        if (gameState == 4) {
            env.drawHUD();
            boolean timeAttack = env.getIsTimeAttack();
            env.drawTimer(timeAttack);
            env.drawScore();
            env.drawCurrentToTargetGoal();
            env.drawGrowth();

            // Debug: Draw the boundaries for the size that the level should be
//            env.drawImageShouldBeSize();
        }
    }
}
