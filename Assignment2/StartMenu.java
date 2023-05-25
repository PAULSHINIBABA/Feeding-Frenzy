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
        buttonWidth = 140;
        buttonHeight = 50;
        this.buttonImages = buttonImages;
        int div16 = 16;
        int incrementY = this.windowHeight / div16;
        int offsetY = this.windowHeight / 3;
        int offsetX = this.windowWidth / div16;
        BUTTON_X_POSITIONS = new int[buttonImagesLength];
        BUTTON_Y_POSITIONS = new int[buttonImagesLength];
        for (int i = 0; i < buttonImagesLength - 1; i++) {
            if (i % 2 == 0) { // Even
                BUTTON_X_POSITIONS[i] = (this.windowWidth / 2) - offsetX - buttonWidth;
            } else { // Odd
                BUTTON_X_POSITIONS[i] = (this.windowWidth / 2) + offsetX;
            }
            BUTTON_Y_POSITIONS[i] = offsetY;
            offsetY += incrementY;
        }
        // Set the help menu position
        int buttonOffset = 10;
        BUTTON_X_POSITIONS[buttonImagesLength - 1] = buttonOffset;
        BUTTON_Y_POSITIONS[buttonImagesLength - 1] = this.windowHeight - buttonHeight - buttonOffset;

        this.title = title;
        titleWidth = titleW;
        titleHeight = titleH;
        titleXPos = (this.windowWidth / 2) - (titleWidth / 2);
        titleYPos = this.windowHeight / div16;

        this.musicButton = musicButton;
        volume = 1.0f;
//        musicButtonXPos = 475;
//        musicButtonYPos = 475;
        musicButtonRadius = 25;
        musicButtonImageWidth = 50;
        musicButtonImageHeight = 50;
        musicButtonXPos = this.windowWidth - musicButtonRadius - buttonOffset;
        musicButtonYPos = this.windowHeight - musicButtonRadius - buttonOffset;

