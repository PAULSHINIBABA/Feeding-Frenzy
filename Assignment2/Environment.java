/*
 * Author: Robert Tubman
 * ID: 11115713
 *
 * The Environment Class
 *
 * The Environment Class is used to instantiate a level for the game.
 * The Environment Class contains fields for:
 * - The current in-game level score
 * - The current in-game level growth
 * - The current value in the in-game level current goal
 * - The current value in the in-game level target goal
 */
package Assignment2;

import javax.swing.*;
import java.awt.*;

public class Environment {
    // Player growth related ==============================
    private int defaultGrowthThresholdM = 1;
    private int defaultGrowthThresholdL = 2;
    private int defaultTargetGoal = 3;
    private int requiredGrowth;
    private int growthThresholdMedium;
    private int growthThresholdLarge;
    // The growth is determined by the number of enemy fish eaten by the player fish.
    // Every fish eaten = 1 growth point
    private int currentGoal;
    private int targetGoal;

    // Level related ==============================
    private int currentLevel;
    private int maxLevel;
    private double timer;
    private double baseTime;
    private double pausableTimer;
    private double countdownTimer;
    private double countdownTimerCurrent;
    private double countdownTimeOffset;
    private double timeAttackLevel;
    private double minTimeAttackLevel;
    private double defaultTimeAttackLevel;
    private boolean isPaused;
    private boolean isTimeAttack;
    private boolean levelComplete;
    private boolean endLevel;

    // Player score related ==============================
    // The score is determined by the number of enemy fish eaten by the player fish.
    // 1 small fish = 1 point
    // 1 medium fish = 2 points
    // 1 large fish = 3 points
    private int currentScore;
    private int enemyTypes;
    private int[] enemiesEaten;

    // System related ==============================
    private GameEngine engine;
    // The max images (equivalent to levels)
    private int levelImageCap;
    // An array for the level backgrounds
    private Image[] levelImages;
    // The image parameters are for defining the background image location and size
//    private double imageOriginX;
//    private double imageOriginY;
//    private double imageYOffset;
//    private double imageXOffset;
//    private double imageWidth;
//    private double imageHeight;
    // The hud parameters are for the feedback on player progress, tied to the screen width and height
    private double hudWidth;
    private double hudHeight;
    private double hudOffsetX;
    private double hudOffsetY;
    private int hudFontSize;
    private double hudBarThickness;
    private double hudInfoSpacing;
    // The play area (which should be renamed) is the area which is visible to the player
    private double visibleAreaOffsetX; // left side offset
    private double visibleAreaOffsetY; // top side offset
    private double visibleAreaCOMX;
    private double visibleAreaCOMY;
    private double visibleAreaWidth;
    private double visibleAreaHeight;
    private boolean restartEnvironment;
    private boolean hardMode;
    // The global play area is the actual environment that the player can move around in
    private double globalPlayAreaCOMX;
    private double globalPlayAreaCOMY;
    private double globalPlayAreaOffsetX;
    private double globalPlayAreaOffsetY;
    private double globalPlayAreaWidth;
    private double globalPlayAreaHeight;
    private double currentPlayerGlobalX;
    private double currentPlayerGlobalY;
    // Revised Global<->Local coordinate fields ==============================
    // Window fields
    private double windowWidth;
    private double windowHeight;
    private double windowCOMX;
    private double windowCOMY;
    private double windowOffsetX;
    private double windowOffsetY;
    // Global fields
    private double globalWidth;
    private double globalHeight;
    private double globalCOMX;
    private double globalCOMY;
    private double globalOffsetX;
    private double globalOffsetY;
    // Offset between global and window
    private double windowToGlobalCOMOffsetX;
    private double windowToGlobalCOMOffsetY;
    private double environmentBoundaryX1;
    private double environmentBoundaryX2;
    private double environmentBoundaryY1;
    private double environmentBoundaryY2;

