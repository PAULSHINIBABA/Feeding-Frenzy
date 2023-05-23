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
    private final int[] ENEMIES_EATEN;
    private final GameEngine engine;

    // Non-final fields
    private Image background;
    private int imageX;
    private int imageY;
    private int width;
    private int height;
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
    private double mainMenuButtonColliderRadius;
    private double nextLevelButtonColliderX;
    private double nextLevelButtonColliderY;
    private double nextLevelButtonColliderRadius;


    public CheckoutPage(GameEngine engine, Image background, int width, int height) {
        // final fields
        this.engine = engine;
        this.ENEMY_TYPES = 3;
        this.ENEMIES_EATEN = new int[ENEMY_TYPES];

        // Non-final fields
        initImage(background);
        this.imageX = 0;
        this.imageY = 0;
        this.width = width;
        this.height = height;
        this.score = 0;
        this.scoreX = 50;
        this.scoreY = 200;
        this.scoreFont = 40;
        this.pearlsEaten = 0;
        this.pEatX = 50;
        this.pEatY = this.scoreY + this.scoreFont + 12;
        this.pEatFont = 40;
        this.starfishEaten = 0;
        this.sfEatX = 50;
        this.sfEatY = this.pEatY + this.pEatFont + 12;
        this.sfEatFont = 40;
        double offset = 50;
        double buttonSize = 25;
        this.mainMenuButtonColliderX = offset;
        this.mainMenuButtonColliderY = height - offset;
        this.mainMenuButtonColliderRadius = buttonSize;
        this.nextLevelButtonColliderX = width - offset;
        this.nextLevelButtonColliderY = height - offset;
        this.nextLevelButtonColliderRadius = buttonSize;

        for (int i = 0; i < this.ENEMY_TYPES; i++) {

        }
    }

    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void setScore(int score) { this.score = score; }
    public void setPearlsEaten(int pearlsEaten) { this.pearlsEaten = pearlsEaten; }
    public void setStarfishEaten(int starfishEaten) { this.starfishEaten = starfishEaten; }
    public void setEnemiesEaten() {}

    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public int getScore() { return this.score; }
    public int getPearlsEaten() { return this.pearlsEaten; }
    public int getStarfishEaten() { return this.starfishEaten; }

    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
//    public void initCheckout() {}
    public void initImage(Image background) { this.background = background; }
//    public void updateCheckout() {}
    public void drawCheckout() {
        drawBackground();
        drawScore();
        drawEnemiesEaten();
        drawPearlsEaten();
        drawStarfishEaten();
        drawReturnToMainMenuButton();
        drawNextLevelButton();
    }
    public void drawBackground() {
        this.engine.drawImage(this.background, this.imageX, this.imageY, this.width, this.height);
    }
    public void drawScore() {
        this.engine.drawText(this.scoreX, this.scoreY, ("Score: " + this.score), "Sans Serif", this.scoreFont);
    }
    public void drawEnemiesEaten() {
    }
    public void drawPearlsEaten() {
        this.engine.drawText(this.pEatX, this.pEatY, ("Pearls: " + this.pearlsEaten), "Sans Serif", this.pEatFont);
    }
    public void drawStarfishEaten() {
        this.engine.drawText(this.sfEatX, this.sfEatY, ("Starfish: " + this.starfishEaten), "Sans Serif", this.sfEatFont);
    }
    public void drawReturnToMainMenuButton() {
        this.engine.changeColor(0,0,0);
        this.engine.drawSolidCircle(this.mainMenuButtonColliderX, this.mainMenuButtonColliderY, this.mainMenuButtonColliderRadius);
    }
    public void drawNextLevelButton() {
        this.engine.changeColor(0,255,0);
        this.engine.drawSolidCircle(this.nextLevelButtonColliderX, this.nextLevelButtonColliderY, this.nextLevelButtonColliderRadius);
    }
    public String checkClickTarget(double mX, double mY) {
        if (checkDistance(mX, mY, this.mainMenuButtonColliderX, this.mainMenuButtonColliderY, this.mainMenuButtonColliderRadius)) {
            System.out.println("checkout - main menu");
            return "main_menu";
        } else if (checkDistance(mX, mY, this.nextLevelButtonColliderX, this.nextLevelButtonColliderY, this.nextLevelButtonColliderRadius)) {
            System.out.println("checkout - next level");
            return "next_level";
        } else {
            System.out.println("checkout - nothing");
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
