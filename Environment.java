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
    private int playAreaX; // left side offset
    private int playAreaY; // top side offset
    private int playAreaWidth;
    private int playAreaHeight;
    private int borderWidth;
    private int borderHeight;
    private boolean levelComplete;

    public Environment(Image levelImage,
                       int imageWidth,
                       int imageHeight,
                       int borderWidth,
                       int borderHeight,
                       int hudWidth,
                       int hudHeight,
                       boolean isTimeAttack,
                       double countdownTimer) {
        this(levelImage, imageWidth, imageHeight, borderWidth, borderHeight, hudWidth, hudHeight, 0, 0, 0, isTimeAttack, countdownTimer);
    }

    public Environment(Image levelImage,
                       int imageWidth,
                       int imageHeight,
                       int borderWidth,
                       int borderHeight,
                       int hudWidth,
                       int hudHeight,
                       int thresholdMedium,
                       int thresholdLarge,
                       int targetGoal,
                       boolean isTimeAttack,
                       double countdownTimer) throws IllegalArgumentException {

        this.isPaused = false;
        this.levelComplete = false;
        this.currentScore = 0;
        this.currentGoal = 0;
        this.isTimeAttack = isTimeAttack;
        this.countdownTimer = countdownTimer;
        this.countdownTimerCurrent = 0.0;
        this.countdownTimeOffset = 0.0;
        this.timer = 0.0;
        this.pausableTimer = 0.0;

        this.borderWidth = borderWidth;
        this.borderHeight = borderHeight;

        this.hudWidth = hudWidth;
        this.hudHeight = hudHeight;

        this.playAreaX = hudWidth;
        this.playAreaY = hudHeight;
        this.playAreaHeight = borderHeight - hudHeight - hudWidth; // hudHeight is from the top, hudWidth is taken from the bottom
        this.playAreaWidth = borderWidth - (2 * hudWidth);

        this.imageOriginX = hudWidth + (this.playAreaWidth / 2);
        this.imageOriginY = hudHeight + (this.playAreaHeight / 2);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.imageXOffset = this.imageOriginX - (this.playAreaWidth / 2);
        this.imageYOffset = this.imageOriginY - (this.playAreaHeight / 2);

        this.SetGrowthThresholdLarge(thresholdLarge);
        this.SetGrowthThresholdMedium(thresholdMedium);
        this.SetTargetGoal(targetGoal);

        this.levelImage = levelImage;
    }

    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void SetEnvironmentImage(Image levelImage) {
        if (levelImage == null) { throw new IllegalArgumentException("Cannot set the environment image to null"); }
        this.levelImage = levelImage;
    }
    public void SetEnvironmentWidth(int width) { this.imageWidth = width; }
    public void SetEnvironmentHeight(int height) { this.imageHeight = height; }
    public void SetEnvironmentOriginX(double x) { this.imageOriginX = x; }
    public void SetEnvironmentOriginY(double y) { this.imageOriginY = y; }
    public void SetEnvironmentXOffset(double x) { this.imageXOffset = x; }
    public void SetEnvironmentYOffset(double y) { this.imageYOffset = y; }
    public void SetEnvironmentPlayWidth(int playAreaWidth) throws IllegalArgumentException {
        if (playAreaWidth < 0) { throw new IllegalArgumentException("Cannot set the play area width to a negative integer"); }
        this.playAreaWidth = playAreaWidth;
    }
    public void SetEnvironmentPlayHeight(int playAreaHeight) throws IllegalArgumentException {
        if (playAreaHeight < 0) { throw new IllegalArgumentException("Cannot set the play area height to a negative integer"); }
        this.playAreaHeight = playAreaHeight;
    }
    // Set the score to an integer value. The score cannot be negative.
    public void SetScore(int score) throws IllegalArgumentException {
        if (score < 0) { throw new IllegalArgumentException("Cannot set score to a negative integer"); }
        this.currentScore = score;
    }

    // Set the medium growth threshold to an integer value. The medium growth threshold cannot be negative.
    public void SetGrowthThresholdMedium(int mediumThreshold) throws IllegalArgumentException {
        if (mediumThreshold < 0) { throw new IllegalArgumentException("Cannot set growth to a negative integer"); }
        if (mediumThreshold > this.growthThresholdLarge) { throw new IllegalArgumentException("The growth threshold medium cannot be greater than the growth threshold large"); }

        if (mediumThreshold == 0) {
            this.growthThresholdMedium = this.defaultGrowthThresholdM;
        } else {
            this.growthThresholdMedium = mediumThreshold;
        }
    }

    // Set the large growth threshold to an integer value. The large growth threshold cannot be negative.
    public void SetGrowthThresholdLarge(int largeThreshold) throws IllegalArgumentException {
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
    public void SetCurrentGoal(int cGoal) {
        if (cGoal < 0) { throw new IllegalArgumentException("Cannot set current goal to a negative integer"); }
        this.currentGoal = cGoal;
    }

    // Set the target goal to an integer value. The target goal cannot be negative.
    public void SetTargetGoal(int tGoal) {
        if (tGoal < 0) { throw new IllegalArgumentException("Cannot set target goal to a negative integer"); }

        if (tGoal == 0) {
            this.targetGoal = this.defaultTargetGoal;
        } else {
            this.targetGoal = tGoal;
        }
    }

    public void SetIsPaused(boolean paused) { this.isPaused = paused; }
    public void SetPlayAreaX(int x) { this.playAreaX = x; }
    public void SetPlayAreaY(int y) { this.playAreaY = y; }
    public void SetPlayAreaWidth(int width) { this.playAreaWidth = width; }
    public void SetPlayAreaHeight(int height) { this.playAreaHeight = height; }
    public void SetHUDWidth(int width) { this.hudWidth = width; }
    public void SetHUDHeight(int height) { this.hudHeight = height; }
    public void SetTimer(double newTime) { this.timer = (newTime - this.baseTime) / 1000.0; }
    public void SetBaseTime(double baseTime) { this.baseTime = baseTime; }
    public void SetIsTimeAttack(boolean isTimeAttack) { this.isTimeAttack = isTimeAttack; }
    public void SetCountDownTimer(double timer) { this.countdownTimer = timer; }
    public void SetCountDownCurrentTimer(double timer) { this.countdownTimerCurrent = timer; }
//    public void SetCountDownTimerOffset(double timer) { this.countdownTimeOffset = timer; }
    public void SetPausableTimer(double timer) { this.pausableTimer = timer; }



    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public int GetScore() { return this.currentScore; }
    public int GetGrowthThresholdMedium() { return this.growthThresholdMedium; }
    public int GetGrowthThresholdLarge() { return this.growthThresholdLarge; }
    public int GetCurrentGoal() { return this.currentGoal; }
    public int GetTargetGoal() { return this.targetGoal; }
    public Image GetEnvironment() { return this.levelImage; }
    public int GetEnvironmentWidth() { return this.imageWidth;}
    public int GetEnvironmentHeight() { return this.imageHeight; }
    public double GetEnvironmentOriginX() { return this.imageOriginX; }
    public double GetEnvironmentOriginY() { return this.imageOriginY; }
    public double GetEnvironmentXOffset() { return this.imageXOffset; }
    public double GetEnvironmentYOffset() { return this.imageYOffset; }
    public boolean GetIsPaused() { return this.isPaused; }
    public int GetHUDWidth() { return this.hudWidth; }
    public int GetHUDHeight() { return this.hudHeight; }
    public int GetPlayAreaX() { return this.playAreaX; }
    public int GetPlayAreaY() { return this.playAreaY; }
    public int GetPlayAreaWidth() { return this.playAreaWidth; }
    public int GetPlayAreaHeight() { return this.playAreaHeight; }
    public double GetTimer() { return this.Round2DP(this.timer); }
    public double GetBaseTime() { return this.Round2DP(this.baseTime); }
    public boolean GetIsTimeAttack() { return this.isTimeAttack; }
    public double GetCountDownTimer() { return this.Round2DP(this.countdownTimer); }
    public double GetCountDownCurrentTimer() { return this.Round2DP(this.countdownTimerCurrent); }
    public double GetCountDownCurrentTimerPercentage() { return this.Round2DP((this.countdownTimerCurrent / this.countdownTimer)); }
    public double GetCountDownTimerWOffset() {
        return Round2DP((this.GetTimer() - this.countdownTimeOffset));
    }
    public double GetPausableTimer() { return this.pausableTimer; }
    public boolean GetIsLevelComplete() { return this.levelComplete; }


    //-------------------------------------------------------
    // Other Methods
    //-------------------------------------------------------
    public void ResetLevel() {
        this.currentScore = 0;
        this.growthThresholdMedium = this.defaultGrowthThresholdM;
        this.growthThresholdLarge = this.defaultGrowthThresholdL;
        this.currentGoal = 0;
        this.targetGoal = 0;
    }

    public void AddCountDownTimerOffset(double timer) { this.countdownTimeOffset += timer; }
    public double Round2DP(double value) { return Math.round(value * 100.0) / 100.0; }
    public void EnvironmentLevelCompleteCheck() {
        if (isTimeAttack) {
            if (this.countdownTimerCurrent > this.countdownTimer) { this.levelComplete = true; }
        } else {
            if (this.currentGoal >= this.targetGoal) { this.levelComplete = true; }
        }
    }
}
