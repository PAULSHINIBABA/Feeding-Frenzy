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

    public void init() {
        this.loadingImage = this.engine.loadImage("Assignment2/assets/image/B2.png");
        this.title = this.engine.loadImage("Assignment2/assets/image/title.png");
        this.images[0] = this.engine.loadImage("Assignment2/assets/image/Warning.png");
        this.images[1] = this.engine.loadImage("Assignment2/assets/image/Tip1.png");
        this.images[2] = this.engine.loadImage("Assignment2/assets/image/Tip2.png");

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
        this.engine.drawImage(this.title, 110, 0, 300, 150);
        this.engine.restoreLastTransform();
    }

    public void drawLoadingBar() {
        int x = 100;
        int y = 400;
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
        int imageX = 25;
        int imageY = 300;

        this.engine.saveCurrentTransform();
        this.engine.drawImage(this.images[this.currentTips], imageX, imageY, imageWidth, imageHeight);
        this.engine.restoreLastTransform();
    }

    public void updatePage(double dt) {
        // Loading bar speed
        this.progress += 0.01;
        if (this.progress > 1.0) {
            this.progress = 1.0;
        }
    }

//    @Override
//    public void paintComponent() {
    public void drawAll() {
        this.engine.setWindowSize(500, 500);
        drawLoadingImage();
        drawTitle();
        drawLoadingBar();
        drawCurrentImage();
    }

//    @Override
//    public int width() {
//        return 500;
//    }

//    @Override
//    public int height() {
//        return 500;
//    }

    class SwitchImageTask extends TimerTask {
        @Override
        public void run() {
            currentTips = (currentTips + 1) % (images.length - 1);
        }
    }
}
