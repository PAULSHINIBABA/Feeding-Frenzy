package Assignment2;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

//public class Loading_Page extends GameEngine {
public class Loading_Page {
//    private Main_program mainProgram;

//    public Loading_Page(Main_program mainProgram) {
//        this.mainProgram = mainProgram;
//    }
    private GameEngine engine;
    public Loading_Page(GameEngine engine) {
//        this.mainProgram = mainProgram;
        this.engine = engine;
    }
    Image loadingImage;
    Image title;

    Image[] images = new Image[3];
    int currentTips = 0;

    double progress;

    public void init() {
        loadingImage = this.engine.loadImage("src/Assignment2/NEW IMAGE/B2.png");
        title = this.engine.loadImage("src/Assignment2/NEW IMAGE/title.png");
        images[0] = this.engine.loadImage("src/Assignment2/NEW IMAGE/Warning.png");
        images[1] = this.engine.loadImage("src/Assignment2/NEW IMAGE/Tip1.png");
        images[2] = this.engine.loadImage("src/Assignment2/NEW IMAGE/Tip2.png");

        // Start the timer to switch images every 2 seconds
        Timer timer = new Timer();
        timer.schedule(new SwitchImageTask(), 0, 1000);
    }

    public void drawLoadingImage() {
        this.engine.saveCurrentTransform();
        this.engine.drawImage(loadingImage, 0, 0, this.engine.width(), this.engine.height());
        this.engine.restoreLastTransform();
    }

    public void drawTitle() {
        this.engine.saveCurrentTransform();
        this.engine.drawImage(title, 110, 0, 300, 150);
        this.engine.restoreLastTransform();
    }

    public void drawloadingBar() {
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
        drawloadingBar();
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