    // The Environment constructor
    public Environment(GameEngine engine, Image[] levelImages, boolean isTimeAttack) {
        this.engine = engine;
        levelImageCap = levelImages.length;
        this.levelImages = levelImages;

        hudWidth = 10.0; // Arbitrary default HUD width
        hudHeight = 80.0; // Arbitrary default HUD height
        hudOffsetX = 10.0; // Offset x for displaying text in the hud
        hudOffsetY = 20.0; // Offset y for displaying text in the hud
        hudFontSize =  12;
        hudBarThickness = 12.0;
        hudInfoSpacing = 2.0;

        windowWidth = this.engine.width();
        windowHeight = this.engine.height();
        windowCOMX = windowWidth / 2.0;
        windowCOMY = windowHeight / 2.0;
        windowOffsetX = windowWidth / 2.0;
        windowOffsetY = windowHeight / 2.0;
        globalWidth = 900;
        globalHeight = (globalWidth / 4) * 3;
        globalCOMX = globalWidth / 2.0;
        globalCOMY = globalHeight / 2.0;
        globalOffsetX = globalWidth / 2.0;
        globalOffsetY = globalHeight / 2.0;
        // The player coordinate on global coordinate
        windowToGlobalCOMOffsetX = 0.0;
        windowToGlobalCOMOffsetY = 0.0;

        // Set the local (visible) play area dimensions
        visibleAreaWidth = this.engine.width() - (2 * hudWidth);
        visibleAreaHeight = this.engine.height() - (hudHeight - hudWidth); // hudHeight is from the top, hudWidth is taken from the bottom
        // Set the (visible) play area centre of mass
        visibleAreaCOMX = (visibleAreaWidth / 2.0) + hudWidth;
        visibleAreaCOMY = (visibleAreaHeight / 2.0) + hudHeight;
        // Set the (visible) play area offsets from origin
//        visibleAreaOffsetX = (visibleAreaWidth / 2.0);
//        visibleAreaOffsetY = (visibleAreaHeight / 2.0);
        visibleAreaOffsetX = (visibleAreaWidth / 2.0);
        visibleAreaOffsetY = (visibleAreaHeight / 2.0);

        // Set the global play area dimensions
        globalPlayAreaWidth = 1500; // Arbitrary // TODO: set this in the settings page
        globalPlayAreaHeight = 1000; // Arbitrary // TODO: set this in the settings page
        // Origin center
        globalPlayAreaCOMX = globalPlayAreaWidth / 2.0;
        globalPlayAreaCOMY = globalPlayAreaHeight / 2.0;
        // Image display offsets from origin
        globalPlayAreaOffsetX = (globalPlayAreaWidth / 2.0);
        globalPlayAreaOffsetY = (globalPlayAreaHeight / 2.0);
        // The coordinates of the player in global space
        currentPlayerGlobalX = globalPlayAreaCOMX; // Initially at global origin x
        currentPlayerGlobalY = globalPlayAreaCOMY; // Initially at global origin y

        environmentBoundaryX1 = 0.0;
        environmentBoundaryX2 = 0.0;
        environmentBoundaryY1 = 0.0;
        environmentBoundaryY2 = 0.0;

        // Set the image fields
//        imageOriginX = hudWidth + (visibleAreaWidth / 2.0);
//        imageOriginY = hudHeight + (visibleAreaHeight / 2.0);
//        imageWidth = visibleAreaWidth;
//        imageHeight = visibleAreaHeight;
//        imageXOffset = imageOriginX - (visibleAreaWidth / 2.0);
//        imageYOffset = imageOriginY - (visibleAreaHeight / 2.0);

        setGrowthThresholdLarge(0);
        setGrowthThresholdMedium(0);
        setTargetGoal(0);
        endLevel = false;
        restartEnvironment = false;
        enemyTypes = 3;
        setEnemyEatenCounter();
        hardMode = false;

        isPaused = true;
        levelComplete = false;
        currentScore = 0;
        currentGoal = 0;
        this.isTimeAttack = isTimeAttack;
        countdownTimer = 60.0; // Time attack default is a minute
        countdownTimerCurrent = 0.0;
        countdownTimeOffset = 0.0;
        timer = 0.0;
        pausableTimer = 0.0;

        initEnvironment();
    }

    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void setEnvironmentImage(Image levelImage, int index) throws IllegalArgumentException {
        if (levelImage == null) { throw new IllegalArgumentException("Cannot set the environment image to null"); }
        if ((index < 0) || (index >= levelImageCap)) { throw new IllegalArgumentException("Index to set image is out of range"); }
        levelImages[index] = levelImage;
    }
//    public void setEnvironmentWidth(int width) { imageWidth = width; }
//    public void setEnvironmentHeight(int height) { imageHeight = height; }
//    public void setEnvironmentOriginX(double x) { imageOriginX = x; }
//    public void setEnvironmentOriginY(double y) { imageOriginY = y; }
//    public void setEnvironmentXOffset(double x) { imageXOffset = x; }
//    public void setEnvironmentYOffset(double y) { imageYOffset = y; }
    public void setEnvironmentPlayWidth(int visibleAreaWidth) throws IllegalArgumentException {
        if (visibleAreaWidth < 0) { throw new IllegalArgumentException("Cannot set the play area width to a negative integer"); }
        this.visibleAreaWidth = visibleAreaWidth - (2 * hudWidth);
    }
    public void setEnvironmentPlayHeight(int visibleAreaHeight) throws IllegalArgumentException {
        if (visibleAreaHeight < 0) { throw new IllegalArgumentException("Cannot set the play area height to a negative integer"); }
        this.visibleAreaHeight = visibleAreaHeight - hudHeight - hudWidth;
    }
    // Set the score to an integer value. The score cannot be negative.
    public void setScore(int score) throws IllegalArgumentException {
        if (score < 0) { throw new IllegalArgumentException("Cannot set score to a negative integer"); }
        currentScore = score;
    }

