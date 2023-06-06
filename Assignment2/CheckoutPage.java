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
    private int pearlsEaten;
    private double pEatX;
    private double pEatY;
    private int starfishEaten;
    private double sfEatX;
    private double sfEatY;
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
        enemyTypes = 3;
        enemiesEaten = new int[enemyTypes];

        // Non-final fields
        setBackgroundImage(background);
        setContinueButtonImage(continueButtonImage);
        setBackButtonImage(backButtonImage);
        drawFonts = 28;
        imageX = 0;
        imageY = 0;
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
        displayOffsetX = this.backgroundWidth / 4.0;
        displayOffsetY = 20;
        score = 0;
        scoreX = (this.backgroundWidth / 2.0) - displayOffsetX;
        scoreY = (this.backgroundHeight / 3.0);

        pearlsEaten = 0;
        pEatX = (this.backgroundWidth / 2.0) - displayOffsetX;
        pEatY = scoreY + drawFonts + displayOffsetY;

        starfishEaten = 0;
        sfEatX = (this.backgroundWidth / 2.0) - displayOffsetX;
        sfEatY = pEatY + drawFonts + displayOffsetY;

        double offset = 50;
        double buttonSize = 50;
        mainMenuButtonColliderWidth = buttonSize * 2;
        mainMenuButtonColliderHeight = buttonSize * 2;
        mainMenuButtonColliderX = offset;
        mainMenuButtonColliderY = backgroundHeight - offset - mainMenuButtonColliderHeight;
        mainMenuButtonColliderRadius = buttonSize;
        nextLevelButtonColliderWidth = buttonSize * 2;
        nextLevelButtonColliderHeight = buttonSize * 2;
        nextLevelButtonColliderX = this.backgroundWidth - offset - nextLevelButtonColliderWidth;
        nextLevelButtonColliderY = this.backgroundHeight - offset - nextLevelButtonColliderHeight;
        nextLevelButtonColliderRadius = buttonSize;
        restartButton = false;

        enemiesEatenXOffset = (this.backgroundWidth / 2.0) + displayOffsetX;
        enemiesEatenYOffset = drawFonts + displayOffsetY;
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
        enemyTypes = types;
        if (reset) { setEnemiesEaten(); }
    }
    public void setEnemiesEaten() {
        enemiesEaten = new int[enemyTypes];
        for (int i = 0; i < enemyTypes; i++) { enemiesEaten[i] = 0; }
    }
    public void setEnemiesEaten(int[] enemiesEaten) {
        this.enemiesEaten = enemiesEaten;
    }
    public void setEnemyEatenBySize(int size) {
        if (size < 0 || size >= enemyTypes) {
            throw new IllegalArgumentException("Cannot eat a enemy of size that doesn't exist");
        }
        enemiesEaten[size] += 1;
    }
    public void setBackgroundWidth(int width) { backgroundWidth = width; }
    public void setBackgroundHeight(int height) { backgroundHeight = height; }
    public void setRestartButton() {
        restartButton = true;
    }
    public void setRestartButton(boolean state) { restartButton = state; }
    public void setEnemyBaseSize(int w, int h) {
        enemyBaseWidth = w;
        enemyBaseHeight = h;
    }

    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public int getScore() { return score; }
    public int getPearlsEaten() { return pearlsEaten; }
    public int getStarfishEaten() { return starfishEaten; }
    public int getBackgroundWidth() { return backgroundWidth; }
    public int getBackgroundHeight() { return backgroundHeight; }

    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
    // draw all of the content in the checkout page
    public void drawCheckout(Image[] enemies) {
        drawBackground();
        drawScore();
        drawEnemiesEaten(enemies);
        drawPearlsEaten();
        drawStarfishEaten();

        drawReturnToMainMenuButton();
        drawNextLevelButton();
        drawCheckoutLine();

        // Debug: draw the buttons colliders
//        drawButtonColliders();
    }

    // Draw the levels background
    public void drawBackground() {
        engine.drawImage(background, imageX, imageY, backgroundWidth, backgroundHeight);
    }

    // Draw the players score
    public void drawScore() {
        engine.drawText(scoreX, scoreY, ("Score: " + score), "Sans Serif", drawFonts);
    }

    // Draw the number of enemies eaten in the level
    public void drawEnemiesEaten(Image[] enemies) {
        double yOffsetsImage = (backgroundHeight / 3.0) - drawFonts;
        double yOffsets = scoreY;
        double x1 = ((enemiesEatenXOffset/3)*2 - enemyBaseWidth);
        double x2 = x1 + (enemyBaseWidth * 2);
        double w = enemyBaseWidth*2;
        double h = enemyBaseHeight*2;
        for (int i = 0; i < enemyTypes; i++) {
            engine.drawImage(enemies[i], x1, yOffsetsImage, w, h);
            engine.drawText(x2, yOffsets, (": " + Integer.toString(enemiesEaten[i])), "Sans serif", drawFonts);
            yOffsets += enemiesEatenYOffset;
            yOffsetsImage += enemiesEatenYOffset;

//            drawLines(enemiesEatenXOffset, yOffsets, enemyBaseWidth, enemyBaseHeight);
        }
    }

    // Debug: Draw the colliders box
    public void drawLines(double x, double y, double w, double h) {
        engine.changeColor(255,0,0);
        engine.drawLine(x,y,x+w,y);
        engine.drawLine(x,y+h,x+w,y+h);
        engine.drawLine(x,y,x,y+h);
        engine.drawLine(x+w,y,x+w,y+h);
    }

    // Draw the number of pearls eaten
    public void drawPearlsEaten() {
        engine.changeColor(0,0,0);
        engine.drawText(pEatX, pEatY, ("Pearls: " + pearlsEaten), "Sans Serif", drawFonts);
    }

    // draw the number of starfish eaten
    public void drawStarfishEaten() {
        engine.changeColor(0,0,0);
        engine.drawText(sfEatX, sfEatY, ("Starfish: " + starfishEaten), "Sans Serif", drawFonts);
    }

    // Draw the button to return to the main menu
    public void drawReturnToMainMenuButton() {
        engine.drawImage(backButtonImage,
                mainMenuButtonColliderX,
                mainMenuButtonColliderY - mainMenuButtonColliderRadius,
                mainMenuButtonColliderWidth,
                mainMenuButtonColliderHeight);
    }

    // Draw the button for the next level
    public void drawNextLevelButton() {
        engine.drawImage(continueButtonImage,
                (nextLevelButtonColliderX + nextLevelButtonColliderWidth - mainMenuButtonColliderRadius),
                nextLevelButtonColliderY - mainMenuButtonColliderRadius,
                (-nextLevelButtonColliderWidth),
                nextLevelButtonColliderHeight);
    }

    // Debug: Draw the button colliders
    public void drawButtonColliders() {
        engine.changeColor(255,0,0);
        engine.drawCircle(mainMenuButtonColliderX + mainMenuButtonColliderRadius, mainMenuButtonColliderY, mainMenuButtonColliderRadius);
        engine.drawCircle(nextLevelButtonColliderX, nextLevelButtonColliderY, nextLevelButtonColliderRadius);
    }

    // Draw the text to show that the level was completed
    public void drawCheckoutLine() {
        double checkoutLineX = (backgroundWidth / 2.0) - displayOffsetX;
        double checkoutLineY = (backgroundHeight / 4.0);
        int fontSize = 32;
        engine.changeColor(0,0,0);
        engine.drawText(checkoutLineX, checkoutLineY, "STAGE COMPLETE!", "a", fontSize);
    }

    // Check if a button was clicked
    public String checkClickTarget(double mX, double mY) {
        if (checkDistance(mX, mY, mainMenuButtonColliderX + mainMenuButtonColliderRadius, mainMenuButtonColliderY, mainMenuButtonColliderRadius)) {
            return "main_menu";
        } else if (checkDistance(mX, mY, nextLevelButtonColliderX, nextLevelButtonColliderY, nextLevelButtonColliderRadius)) {
            if (restartButton) { return "restart"; }
            else { return "next_level"; }

        } else { return "nothing"; }
    }

    // Check the distance between the mouse and button (by radius)
    public boolean checkDistance(double x1, double y1, double x2, double y2, double r) {
        return Math.sqrt((Math.pow((x2 - x1), 2.0) + Math.pow((y2 - y1), 2.0))) <= r;
    }
}
