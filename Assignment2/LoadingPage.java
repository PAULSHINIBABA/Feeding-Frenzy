/*
 * Author: Paul (Zeju Fan)
 * ID: 21019135
 *
 * Co-Author: Robert Tubman (Major refactoring to merge with team code)
 * ID: 11115713
 *
 * Co-Author: Lucass (Minor refactoring)
 * ID: 21008041
 *
 * The LoadingPage class
 *
 * This class handles the switching of the context from the main menu to the in-game
 * context.
 */

package Assignment2;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingPage {
    // Final fields
    private final int IMAGES_LENGTH;
    private final Image[] IMAGES;

    // Non-final fields
    private GameEngine engine;
    private Image loadingImage;
    private Image title;
    private int titleX;
    private int titleY;
    private int titleWidth;
    private int titleHeight;
    private int currentTips;
    private double progress;
    private boolean isLoading;
    private int loadingBarX;
    private int loadingBarY;
    private int loadingBarWidth;
    private int loadingBarHeight;
    private int tipsImageWidth;
    private int tipsImageHeight;
    private int tipsImageX;
    private int tipsImageY;
    private int linePosX;
    private int linePosY;
    private String gameStartPrompt;
    private int gameStartPromptWidth;
    private int gameStartPromptHeight;
    private final int gameStartPromptWidthDefault;
    private int windowWidth;
    private int windowHeight;
    private boolean canDrawLine;

    // Constructor
    public LoadingPage(GameEngine engine, Image loadingImage, Image title, int titleW, int titleH, Image[] tips, int imagTipsLength, int windowWidth, int windowHeight) {
        this.engine = engine;

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        // title fields
        this.loadingImage = loadingImage;
        this.title = title;
        titleWidth = titleW;
        titleHeight = titleH;
        titleX = (windowWidth / 2) - (titleWidth / 2);
        titleY = windowHeight / 16;

        IMAGES_LENGTH = imagTipsLength;
        IMAGES = new Image[IMAGES_LENGTH];
        System.arraycopy(tips, 0, IMAGES, 0, IMAGES_LENGTH);

        // Loading bar and tip fields
        currentTips = 0;
        progress = 0.0;
        isLoading = false;
        loadingBarWidth = 300;
        loadingBarHeight = 10;
        loadingBarX = (windowWidth / 2) - (loadingBarWidth / 2);
        loadingBarY = (windowHeight / 2) + (windowHeight / 3);

        // Tips images fields
        tipsImageWidth = 450;
        tipsImageHeight = 100;
        tipsImageX = (windowWidth / 2) - (tipsImageWidth / 2);
        tipsImageY = (windowHeight / 2) + (windowHeight / 8);

        // Game start prompt fields
        gameStartPrompt = "Press Space to Start";
        gameStartPromptWidth = 240;
        gameStartPromptHeight = 26;
        linePosX = (windowWidth / 2) - (gameStartPromptWidth / 2);
        linePosY = (windowHeight / 2) + (windowHeight / 4);
        gameStartPromptWidthDefault = 240;
        canDrawLine = false;
    }


    //**************************************************
    // Getters
    //**************************************************
    public double getProgress() { return progress; }


    //**************************************************
    // Other methods
    //**************************************************
    // Emulate loading page
    // This loading page doesn't actually load anything.
    // It is a page to give the user a temporary reprieve between contexts
    public void startLoading() {
        if (!isLoading) {
            // Start the timer to switch images every 2 seconds
            Timer timer = new Timer();
            timer.schedule(new SwitchImageTask(), 0, 1000);
            isLoading = true;
        }
    }

    // Reset the loading page so it can be reused
    public void resetLoadingPage() {
        currentTips = 0;
        progress = 0.0;
        isLoading = false;
    }

    // Draw the loading page background
    public void drawLoadingImage() {
        engine.drawImage(loadingImage, 0, 0, engine.width(), engine.height());
    }

    // Draw the title
    public void drawTitle() {
        engine.drawImage(title, titleX, titleY, titleWidth, titleHeight);
    }

    // Draw the loading bar
    public void drawLoadingBar() {
        engine.changeColor(Color.lightGray);
        engine.drawRectangle(loadingBarX, loadingBarY, loadingBarWidth, loadingBarHeight);

        engine.changeColor(Color.red);
        engine.drawSolidRectangle(loadingBarX, loadingBarY, (int) (loadingBarWidth * progress), loadingBarHeight);
    }

    // Draw the current tip
    public void drawCurrentImage() {
        engine.saveCurrentTransform();
        engine.drawImage(IMAGES[currentTips], tipsImageX, tipsImageY, tipsImageWidth, tipsImageHeight);
        engine.restoreLastTransform();
    }

    // Draw the prompt to show the user that they can start the game
    public void drawLine(){
        if (canDrawLine) {
            engine.changeColor(0, 0, 50);
            engine.drawText(linePosX, linePosY + gameStartPromptHeight, gameStartPrompt, "a", gameStartPromptHeight);
        }
    }

    // Update the loading bar in the page
    public void updatePage(double dt) {
        // Loading bar speed
        progress += 0.01;
        if (progress > 1.0) {
            progress = 1.0;
            canDrawLine = true;
        }
    }

    // Draw all the images with this method
    public void drawAll() {
        drawLoadingImage();
        drawTitle();
        drawLoadingBar();
        drawCurrentImage();
        drawLine();
    }

    // Switch the displayed tip context
    class SwitchImageTask extends TimerTask {
        @Override
        public void run() {
            currentTips = (currentTips + 1) % (IMAGES.length - 1);
        }
    }
}