    // Set the medium growth threshold to an integer value. The medium growth threshold cannot be negative.
    public void setGrowthThresholdMedium(int mediumThreshold) throws IllegalArgumentException {
        if (mediumThreshold < 0) { throw new IllegalArgumentException("Cannot set growth to a negative integer"); }
        if (mediumThreshold > growthThresholdLarge) { throw new IllegalArgumentException("The growth threshold medium cannot be greater than the growth threshold large"); }

        if (mediumThreshold == 0) { growthThresholdMedium = defaultGrowthThresholdM; }
        else { growthThresholdMedium = mediumThreshold; }
    }

    // Set the large growth threshold to an integer value. The large growth threshold cannot be negative.
    public void setGrowthThresholdLarge(int largeThreshold) throws IllegalArgumentException {
        if (largeThreshold < 0) { throw new IllegalArgumentException("Cannot set growth to a negative integer"); }
        if (largeThreshold < growthThresholdMedium) { throw new IllegalArgumentException("The growth threshold large cannot be less than the growth threshold medium"); }

        if (largeThreshold == 0) { growthThresholdLarge = defaultGrowthThresholdL; }
        else { growthThresholdLarge = largeThreshold; }
    }

    // Set the current goal to an integer value. The current goal cannot be negative.
    // currentGoal / targetGoal gives us the progression for the current level
    public void setCurrentGoal(int cGoal) throws IllegalArgumentException {
        if (cGoal < 0) { throw new IllegalArgumentException("Cannot set current goal to a negative integer"); }
        currentGoal = cGoal;
    }

    // Set the target goal to an integer value. The target goal cannot be negative.
    public void setTargetGoal(int tGoal) throws IllegalArgumentException {
        if (tGoal < 0) { throw new IllegalArgumentException("Cannot set target goal to a negative integer"); }

        if (tGoal == 0) { targetGoal = defaultTargetGoal; }
        else { targetGoal = tGoal; }
    }

