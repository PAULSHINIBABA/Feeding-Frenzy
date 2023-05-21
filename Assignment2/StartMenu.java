package Assignment2;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

//public class StartMenu extends GameEngine {
public class StartMenu {
    // Fields
    private Clip musicClip;
    private boolean isMusicPlaying;
//    private Main_program mainProgram;  // The main program that controls this start menu

    // Image of the background
    private Image background;
    private double backgroundX;
    private double backgroundY;
    private double backgroundWidth;
    private double backgroundHeight;
    private Image title;
    private double titleX;
    private double titleY;
    private double titleWidth;
    private double titleHeight;
    private Image musicButton;
    private double musicButtonX;
    private double musicButtonY;
//    private double musicButtonRadius;
    private double musicButtonWidth;
    private double musicButtonHeight;
    private final int BUTTON_IMAGES_SIZE;
    private Image[] buttonImages;

    // Variables for mouse click position
    private final double[] buttonXPositions;
    private final double[] buttonYPositions;
    private double buttonWidth;
    private double buttonHeight;
    private double buttonXOffset;
    private double buttonYOffset;
    private boolean[] buttonClicked;
    private File soundFile;

    // Constructor
    //    public StartMenu(Main_program mainProgram) {
//        this.mainProgram = mainProgram;
//    }
//    public StartMenu() {
////        this.mainProgram = mainProgram;
//    }
    public StartMenu(double bx, double by, double bw, double bh,
                     double tx, double ty, double tw, double th,
                     double butW, double butH, double bxo, double byo,
                     double mbx, double mby, double mbw, double mbh) {
        // Final fields initialize
        this.BUTTON_IMAGES_SIZE = 5;

        // Fields initialize
        this.background = null;
        this.title = null;
        this.musicButton = null;
        this.soundFile = null;

//        this.buttonXPositions = new double[]{150.0, 300.0, 140, 280.0, 20.0};
        this.buttonXPositions = new double[]{100.0, 250.0, 90, 230.0, 20.0};
        this.buttonYPositions = new double[]{180.0, 210.0, 280.0, 320.0, 500.0};
        this.buttonClicked = new boolean[]{false, false, false, false, false};
        this.buttonImages = new Image[this.BUTTON_IMAGES_SIZE];

        this.isMusicPlaying = false;

        this.backgroundX = bx;
        this.backgroundY = by;
        this.backgroundWidth = bw;
        this.backgroundHeight = bh;
        this.titleX = tx;
        this.titleY = ty;
        this.titleWidth = tw;
        this.titleHeight = th;
        this.buttonWidth = butW;
        this.buttonHeight = butH;
        this.buttonXOffset = bxo;
        this.buttonYOffset = byo;
        this.musicButtonX = mbx;
        this.musicButtonY = mby;
//        this.musicButtonRadius = mbr;
        this.musicButtonWidth = mbw;
        this.musicButtonHeight = mbh;
    }



    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void SetBackgroundImage(Image background) throws IllegalArgumentException {
        if (background == null) {
            throw new IllegalArgumentException("Background image cannot be null");
        }
        this.background = background;
    }
    public void SetBackgroundPos(double x, double y) {
        this.backgroundX = x;
        this.backgroundY = y;
    }
    public void SetTitleImage(Image title) { this.title = title; }
    public void SetTitlePos(double x, double y) {
        this.titleX = x;
        this.titleY = y;
    }
    public void SetButtonImages(Image[] buttonImages) throws IllegalArgumentException {
        if (buttonImages.length < this.BUTTON_IMAGES_SIZE) { throw new IllegalArgumentException("There must be 5 button images"); }
        for (int i = 0; i < this.BUTTON_IMAGES_SIZE; i++) { this.buttonImages[i] = buttonImages[i]; }
    }
    public void SetMusicButtonImage(Image musicImage) { this.musicButton = musicImage; }
    public void SetMusicButtonPos(double x, double y) {
        this.musicButtonX = x;
        this.musicButtonY = y;
    }
    public void SetMusicFile(String path) {
        try {
            //import the music file
//            this.soundFile = new File("src/Assignment2/Music/MENU.wav");
            this.soundFile = new File(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(this.soundFile);

            //get the music format
            AudioFormat format = audioIn.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            this.musicClip = (Clip) AudioSystem.getLine(info);
            this.musicClip.open(audioIn);

        } catch (Exception e) {
            // If the audio file failed to load, this the musicClip is null
            e.printStackTrace();
        }
    }


    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    //draw background
//    public void drawBackground() {
//        saveCurrentTransform();
//        drawImage(background, 0, 0, 500, 500);
//        restoreLastTransform();
//    }
//    //draw title
//    public void drawTitle() {
//        saveCurrentTransform();
//        drawImage(title, 110, 0, 300, 150);
//        restoreLastTransform();
//    }
//    //draw button
//    public void drawButton() {
//        for (int i = 0; i < 5; i++) {
//            int buttonX = ButtonXPositions[i];
//            int buttonY = ButtonYPositions[i];
//
//            drawImage(buttonImages[i], buttonX-30, buttonY - 30, 120, 50);
//        }
//        drawImage(MUSICbutton,450,450,50,50);
//    }
    public Image GetBackgroundImage() { return this.background; }
    public double GetBackgroundX() { return this.backgroundX; }
    public double GetBackgroundY() { return this.backgroundY; }
    public double GetBackgroundWidth() { return this.backgroundWidth; }
    public double GetBackgroundHeight() { return this.backgroundHeight; }
    public Image GetTitleImage() { return this.title; }
    public double GetTitleX() { return this.titleX; }
    public double GetTitleY() { return this.titleY; }
    public double GetTitleWidth() { return this.titleWidth; }
    public double GetTitleHeight() { return this.titleHeight; }
    public Image[] GetButtonImages() { return this.buttonImages; }
    public Image GetButtonImageByIndex(int i) throws IllegalArgumentException {
        if ((i < 0) || (i >= this.buttonXPositions.length)) {
            throw new IllegalArgumentException("Index for buttons out of range");
        }
        return this.buttonImages[i];
    }
    public int GetNumberOfButtons() { return this.buttonXPositions.length; }
    public double[] GetButtonXs() { return this.buttonXPositions; }
    public double GetButtonXByIndex(int i) throws IllegalArgumentException {
        if ((i < 0) || (i >= this.buttonXPositions.length)) {
            throw new IllegalArgumentException("Index for buttons out of range");
        }
        return this.buttonXPositions[i] - this.buttonXOffset;
    }
//    public double GetButtonXOffset() { return this.buttonXOffset; }
    public double[] GetButtonYs() { return this.buttonYPositions; }
    public double GetButtonYByIndex(int i) {
        if ((i < 0) || (i >= this.buttonYPositions.length)) {
            throw new IllegalArgumentException("Index for buttons out of range");
        }
        return this.buttonYPositions[i] - this.buttonYOffset;
    }
//    public double GetButtonYOffset() { return this.buttonYOffset; }
    public double GetButtonsWidth() { return this.buttonWidth; }
    public double GetButtonsHeight() { return this.buttonHeight; }
    public Image GetMusicButton() { return this.musicButton; }
    public double GetMusicButtonX() { return this.musicButtonX; }
    public double GetMusicButtonY() { return this.musicButtonY; }
    public double GetMusicButtonWidth() { return this.musicButtonWidth; }
    public double GetMusicButtonHeight() { return this.musicButtonHeight; }
//    public double GetMusicButtonRadius() { return this.musicButtonRadius; }



    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
//    @Override
//    public void mouseClicked(MouseEvent e) {
    public int MenuMouseClicked(double mouseX, double mouseY) {
//        double mouseX = e.getX();
//        double mouseY = e.getY();
        for (int i = 0; i < 5; i++) {
            double buttonX = this.buttonXPositions[i];
            double buttonY = this.buttonYPositions[i];
            double radius = 50;

            if (clickButton(mouseX, mouseY, buttonX, buttonY, radius)) {
                // Quit game was pressed.
                if (i == 3) {
                    // Click the Exit and wait 1 second then Exit the program
                    Timer timer = new Timer(1000, e -> System.exit(0));
                    timer.setRepeats(false);
                    timer.start();
                } else {
                    // Otherwise toggle? (toggle to what?)
                    this.buttonClicked[i] = !this.buttonClicked[i];
                }
            }
            // Handle TIME ATTACK(?) click
            if (i == 0 && this.buttonClicked[i]) {
                // TODO: Should not be calling "startMainGame", because this creates a new game instance.
//                mainProgram.startMainGame();
//                Loading_Page loadingPage = new Loading_Page(mainProgram);
//                createGame(loadingPage); // Should not create a new instance of the game
                return i; // Menu for ...

            }
            // Handle SINGLE PLAYER click
            if(i == 1 && this.buttonClicked[i]) {
                // TODO: Should not be calling "startMainGame", because this creates a new game instance.
//                mainProgram.startMainGame();
//                Loading_Page loadingPage = new Loading_Page(mainProgram);
//                createGame(loadingPage); // Should not create a new instance of the game
                return i; // Menu for ...
            }
        }

        // Handle music button click
        double musicButtonX = 475;
        double musicButtonY = 475;
        double musicButtonRadius = 25;
        if (clickButton(mouseX, mouseY, musicButtonX, musicButtonY, musicButtonRadius)) { toggleMusic(); }

        // Otherwise the method ends
        return 5; // Return a value that isn't used. (the for loop above is from 0-4)
    }
//check the mouse location
    public boolean clickButton(double mouseX, double mouseY, double buttonX, double buttonY, double radius) {
        double dx = mouseX - buttonX;
        double dy = mouseY - buttonY;
        return (dx * dx + dy * dy) <= (radius * radius);
    }
//music play

    public void playMusic() {
        try {
            this.musicClip.start();
            this.isMusicPlaying = true;

        }
        catch (Exception e) {
            // If the audio file failed to load, this the musicClip is null
            e.printStackTrace();
        }
    }
    //control the music play
    public void pauseMusic() {
        try {
            this.musicClip.stop();
            this.isMusicPlaying = false;

        } catch (Exception e) {
            // If the audio file failed to load, this the musicClip is null
            e.printStackTrace();
        }
    }

//    @Override
//    public void init() {
//        background = loadImage("src/Assignment2/NEW IMAGE/B1.png");
//        title = loadImage("src/Assignment2/NEW IMAGE/title.png");
//
//        buttonImages[0] = loadImage("src/Assignment2/NEW IMAGE/StartButton.png");
//        buttonImages[1] = loadImage("src/Assignment2/NEW IMAGE/TIMERACE.png");
//        buttonImages[2] = loadImage("src/Assignment2/NEW IMAGE/SETTINGS.png");
//        buttonImages[3] = loadImage("src/Assignment2/NEW IMAGE/QUIT.png");
//        buttonImages[4] = loadImage("src/Assignment2/NEW IMAGE/HELP.png");
//        MUSICbutton = loadImage("src/Assignment2/NEW IMAGE/MUSICbutton.png");
//
//        playMusic();
//    }
    public void startMusic() {
        playMusic();
    }

    // TODO: Extract out to settings.
    public void toggleMusic() {
        if (isMusicPlaying) {
            pauseMusic();
        } else {
            playMusic();
        }
//        isMusicPlaying = !isMusicPlaying;
    }

//    @Override
//    public void update(double dt) {
//
//    }

//    @Override
//    public void paintComponent() {
//        setWindowSize(500, 500);
//        drawBackground();
//        drawTitle();
//        drawButton();
//    }

//    @Override
//    public int width() {
//        return 500; // Set the window width to 500 pixels
//    }

//    @Override
//    public int height() {
//        return 500; // Set the window height to 500 pixels
//    }
}
