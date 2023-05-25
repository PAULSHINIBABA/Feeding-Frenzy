/*
 * Author: Robert Tubman
 * ID: 11115713
 *
 * The CheckoutPage Class
 *
 * Use this class to instantiate the game summary after completing a level.
 * This will mostly be used to show the player score.
 */

package Assignment2;

import java.awt.*;

public class CheckoutPage {
    // Final fields
    private int enemyTypes;
    // TODO: implement the number and types of enemies eaten
    private int[] enemiesEaten;
    private double enemiesEatenXOffset;
    private double enemiesEatenYOffset;
    private final GameEngine engine;

    // Non-final fields
    private Image background;
    private Image backButtonImage;
    private Image continueButtonImage;
    private int imageX;
    private int imageY;
    private int backgroundWidth;
    private int backgroundHeight;
    private int score;
    private double scoreX;
    private double scoreY;
    private int drawFonts;
//    private int scoreFont;
    private int pearlsEaten;
    private double pEatX;
    private double pEatY;
//    private int pEatFont;
    private int starfishEaten;
    private double sfEatX;
    private double sfEatY;
//    private int sfEatFont;
    private double mainMenuButtonColliderX;
    private double mainMenuButtonColliderY;
    private double mainMenuButtonColliderWidth;
    private double mainMenuButtonColliderHeight;
    private double mainMenuButtonColliderRadius;
    private double nextLevelButtonColliderX;
    private double nextLevelButtonColliderY;
    private double nextLevelButtonColliderWidth;
    private double nextLevelButtonColliderHeight;
    private double nextLevelButtonColliderRadius;
    private double displayOffsetX;
    private double displayOffsetY;
    private boolean restartButton;
    private int enemyBaseWidth;
    private int enemyBaseHeight;



    public CheckoutPage(GameEngine engine, Image background, Image continueButtonImage, Image backButtonImage, int backgroundWidth, int backgroundHeight) {
        // final fields
        this.engine = engine;
        this.enemyTypes = 3;
        this.enemiesEaten = new int[enemyTypes];

        // Non-final fields
        setBackgroundImage(background);
        setContinueButtonImage(continueButtonImage);
        setBackButtonImage(backButtonImage);
        this.drawFonts = 28;
        this.imageX = 0;
        this.imageY = 0;
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
        this.displayOffsetX = this.backgroundWidth / 4.0;
        this.displayOffsetY = 20;
        this.score = 0;
        this.scoreX = (this.backgroundWidth / 2.0) - this.displayOffsetX;
        this.scoreY = (this.backgroundHeight / 3.0);
//        this.scoreFont = 28;
        this.pearlsEaten = 0;
        this.pEatX = (this.backgroundWidth / 2.0) - this.displayOffsetX;
        this.pEatY = this.scoreY + this.drawFonts + this.displayOffsetY;
//        this.pEatFont = 28;
        this.starfishEaten = 0;
        this.sfEatX = (this.backgroundWidth / 2.0) - this.displayOffsetX;
        this.sfEatY = this.pEatY + this.drawFonts + this.displayOffsetY;
//        this.sfEatFont = 28;
        double offset = 50;
        double buttonSize = 50;
        this.mainMenuButtonColliderWidth = buttonSize * 2;
        this.mainMenuButtonColliderHeight = buttonSize * 2;
        this.mainMenuButtonColliderX = offset;
        this.mainMenuButtonColliderY = backgroundHeight - offset - this.mainMenuButtonColliderHeight;
        this.mainMenuButtonColliderRadius = buttonSize;
        this.nextLevelButtonColliderWidth = buttonSize * 2;
        this.nextLevelButtonColliderHeight = buttonSize * 2;
        this.nextLevelButtonColliderX = backgroundWidth - offset - this.nextLevelButtonColliderWidth;
        this.nextLevelButtonColliderY = backgroundHeight - offset - this.nextLevelButtonColliderHeight;
        this.nextLevelButtonColliderRadius = buttonSize;
        this.restartButton = false;

        this.enemiesEatenXOffset = (this.backgroundWidth / 2.0) + this.displayOffsetX;
        this.enemiesEatenYOffset = this.drawFonts + this.displayOffsetY;
    }

    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void setBackgroundImage(Image background) { this.background = background; }
    public void setContinueButtonImage(Image continueButtonImage) { this.continueButtonImage = continueButtonImage; }
    public void setBackButtonImage(Image backButtonImage) { this.backButtonImage = backButtonImage; }
    public void setScore(int score) { this.score = score; }
    public void setPearlsEaten(int pearlsEaten) { this.pearlsEaten = pearlsEaten; }
    public void setStarfishEaten(int starfishEaten) { this.starfishEaten = starfishEaten; }
    public void setEnemyTypes(int types, boolean reset) {
        this.enemyTypes = types;
        if (reset) { setEnemiesEaten(); }
    }
    public void setEnemiesEaten() {
        this.enemiesEaten = new int[this.enemyTypes];
        for (int i = 0; i < this.enemyTypes; i++) { this.enemiesEaten[i] = 0; }
    }
    public void setEnemiesEaten(int[] enemiesEaten) {
        this.enemiesEaten = enemiesEaten;
    }
    public void setEnemyEatenBySize(int size) {
        if (size < 0 || size >= this.enemyTypes) {
            throw new IllegalArgumentException("Cannot eat a enemy of size that doesn't exist");
        }
        this.enemiesEaten[size] += 1;
    }
    public void setBackgroundWidth(int width) { this.backgroundWidth = width; }
    public void setBackgroundHeight(int height) { this.backgroundHeight = height; }
    public void setRestartButton() { this.restartButton = true; }
    public void setEnemyBaseSize(int w, int h) {
        this.enemyBaseWidth = w;
        this.enemyBaseHeight = h;
    }

    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public int getScore() { return this.score; }
    public int getPearlsEaten() { return this.pearlsEaten; }
    public int getStarfishEaten() { return this.starfishEaten; }
    public int getBackgroundWidth() { return this.backgroundWidth; }
    public int getBackgroundHeight() { return this.backgroundHeight; }

    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
//    public void initCheckout() {}
//    public void updateCheckout() {}
    public void drawCheckout(Image[] enemies) {
        drawBackground();
        drawScore();
        drawEnemiesEaten(enemies);
        drawPearlsEaten();
        drawStarfishEaten();

        drawReturnToMainMenuButton();
        drawNextLevelButton();
        drawCheckoutLine();
        drawButtonColliders();
    }
    public void drawBackground() {
        this.engine.drawImage(this.background, this.imageX, this.imageY, this.backgroundWidth, this.backgroundHeight);
    }
    public void drawScore() {
        this.engine.drawText(this.scoreX, this.scoreY, ("Score: " + this.score), "Sans Serif", this.drawFonts);
    }
    public void drawEnemiesEaten(Image[] enemies) {
        double yOffsets = (this.backgroundHeight / 3.0) - this.drawFonts;
        double x1 = ((this.enemiesEatenXOffset/3)*2 - this.enemyBaseWidth);
        double x2 = ((this.enemiesEatenXOffset) - this.enemyBaseWidth);
        double w = this.enemyBaseWidth*2;
        double h = this.enemyBaseHeight*2;
        for (int i = 0; i < this.enemyTypes; i++) {
            this.engine.drawImage(enemies[i], x1, yOffsets, w, h);
            this.engine.drawText(x2, (yOffsets - (this.drawFonts / 2.0)), (": " + Integer.toString(this.enemiesEaten[i])), "Sans serif", this.drawFonts);
            yOffsets += this.enemiesEatenYOffset;

//            drawLines(this.enemiesEatenXOffset, yOffsets, this.enemyBaseWidth, this.enemyBaseHeight);
        }
    }
//    public void drawLines(double x, double y, double w, double h) {
//        this.engine.changeColor(255,0,0);
//        this.engine.drawLine(x,y,x+w,y);
//        this.engine.drawLine(x,y+h,x+w,y+h);
//        this.engine.drawLine(x,y,x,y+h);
//        this.engine.drawLine(x+w,y,x+w,y+h);
//    }
    public void drawPearlsEaten() {
        this.engine.changeColor(0,0,0);
        this.engine.drawText(this.pEatX, this.pEatY, ("Pearls: " + this.pearlsEaten), "Sans Serif", this.drawFonts);
    }
    public void drawStarfishEaten() {
        this.engine.changeColor(0,0,0);
        this.engine.drawText(this.sfEatX, this.sfEatY, ("Starfish: " + this.starfishEaten), "Sans Serif", this.drawFonts);
    }
    public void drawReturnToMainMenuButton() {
        this.engine.drawImage(this.backButtonImage,
                this.mainMenuButtonColliderX - this.mainMenuButtonColliderRadius,
                this.mainMenuButtonColliderY - this.mainMenuButtonColliderRadius,
                this.mainMenuButtonColliderWidth,
                this.mainMenuButtonColliderHeight);
    }
    public void drawNextLevelButton() {
        this.engine.drawImage(this.continueButtonImage,
                (this.nextLevelButtonColliderX + this.nextLevelButtonColliderWidth - this.mainMenuButtonColliderRadius),
                this.nextLevelButtonColliderY - this.mainMenuButtonColliderRadius,
                (-this.nextLevelButtonColliderWidth),
                this.nextLevelButtonColliderHeight);
    }

