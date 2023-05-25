/*
 * Author: Paul
 * ID:
 *
 * Co-Author: Robert Tubman (Minor tweaking to merge with team code)
 * ID: 11115713
 * Co-Author: Lucass (Minor tweaks)
 * ID:
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
    private int oscillatingPromptMultiplier;
    private int oscillator;
    private boolean canDrawLine;
    private boolean oscillationDirection;

    // Constructor
    public LoadingPage(GameEngine engine, Image loadingImage, Image title, int titleW, int titleH, Image[] tips, int imagTipsLength, int windowWidth, int windowHeight) {
        this.engine = engine;

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        this.loadingImage = loadingImage;
        this.title = title;
        this.titleWidth = titleW;
        this.titleHeight = titleH;
        this.titleX = (windowWidth / 2) - (this.titleWidth / 2);
        this.titleY = windowHeight / 16;

        this.IMAGES_LENGTH = imagTipsLength;
        this.IMAGES = new Image[this.IMAGES_LENGTH];
        System.arraycopy(tips, 0, this.IMAGES, 0, this.IMAGES_LENGTH);

        this.currentTips = 0;
        this.progress = 0.0;
        this.isLoading = false;

        this.loadingBarWidth = 300;
        this.loadingBarHeight = 10;
        this.loadingBarX = (windowWidth / 2) - (this.loadingBarWidth / 2);
        this.loadingBarY = (windowHeight / 2) + (windowHeight / 3);

        this.tipsImageWidth = 450;
        this.tipsImageHeight = 100;
        this.tipsImageX = (windowWidth / 2) - (this.tipsImageWidth / 2);
        this.tipsImageY = (windowHeight / 2) + (windowHeight / 8);

        this.gameStartPrompt = "Press Space to Start";
        this.gameStartPromptWidth = 240;
        this.gameStartPromptHeight = 26;
        this.linePosX = (windowWidth / 2) - (this.gameStartPromptWidth / 2);
        this.linePosY = (windowHeight / 2) + (windowHeight / 4);
        this.oscillatingPromptMultiplier = 0;
        this.oscillator = 0;
        this.gameStartPromptWidthDefault = 240;
        this.canDrawLine = false;
        this.oscillationDirection = false;
    }


    //**************************************************
    // Getters
    //**************************************************
    public double getProgress() { return this.progress; }


    //**************************************************
    // Other methods
    //**************************************************
//    public void init() {
////        this.loadingImage = this.engine.loadImage("Assignment2/assets/image/background/background2.png");
////        this.title = this.engine.loadImage("Assignment2/assets/image/icon/icon_title1.png");
////        this.IMAGES[0] = this.engine.loadImage("Assignment2/assets/image/tip/icon_tip1.png");
////        this.IMAGES[1] = this.engine.loadImage("Assignment2/assets/image/tip/icon_tip2.png");
////        this.IMAGES[2] = this.engine.loadImage("Assignment2/assets/image/tip/icon_tip3.png");
//
//    }

    // Emulate loading page
    public void startLoading() {
        if (!this.isLoading) {
            // Start the timer to switch images every 2 seconds
            Timer timer = new Timer();
            timer.schedule(new SwitchImageTask(), 0, 1000);
            this.isLoading = true;
        }
    }

    // Reset the loading page so it can be reused
    public void resetLoadingPage() {
        this.currentTips = 0;
        this.progress = 0.0;
        this.isLoading = false;
    }

    public void drawLoadingImage() {
        this.engine.drawImage(this.loadingImage, 0, 0, this.engine.width(), this.engine.height());
    }

    public void drawTitle() {
        this.engine.drawImage(this.title, this.titleX, this.titleY, this.titleWidth, this.titleHeight);
    }

    public void drawLoadingBar() {
        this.engine.changeColor(Color.lightGray);
        this.engine.drawRectangle(this.loadingBarX, this.loadingBarY, this.loadingBarWidth, this.loadingBarHeight);

        this.engine.changeColor(Color.red);
        this.engine.drawSolidRectangle(this.loadingBarX, this.loadingBarY, (int) (this.loadingBarWidth * this.progress), this.loadingBarHeight);
    }

    public void drawCurrentImage() {
        this.engine.saveCurrentTransform();
        this.engine.drawImage(this.IMAGES[this.currentTips], this.tipsImageX, this.tipsImageY, this.tipsImageWidth, this.tipsImageHeight);
        this.engine.restoreLastTransform();
    }
    public void drawLine(){
        if (this.canDrawLine) {
            this.engine.changeColor(0, 0, 0);
            this.engine.drawText(this.linePosX, this.linePosY + this.gameStartPromptHeight, this.gameStartPrompt, "a", this.gameStartPromptHeight);

            this.engine.changeColor(255, 0, 0);
            this.engine.drawLine(this.linePosX,
                    this.linePosY,
                    this.linePosX + this.gameStartPromptWidth,
                    this.linePosY);
            this.engine.drawLine(this.linePosX,
                    this.linePosY + this.gameStartPromptHeight,
                    this.linePosX + this.gameStartPromptWidth,
                    this.linePosY + this.gameStartPromptHeight);
            this.engine.drawLine(this.linePosX,
                    this.linePosY,
                    this.linePosX,
                    this.linePosY + this.gameStartPromptHeight);
            this.engine.drawLine(this.linePosX + this.gameStartPromptWidth,
                    this.linePosY,
                    this.linePosX + this.gameStartPromptWidth,
                    this.linePosY + this.gameStartPromptHeight);
        }
    }

    public void updatePage(double dt) {
        // Loading bar speed
        this.progress += 0.01;
        if (this.progress > 1.0) {
            this.progress = 1.0;
            this.canDrawLine = true;
        }

//        if (this.canDrawLine) { updateLine(dt); }
    }

    // Oscillating number
    // Reference: https://stackoverflow.com/questions/3671160/how-to-use-a-sine-cosine-wave-to-return-an-oscillating-number
//    public void updateLine(double dt) {
////        Number1 = (int)(Math.sin(_pos*2*Math.PI/PERIOD)*(SCALE/2) + (SCALE/2));
//        final int period = 6;
//        final int scale = 6;
//        int value = (int)(Math.sin(this.oscillator * 2 * Math.PI / period) * (scale / 2));
//        if (this.oscillationDirection) {
//            this.oscillator += 1;
//        } else {
//            this.oscillator -= 1;
//        }
//
//        if (this.oscillator > period) { this.oscillationDirection = false; }
//        if (this.oscillator < 0) { this.oscillationDirection = true; }
//
//        this.oscillatingPromptMultiplier = value + this.gameStartPromptHeight;
//        this.gameStartPromptWidth = this.gameStartPromptWidthDefault - this.gameStartPromptHeight + this.oscillatingPromptMultiplier;
//        this.gameStartPromptHeight = this.oscillatingPromptMultiplier;
//        this.linePosX = (this.windowWidth / 2) - (this.gameStartPromptWidth / 2);
//        this.linePosY = (this.windowHeight / 2) + (this.windowHeight / 4) - value;
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
