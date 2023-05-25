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

import java.awt.*;

public class Environment {
    int defaultGrowthThresholdM = 1;
    int defaultGrowthThresholdL = 2;
    int defaultTargetGoal = 3;
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
    private boolean canPauseNow;
    private GameEngine engine;

    public Environment(GameEngine engine, boolean isTimeAttack) {
        this.engine = engine;

        this.isPaused = true;
        this.levelComplete = false;
        this.canPauseNow = false;
        this.currentScore = 0;
        this.currentGoal = 0;
        this.isTimeAttack = isTimeAttack;
        // TODO: Reimplement this
//        this.countdownTimer = 60.0; // Time attack default is a minute
        this.countdownTimer = 10.0; // Time attack default is a minute
        this.countdownTimerCurrent = 0.0;
        this.countdownTimeOffset = 0.0;
        this.timer = 0.0;
        this.pausableTimer = 0.0;

        this.hudWidth = 10; // Arbitrary default HUD width
        this.hudHeight = 80; // Arbitrary default HUD height
        this.hudOffsetX = 10;
        this.hudOffsetY = 20;
        this.hudFontSize =  12;
        this.hudBarThickness = 12;
        this.hudInfoSpacing = 2;

        this.playAreaX = this.hudWidth;
        this.playAreaY = this.hudHeight;
        this.playAreaWidth = this.engine.width() - (2 * this.hudWidth);
        this.playAreaHeight = this.engine.height() - this.hudHeight - this.hudWidth; // hudHeight is from the top, hudWidth is taken from the bottom

        this.imageOriginX = this.hudWidth + (this.playAreaWidth / 2.0);
        this.imageOriginY = this.hudHeight + (this.playAreaHeight / 2.0);
        this.imageWidth = this.playAreaWidth; // Arbitrary image width
        this.imageHeight = this.playAreaHeight; // Arbitrary image height
        this.imageXOffset = this.imageOriginX - (this.playAreaWidth / 2.0);
        this.imageYOffset = this.imageOriginY - (this.playAreaHeight / 2.0);

        this.setGrowthThresholdLarge(0);
        this.setGrowthThresholdMedium(0);
        this.setTargetGoal(0);

        initEnvironment();
    }

    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void setEnvironmentImage(Image levelImage) {
        if (levelImage == null) { throw new IllegalArgumentException("Cannot set the environment image to null"); }
        this.levelImage = levelImage;
    }
    public void setEnvironmentWidth(int width) { this.imageWidth = width; }
    public void setEnvironmentHeight(int height) { this.imageHeight = height; }
    public void setEnvironmentOriginX(double x) { this.imageOriginX = x; }
    public void setEnvironmentOriginY(double y) { this.imageOriginY = y; }
    public void setEnvironmentXOffset(double x) { this.imageXOffset = x; }
    public void setEnvironmentYOffset(double y) { this.imageYOffset = y; }
    public void setEnvironmentPlayWidth(int playAreaWidth) throws IllegalArgumentException {
        if (playAreaWidth < 0) { throw new IllegalArgumentException("Cannot set the play area width to a negative integer"); }
        this.playAreaWidth = playAreaWidth - (2 * this.hudWidth);
//        this.playAreaWidth = playAreaWidth;
    }
    public void setEnvironmentPlayHeight(int playAreaHeight) throws IllegalArgumentException {
        if (playAreaHeight < 0) { throw new IllegalArgumentException("Cannot set the play area height to a negative integer"); }
        this.playAreaHeight = playAreaHeight - this.hudHeight - this.hudWidth;
//        this.playAreaHeight = playAreaHeight;
    }
    // Set the score to an integer value. The score cannot be negative.
    public void setScore(int score) throws IllegalArgumentException {
        if (score < 0) { throw new IllegalArgumentException("Cannot set score to a negative integer"); }
        this.currentScore = score;
    }

