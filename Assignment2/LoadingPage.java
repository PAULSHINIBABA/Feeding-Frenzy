/*
 * Author: Paul
 * ID:
 *
 * Co-Author: Robert Tubman (Minor tweaking to merge with team code)
 * ID: 11115713
 */

package Assignment2;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingPage {
    // Final fields
    private final Image[] images = new Image[3];

    // Non-final fields
    private GameEngine engine;
    private Image loadingImage;
    private Image title;
    private int currentTips;
    private double progress;
    private boolean isLoading;

    // Constructor
    public LoadingPage(GameEngine engine) {
        this.engine = engine;

        this.currentTips = 0;
        this.progress = 0.0;
        this.isLoading = false;

        this.init();
    }


    //**************************************************
    // Getters
    //**************************************************
    public double getProgress() { return this.progress; }


    //**************************************************
    // Other methods
    //**************************************************
    public void init() {
        this.loadingImage = this.engine.loadImage("Assignment2/assets/image/background/background2.png");
        this.title = this.engine.loadImage("Assignment2/assets/image/icon/icon_title1.png");
        this.images[0] = this.engine.loadImage("Assignment2/assets/image/tip/icon_tip1.png");
        this.images[1] = this.engine.loadImage("Assignment2/assets/image/tip/icon_tip2.png");
        this.images[2] = this.engine.loadImage("Assignment2/assets/image/tip/icon_tip3.png");

    }

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
        this.engine.saveCurrentTransform();
        this.engine.drawImage(this.loadingImage, 0, 0, this.engine.width(), this.engine.height());
        this.engine.restoreLastTransform();
    }

    public void drawTitle() {
        this.engine.saveCurrentTransform();
        this.engine.drawImage(this.title, 230, 0, 300, 150);
        this.engine.restoreLastTransform();
    }

    public void drawLoadingBar() {
        int x = 220;
        int y = 550;
        int width = 300;
        int height = 10;

        this.engine.changeColor(Color.lightGray);
        this.engine.drawRectangle(x, y, width, height);

        this.engine.changeColor(Color.red);
        this.engine.drawSolidRectangle(x, y, (int) (width * this.progress), height);
    }

    public void drawCurrentImage() {
        int imageWidth = 450;
        int imageHeight = 100;
        int imageX = 145;
        int imageY = 400;

        this.engine.saveCurrentTransform();
        this.engine.drawImage(this.images[this.currentTips], imageX, imageY, imageWidth, imageHeight);
        this.engine.restoreLastTransform();
    }
    public void drawLine(){
        int linePosX = 230;
        int linePosY = 650;

        this.engine.drawText(linePosX, linePosY, "Press Space to start!!", "a", 26);
    }

    public void updatePage(double dt) {
        // Loading bar speed
        this.progress += 0.01;
        if (this.progress > 1.0) {
            this.progress = 1.0;
        }
    }

    public void drawAll() {
//        this.engine.setWindowSize(500, 500);
        drawLoadingImage();
        drawTitle();
        drawLoadingBar();
        drawCurrentImage();
        drawLine();
    }

    class SwitchImageTask extends TimerTask {
        @Override
        public void run() {
            currentTips = (currentTips + 1) % (images.length - 1);
        }
    }
}
