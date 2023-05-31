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
    private int defaultGrowthThresholdM = 1;
    private int defaultGrowthThresholdL = 2;
    private int defaultTargetGoal = 3;
    private int currentLevel;
    private int requiredGrowth;
    private int maxLevel;
    private double timeAttackLevel;
    private double minTimeAttackLevel;
    private double defaultTimeAttackLevel;
    // The score is determined by the number of enemy fish eaten by the player fish.
    // 1 small fish = 1 point
    // 1 medium fish = 2 points
    // 1 large fish = 3 points
    private boolean isPaused;
    private double timer;
    private double baseTime;
    private double pausableTimer;
    private boolean isTimeAttack;
    private double countdownTimer;
    private double countdownTimerCurrent;
    private double countdownTimeOffset;
    private int currentScore;
    // The growth is determined by the number of enemy fish eaten by the player fish.
    // Every fish eaten = 1 growth point
    private int growthThresholdMedium;
    private int growthThresholdLarge;
    private int currentGoal;
    private int targetGoal;
    private int levelImageCap;
    private Image[] levelImages;
    private Image levelImage;
    private double imageOriginX;
    private double imageOriginY;
    private double imageYOffset;
    private double imageXOffset;
    private int imageWidth;
    private int imageHeight;
    private int hudWidth;
    private int hudHeight;
    private int hudOffsetX;
    private int hudOffsetY;
    private int hudFontSize;
    private int hudBarThickness;
    private int hudInfoSpacing;
    private int playAreaX; // left side offset
    private int playAreaY; // top side offset
    private int playAreaWidth;
    private int playAreaHeight;
    private boolean levelComplete;
    private boolean endLevel;
    private boolean canPauseNow;
    private GameEngine engine;
    private boolean restartEnvironment;
    private int enemyTypes;
    private int[] enemiesEaten;
    private boolean hardMode;

    public Environment(GameEngine engine, Image[] levelImages, boolean isTimeAttack) {
        this.engine = engine;
        levelImageCap = levelImages.length;
//        this.levelImages = new Image[levelImageCap];
        this.levelImages = levelImages;

        isPaused = true;
        levelComplete = false;
        canPauseNow = false;
        currentScore = 0;
        currentGoal = 0;
        this.isTimeAttack = isTimeAttack;
        countdownTimer = 60.0; // Time attack default is a minute
        countdownTimerCurrent = 0.0;
        countdownTimeOffset = 0.0;
        timer = 0.0;
        pausableTimer = 0.0;

        hudWidth = 10; // Arbitrary default HUD width
        hudHeight = 80; // Arbitrary default HUD height
        hudOffsetX = 10;
        hudOffsetY = 20;
        hudFontSize =  12;
        hudBarThickness = 12;
        hudInfoSpacing = 2;

        playAreaX = hudWidth;
        playAreaY = hudHeight;
        playAreaWidth = this.engine.width() - (2 * hudWidth);
        playAreaHeight = this.engine.height() - hudHeight - hudWidth; // hudHeight is from the top, hudWidth is taken from the bottom

        imageOriginX = hudWidth + (playAreaWidth / 2.0);
        imageOriginY = hudHeight + (playAreaHeight / 2.0);
        imageWidth = playAreaWidth; // Arbitrary image width
        imageHeight = playAreaHeight; // Arbitrary image height
        imageXOffset = imageOriginX - (playAreaWidth / 2.0);
        imageYOffset = imageOriginY - (playAreaHeight / 2.0);

        setGrowthThresholdLarge(0);
        setGrowthThresholdMedium(0);
        setTargetGoal(0);
        endLevel = false;
        restartEnvironment = false;
        enemyTypes = 3;
        setEnemyEatenCounter();
        hardMode = false;

        initEnvironment();
    }

    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void setEnvironmentImage(Image levelImage, int index) throws IllegalArgumentException {
        if (levelImage == null) { throw new IllegalArgumentException("Cannot set the environment image to null"); }
        if ((index < 0) || (index >= levelImageCap)) { throw new IllegalArgumentException("Index to set image is out of range"); }
//        this.levelImage = levelImage;
        levelImages[index] = levelImage;
    }
    public void setEnvironmentWidth(int width) { imageWidth = width; }
    public void setEnvironmentHeight(int height) { imageHeight = height; }
    public void setEnvironmentOriginX(double x) { imageOriginX = x; }
    public void setEnvironmentOriginY(double y) { imageOriginY = y; }
    public void setEnvironmentXOffset(double x) { imageXOffset = x; }
    public void setEnvironmentYOffset(double y) { imageYOffset = y; }
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

        if (mediumThreshold == 0) {
            growthThresholdMedium = defaultGrowthThresholdM;
        } else {
            growthThresholdMedium = mediumThreshold;
        }
    }

    // Set the large growth threshold to an integer value. The large growth threshold cannot be negative.
    public void setGrowthThresholdLarge(int largeThreshold) throws IllegalArgumentException {
        if (largeThreshold < 0) { throw new IllegalArgumentException("Cannot set growth to a negative integer"); }
        if (largeThreshold < growthThresholdMedium) { throw new IllegalArgumentException("The growth threshold large cannot be less than the growth threshold medium"); }

        if (largeThreshold == 0) {
            growthThresholdLarge = defaultGrowthThresholdL;
        } else {
            growthThresholdLarge = largeThreshold;
        }
    }

    // Set the current goal to an integer value. The current goal cannot be negative.
    // currentGoal / targetGoal gives us the progression for the current level
    public void setCurrentGoal(int cGoal) {
        if (cGoal < 0) { throw new IllegalArgumentException("Cannot set current goal to a negative integer"); }
        currentGoal = cGoal;
    }

    // Set the target goal to an integer value. The target goal cannot be negative.
    public void setTargetGoal(int tGoal) {
        if (tGoal < 0) { throw new IllegalArgumentException("Cannot set target goal to a negative integer"); }

        if (tGoal == 0) {
            targetGoal = defaultTargetGoal;
        } else {
            targetGoal = tGoal;
        }
    }

    public void setIsPaused(boolean paused) { isPaused = paused; }
    public void setPlayAreaX(int x) { playAreaX = x; }
    public void setPlayAreaY(int y) { playAreaY = y; }
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
        if (reset) {
            setEnemyEatenCounter();
        }
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
    public int getEnvironmentWidth() { return imageWidth;}
    public int getEnvironmentHeight() { return imageHeight; }
    public double getEnvironmentOriginX() { return imageOriginX; }
    public double getEnvironmentOriginY() { return imageOriginY; }
    public double getEnvironmentXOffset() { return imageXOffset; }
    public double getEnvironmentYOffset() { return imageYOffset; }
    public boolean getIsPaused() { return isPaused; }
    public int getHUDWidth() { return hudWidth; }
    public int getHUDHeight() { return hudHeight; }
    public int getPlayAreaX() { return playAreaX; }
    public int getPlayAreaY() { return playAreaY; }
    public int getPlayAreaWidth() { return playAreaWidth; }
    public int getPlayAreaHeight() { return playAreaHeight; }
    public double getTimer() { return round2DP(timer); }
    public double getBaseTime() { return round2DP(baseTime); }
    public boolean getIsTimeAttack() { return isTimeAttack; }
    public double getCountDownTimer() { return round2DP(countdownTimer); }
    public double getCountDownCurrentTimer() { return round2DP(countdownTimerCurrent); }
    public double getCountDownCurrentTimerPercentage() { return round2DP((countdownTimerCurrent / countdownTimer)); }
    public double getCountDownTimerWOffset() {
        double theTime = getTimer();
        double theOffset = countdownTimeOffset;
        if (theOffset > theTime) {
            return round2DP(theTime);
        } else {
            return round2DP((theTime - theOffset));
        }
    }
    public double getPausableTimer() { return pausableTimer; }
    public boolean getIsLevelComplete() { return levelComplete; }
    public boolean getEndLevel() { return endLevel; }
    public boolean getRestartLevel() { return restartEnvironment; }
    public int getEnemyTypes() { return enemyTypes; }
    public int[] getEnemiesEaten() { return enemiesEaten; }
    public int getEnemyEatenBySize(int size) {
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


    //-------------------------------------------------------
    // Other Methods
    //-------------------------------------------------------
    public void initEnvironment() {
//        levelImage = engine.loadImage("Assignment2/assets/image/background/background5.png");

        setBaseTime(engine.getTime());
    }
    public void drawEnvironment() {
        engine.saveCurrentTransform();

        if (!isTimeAttack) {
            System.out.println("current level: " + currentLevel + "\tmaxLevel: " + maxLevel);
            if (requiredGrowth > ((maxLevel / 3) * 2)) {
                // Last third of levels
                engine.drawImage(levelImages[2],
                        getEnvironmentXOffset(),
                        getEnvironmentYOffset(),
                        getPlayAreaWidth(),
                        getPlayAreaHeight());
            } else if (requiredGrowth > (maxLevel / 3)) {
                // Second third of levels
                engine.drawImage(levelImages[0],
                        getEnvironmentXOffset(),
                        getEnvironmentYOffset(),
                        getPlayAreaWidth(),
                        getPlayAreaHeight());
            } else {
                engine.drawImage(levelImages[1],
                        getEnvironmentXOffset(),
                        getEnvironmentYOffset(),
                        getPlayAreaWidth(),
                        getPlayAreaHeight());
            }
        } else {
            System.out.println("current time attack level: " + timeAttackLevel + "\tdefault - minTime: " + (defaultTimeAttackLevel - minTimeAttackLevel));
            if (timeAttackLevel < ((defaultTimeAttackLevel - minTimeAttackLevel) / 3)) {
                // Last third of levels
                engine.drawImage(levelImages[2],
                        getEnvironmentXOffset(),
                        getEnvironmentYOffset(),
                        getPlayAreaWidth(),
                        getPlayAreaHeight());
            } else if (timeAttackLevel < (((defaultTimeAttackLevel - minTimeAttackLevel) / 3) * 2)) {
                // Second third of levels
                engine.drawImage(levelImages[0],
                        getEnvironmentXOffset(),
                        getEnvironmentYOffset(),
                        getPlayAreaWidth(),
                        getPlayAreaHeight());
            } else {
                engine.drawImage(levelImages[1],
                        getEnvironmentXOffset(),
                        getEnvironmentYOffset(),
                        getPlayAreaWidth(),
                        getPlayAreaHeight());
            }
        }



        engine.restoreLastTransform();
    }

    public void drawHUD() {
        int hudWidth = getHUDWidth();
        int hudHeight = getHUDHeight();

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
        int lighter = 175;
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

//    public void drawEatenEnemyCount() {
//        int x = 100;
//        int[] y = {100, 200, 300};
//        engine.changeColor(255,0,0);
//        for (int i = 0; i < enemyTypes; i++) {
//            engine.drawText(x, y[i], Integer.toString(enemiesEaten[i]), "Sans serif", 24);
//        }
//    }

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
}
