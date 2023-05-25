/*
 * Author: Paul
 * ID:
 *
 * Co-Author: Robert Tubman (Tweaked to merge with team code)
 * ID: 11115713
 * Co-Author: Lucass (Minor tweaks)
 * ID:
 */

package Assignment2;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class StartMenu {
    // TODO: Should remove all the "Magic number" fields in the methods in the class
    private final GameEngine engine;
    private Clip musicClip;
    private boolean isMusicPlaying = true;

    // Image of the background
    private Image background;
    private Image title;
    private int titleXPos;
    private int titleYPos;
    private int titleWidth;
    private int titleHeight;
    private Image musicButton;
    private int musicButtonXPos;
    private int musicButtonYPos;
    private int musicButtonRadius;
    private int musicButtonImageWidth;
    private int musicButtonImageHeight;
    private File soundFile;
    private final int buttonImagesLength;
    private final Image[] buttonImages;
    private int windowWidth;
    private int windowHeight;

    // Variables for mouse click position
//    private final int[] BUTTON_X_POSITIONS = {260, 420, 260, 420, 60};
    private final int[] BUTTON_X_POSITIONS;
//    private final int[] BUTTON_Y_POSITIONS = {250, 340, 420, 500, 650};
    private final int[] BUTTON_Y_POSITIONS;
//    private int buttonOffset;
    private int buttonWidth;
    private int buttonHeight;
    private GameEngine.AudioClip music;
    private float volume;

    // Constructor
    public StartMenu(GameEngine engine,
                     Image background,
                     Image title,
                     int titleW,
                     int titleH,
                     Image[] buttonImages,
                     int buttonsImageLength,
                     Image musicButton,
                     int windowWidth,
                     int windowHeight) {
        this.engine = engine;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        this.background = background;

        this.buttonImagesLength = buttonsImageLength;
        this.buttonWidth = 140;
        this.buttonHeight = 50;
        this.buttonImages = buttonImages;
        int div16 = 16;
        int incrementY = this.windowHeight / div16;
        int offsetY = this.windowHeight / 3;
        int offsetX = this.windowWidth / div16;
        this.BUTTON_X_POSITIONS = new int[this.buttonImagesLength];
        this.BUTTON_Y_POSITIONS = new int[this.buttonImagesLength];
        for (int i = 0; i < this.buttonImagesLength - 1; i++) {
            if (i % 2 == 0) { // Even
                this.BUTTON_X_POSITIONS[i] = (this.windowWidth / 2) - offsetX - this.buttonWidth;
            } else { // Odd
                this.BUTTON_X_POSITIONS[i] = (this.windowWidth / 2) + offsetX;
            }
            this.BUTTON_Y_POSITIONS[i] = offsetY;
            offsetY += incrementY;
        }
        // Set the help menu position
        int buttonOffset = 10;
        this.BUTTON_X_POSITIONS[this.buttonImagesLength - 1] = buttonOffset;
        this.BUTTON_Y_POSITIONS[this.buttonImagesLength - 1] = this.windowHeight - this.buttonHeight - buttonOffset;

        this.title = title;
        this.titleWidth = titleW;
        this.titleHeight = titleH;
        this.titleXPos = (this.windowWidth / 2) - (this.titleWidth / 2);
        this.titleYPos = this.windowHeight / div16;

        this.musicButton = musicButton;
        this.volume = 1.0f;
//        this.musicButtonXPos = 475;
//        this.musicButtonYPos = 475;
        this.musicButtonRadius = 25;
        this.musicButtonImageWidth = 50;
        this.musicButtonImageHeight = 50;
        this.musicButtonXPos = this.windowWidth - this.musicButtonRadius - buttonOffset;
        this.musicButtonYPos = this.windowHeight - this.musicButtonRadius - buttonOffset;

//        this.init();
    }


    //-------------------------------------------------------
    // Setter
    //-------------------------------------------------------
    public void setTitleXPosition(int x) { this.titleXPos = x; }
    public void setTitleYPosition(int y) { this.titleYPos = y; }
    public void setMusicButtonXPosition(int x) { this.musicButtonXPos = x; }
    public void setMusicButtonYPosition(int y) { this.musicButtonYPos = y; }
    public void setButtonsXPositions(int[] newButtonPositions) throws IllegalArgumentException {
        if ((newButtonPositions.length > this.buttonImagesLength) || newButtonPositions.length < this.buttonImagesLength) {
            throw new IllegalArgumentException("The new buttons x positions array length is incorrect");
        }
        System.arraycopy(newButtonPositions, 0, BUTTON_X_POSITIONS, 0, this.buttonImagesLength);
    }
    public void setButtonsYPositions(int[] newButtonPositions) throws IllegalArgumentException {
        if ((newButtonPositions.length > this.buttonImagesLength) || newButtonPositions.length < this.buttonImagesLength) {
            throw new IllegalArgumentException("The new buttons y positions array length is incorrect");
        }
        System.arraycopy(newButtonPositions, 0, BUTTON_Y_POSITIONS, 0, this.buttonImagesLength);
    }
    public void setButtonsXPositionAt(int index, int x) throws IllegalArgumentException {
        if (index < 0 || index >= this.buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons x position is out of bounds");
        }
        this.BUTTON_X_POSITIONS[index] = x;
    }
    public void setButtonsYPositionAt(int index, int y) {
        if (index < 0 || index >= this.buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons y position is out of bounds");
        }
        this.BUTTON_Y_POSITIONS[index] = y;
    }
    public void setMusicFile(String audioPath) {
        try {
            this.soundFile = new File(audioPath);

        } catch(Exception e) {
            e.printStackTrace();

        }
    }


    //-------------------------------------------------------
    // Getter
    //-------------------------------------------------------
    public int getTitleXPos() { return this.titleXPos; }
    public int getTitleYPos() { return this.titleYPos; }
    public int getMusicButtonXPos() { return this.musicButtonXPos; }
    public int getMusicButtonYPos() { return this.musicButtonYPos; }
    public int getButtonsXPositionAt(int index) throws IllegalArgumentException {
        if (index < 0 || index >= this.buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons x position is out of bounds");
        }
        return this.BUTTON_X_POSITIONS[index];
    }
    public int getButtonsYPositionAt(int index) throws IllegalArgumentException {
        if (index < 0 || index >= this.buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons y position is out of bounds");
        }
        return this.BUTTON_Y_POSITIONS[index];
    }



    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
    public String menuMouseClicked(MouseEvent e) {
        System.out.println(" > ");
        double mouseX = e.getX();
        double mouseY = e.getY();

        // Handle menu button clicks
        for (int i = 0; i < this.buttonImagesLength; i++) {
            double buttonX = this.BUTTON_X_POSITIONS[i];
            double buttonY = this.BUTTON_Y_POSITIONS[i];
            double buttonW = this.buttonWidth;
            double buttonH = this.buttonHeight;
//            double radius = 50; // TODO: this is a "magic number" = 50, replace with actual parameter fields

            switch(i) {
                case 0: // Single player
                    if (this.clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        System.out.println(" > Single Player");
                        return "single_player";
                    }
                case 1: // Time attack
                    if (this.clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        System.out.println(" > Time Attack");
                        return "time_attack";
                    }
                case 2: // Settings/Options?
                    if (this.clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        System.out.println(" > Settings");
                    }
                    break;
                case 3: // Quit
                    if (this.clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        System.out.println(" > Quit");
                        Timer timer = new Timer(1000, new ActionListener() {
                            // Click the Exit and wait 1 second then Exit the program
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.exit(0);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                    break;
                case 4: // Help menu?
                    if (this.clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        System.out.println(" > Help Menu");
                    }
                    break;
                default:
                    break;
            }
        }

        // Handle music button toggle
//        double musicButtonX = 475;
//        double musicButtonY = 475;
//        double musicButtonRadius = 25;
        if (this.clickButton(mouseX, mouseY, this.musicButtonXPos, this.musicButtonYPos, this.musicButtonRadius)) {
            System.out.println(" > Music toggle");
            this.toggleMusic();
        }

        return "nothing";
    }
    
    //check the mouse location
    public boolean clickButton(double mouseX, double mouseY, double buttonX, double buttonY, double radius) {
        double dx = mouseX - buttonX;
        double dy = mouseY - buttonY;
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    public boolean clickButton(double mouseX, double mouseY, double buttonX, double buttonY, double buttonW, double buttonH) {
        // Check if the mouse is in the box
        if ((mouseX > buttonX) && (mouseX < buttonX + buttonW) && (mouseY > buttonY) && (mouseY < buttonY + buttonH)) { return true; }
        else { return false; }
    }
    
    //draw background
    public void drawBackground() {
        this.engine.saveCurrentTransform();
        this.engine.drawImage(this.background, 0, 0, this.windowWidth, this.windowHeight);
        this.engine.restoreLastTransform();
    }
    
    //draw title
    public void drawTitle() {
        this.engine.saveCurrentTransform();
        this.engine.drawImage(this.title, this.titleXPos, this.titleYPos, this.titleWidth, this.titleHeight);
        this.engine.restoreLastTransform();
    }
    
    //draw button
    public void drawButton() {
        for (int i = 0; i < this.buttonImagesLength; i++) {
            int buttonX = this.BUTTON_X_POSITIONS[i];
            int buttonY = this.BUTTON_Y_POSITIONS[i];

//            this.engine.drawImage(this.buttonImages[i], buttonX - this.buttonOffset, buttonY - this.buttonOffset, this.buttonWidth, this.buttonHeight);
            this.engine.drawImage(this.buttonImages[i], buttonX, buttonY, this.buttonWidth, this.buttonHeight);
        }
//        this.engine.drawImage(this.musicButton,this.musicButtonXPos,this.musicButtonYPos,this.musicButtonImageWidth,this.musicButtonImageHeight);
        this.engine.drawImage(this.musicButton,
                this.musicButtonXPos - this.musicButtonRadius,
                this.musicButtonYPos - this.musicButtonRadius,
                this.musicButtonImageWidth,
                this.musicButtonImageHeight);
    }

    public void drawButtonsColliders() {
        for (int i = 0; i < this.buttonImagesLength; i++) {
            int buttonX = this.BUTTON_X_POSITIONS[i];
            int buttonY = this.BUTTON_Y_POSITIONS[i];
            int buttonW = this.buttonWidth;
            int buttonH = this.buttonHeight;

            this.engine.changeColor(255,0,0);
            this.engine.drawLine(buttonX,
                    buttonY,
                    buttonX + buttonW,
                    buttonY);
            this.engine.drawLine(buttonX,
                    buttonY + buttonH,
                    buttonX + buttonW,
                    buttonY + buttonH);
            this.engine.drawLine(buttonX,
                    buttonY,
                    buttonX,
                    buttonY + buttonH);
            this.engine.drawLine(buttonX + buttonW,
                    buttonY,
                    buttonX + buttonW,
                    buttonY + buttonH);
        }
//        this.engine.drawCircle(this.musicButtonXPos + (this.musicButtonImageWidth / 2.0), this.musicButtonYPos + (this.musicButtonImageHeight / 2.0), this.musicButtonRadius);
        this.engine.drawCircle(this.musicButtonXPos, this.musicButtonYPos, this.musicButtonRadius);
    }

    // Initialize the music file to play on the StartMenu
    public void initMusic(String audioPath) {
        try {
            this.music = this.engine.loadAudio(audioPath);
//            //import the music file
//            this.soundFile = new File(audioPath);
//            AudioInputStream audioIn = AudioSystem.getAudioInputStream(this.soundFile);
//
//            //get the music format
//            AudioFormat format = audioIn.getFormat();
//            DataLine.Info info = new DataLine.Info(Clip.class, format);
//
//            this.musicClip = (Clip) AudioSystem.getLine(info);
//            this.musicClip.open(audioIn);

        } catch(Exception e) {
            e.printStackTrace();

        }
    }

    // Play/Pause the music
    public void playMusic(){
//        this.musicClip.start();
        this.engine.startAudioLoop(this.music, this.volume);
    }
    public void pauseMusic(){
//        this.musicClip.stop();
        this.engine.stopAudioLoop(this.music);
    }


    // Toggle the music
    public void toggleMusic() {
        if (this.isMusicPlaying) {
            this.pauseMusic();
        } else {
            this.playMusic();
        }
        this.isMusicPlaying = !this.isMusicPlaying;
    }

    // Draw all the StartMenu components
    public void drawAll() {
        this.drawBackground();
        this.drawTitle();
        this.drawButton();

        // TODO: REMOVE BEFORE SUBMISSION
        this.drawButtonsColliders();
    }
}
