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
    private final int ENEMY_TYPES;
    // TODO: implement the number and types of enemies eaten
    private final int[] ENEMIES_EATEN;
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
    private int scoreFont;
    private int pearlsEaten;
    private double pEatX;
    private double pEatY;
    private int pEatFont;
    private int starfishEaten;
    private double sfEatX;
    private double sfEatY;
    private int sfEatFont;
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


    public CheckoutPage(GameEngine engine, Image background, Image continueButtonImage, Image backButtonImage, int backgroundWidth, int backgroundHeight) {
        // final fields
        this.engine = engine;
        this.ENEMY_TYPES = 3;
        this.ENEMIES_EATEN = new int[ENEMY_TYPES];

        // Non-final fields
        setBackgroundImage(background);
        setContinueButtonImage(continueButtonImage);
        setBackButtonImage(backButtonImage);
        this.imageX = 0;
        this.imageY = 0;
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
        this.score = 0;
        this.scoreX = 300;
        this.scoreY = 280;
        this.scoreFont = 28;
        this.pearlsEaten = 0;
        this.pEatX = 300;
        this.pEatY = this.scoreY + this.scoreFont + 20;
        this.pEatFont = 28;
        this.starfishEaten = 0;
        this.sfEatX = 300;
        this.sfEatY = this.pEatY + this.pEatFont + 20;
        this.sfEatFont = 28;
        double offset = 50;
        double buttonSize = 25;
        this.mainMenuButtonColliderWidth = 140;
        this.mainMenuButtonColliderHeight = 50;
        this.mainMenuButtonColliderX = offset;
        this.mainMenuButtonColliderY = backgroundHeight - offset - this.mainMenuButtonColliderHeight;
        this.mainMenuButtonColliderRadius = buttonSize;
        this.nextLevelButtonColliderWidth = 140;
        this.nextLevelButtonColliderHeight = 50;
        this.nextLevelButtonColliderX = backgroundWidth - offset - this.nextLevelButtonColliderWidth;
        this.nextLevelButtonColliderY = backgroundHeight - offset - this.nextLevelButtonColliderHeight;
        this.nextLevelButtonColliderRadius = buttonSize;

        // TODO: the enemy types eaten display
//        for (int i = 0; i < this.ENEMY_TYPES; i++) {
//        }
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
    public void setEnemiesEaten() {}
    public void setBackgroundWidth(int width) { this.backgroundWidth = width; }
    public void setBackgroundHeight(int height) { this.backgroundHeight = height; }

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
    public void drawCheckout() {
        drawBackground();
        drawScore();
        drawEnemiesEaten();
        drawPearlsEaten();
        drawStarfishEaten();
        drawReturnToMainMenuButton();
        drawNextLevelButton();
        drawCheckoutLine();
    }
    public void drawBackground() {
        this.engine.drawImage(this.background, this.imageX, this.imageY, this.backgroundWidth, this.backgroundHeight);
    }
    public void drawScore() {
        this.engine.drawText(this.scoreX, this.scoreY, ("Score: " + this.score), "Sans Serif", this.scoreFont);
    }
    public void drawEnemiesEaten() {
        // TODO: Need to implement
    }
    public void drawPearlsEaten() {
        this.engine.drawText(this.pEatX, this.pEatY, ("Pearls: " + this.pearlsEaten), "Sans Serif", this.pEatFont);
    }
    public void drawStarfishEaten() {
        this.engine.drawText(this.sfEatX, this.sfEatY, ("Starfish: " + this.starfishEaten), "Sans Serif", this.sfEatFont);
    }
    public void drawReturnToMainMenuButton() {
        this.engine.drawImage(this.backButtonImage,
                this.mainMenuButtonColliderX,
                this.mainMenuButtonColliderY,
                this.mainMenuButtonColliderWidth,
                this.mainMenuButtonColliderHeight);
    }
    public void drawNextLevelButton() {
        this.engine.drawImage(this.continueButtonImage,
                (this.nextLevelButtonColliderX + this.nextLevelButtonColliderWidth),
                this.nextLevelButtonColliderY,
                (-this.nextLevelButtonColliderWidth),
                this.nextLevelButtonColliderHeight);
    }

    public void drawCheckoutLine() {
        int checkoutLineX = 230;
        int checkoutLineY = 100;
        this.engine.drawText(checkoutLineX, checkoutLineY, "STAGE COMPLETE!", "a", 28);
    }

    public String checkClickTarget(double mX, double mY) {
        if (checkDistance(mX, mY, this.mainMenuButtonColliderX, this.mainMenuButtonColliderY, this.mainMenuButtonColliderRadius)) {
            return "main_menu";
        } else if (checkDistance(mX, mY, this.nextLevelButtonColliderX, this.nextLevelButtonColliderY, this.nextLevelButtonColliderRadius)) {
            return "next_level";
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