    // Set the medium growth threshold to an integer value. The medium growth threshold cannot be negative.
    public void setGrowthThresholdMedium(int mediumThreshold) throws IllegalArgumentException {
        if (mediumThreshold < 0) { throw new IllegalArgumentException("Cannot set growth to a negative integer"); }
        if (mediumThreshold > this.growthThresholdLarge) { throw new IllegalArgumentException("The growth threshold medium cannot be greater than the growth threshold large"); }

        if (mediumThreshold == 0) {
            this.growthThresholdMedium = this.defaultGrowthThresholdM;
        } else {
            this.growthThresholdMedium = mediumThreshold;
        }
    }

    // Set the large growth threshold to an integer value. The large growth threshold cannot be negative.
    public void setGrowthThresholdLarge(int largeThreshold) throws IllegalArgumentException {
        if (largeThreshold < 0) { throw new IllegalArgumentException("Cannot set growth to a negative integer"); }
        if (largeThreshold < this.growthThresholdMedium) { throw new IllegalArgumentException("The growth threshold large cannot be less than the growth threshold medium"); }

        if (largeThreshold == 0) {
            this.growthThresholdLarge = this.defaultGrowthThresholdL;
        } else {
            this.growthThresholdLarge = largeThreshold;
        }
    }

    // Set the current goal to an integer value. The current goal cannot be negative.
    // currentGoal / targetGoal gives us the progression for the current level
    public void setCurrentGoal(int cGoal) {
        if (cGoal < 0) { throw new IllegalArgumentException("Cannot set current goal to a negative integer"); }
        this.currentGoal = cGoal;
    }

    // Set the target goal to an integer value. The target goal cannot be negative.
    public void setTargetGoal(int tGoal) {
        if (tGoal < 0) { throw new IllegalArgumentException("Cannot set target goal to a negative integer"); }

        if (tGoal == 0) {
            this.targetGoal = this.defaultTargetGoal;
        } else {
            this.targetGoal = tGoal;
        }
    }

    public void setIsPaused(boolean paused) { this.isPaused = paused; }
    public void setPlayAreaX(int x) { this.playAreaX = x; }
    public void setPlayAreaY(int y) { this.playAreaY = y; }
//    public void setPlayAreaWidth(int width) { this.playAreaWidth = width; }
//    public void setPlayAreaHeight(int height) { this.playAreaHeight = height; }
    public void setHUDWidth(int width) { this.hudWidth = width; }
    public void setHUDHeight(int height) { this.hudHeight = height; }
    public void setTimer(double newTime) { this.timer = (newTime - this.baseTime) / 1000.0; }
    public void setBaseTime(double baseTime) { this.baseTime = baseTime; }
    public void setIsTimeAttack(boolean isTimeAttack) { this.isTimeAttack = isTimeAttack; }
    public void setCountDownTimer(double timer) { this.countdownTimer = timer; }
    public void setCountDownCurrentTimer(double timer) { this.countdownTimerCurrent = timer; }
//    public void setCountDownTimerOffset(double timer) { this.countdownTimeOffset = timer; }
    public void setPausableTimer(double timer) { this.pausableTimer = timer; }



    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public int getScore() { return this.currentScore; }
    public int getGrowthThresholdMedium() { return this.growthThresholdMedium; }
    public int getGrowthThresholdLarge() { return this.growthThresholdLarge; }
    public int getCurrentGoal() { return this.currentGoal; }
    public int getTargetGoal() { return this.targetGoal; }
    public Image getEnvironment() { return this.levelImage; }
    public int getEnvironmentWidth() { return this.imageWidth;}
    public int getEnvironmentHeight() { return this.imageHeight; }
    public double getEnvironmentOriginX() { return this.imageOriginX; }
    public double getEnvironmentOriginY() { return this.imageOriginY; }
    public double getEnvironmentXOffset() { return this.imageXOffset; }
    public double getEnvironmentYOffset() { return this.imageYOffset; }
    public boolean getIsPaused() { return this.isPaused; }
    public int getHUDWidth() { return this.hudWidth; }
    public int getHUDHeight() { return this.hudHeight; }
    public int getPlayAreaX() { return this.playAreaX; }
    public int getPlayAreaY() { return this.playAreaY; }
    public int getPlayAreaWidth() { return this.playAreaWidth; }
    public int getPlayAreaHeight() { return this.playAreaHeight; }
    public double getTimer() { return this.round2DP(this.timer); }
    public double getBaseTime() { return this.round2DP(this.baseTime); }
    public boolean getIsTimeAttack() { return this.isTimeAttack; }
    public double getCountDownTimer() { return this.round2DP(this.countdownTimer); }
    public double getCountDownCurrentTimer() { return this.round2DP(this.countdownTimerCurrent); }
    public double getCountDownCurrentTimerPercentage() { return this.round2DP((this.countdownTimerCurrent / this.countdownTimer)); }
    public double getCountDownTimerWOffset() {
        double theTime = this.getTimer();
        double theOffset = this.countdownTimeOffset;
        if (theOffset > theTime) {
            return round2DP(theTime);
        } else {
            return round2DP((theTime - theOffset));
        }
    }
    public double getPausableTimer() { return this.pausableTimer; }
    public boolean getIsLevelComplete() { return this.levelComplete; }