    public void setIsPaused(boolean paused) { isPaused = paused; }
    public void setPlayAreaOffsetX(int x) { visibleAreaOffsetX = x; }
    public void setPlayAreaOffsetY(int y) { visibleAreaOffsetY = y; }
//    public void setPlayAreaWidth(int width) { visibleAreaWidth = width; }
//    public void setPlayAreaHeight(int height) { visibleAreaHeight = height; }
    public void setHUDWidth(int width) { hudWidth = width; }
    public void setHUDHeight(int height) { hudHeight = height; }
    public void setTimer(double newTime) { timer = (newTime - baseTime) / 1000.0; }
    public void setBaseTime(double baseTime) { this.baseTime = baseTime; }
    public void setIsTimeAttack(boolean isTimeAttack) { this.isTimeAttack = isTimeAttack; }
    public void setCountDownTimer(double timer) { countdownTimer = timer; }
    public void setCountDownCurrentTimer(double timer) { countdownTimerCurrent = timer; }
//    public void setCountDownTimerOffset(double timer) { countdownTimeOffset = timer; }
    public void setPausableTimer(double timer) { pausableTimer = timer; }
    public void setEndLevel() {
        Timer timer = new Timer(600, e->endLevel = true);
        timer.setRepeats(false);
        timer.start();
        setRestartLevel(true);
    }
    public void setRestartLevel(boolean state) { restartEnvironment = state; }
    public void setEnemyTypes(int enemyTypes, boolean reset) {
        this.enemyTypes = enemyTypes;
        if (reset) { setEnemyEatenCounter(); }
    }
    public void setEnemyEatenCounter() {
        enemiesEaten = new int[enemyTypes];
        for (int i = 0; i < enemyTypes; i++) { enemiesEaten[i] = 0; }
    }
    public void setEatEnemyBySize(int size) throws IllegalArgumentException {
        if (size < 0 || size >= enemyTypes) { throw new IllegalArgumentException("Cannot eat a enemy of size that doesn't exist"); }
        enemiesEaten[size] += 1;
    }
    public void setCurrentLevel(int level) { currentLevel = level; }
    public void setCurrentRequiredGrowth(int requiredGrowth) { this.requiredGrowth = requiredGrowth; }
    public void setMaxLevel(int maxLevel) { this.maxLevel = maxLevel; }
    public void setCurrentTimeAttackLevel(double timeAttackLevel) { this.timeAttackLevel = timeAttackLevel; }
    public void setMinTimeAttackLevel(double minTimeAttackLevel) { this.minTimeAttackLevel = minTimeAttackLevel; }
    public void setDefaultTimeAttackLevel(double defaultTimeAttackLevel) { this.defaultTimeAttackLevel = defaultTimeAttackLevel; }
    public void setHardMode(boolean mode) { hardMode = mode; }
    public void setIsLevelComplete(boolean state) { levelComplete = state; }
    public void setEndLevel(boolean state) { endLevel = state; }
    public void setGlobalPlayAreaCOMX(double x) { globalPlayAreaCOMX = x; }
    public void setGlobalPlayAreaCOMY(double y) { globalPlayAreaCOMY = y; }
    public void setGlobalPlayAreaOffsetX(double x) { globalPlayAreaOffsetX = x; }
    public void setGlobalPlayAreaOffsetY(double y) { globalPlayAreaOffsetY = y; }
    public void setGlobalPlayAreaWidth(double w) { globalPlayAreaWidth = w; }
    public void setGlobalPlayAreaHeight(double l) { globalPlayAreaHeight = l; }
    public void setCurrentPlayerGlobalX(double px) { currentPlayerGlobalX = px; }
    public void setCurrentPlayerGlobalY(double py) { currentPlayerGlobalY = py; }
    public void setVisibleAreaCOMX(double x) { visibleAreaCOMX = x; }
    public void setVisibleAreaCOMY(double y) { visibleAreaCOMY = y; }

