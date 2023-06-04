/*
 * Author: Robert Tubman
 * ID: 11115713
 *
 * Co-Author: Lucass (Xidi Kuang)
 * ID: 21008041
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
    public static void main(String[] args) {
        createGame(new Main());
    }
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
    public void paintComponent() {
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

    public void updateSystem(double dt) {
        mouseHandler();
    }

    public void colliderCheck() {
        // Get the player head x collider
        double px;
        if (myfish.getFacingLeft()) { px = myfish.getXPos() - myfish.getOffsetX(); }
        else { px = myfish.getXPos() + myfish.getOffsetX(); }

        // Check if the enemy is at the head
        ArrayList<Enemy> removalValues = new ArrayList<Enemy>();
        for (Enemy en : enemies) {
            // Check players head to enemies body collisionF
            if (calcDistPointToSquare(px,
                    (myfish.getYPos()),
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
//            System.out.println("Updating the fish size to 1");
            myfish.setSize(1);
            myfish.updateSize();
        }
        // Check if the player has reached the large size threshold
        if (myfish.getSize() == 1 && (currentGoal >= env.getGrowthThresholdLarge())) {
            myfish.setSize(2);
            myfish.updateSize();
        }
    }
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
        if(e.getButton() == MouseEvent.BUTTON1) {
            mouse1Pressed = true;
            System.out.println(mouse1Pressed);
        }
    }

    public void mouseHandler() {
        if (mouse1Pressed) {
            if (mousePressedCount == 0) {
                // Play the mouse click sfx
                playAudioSFX(7, 1.2f);

                // Process mouse state
                if (gameState == 0) {
                    gameStateString = sm.menuMouseClicked(mouseX, mouseY);

                } else if (gameState == 2) {
                    gameStateString = inGameMenuFromMain.inGameMenuMouseClicked(mouseX, mouseY);

                }  else if (gameState == 3) {
                    gameStateString = hp.inGameMenuMouseClicked(mouseX, mouseY);

                }  else if (gameState == 4) {
                    if (env.getIsPaused()) {
                        gameStateString = inGameMenu.inGameMenuMouseClicked(mouseX, mouseY);
                    }

                } else if (gameState == 6) {
                    gameStateString = chkpg.checkClickTarget(mouseX, mouseY);

                }
            }
            mousePressedCount += 1;
        } else {
            mousePressedCount = 0;
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
            // TODO: add this back in so the player can't skip the loading page
//            if (spaceKey && (lp.getProgress() >= 1.0)) {
            if (spaceKey) {
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

    public void drawMenu() {
        if (gameState == 0) { sm.drawAll(); }
        else if (gameState == 1) { lp.drawAll(); }
    }


    //-------------------------------------------------------
    // Help page methods
    //-------------------------------------------------------
    helppage hp;
    Image helpPageImage;

    public void initHelpPage() {
        helpPageImage = loadImage(assetPathImage + "background/background4.png");
        hp = new helppage(this, helpPageImage, startTitle, backButtonImage, titleWidth, titleHeight, width, height);
        hp.setWhiteBackgroundImage(transparentWhiteBackground);
    }
    public void updateHelpPage() {
        if (gameState == 3) {
            if (Objects.equals(gameStateString, "main_menu")) { gameState = 0; }
            gameStateString = "nothing";
        }
    }
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
    public void updateInGameMenu(double dt) {
        if (gameState == 2) { processInGameMenu(); }
        if (gameState == 4) {
            if (env.getIsPaused()) { processInGameMenu(); }
        }
    }

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
    Image pearlimage;
    Image boomimage;
    Image starfishimage;
    public void initPlayer() {
        double fishW = 60;
        double fishH = 40;
        double fishX = (width / 2.0);
        double fishY = fishH * 3;
        myfish = new myfish(fishX, fishY, fishW, fishH);
        myfish.setYVel(175);
        double fishImageW = fishW * 1.5;
        double fishImageH = fishH * 1.5;
        myfish.setImageParameters((fishImageW / 2.0),
                (fishImageH / 2.0),
                fishImageW,
                fishImageH);

        myFishImage = loadImage(assetPathImage + "entity/entity_player1.png");
    }
    public void updatePlayer(double dt) {
        if (gameState == 4) {
            if (spaceKey) {
                myfish.setIsAlive(true);
                env.setCurrentGoal(env.getCurrentGoal() + 1);
            } // TODO: CHEAT for debug, remove when done

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
            drawImage(currentFishImage,
                    (myfish.getXPos() + myfish.getImageOffsetX()),
                    (myfish.getYPos() - myfish.getImageOffsetY()),
                    -myfish.getImageWidth(),
                    myfish.getImageHeight());
        } else {
            drawImage(currentFishImage,
                    (myfish.getXPos() - myfish.getImageOffsetX()),
                    (myfish.getYPos() - myfish.getImageOffsetY()),
                    myfish.getImageWidth(),
                    myfish.getImageHeight());
        }

        drawPlayerCollider(); // TODO: Remove before submitting, or add as a setting to show some debug commands?
    }

    public void drawPlayerCollider() {
        // Draw the collider box
        changeColor(0,255,0);
        // top
        drawLine((myfish.getXPos() - myfish.getOffsetX()),
                (myfish.getYPos() - myfish.getOffsetY()),
                (myfish.getXPos() + myfish.getOffsetX()),
                (myfish.getYPos() - myfish.getOffsetY()));
        // bottom
        drawLine((myfish.getXPos() - myfish.getOffsetX()),
                (myfish.getYPos() + myfish.getOffsetY()),
                (myfish.getXPos() + myfish.getOffsetX()),
                (myfish.getYPos() + myfish.getOffsetY()));
        // left
        drawLine((myfish.getXPos() - myfish.getOffsetX()),
                (myfish.getYPos() - myfish.getOffsetY()),
                (myfish.getXPos() - myfish.getOffsetX()),
                (myfish.getYPos() + myfish.getOffsetY()));
        // right
        drawLine((myfish.getXPos() + myfish.getOffsetX()),
                (myfish.getYPos() - myfish.getOffsetY()),
                (myfish.getXPos() + myfish.getOffsetX()),
                (myfish.getYPos() + myfish.getOffsetY()));

        changeColor(255,0,0);
        double fishX;
        double fishY = myfish.getYPos();
        if (myfish.getFacingLeft()) { fishX = myfish.getXPos() - myfish.getOffsetX(); }
        else { fishX = myfish.getXPos() + myfish.getOffsetX(); }
        drawCircle(fishX, fishY, 2);
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
            double fishXSpeed = myfish.getXVel();
            double fishYSpeed = myfish.getYVel();
            if (pearl.updatepearl(dt,
                    myfish.getmyfishRec(),
                    fishXSpeed,
                    fishYSpeed,
                    rand(areaW),
                    rand(areaH),
                    spawnOffsetWidth,
                    spawnOffsetHeight)) {

                // Play bite sfx
                playAudioSFX(1, 1.0f);
                // Play pop sfx
                playAudioSFX(7, 1.0f);

                double speedVal = 25.0;
                myfish.incrementMaxSpeed(speedVal);
                myfish.incrementAccelerationSpeed(speedVal);
            }

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

                // Play bite sound
                playAudioSFX(1, 1.0f);
                // Play power up sfx
                playAudioSFX(6, 1.0f);

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

                // Play explosion sfx
                playAudioSFX(10, 1.2f);

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
        enemyWidth = 64;
        enemyHeight = 32;

        try {
            enemyFish = new Image[enemyTypes];
            enemyFish[0] = loadImage(assetPathImage + "entity/entity_fish1_x1.png");
            enemyFish[1] = loadImage(assetPathImage + "entity/entity_fish2_x1.png");
            enemyFish[2] = loadImage(assetPathImage + "entity/entity_fish3.png");
            shark = loadImage(assetPathImage + "entity/entity_shark1.png");

        } catch(Exception e) {
//            e.printStackTrace();
            System.out.println("Could not find enemy images. Skipping loading the images.");
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
        if (en.getHeadingX() == -1.0) { ex = en.getXPos() - en.getColliderHeadXOffset(); }
        else { ex = en.getXPos() + en.getColliderHeadXOffset(); }

        if (calcDistPointToSquare(ex,
                (en.getYPos() + en.getColliderHeadYOffset()),
                (myfish.getXPos() - myfish.getOffsetX()),
                (myfish.getYPos() - myfish.getOffsetY()),
                myfish.getWidth(),
                myfish.getHeight())) {

            if (en.getSize() > myfish.getSize()) {
                // Play bite sfx
                if (myfish.getIsAlive()) { playAudioSFX(0, 1.0f); }

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
            else { en.setXHeading(1.0); }
        }
        if ((en.getXPos() >= (playAreaXOffset + env.getPlayAreaWidth() + bodyLengthOffset)) && (en.getHeadingX() == 1.0)) {
            if (removeEnemy) { removalValues.add(en); }
            else { en.setXHeading(-1.0); }
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

    public void removeAllEnemies(ArrayList<Enemy> enemies, ArrayList<Enemy> sharkEnemies) {
        enemies.clear();
        sharkEnemies.clear();
    }

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
        double imgOX = en.getXPos();
        double imgOY = en.getYPos();
        double imgX = en.getImageXOffset();
        double imgY = en.getImageYOffset();
        double imgLen = en.getImageLength();
        double imgHei = en.getImageHeight();
        Image enImg = en.getImage();

        // Draw the enemy image
        if (en.getHeadingX() == -1.0) {
            drawImage(enImg,
                    (imgOX + imgX),
                    (imgOY - imgY),
                    -imgLen,
                    imgHei);
        } else {
            drawImage(enImg,
                    (imgOX - imgX),
                    (imgOY - imgY),
                    imgLen,
                    imgHei);
        }
    }

    // Debug: Used to show the enemy body colliders
    public void drawEnemyCollider(Enemy en) {
        // Retrieve the enemy collision fields
        double xPos = en.getXPos();
        double yPos = en.getYPos();
        double xPosOffset = en.getColliderBodyXOffset();
        double yPosOffset = en.getColliderBodyYOffset();

        // Calculate the collision offsets
        double x1 = xPos - xPosOffset;
        double y1 = yPos - yPosOffset;
        double x2 = xPos + xPosOffset;
        double y2 = yPos + yPosOffset;

        // Draw the collision boxes
        changeColor(0, 255, 0);
        drawLine(x1,y1,x2,y1); // Collision line
        drawLine(x1,y2,x2,y2); // Collision line
        drawLine(x1,y1,x1,y2); // Collision line
        drawLine(x2,y1,x2,y2); // Collision line

        // Draw the origin
        changeColor(255, 0, 0);
        drawSolidCircle(xPos, yPos, 2);
    }

    // Debug: Used to show the enemy head colliders
    public void drawEnemyHeadCollider(Enemy en) {
        double x = en.getXPos();
        double y = en.getYPos() + en.getColliderHeadYOffset();
        double xH = en.getColliderHeadXOffset();

        double x1;
        if (en.getHeadingX() == -1.0) { x1 = x - xH; }
        else { x1 = x + xH; }

        changeColor(255,0,0);
        drawCircle(x1, y, 2);
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
            en.setHeadYOffset(10);

        } else if (selectSize < enemyMedium) { // spawn a medium fish
            size = 1;
            en = new Enemy(enemyFish[1], size, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            en.setHeadYOffset(0);

        } else { // spawn a large fish
            size = 2;
            en = new Enemy(enemyFish[2], size, playAreaXOffset, playAreaYOffset, playAreaWidth, playAreaHeight);
            en.setHeadYOffset(0);

        }
        enemies.add(en);
    }

    public void createShark() {
        if (sharkEnemies.size() >= maxShark) { return; }

        // The shark should be the largest size
        Enemy shk = new Enemy(shark, 3, env.getEnvironmentXOffset(), env.getEnvironmentYOffset(), env.getPlayAreaWidth(), env.getPlayAreaHeight());

        // The shark should be very fast, ie (400 + range(400))
        shk.setRandomVelocity(400, 400);
        shk.setYHeading(0.0); // Don't move vertically (unless spawning close to the top or bottom)
        shk.setChanceToLeaveEnvironment(50); // The shark will always leave the environment
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
    public void updateEnvironment(double dt) {
        if (gameState == 4) {
            // Check if the level is complete
            env.environmentLevelCompleteCheck();

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
        }
    }

    public void resetGameLevel() {
        // Remove all enemies from the level
        removeAllEnemies(enemies, sharkEnemies);

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
        myfish.setYPos(myfish.getHeight() * 3);
        myfish.setXVel(0);
        myfish.setYVel(175);
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

    public void finalReset() {
        env.setScore(0);
        env.setEnemyEatenCounter();
        pearl.setTimesEaten(0);
        starfish.setTimesEaten(0);
        myfish.setSize(0);
        myfish.updateSize();
        myfish.setYVel(175);
    }

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
        if (gameState == 4) { env.drawEnvironment(); }
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