    //-------------------------------------------------------
    // Other Methods
    //-------------------------------------------------------
    public void initEnvironment() {
        this.levelImage = this.engine.loadImage("Assignment2/assets/image/background/background5.png");

        this.setBaseTime(this.engine.getTime());
    }
    public void drawEnvironment() {
        this.engine.saveCurrentTransform();
//        System.out.println("x:" + this.getEnvironmentXOffset() +
//                "\ty:" + this.getEnvironmentYOffset() +
//                "\tw:" + this.getPlayAreaWidth() +
//                "\th:" + this.getPlayAreaHeight());

        this.engine.drawImage(this.levelImage,
                this.getEnvironmentXOffset(),
                this.getEnvironmentYOffset(),
                this.getPlayAreaWidth(),
                this.getPlayAreaHeight());
//        this.engine.drawImage(this.levelImage, 0, 0, 500, 500);

        this.engine.restoreLastTransform();
    }

    public void drawHUD() {
        int hudWidth = this.getHUDWidth();
        int hudHeight = this.getHUDHeight();

        int whiteOutValue = 100;
        this.engine.changeColor(whiteOutValue,whiteOutValue,255);
        // HUD left
        this.engine.drawSolidRectangle(0,0, hudWidth, this.engine.height());
        // HUD Right
        this.engine.drawSolidRectangle((this.engine.width() - hudWidth),0, hudWidth, this.engine.height());
        // HUD bottom
        this.engine.drawSolidRectangle(0,(this.engine.height() - hudWidth), this.engine.width(), hudWidth);
        // HUD top
        this.engine.drawSolidRectangle(0,0, this.engine.width(), hudHeight);
    }

    public void drawTimer(boolean isTimeAttack) {
        double displayTime = this.getCountDownTimerWOffset();
        int clr = 0;
        this.engine.changeColor(clr,clr,clr);
        this.engine.drawText(10, 20, ("Time: " + Double.toString(displayTime)), "Sans Serif", 12);

        if (isTimeAttack) {
            this.drawTimeAttackBar();
        }
    }
    public void drawScore() {
        int playerScore = this.getScore();
        int clr = 0;
        this.engine.changeColor(clr,clr,clr);
        this.engine.drawText(this.hudOffsetX, (this.hudOffsetY + this.hudFontSize + this.hudInfoSpacing), ("Score: " + Double.toString(playerScore)), "Sans Serif", this.hudFontSize);
    }