//        init();
    }


    //-------------------------------------------------------
    // Setter
    //-------------------------------------------------------
    public void setTitleXPosition(int x) { titleXPos = x; }
    public void setTitleYPosition(int y) { titleYPos = y; }
    public void setMusicButtonXPosition(int x) { musicButtonXPos = x; }
    public void setMusicButtonYPosition(int y) { musicButtonYPos = y; }
    public void setButtonsXPositions(int[] newButtonPositions) throws IllegalArgumentException {
        if ((newButtonPositions.length > buttonImagesLength) || newButtonPositions.length < buttonImagesLength) {
            throw new IllegalArgumentException("The new buttons x positions array length is incorrect");
        }
        System.arraycopy(newButtonPositions, 0, BUTTON_X_POSITIONS, 0, buttonImagesLength);
    }
    public void setButtonsYPositions(int[] newButtonPositions) throws IllegalArgumentException {
        if ((newButtonPositions.length > buttonImagesLength) || newButtonPositions.length < buttonImagesLength) {
            throw new IllegalArgumentException("The new buttons y positions array length is incorrect");
        }
        System.arraycopy(newButtonPositions, 0, BUTTON_Y_POSITIONS, 0, buttonImagesLength);
    }
    public void setButtonsXPositionAt(int index, int x) throws IllegalArgumentException {
        if (index < 0 || index >= buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons x position is out of bounds");
        }
        BUTTON_X_POSITIONS[index] = x;
    }
    public void setButtonsYPositionAt(int index, int y) {
        if (index < 0 || index >= buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons y position is out of bounds");
        }
        BUTTON_Y_POSITIONS[index] = y;
    }
    public void setMusicFile(String audioPath) {
        try {
            soundFile = new File(audioPath);

        } catch(Exception e) {
            e.printStackTrace();

        }
    }


    //-------------------------------------------------------
    // Getter
    //-------------------------------------------------------
    public int getTitleXPos() { return titleXPos; }
    public int getTitleYPos() { return titleYPos; }
    public int getMusicButtonXPos() { return musicButtonXPos; }
    public int getMusicButtonYPos() { return musicButtonYPos; }
    public int getButtonsXPositionAt(int index) throws IllegalArgumentException {
        if (index < 0 || index >= buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons x position is out of bounds");
        }
        return BUTTON_X_POSITIONS[index];
    }
    public int getButtonsYPositionAt(int index) throws IllegalArgumentException {
        if (index < 0 || index >= buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons y position is out of bounds");
        }
        return BUTTON_Y_POSITIONS[index];
    }



    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
    public String menuMouseClicked(MouseEvent e) {
        System.out.println(" > ");
        double mouseX = e.getX();
        double mouseY = e.getY();

        // Handle menu button clicks
        for (int i = 0; i < buttonImagesLength; i++) {
            double buttonX = BUTTON_X_POSITIONS[i];
            double buttonY = BUTTON_Y_POSITIONS[i];
            double buttonW = buttonWidth;
            double buttonH = buttonHeight;

            switch(i) {
                case 0: // Single player
                    if (clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        System.out.println(" > Single Player");
                        return "single_player";
                    }
                case 1: // Time attack
                    if (clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        System.out.println(" > Time Attack");
                        return "time_attack";
                    }
                case 2: // Settings/Options?
                    if (clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        System.out.println(" > Settings");
                    }
                    break;
                case 3: // Quit
                    if (clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
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
                    if (clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
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
        if (clickButton(mouseX, mouseY, musicButtonXPos, musicButtonYPos, musicButtonRadius)) {
            System.out.println(" > Music toggle");
            toggleMusic();
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
        engine.saveCurrentTransform();
        engine.drawImage(background, 0, 0, windowWidth, windowHeight);
        engine.restoreLastTransform();
    }
    
    //draw title
    public void drawTitle() {
        engine.saveCurrentTransform();
        engine.drawImage(title, titleXPos, titleYPos, titleWidth, titleHeight);
        engine.restoreLastTransform();
    }
    
    //draw button
    public void drawButton() {
        for (int i = 0; i < buttonImagesLength; i++) {
            int buttonX = BUTTON_X_POSITIONS[i];
            int buttonY = BUTTON_Y_POSITIONS[i];

//            engine.drawImage(buttonImages[i], buttonX - buttonOffset, buttonY - buttonOffset, buttonWidth, buttonHeight);
            engine.drawImage(buttonImages[i], buttonX, buttonY, buttonWidth, buttonHeight);
        }
//        engine.drawImage(musicButton,musicButtonXPos,musicButtonYPos,musicButtonImageWidth,musicButtonImageHeight);
        engine.drawImage(musicButton,
                musicButtonXPos - musicButtonRadius,
                musicButtonYPos - musicButtonRadius,
                musicButtonImageWidth,
                musicButtonImageHeight);
    }

    public void drawButtonsColliders() {
        for (int i = 0; i < buttonImagesLength; i++) {
            int buttonX = BUTTON_X_POSITIONS[i];
            int buttonY = BUTTON_Y_POSITIONS[i];
            int buttonW = buttonWidth;
            int buttonH = buttonHeight;

            engine.changeColor(255,0,0);
            engine.drawLine(buttonX,
                    buttonY,
                    buttonX + buttonW,
                    buttonY);
            engine.drawLine(buttonX,
                    buttonY + buttonH,
                    buttonX + buttonW,
                    buttonY + buttonH);
            engine.drawLine(buttonX,
                    buttonY,
                    buttonX,
                    buttonY + buttonH);
            engine.drawLine(buttonX + buttonW,
                    buttonY,
                    buttonX + buttonW,
                    buttonY + buttonH);
        }
//        engine.drawCircle(musicButtonXPos + (musicButtonImageWidth / 2.0), musicButtonYPos + (musicButtonImageHeight / 2.0), musicButtonRadius);
        engine.drawCircle(musicButtonXPos, musicButtonYPos, musicButtonRadius);
    }

    // Initialize the music file to play on the StartMenu
    public void initMusic(String audioPath) {
        try {
            music = engine.loadAudio(audioPath);
//            //import the music file
//            soundFile = new File(audioPath);
//            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
//
//            //get the music format
//            AudioFormat format = audioIn.getFormat();
//            DataLine.Info info = new DataLine.Info(Clip.class, format);
//
//            musicClip = (Clip) AudioSystem.getLine(info);
//            musicClip.open(audioIn);

        } catch(Exception e) {
            e.printStackTrace();

        }
    }

    // Play/Pause the music
    public void playMusic(){
//        musicClip.start();
        engine.startAudioLoop(music, volume);
    }
    public void pauseMusic(){
//        musicClip.stop();
        engine.stopAudioLoop(music);
    }


    // Toggle the music
    public void toggleMusic() {
        if (isMusicPlaying) {
            pauseMusic();
        } else {
            playMusic();
        }
        isMusicPlaying = !isMusicPlaying;
    }

    // Draw all the StartMenu components
    public void drawAll() {
        drawBackground();
        drawTitle();
        drawButton();

        // TODO: REMOVE BEFORE SUBMISSION
//        drawButtonsColliders();
    }
}
