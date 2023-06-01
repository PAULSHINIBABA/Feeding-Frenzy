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
    private double playAreaOffsetX; // left side offset
    private double playAreaOffsetY; // top side offset
    private double playAreaOriginX;
    private double playAreaOriginY;
    private double playAreaWidth;
    private double playAreaHeight;
    private boolean restartEnvironment;
    private boolean hardMode;
    // The global play area is the actual environment that the player can move around in
    private double globalPlayAreaOriginX;
    private double globalPlayAreaOriginY;
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
    private double windowOriginX;
    private double windowOriginY;
    private double windowOffsetX;
    private double windowOffsetY;
    // Global fields
    private double globalWidth;
    private double globalHeight;
    private double globalOriginX;
    private double globalOriginY;
    private double globalOffsetX;
    private double globalOffsetY;
    // Offset between global and window
    private double windowToGlobalOriginOffsetX;
    private double windowToGlobalOriginOffsetY;

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
        windowOriginX = windowWidth / 2.0;
        windowOriginY = windowHeight / 2.0;
        windowOffsetX = windowWidth / 2.0;
        windowOffsetY = windowHeight / 2.0;
        globalWidth = 900;
        globalHeight = (globalWidth / 4) * 3;
        globalOriginX = globalWidth / 2.0;
        globalOriginY = globalHeight / 2.0;
        globalOffsetX = globalWidth / 2.0;
        globalOffsetY = globalHeight / 2.0;
        // The player coordinate on global coordinate
        windowToGlobalOriginOffsetX = 0.0;
        windowToGlobalOriginOffsetY = 0.0;

        // Set the local (visible) play area dimensions
        playAreaWidth = this.engine.width() - (2 * hudWidth);
        playAreaHeight = this.engine.height() - (hudHeight - hudWidth); // hudHeight is from the top, hudWidth is taken from the bottom
        // Set the (visible) play area origin
        playAreaOriginX = (playAreaWidth / 2.0) + hudWidth;
        playAreaOriginY = (playAreaHeight / 2.0) + hudHeight;
        // Set the (visible) play area offsets from origin
//        playAreaOffsetX = (playAreaWidth / 2.0);
//        playAreaOffsetY = (playAreaHeight / 2.0);
        playAreaOffsetX = (this.engine.width() / 2.0);
        playAreaOffsetY = (this.engine.height() / 2.0);

        // Set the global play area dimensions
        globalPlayAreaWidth = 1500; // Arbitrary // TODO: set this in the settings page
        globalPlayAreaHeight = 1000; // Arbitrary // TODO: set this in the settings page
        // Origin center
        globalPlayAreaOriginX = globalPlayAreaWidth / 2.0;
        globalPlayAreaOriginY = globalPlayAreaHeight / 2.0;
        // Image display offsets from origin
        globalPlayAreaOffsetX = (globalPlayAreaWidth / 2.0);
        globalPlayAreaOffsetY = (globalPlayAreaHeight / 2.0);
        // The coordinates of the player in global space
        currentPlayerGlobalX = globalPlayAreaOriginX; // Initially at global origin x
        currentPlayerGlobalY = globalPlayAreaOriginY; // Initially at global origin y

        // Set the image fields