    public void drawGrowth() {
        double growthTarget = this.getTargetGoal();
        double growthM = this.getGrowthThresholdMedium();
        double growthL = this.getGrowthThresholdLarge();

        double growthBarLength = this.engine.width() * 0.6;
        double growthBarXOffset = (this.engine.width() - growthBarLength) / 2.0;
        double growthBarYOffset = this.hudOffsetY;

        double gSS = ((growthBarLength / growthTarget) * (growthM));
        double gSM = ((growthBarLength / growthTarget) * (growthL - growthM));
        double gSL = ((growthBarLength / growthTarget) * (growthTarget - growthL));

        // draw the small size
        int dark = 150;
        int lighter = 175;
        int light = 200;
        int basic = 120;
        this.engine.changeColor(basic,dark,basic);
        this.engine.drawSolidRectangle(growthBarXOffset, growthBarYOffset, gSS, this.hudBarThickness);

        // draw the medium size
        double nextBar = growthL - growthM;
        this.engine.changeColor(basic,lighter,basic);
        this.engine.drawSolidRectangle(growthBarXOffset + gSS, growthBarYOffset, gSM, this.hudBarThickness);

        // draw the large size
        double finalBar = growthTarget - growthL;
        this.engine.changeColor(basic,light,basic);
        this.engine.drawSolidRectangle(growthBarXOffset + gSS + gSM, growthBarYOffset, gSL, this.hudBarThickness);

        // Draw the current growth
        double growthState = ((growthBarLength / growthTarget) * (this.getCurrentGoal()));
        if (growthState >= growthBarLength) { growthState = growthBarLength; }
        this.engine.changeColor(0,255,0);
        this.engine.drawSolidRectangle(growthBarXOffset, growthBarYOffset, growthState, this.hudBarThickness);

        // draw the growth bar borders
        this.drawPanelBorder(growthBarXOffset, growthBarYOffset, growthBarLength, this.hudBarThickness);
    }

    public void drawTimeAttackBar() {
        double timeAttackLimit = this.getCountDownTimer();
        double timeAttackRemaining = this.getCountDownCurrentTimer();

        double timeAttackBarLength = this.engine.width() * 0.6;
        double timeAttackBarXOffset = (this.engine.width() - timeAttackBarLength) / 2.0;
        double timeAttackBarYOffset = this.hudOffsetY * 2;

        // draw the small size
        int dark = 120;
        int lighter = 175;
        int light = 240;
        int basic = 50;
        this.engine.changeColor(light,basic,basic);
        this.engine.drawSolidRectangle(timeAttackBarXOffset, timeAttackBarYOffset, timeAttackBarLength, this.hudBarThickness);

        // Draw the current remaining time
        double remainingTime = ((timeAttackBarLength / timeAttackLimit) * timeAttackRemaining);
        if (remainingTime >= timeAttackBarLength) { remainingTime = timeAttackBarLength; }

        this.engine.changeColor(dark,basic,basic);
        this.engine.drawSolidRectangle(timeAttackBarXOffset, timeAttackBarYOffset, remainingTime, this.hudBarThickness);

        // draw the time attack borders
        this.drawPanelBorder(timeAttackBarXOffset, timeAttackBarYOffset, timeAttackBarLength, this.hudBarThickness);
    }

    public void drawPanelBorder(double x, double y, double l, double h) {
        int clr = 0;
        this.engine.changeColor(clr,clr,clr);
        int thickness = 2;

        this.engine.drawLine(x,y,x+l,y,thickness);
        this.engine.drawLine(x,y+h,x+l,y+h,thickness);
        this.engine.drawLine(x,y,x,y+h,thickness);
        this.engine.drawLine(x+l,y,x+l,y+h,thickness);
    }

    public void resetLevel() {
        this.currentScore = 0;
        this.growthThresholdMedium = this.defaultGrowthThresholdM;
        this.growthThresholdLarge = this.defaultGrowthThresholdL;
        this.currentGoal = 0;
        this.targetGoal = 0;
    }

    public void addCountDownTimerOffset(double timer) { this.countdownTimeOffset += timer; }
    public double round2DP(double value) { return Math.round(value * 100.0) / 100.0; }
    public void environmentLevelCompleteCheck() {
        if (isTimeAttack) {
            if (this.countdownTimerCurrent > this.countdownTimer) { this.levelComplete = true; }
        } else {
            if (this.currentGoal >= this.targetGoal) { this.levelComplete = true; }
        }
    }
    public void addScore(int score) { this.currentScore += score; }
}
