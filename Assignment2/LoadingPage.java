package Assignment2;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class LoadingPage {
//    private Main_program mainProgram;
    private final int TIP_IMAGES_SET;
    private Image loadingImage;
    private double loadingImageX;
    private double loadingImageY;
    private double loadingImageWidth;
    private double loadingImageHeight;
    private double loadingBarX;
    private double loadingBarY;
    private double loadingBarWidth;
    private double loadingBarHeight;
    private Image title;
    private double titleX;
    private double titleY;
    private double titleWidth;
    private double titleHeight;
    private Image[] images;
    private int currentTips;
    private double progress;
    private long tipsDuration;

//    //    public Loading_Page(Main_program mainProgram) {
////        this.mainProgram = mainProgram;
////    }
//    public Loading_Page() {
////        this.mainProgram = mainProgram;
//    }
    public LoadingPage() {
        // Set final fields
        this.TIP_IMAGES_SET = 3;

        // Set fields
        this.images = new Image[TIP_IMAGES_SET];
        this.currentTips = 0;
        this.tipsDuration = 2000;
    }



    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void SetLoadingImage(Image loadingImage) { this.loadingImage = loadingImage; }
    public void SetLoadingImageX(double x) { this.loadingImageX = x;}
    public void SetTitleImage(Image titleImage) { this.title = titleImage; }
    public void SetTipImages(Image[] tipImages) throws IllegalArgumentException {
        if (tipImages.length < this.TIP_IMAGES_SET) { throw new IllegalArgumentException("The number of images cannot be less than 3"); }
        for (int i = 0; i < this.TIP_IMAGES_SET; i++) { this.images[i] = tipImages[i]; }
    }


    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public Image GetTitleImage() { return this.title; }
    public Image GetLoadingImage() { return this.loadingImage; }
    public Image[] GetTipImages() { return this.images; }
    public Image GetCurrentTipByIndex(int i) throws IllegalArgumentException {
        if ((i < 0) || (i >= this.TIP_IMAGES_SET)) { throw new IllegalArgumentException("Invalid index"); }
        return this.images[i];
    }
    public Image GetCurrentTip() { return this.images[this.currentTips]; }
    public double GetProgress() { return this.progress; }



    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------

//    public void init() {
    // TODO: reuse
//        loadingImage = loadImage("src/Assignment2/NEW IMAGE/B2.png");
//        title = loadImage("src/Assignment2/NEW IMAGE/title.png");
//        images[0] = loadImage("src/Assignment2/NEW IMAGE/Warning.png");
//        images[1] = loadImage("src/Assignment2/NEW IMAGE/Tip1.png");
//        images[2] = loadImage("src/Assignment2/NEW IMAGE/Tip2.png");
//
    // Call this every loading image update loop to change the tip images
    public void SwitchImage() {
        // Start the timer to switch images every 2 seconds
        Timer timer = new Timer();
        timer.schedule(new LoadingPage.SwitchImageTask(), 0, this.tipsDuration);
    }
//        // Start the timer to switch images every 2 seconds
//        Timer timer = new Timer();
//        timer.schedule(new Loading_Page.SwitchImageTask(), 0, 1000);
//    }
//
//    public void drawLoadingImage() {
//        saveCurrentTransform();
    // TODO: reuse
//        drawImage(loadingImage, 0, 0, 500, 500);
//        restoreLastTransform();
//    }
//
//    public void drawTitle() {
//        saveCurrentTransform();
    // TODO: reuse
//        drawImage(title, 110, 0, 300, 150);
//        restoreLastTransform();
//    }
//
//    public void drawloadingBar() {
    // TODO: reuse
//        int x = 100;
//        int y = 400;
//        int width = 300;
//        int height = 10;
//
//        changeColor(Color.lightGray);
//        drawRectangle(x, y, width, height);
////
    // TODO: reuse
//        changeColor(Color.red);
//        drawSolidRectangle(x, y, (int) (width * progress), height);
//    }
//
//    public void drawCurrentImage() {
    // TODO: reuse
//        int imageWidth = 450;
//        int imageHeight = 100;
//        int imageX = 25;
//        int imageY = 300;
//
//        saveCurrentTransform();
//        drawImage(images[currentTips], imageX, imageY, imageWidth, imageHeight);
//        restoreLastTransform();
//    }

    // TODO: Reuse
    public void UpdateProgress() {
        this.progress += 0.01;
        if (this.progress > 1.0) {
            this.progress = 1.0;
        }
    }
//    @Override
//    public void update(double dt) {
//        // Loading bar speed
//        progress += 0.01;
//        if (progress > 1.0) {
//            progress = 1.0;
//        }
//    }

//    @Override
//    public void paintComponent() {
//        setWindowSize(500, 500);
//        drawLoadingImage();
//        drawTitle();
//        drawloadingBar();
//        drawCurrentImage();
//    }

//    @Override
//    public int width() {
//        return 500;
//    }
//
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