//        imageOriginX = hudWidth + (playAreaWidth / 2.0);
//        imageOriginY = hudHeight + (playAreaHeight / 2.0);
//        imageWidth = playAreaWidth;
//        imageHeight = playAreaHeight;
//        imageXOffset = imageOriginX - (playAreaWidth / 2.0);
//        imageYOffset = imageOriginY - (playAreaHeight / 2.0);

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
    public void setEnvironmentPlayWidth(int playAreaWidth) throws IllegalArgumentException {
        if (playAreaWidth < 0) { throw new IllegalArgumentException("Cannot set the play area width to a negative integer"); }
        this.playAreaWidth = playAreaWidth - (2 * hudWidth);
    }
    public void setEnvironmentPlayHeight(int playAreaHeight) throws IllegalArgumentException {
        if (playAreaHeight < 0) { throw new IllegalArgumentException("Cannot set the play area height to a negative integer"); }
        this.playAreaHeight = playAreaHeight - hudHeight - hudWidth;
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
    public void setPlayAreaOffsetX(int x) { playAreaOffsetX = x; }
    public void setPlayAreaOffsetY(int y) { playAreaOffsetY = y; }
//    public void setPlayAreaWidth(int width) { playAreaWidth = width; }
//    public void setPlayAreaHeight(int height) { playAreaHeight = height; }
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
    public void setEnvironmentGlobalPlayAreaX(double x) { globalPlayAreaOriginX = x; }
    public void setEnvironmentGlobalPlayAreaY(double y) { globalPlayAreaOriginY = y; }
    public void setEnvironmentGlobalPlayAreaOffsetX(double x) { globalPlayAreaOffsetX = x; }
    public void setEnvironmentGlobalPlayAreaOffsetY(double y) { globalPlayAreaOffsetY = y; }
    public void setEnvironmentGlobalPlayAreaWidth(double w) { globalPlayAreaWidth = w; }
    public void setEnvironmentGlobalPlayAreaHeight(double l) { globalPlayAreaHeight = l; }
    public void setCurrentPlayerGlobalX(double px) { currentPlayerGlobalX = px; }
    public void setCurrentPlayerGlobalY(double py) { currentPlayerGlobalY = py; }
    public void setPlayAreaOriginX(double x) { playAreaOriginX = x; }
    public void setPlayAreaOriginY(double y) { playAreaOriginY = y; }

    public void setWindowOriginX(double x) { windowOriginX = x; }
    public void setWindowOriginY(double y) { windowOriginY = y; }
    public void setWindowWidth(double width) { windowWidth = width; }
    public void setWindowHeight(double height) { windowHeight = height; }
    public void setGlobalOffsetX(double x) { globalOffsetX = x; }
    public void setGlobalOffsetY(double y) { globalOffsetY = y; }
    public void setGlobalOriginX(double x) { globalOriginX = x; }
    public void setGlobalOriginY(double y) { globalOriginY = y; }
    public void setGlobalWidth(double width) { globalWidth = width; }
    public void setGlobalHeight(double height) { globalHeight = height; }
    public void setWindowToGlobalOriginOffsetX(double originOffset) { windowToGlobalOriginOffsetX = originOffset; }
    public void setWindowToGlobalOriginOffsetY(double originOffset) { windowToGlobalOriginOffsetY = originOffset; }


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
    public double getPlayAreaOffsetX() { return playAreaOffsetX; }
    public double getPlayAreaOffsetY() { return playAreaOffsetY; }
    public double getPlayAreaWidth() { return playAreaWidth; }
    public double getPlayAreaHeight() { return playAreaHeight; }
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
    public double getEnvironmentGlobalPlayAreaX() { return globalPlayAreaOriginX; }
    public double getEnvironmentGlobalPlayAreaY() { return globalPlayAreaOriginY; }
    public double getEnvironmentGlobalPlayAreaOffsetX() { return globalPlayAreaOffsetX; }
    public double getEnvironmentGlobalPlayAreaOffsetY() { return globalPlayAreaOffsetY; }
    public double getEnvironmentGlobalPlayAreaWidth() { return globalPlayAreaWidth; }
    public double getEnvironmentGlobalPlayAreaHeight() { return globalPlayAreaHeight; }
    public double getCurrentPlayerGlobalX() { return currentPlayerGlobalX; }
    public double getCurrentPlayerGlobalY() { return currentPlayerGlobalY; }
    public double getPlayAreaOriginX() { return playAreaOriginX; }
    public double getPlayAreaOriginY() { return playAreaOriginY; }

    public double getWindowOriginX() { return windowOriginX; }
    public double getWindowOriginY() { return windowOriginY; }
    public double getWindowWidth() { return windowWidth; }
    public double getWindowHeight() { return windowHeight; }
    public double getGlobalOriginX() { return globalOriginX; }
    public double getGlobalOriginY() { return globalOriginY; }
    public double getGlobalOffsetX() { return globalOffsetX; }
    public double getGlobalOffsetY() { return globalOffsetY; }
    public double getGlobalWidth() { return globalWidth; }
    public double getGlobalHeight() { return globalHeight; }
    public double getWindowToGlobalOriginOffsetX() { return windowToGlobalOriginOffsetX; }
    public double getWindowToGlobalOriginOffsetY() { return windowToGlobalOriginOffsetY; }


    //-------------------------------------------------------
    // Other Methods
    //-------------------------------------------------------
    public void initEnvironment() { setBaseTime(engine.getTime()); }
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
//        double ix = (currentPlayerGlobalX + globalPlayAreaOffsetX);
//        double iy = (currentPlayerGlobalY + globalPlayAreaOffsetY);
//        double iw = globalPlayAreaWidth;
//        double ih = globalPlayAreaHeight;
//        engine.drawImage(tempImage,
//                getEnvironmentXOffset(),
//                getEnvironmentYOffset(),
//                getPlayAreaWidth(),
//                getPlayAreaHeight());
//        System.out.println("ix:" + ix + "\tiy:" + iy + "\tiw:" + iw + "\tih:" + ih + "\tgx:" + globalPlayAreaOffsetX + "\tgy:" + globalPlayAreaOffsetY);
//        engine.drawImage(tempImage,
//                ix,
//                iy,
//                iw,
//                ih);
        System.out.print("\twtgx:" + windowToGlobalOriginOffsetX + "\twtgy:" + windowToGlobalOriginOffsetY + "\tgw:" + globalWidth + "\tgy:" + globalHeight);
        engine.drawImage(tempImage,
                playAreaOriginX + windowToGlobalOriginOffsetX,
                playAreaOriginY + windowToGlobalOriginOffsetY,
                globalWidth,
                globalHeight);

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
    public void updateWindowToGlobalOriginOffsets(double x, double y) {
//        windowToGlobalOriginOffsetX = -x + (globalWidth / 2.0);
//        windowToGlobalOriginOffsetY = -y + (globalHeight / 2.0);
//        windowToGlobalOriginOffsetX = -x + (globalWidth);
//        windowToGlobalOriginOffsetY = -y + (globalHeight);
        windowToGlobalOriginOffsetX = -x;
        windowToGlobalOriginOffsetY = -y;
    }
}