    public void setWindowCOMX(double x) { windowCOMX = x; }
    public void setWindowCOMY(double y) { windowCOMY = y; }
    public void setWindowWidth(double width) { windowWidth = width; }
    public void setWindowHeight(double height) { windowHeight = height; }
    public void setGlobalOffsetX(double x) { globalOffsetX = x; }
    public void setGlobalOffsetY(double y) { globalOffsetY = y; }
    public void setGlobalCOMX(double x) { globalCOMX = x; }
    public void setGlobalCOMY(double y) { globalCOMY = y; }
    public void setGlobalWidth(double width) { globalWidth = width; }
    public void setGlobalHeight(double height) { globalHeight = height; }
    public void setWindowToGlobalOriginOffsetX(double originOffset) { windowToGlobalCOMOffsetX = originOffset; }
    public void setWindowToGlobalOriginOffsetY(double originOffset) { windowToGlobalCOMOffsetY = originOffset; }


    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public int getScore() { return currentScore; }
    public int getGrowthThresholdMedium() { return growthThresholdMedium; }
    public int getGrowthThresholdLarge() { return growthThresholdLarge; }
    public int getCurrentGoal() { return currentGoal; }
    public int getTargetGoal() { return targetGoal; }
//    public Image getEnvironment() { return levelImage; }
    public Image getEnvironmentAtIndex(int index) throws IllegalArgumentException {
        if ((index < 0) || (index >= levelImageCap)) { throw new IllegalArgumentException("The index to get the image is out of range"); }
        return levelImages[index];
    }
//    public double getEnvironmentWidth() { return imageWidth;}
//    public double getEnvironmentHeight() { return imageHeight; }
//    public double getEnvironmentOriginX() { return imageOriginX; }
//    public double getEnvironmentOriginY() { return imageOriginY; }
//    public double getEnvironmentXOffset() { return imageXOffset; }
//    public double getEnvironmentYOffset() { return imageYOffset; }
    public boolean getIsPaused() { return isPaused; }
    public double getHUDWidth() { return hudWidth; }
    public double getHUDHeight() { return hudHeight; }
    public double getPlayAreaOffsetX() { return visibleAreaOffsetX; }
    public double getPlayAreaOffsetY() { return visibleAreaOffsetY; }
    public double getPlayAreaWidth() { return visibleAreaWidth; }
    public double getPlayAreaHeight() { return visibleAreaHeight; }
    public double getTimer() { return round2DP(timer); }
    public double getBaseTime() { return round2DP(baseTime); }
    public boolean getIsTimeAttack() { return isTimeAttack; }
    public double getCountDownTimer() { return round2DP(countdownTimer); }
    public double getCountDownCurrentTimer() { return round2DP(countdownTimerCurrent); }
    public double getCountDownCurrentTimerPercentage() { return round2DP((countdownTimerCurrent / countdownTimer)); }
    public double getCountDownTimerWOffset() {
        double theTime = getTimer();
        double theOffset = countdownTimeOffset;
        if (theOffset > theTime) { return round2DP(theTime); }
        else { return round2DP((theTime - theOffset)); }
    }
    public double getPausableTimer() { return pausableTimer; }
    public boolean getIsLevelComplete() { return levelComplete; }
    public boolean getEndLevel() { return endLevel; }
    public boolean getRestartLevel() { return restartEnvironment; }
    public int getEnemyTypes() { return enemyTypes; }
    public int[] getEnemiesEaten() { return enemiesEaten; }
    public int getEnemyEatenBySize(int size) throws IllegalArgumentException {
        if (size < 0 || size >= enemyTypes) { throw new IllegalArgumentException("Cannot eat a enemy of size that doesn't exist"); }
        return enemiesEaten[size];
    }
    public int getCurrentLevel() { return currentLevel; }
    public int getRequiredGrowth() { return requiredGrowth; }
    public int getMaxLevel() { return maxLevel; }
    public double getCurrentTimeAttackLevel() { return timeAttackLevel; }
    public double getMinTimeAttackLevel() { return minTimeAttackLevel; }
    public double getDefaultTimeAttackLevel() { return defaultTimeAttackLevel; }
    public boolean getHardMode() { return hardMode; }
    public double getGlobalPlayAreaCOMX() { return globalPlayAreaCOMX; }
    public double getGlobalPlayAreaCOMY() { return globalPlayAreaCOMY; }
    public double getGlobalPlayAreaOffsetX() { return globalPlayAreaOffsetX; }
    public double getGlobalPlayAreaOffsetY() { return globalPlayAreaOffsetY; }
    public double getGlobalPlayAreaWidth() { return globalPlayAreaWidth; }
    public double getGlobalPlayAreaHeight() { return globalPlayAreaHeight; }
    public double getCurrentPlayerGlobalX() { return currentPlayerGlobalX; }
    public double getCurrentPlayerGlobalY() { return currentPlayerGlobalY; }
    public double getVisibleAreaCOMX() { return visibleAreaCOMX; }
    public double getVisibleAreaCOMY() { return visibleAreaCOMY; }

