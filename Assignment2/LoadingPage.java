/*
 * Author: Paul (Zeju Fan)
 * ID:
 *
 * Co-Author: Robert Tubman (Major refactoring to merge with team code)
 * ID: 11115713
 * Co-Author: Lucass (Minor refactoring)
 * ID: 21008041
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
//    private int oscillatingPromptMultiplier;
//    private int oscillator;
    private boolean canDrawLine;
//    private boolean oscillationDirection;

    // Constructor
    public LoadingPage(GameEngine engine, Image loadingImage, Image title, int titleW, int titleH, Image[] tips, int imagTipsLength, int windowWidth, int windowHeight) {
        this.engine = engine;

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        this.loadingImage = loadingImage;
        this.title = title;
        titleWidth = titleW;
        titleHeight = titleH;
        titleX = (windowWidth / 2) - (titleWidth / 2);
        titleY = windowHeight / 16;

        IMAGES_LENGTH = imagTipsLength;
        IMAGES = new Image[IMAGES_LENGTH];
        System.arraycopy(tips, 0, IMAGES, 0, IMAGES_LENGTH);

        currentTips = 0;
        progress = 0.0;
        isLoading = false;

        loadingBarWidth = 300;
        loadingBarHeight = 10;
        loadingBarX = (windowWidth / 2) - (loadingBarWidth / 2);
        loadingBarY = (windowHeight / 2) + (windowHeight / 3);

        tipsImageWidth = 450;
        tipsImageHeight = 100;
        tipsImageX = (windowWidth / 2) - (tipsImageWidth / 2);
        tipsImageY = (windowHeight / 2) + (windowHeight / 8);

        gameStartPrompt = "Press Space to Start";
        gameStartPromptWidth = 240;
        gameStartPromptHeight = 26;
        linePosX = (windowWidth / 2) - (gameStartPromptWidth / 2);
        linePosY = (windowHeight / 2) + (windowHeight / 4);
//        oscillatingPromptMultiplier = 0;
//        oscillator = 0;
        gameStartPromptWidthDefault = 240;
        canDrawLine = false;
//        oscillationDirection = false;
    }


    //**************************************************
    // Getters
    //**************************************************
    public double getProgress() { return progress; }


    //**************************************************
    // Other methods
    //**************************************************
    // Emulate loading page
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

    public void drawLoadingImage() {
        engine.drawImage(loadingImage, 0, 0, engine.width(), engine.height());
    }

    public void drawTitle() {
        engine.drawImage(title, titleX, titleY, titleWidth, titleHeight);
    }

    public void drawLoadingBar() {
        engine.changeColor(Color.lightGray);
        engine.drawRectangle(loadingBarX, loadingBarY, loadingBarWidth, loadingBarHeight);

        engine.changeColor(Color.red);
        engine.drawSolidRectangle(loadingBarX, loadingBarY, (int) (loadingBarWidth * progress), loadingBarHeight);
    }

    public void drawCurrentImage() {
        engine.saveCurrentTransform();
        engine.drawImage(IMAGES[currentTips], tipsImageX, tipsImageY, tipsImageWidth, tipsImageHeight);
        engine.restoreLastTransform();
    }
    public void drawLine(){
        if (canDrawLine) {
            engine.changeColor(0, 0, 50);
            engine.drawText(linePosX, linePosY + gameStartPromptHeight, gameStartPrompt, "a", gameStartPromptHeight);

//            engine.changeColor(255, 0, 0);
//            engine.drawLine(linePosX,
//                    linePosY,
//                    linePosX + gameStartPromptWidth,
//                    linePosY);
//            engine.drawLine(linePosX,
//                    linePosY + gameStartPromptHeight,
//                    linePosX + gameStartPromptWidth,
//                    linePosY + gameStartPromptHeight);
//            engine.drawLine(linePosX,
//                    linePosY,
//                    linePosX,
//                    linePosY + gameStartPromptHeight);
//            engine.drawLine(linePosX + gameStartPromptWidth,
//                    linePosY,
//                    linePosX + gameStartPromptWidth,
//                    linePosY + gameStartPromptHeight);
        }
    }

    public void updatePage(double dt) {
        // Loading bar speed
        progress += 0.01;
        if (progress > 1.0) {
            progress = 1.0;
            canDrawLine = true;
        }

//        if (canDrawLine) { updateLine(dt); }
    }

    // Oscillating number
    // Reference: https://stackoverflow.com/questions/3671160/how-to-use-a-sine-cosine-wave-to-return-an-oscillating-number
//    public void updateLine(double dt) {
////        Number1 = (int)(Math.sin(_pos*2*Math.PI/PERIOD)*(SCALE/2) + (SCALE/2));
//        final int period = 6;
//        final int scale = 6;
//        int value = (int)(Math.sin(oscillator * 2 * Math.PI / period) * (scale / 2));
//        if (oscillationDirection) {
//            oscillator += 1;
//        } else {
//            oscillator -= 1;
//        }
//
//        if (oscillator > period) { oscillationDirection = false; }
//        if (oscillator < 0) { oscillationDirection = true; }
//
//        oscillatingPromptMultiplier = value + gameStartPromptHeight;
//        gameStartPromptWidth = gameStartPromptWidthDefault - gameStartPromptHeight + oscillatingPromptMultiplier;
//        gameStartPromptHeight = oscillatingPromptMultiplier;
//        linePosX = (windowWidth / 2) - (gameStartPromptWidth / 2);
//        linePosY = (windowHeight / 2) + (windowHeight / 4) - value;
//    }

    public void drawAll() {
        drawLoadingImage();
        drawTitle();
        drawLoadingBar();
        drawCurrentImage();
        drawLine();
    }

    class SwitchImageTask extends TimerTask {
        @Override
        public void run() {
            currentTips = (currentTips + 1) % (IMAGES.length - 1);
        }
    }
}