    public void drawButtonColliders() {
        this.engine.changeColor(255,0,0);
        this.engine.drawCircle(mainMenuButtonColliderX, mainMenuButtonColliderY, mainMenuButtonColliderRadius);
        this.engine.drawCircle(nextLevelButtonColliderX, nextLevelButtonColliderY, nextLevelButtonColliderRadius);
    }

    public void drawCheckoutLine() {
        double checkoutLineX = (this.backgroundWidth / 2.0) - this.displayOffsetX;
        double checkoutLineY = (this.backgroundHeight / 4.0);
        int fontSize = 32;
        this.engine.changeColor(0,0,0);
        this.engine.drawText(checkoutLineX, checkoutLineY, "STAGE COMPLETE!", "a", fontSize);
    }

    public String checkClickTarget(double mX, double mY) {
        if (checkDistance(mX, mY, this.mainMenuButtonColliderX, this.mainMenuButtonColliderY, this.mainMenuButtonColliderRadius)) {
            return "main_menu";
        } else if (checkDistance(mX, mY, this.nextLevelButtonColliderX, this.nextLevelButtonColliderY, this.nextLevelButtonColliderRadius)) {
            if (this.restartButton) {
                return "restart";
            } else {
                return "next_level";
            }
        } else {
            return "nothing";
        }
    }

    public boolean checkDistance(double x1, double y1, double x2, double y2, double r) {
        double value = Math.sqrt((powerOf((x2 - x1), 2.0) + powerOf((y2 - y1), 2.0)));
        System.out.println("distance:" + value);
        if (value <= r) { return true; }
        else { return false; }
    }

    public double powerOf(double x, double power) {
        if (power == 0) { return 1.0; }
        else if (power == 1) { return x; }
        else {
            double value = x;
            for (int i = 1; i < power; i++) { value *= x; }
            return value;
        }
    }
}