    public double getWindowCOMX() { return windowCOMX; }
    public double getWindowCOMY() { return windowCOMY; }
    public double getWindowWidth() { return windowWidth; }
    public double getWindowHeight() { return windowHeight; }
    public double getGlobalCOMX() { return globalCOMX; }
    public double getGlobalCOMY() { return globalCOMY; }
    public double getGlobalOffsetX() { return globalOffsetX; }
    public double getGlobalOffsetY() { return globalOffsetY; }
    public double getGlobalWidth() { return globalWidth; }
    public double getGlobalHeight() { return globalHeight; }
    public double getWindowToGlobalOriginOffsetX() { return windowToGlobalCOMOffsetX; }
    public double getWindowToGlobalOriginOffsetY() { return windowToGlobalCOMOffsetY; }
    public double getBoundaryX1() { return environmentBoundaryX1; }
    public double getBoundaryX2() { return environmentBoundaryX2; }
    public double getBoundaryY1() { return environmentBoundaryY1; }
    public double getBoundaryY2() { return environmentBoundaryY2; }


    //-------------------------------------------------------
    // Other Methods
    //-------------------------------------------------------
    public void initEnvironment() { setBaseTime(engine.getTime()); }
    public void updateEnvironment(double dt) {
        environmentBoundaryX1 = visibleAreaCOMX + windowToGlobalCOMOffsetX;
        environmentBoundaryX2 = visibleAreaCOMX + windowToGlobalCOMOffsetX + globalWidth;
        environmentBoundaryY1 = visibleAreaCOMY + windowToGlobalCOMOffsetY;
        environmentBoundaryY2 = visibleAreaCOMY + windowToGlobalCOMOffsetY + globalHeight;
    }
    public void drawEnvironment() {
        engine.changeBackgroundColor(0,0,0);
        engine.clearBackground((int)globalPlayAreaWidth, (int)globalPlayAreaHeight);
        engine.saveCurrentTransform();

        // Select the image based on current level
        Image tempImage;
        if (!isTimeAttack) {
            if (requiredGrowth > ((maxLevel / 3) * 2)) {
                // Last third of levels
                tempImage = levelImages[2];
            } else if (requiredGrowth > (maxLevel / 3)) {
                // Second third of levels
                tempImage = levelImages[0];
            } else {
                tempImage = levelImages[1];
            }
        } else {
            if (timeAttackLevel < ((defaultTimeAttackLevel - minTimeAttackLevel) / 3)) {
                // Last third of levels
                tempImage = levelImages[2];
            } else if (timeAttackLevel < (((defaultTimeAttackLevel - minTimeAttackLevel) / 3) * 2)) {
                // Second third of levels
                tempImage = levelImages[0];
            } else {
                tempImage = levelImages[1];
            }
        }

        // Draw the level image
        engine.drawImage(tempImage,
                visibleAreaCOMX + windowToGlobalCOMOffsetX,
                visibleAreaCOMY + windowToGlobalCOMOffsetY,
                globalWidth,
                globalHeight);

        drawEnvironmentCollider();

        engine.restoreLastTransform();
    }
    public void drawHUD() {
        double hudWidth = getHUDWidth();
        double hudHeight = getHUDHeight();

        int whiteOutValue = 100;
        engine.changeColor(whiteOutValue,whiteOutValue,255);
        // HUD left
        engine.drawSolidRectangle(0,0, hudWidth, engine.height());
        // HUD Right
        engine.drawSolidRectangle((engine.width() - hudWidth),0, hudWidth, engine.height());
        // HUD bottom
        engine.drawSolidRectangle(0,(engine.height() - hudWidth), engine.width(), hudWidth);
        // HUD top
        engine.drawSolidRectangle(0,0, engine.width(), hudHeight);
    }

    public void drawImageShouldBeSize() {
        double areaX = engine.width() - (2 * hudWidth);
        double areaY = engine.height() - (hudHeight + hudWidth);
        double x = hudWidth;
        double y = hudHeight;
        double levelOriginX = x + (areaX / 2.0);
        double levelOriginY = y + (areaY / 2.0);

        double x1 = levelOriginX - (globalWidth / 2.0);
        double y1 = levelOriginY - (globalHeight / 2.0);
        double x2 = levelOriginX + (globalWidth / 2.0);
        double y2 = levelOriginY + (globalHeight / 2.0);

        engine.changeColor(255,0,255);
        engine.drawLine(x1, y1, x2, y1);
        engine.drawLine(x1, y2, x2, y2);
        engine.drawLine(x1, y1, x1, y2);
        engine.drawLine(x2, y1, x2, y2);

    }
    public void drawEnvironmentCollider() {
//        visibleAreaCOMX + windowToGlobalCOMOffsetX,
//                visibleAreaCOMY + windowToGlobalCOMOffsetY,
//                globalWidth,
//                globalHeight);
        double x1 = visibleAreaCOMX + windowToGlobalCOMOffsetX;
        double y1 = visibleAreaCOMY + windowToGlobalCOMOffsetY;
        double x2 = visibleAreaCOMX + windowToGlobalCOMOffsetX + globalWidth;
        double y2 = visibleAreaCOMY + windowToGlobalCOMOffsetY + globalHeight;

        engine.changeColor(0,255,255);
        engine.drawLine(x1,y1,x2,y1);
        engine.drawLine(x1,y2,x2,y2);
        engine.drawLine(x1,y1,x1,y2);
        engine.drawLine(x2,y1,x2,y2);
    }
    public void drawTimer(boolean isTimeAttack) {
        double displayTime = getCountDownTimerWOffset();
        int clr = 0;
        engine.changeColor(clr,clr,clr);
        engine.drawText(10, 20, ("Time: " + Double.toString(displayTime)), "Sans Serif", 12);

        if (isTimeAttack) {
            drawTimeAttackBar();
        }
    }
    public void drawScore() {
        int clr = 0;
        engine.changeColor(clr,clr,clr);
        engine.drawText(hudOffsetX, (hudOffsetY + hudFontSize + hudInfoSpacing), ("Score: " + Double.toString(getScore())), "Sans Serif", hudFontSize);
    }
    public void drawGrowth() {
        double growthTarget = getTargetGoal();
        double growthM = getGrowthThresholdMedium();
        double growthL = getGrowthThresholdLarge();

        double growthBarLength = engine.width() * 0.6;
        double growthBarXOffset = (engine.width() - growthBarLength) / 2.0;
        double growthBarYOffset = hudOffsetY;

        double gSS = ((growthBarLength / growthTarget) * (growthM));
        double gSM = ((growthBarLength / growthTarget) * (growthL - growthM));
        double gSL = ((growthBarLength / growthTarget) * (growthTarget - growthL));

        // draw the small size
        int dark = 150;
        int lighter = 175;
        int light = 200;
        int basic = 120;
        engine.changeColor(basic,dark,basic);
        engine.drawSolidRectangle(growthBarXOffset, growthBarYOffset, gSS, hudBarThickness);

        // draw the medium size
        double nextBar = growthL - growthM;
        engine.changeColor(basic,lighter,basic);
        engine.drawSolidRectangle(growthBarXOffset + gSS, growthBarYOffset, gSM, hudBarThickness);

        // draw the large size
        double finalBar = growthTarget - growthL;
        engine.changeColor(basic,light,basic);
        engine.drawSolidRectangle(growthBarXOffset + gSS + gSM, growthBarYOffset, gSL, hudBarThickness);

        // Draw the current growth
        double growthState = ((growthBarLength / growthTarget) * (getCurrentGoal()));
        if (growthState >= growthBarLength) { growthState = growthBarLength; }
        engine.changeColor(0,255,0);
        engine.drawSolidRectangle(growthBarXOffset, growthBarYOffset, growthState, hudBarThickness);

        // draw the growth bar borders
        drawPanelBorder(growthBarXOffset, growthBarYOffset, growthBarLength, hudBarThickness);
    }
    public void drawTimeAttackBar() {
        double timeAttackLimit = getCountDownTimer();
        double timeAttackRemaining = getCountDownCurrentTimer();

        double timeAttackBarLength = engine.width() * 0.6;
        double timeAttackBarXOffset = (engine.width() - timeAttackBarLength) / 2.0;
        double timeAttackBarYOffset = hudOffsetY * 2;

        // draw the small size
        int dark = 120;
        int light = 240;
        int basic = 50;
        engine.changeColor(light,basic,basic);
        engine.drawSolidRectangle(timeAttackBarXOffset, timeAttackBarYOffset, timeAttackBarLength, hudBarThickness);

        // Draw the current remaining time
        double remainingTime = ((timeAttackBarLength / timeAttackLimit) * timeAttackRemaining);
        if (remainingTime >= timeAttackBarLength) { remainingTime = timeAttackBarLength; }

        engine.changeColor(dark,basic,basic);
        engine.drawSolidRectangle(timeAttackBarXOffset, timeAttackBarYOffset, remainingTime, hudBarThickness);

        // draw the time attack borders
        drawPanelBorder(timeAttackBarXOffset, timeAttackBarYOffset, timeAttackBarLength, hudBarThickness);
    }
    public void drawPanelBorder(double x, double y, double l, double h) {
        int clr = 0;
        engine.changeColor(clr,clr,clr);
        int thickness = 2;

        engine.drawLine(x,y,x+l,y,thickness);
        engine.drawLine(x,y+h,x+l,y+h,thickness);
        engine.drawLine(x,y,x,y+h,thickness);
        engine.drawLine(x+l,y,x+l,y+h,thickness);
    }
    public void drawCurrentToTargetGoal() {
        engine.changeColor(0,0,0);
        engine.drawText(hudOffsetX,
                (hudOffsetY + ((hudFontSize + hudInfoSpacing) * 2)),
                (Integer.toString(currentGoal) + "/" + Integer.toString(targetGoal)),
                "Sans Serif",
                hudFontSize);
    }
    public void resetLevel() {
        currentScore = 0;
        growthThresholdMedium = defaultGrowthThresholdM;
        growthThresholdLarge = defaultGrowthThresholdL;
        currentGoal = 0;
        targetGoal = 0;
    }
    public void addCountDownTimerOffset(double timer) { countdownTimeOffset += timer; }
    public double round2DP(double value) { return Math.round(value * 100.0) / 100.0; }
    public void environmentLevelCompleteCheck() {
        if (isTimeAttack) {
            if (countdownTimerCurrent >= countdownTimer) { levelComplete = true; }
        } else {
            if (currentGoal >= targetGoal) { levelComplete = true; }
        }
    }
    public void addScore(int score) { currentScore += score; }
    public void updateCurrentGlobalPlayerCoordinates(double px, double py) {
        currentPlayerGlobalX = -px;
        currentPlayerGlobalY = -py;
    }
    public void windowToGlobalCOMOffsetX(double x, double y) {
        windowToGlobalCOMOffsetX = -x;
        windowToGlobalCOMOffsetY = -y;
    }
}
