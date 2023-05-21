/*
 * Author: Paul (Zeju Fan)
 * ID: 21019135
 *
 * Co-Author: Robert Tubman (Major refactoring to merge with team code)
 * ID: 11115713
 *
 * Co-Author: Lucass (Xidi Kuang) (Minor tweaks)
 * ID: 21008041
 *
 * Team:
 * David, 22004319
 * Lucas (Xidi Kuang), 21008041
 * Paul (Zeju Fan), 21019135
 * Robert Tubman, 11115713
 *
 *
 * The StartMenu class
 *
 * This class is used to handle the main menu visuals and controls
 */

package Assignment2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartMenu {
    private final GameEngine engine;
    // Image of the background
    private Image background;
    private Image title;
    private int titleXPos;
    private int titleYPos;
    private int titleWidth;
    private int titleHeight;
    private final int buttonImagesLength;
    private final Image[] buttonImages;
    private int windowWidth;
    private int windowHeight;

    // Variables for mouse click position
    private final int[] BUTTON_X_POSITIONS;
    private final int[] BUTTON_Y_POSITIONS;
    private int buttonWidth;
    private int buttonHeight;

    // Refactored into AudioHandler Class
//    private Clip musicClip;
//    private boolean isMusicPlaying = true;
//    private Image musicButton;
//    private int musicButtonXPos;
//    private int musicButtonYPos;
//    private int musicButtonRadius;
//    private int musicButtonImageWidth;
//    private int musicButtonImageHeight;
//    private File soundFile;
//    private GameEngine.AudioClip music;
//    private float volume;

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
        this.background = background;

        // Set the screen dimensions
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        // Set the buttons positions fields
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

        // Set the help menu button fields
        int buttonOffset = 10;
        BUTTON_X_POSITIONS[buttonImagesLength - 1] = buttonOffset;
        BUTTON_Y_POSITIONS[buttonImagesLength - 1] = this.windowHeight - buttonHeight - buttonOffset;

        // Set the title fields
        this.title = title;
        titleWidth = titleW;
        titleHeight = titleH;
        titleXPos = (this.windowWidth / 2) - (titleWidth / 2);
        titleYPos = this.windowHeight / div16;

        // Refactored into AudioHandler Class
//        this.musicButton = musicButton;
//        volume = 1.0f;
//        musicButtonXPos = 475;
//        musicButtonYPos = 475;
//        musicButtonRadius = 25;
//        musicButtonImageWidth = 50;
//        musicButtonImageHeight = 50;
//        musicButtonXPos = this.windowWidth - musicButtonRadius - buttonOffset;
//        musicButtonYPos = this.windowHeight - musicButtonRadius - buttonOffset;
//        init();
    }


    //-------------------------------------------------------
    // Setter
    //-------------------------------------------------------
    // Set the title x position
    public void setTitleXPosition(int x) { titleXPos = x; }

    // Set the title y position
    public void setTitleYPosition(int y) { titleYPos = y; }

    // Refactored into AudioHandler Class
//    public void setMusicButtonXPosition(int x) { musicButtonXPos = x; }
//    public void setMusicButtonYPosition(int y) { musicButtonYPos = y; }

    // Set all the buttons x positions
    public void setButtonsXPositions(int[] newButtonPositions) throws IllegalArgumentException {
        if ((newButtonPositions.length > buttonImagesLength) || newButtonPositions.length < buttonImagesLength) {
            throw new IllegalArgumentException("The new buttons x positions array length is incorrect");
        }
        System.arraycopy(newButtonPositions, 0, BUTTON_X_POSITIONS, 0, buttonImagesLength);
    }

    // Set all the buttons y positions
    public void setButtonsYPositions(int[] newButtonPositions) throws IllegalArgumentException {
        if ((newButtonPositions.length > buttonImagesLength) || newButtonPositions.length < buttonImagesLength) {
            throw new IllegalArgumentException("The new buttons y positions array length is incorrect");
        }
        System.arraycopy(newButtonPositions, 0, BUTTON_Y_POSITIONS, 0, buttonImagesLength);
    }

    // Set the buttons x position at index
    public void setButtonsXPositionAt(int index, int x) throws IllegalArgumentException {
        if (index < 0 || index >= buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons x position is out of bounds");
        }
        BUTTON_X_POSITIONS[index] = x;
    }

    // Set the buttons y position at index
    public void setButtonsYPositionAt(int index, int y) {
        if (index < 0 || index >= buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons y position is out of bounds");
        }
        BUTTON_Y_POSITIONS[index] = y;
    }

    // Refactored into AudioHandler Class
//    public void setMusicFile(String audioPath) {
//        try {
//            soundFile = new File(audioPath);
//
//        } catch(Exception e) {
//            e.printStackTrace();
//
//        }
//    }


    //-------------------------------------------------------
    // Getter
    //-------------------------------------------------------
    // Get the title x position
    public int getTitleXPos() { return titleXPos; }

    // Get the title y position
    public int getTitleYPos() { return titleYPos; }

    // Refactored into AudioHandler Class
//    public int getMusicButtonXPos() { return musicButtonXPos; }
//    public int getMusicButtonYPos() { return musicButtonYPos; }

    // Get the button x position at index
    public int getButtonsXPositionAt(int index) throws IllegalArgumentException {
        if (index < 0 || index >= buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons x position is out of bounds");
        }
        return BUTTON_X_POSITIONS[index];
    }

    // get the button y position at index
    public int getButtonsYPositionAt(int index) throws IllegalArgumentException {
        if (index < 0 || index >= buttonImagesLength) {
            throw new IllegalArgumentException("Index for buttons y position is out of bounds");
        }
        return BUTTON_Y_POSITIONS[index];
    }



    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
    // Handle the mouse clicked event for the main menu
    public String menuMouseClicked(double mouseX, double mouseY) {

        // Handle menu button clicks
        for (int i = 0; i < buttonImagesLength; i++) {
            double buttonX = BUTTON_X_POSITIONS[i];
            double buttonY = BUTTON_Y_POSITIONS[i];
            double buttonW = buttonWidth;
            double buttonH = buttonHeight;

            switch(i) {
                case 0: // Single player
                    if (clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        return "single_player";
                    }
                case 1: // Time attack
                    if (clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        return "time_attack";
                    }
                case 2: // Settings/Options?
                    if (clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        return "settings";
                    }
                    break;
                case 3: // Quit
                    if (clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        Timer timer = new Timer(600, new ActionListener() {
                            // Click the Exit and wait 0.6 seconds then Exit the program
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.exit(0);
                            }
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                    break;
                case 4: // Help menu
                    if (clickButton(mouseX, mouseY, buttonX, buttonY, buttonW, buttonH)) {
                        return "help_page";
                    }
                    break;
                default:
                    break;
            }
        }

        // Refactored into AudioHandler Class
//        // Handle music button toggle
//        double musicButtonX = 475;
//        double musicButtonY = 475;
//        double musicButtonRadius = 25;
//        if (clickButton(mouseX, mouseY, musicButtonXPos, musicButtonYPos, musicButtonRadius)) {
//            System.out.println(" > Music toggle");
//            toggleMusic();
//        }

        // Nothing was clicked
        return "nothing";
    }

    // Check whether the button was clicked (by radius)
    public boolean clickButton(double mouseX, double mouseY, double buttonX, double buttonY, double radius) {
        double dx = mouseX - buttonX;
        double dy = mouseY - buttonY;
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    // Check whether the button was clicked (by box)
    public boolean clickButton(double mouseX, double mouseY, double buttonX, double buttonY, double buttonW, double buttonH) {
        // Check if the mouse is in the box
        if ((mouseX > buttonX) && (mouseX < buttonX + buttonW) && (mouseY > buttonY) && (mouseY < buttonY + buttonH)) { return true; }
        else { return false; }
    }
    
    // Draw the background
    public void drawBackground() {
        engine.saveCurrentTransform();
        engine.drawImage(background, 0, 0, windowWidth, windowHeight);
        engine.restoreLastTransform();
    }
    
    // Draw the title
    public void drawTitle() {
        engine.saveCurrentTransform();
        engine.drawImage(title, titleXPos, titleYPos, titleWidth, titleHeight);
        engine.restoreLastTransform();
    }
    
    // Draw the buttons on the main menu
    public void drawButton() {
        for (int i = 0; i < buttonImagesLength; i++) {
            int buttonX = BUTTON_X_POSITIONS[i];
            int buttonY = BUTTON_Y_POSITIONS[i];
            engine.drawImage(buttonImages[i], buttonX, buttonY, buttonWidth, buttonHeight);
        }

        // Refactored into INGAMEMENU Class
//        engine.drawImage(musicButton,musicButtonXPos,musicButtonYPos,musicButtonImageWidth,musicButtonImageHeight);
//        engine.drawImage(musicButton,
//                musicButtonXPos - musicButtonRadius,
//                musicButtonYPos - musicButtonRadius,
//                musicButtonImageWidth,
//                musicButtonImageHeight);
    }

    // Debug method to visualize the collider locations
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

        // Refactored into INGAMEMENU Class
//        engine.drawCircle(musicButtonXPos + (musicButtonImageWidth / 2.0), musicButtonYPos + (musicButtonImageHeight / 2.0), musicButtonRadius);
//        engine.drawCircle(musicButtonXPos, musicButtonYPos, musicButtonRadius);
    }

    // Draw all the StartMenu components
    public void drawAll() {
        drawBackground();
        drawTitle();
        drawButton();
    }

    // Refactored into the AudioHandler Class
//    // Initialize the music file to play on the StartMenu
//    public void initMusic(String audioPath) {
//        try {
//            music = engine.loadAudio(audioPath);
////            //import the music file
////            soundFile = new File(audioPath);
////            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
////
////            //get the music format
////            AudioFormat format = audioIn.getFormat();
////            DataLine.Info info = new DataLine.Info(Clip.class, format);
////
////            musicClip = (Clip) AudioSystem.getLine(info);
////            musicClip.open(audioIn);
//
//        } catch(Exception e) {
//            e.printStackTrace();
//
//        }
//    }
//    // Play/Pause the music
//    public void playMusic(){
////        musicClip.start();
//        engine.startAudioLoop(music, volume);
//    }
//    public void pauseMusic(){
////        musicClip.stop();
//        engine.stopAudioLoop(music);
//    }
//    // Toggle the music
//    public void toggleMusic() {
//        if (isMusicPlaying) {
//            pauseMusic();
//        } else {
//            playMusic();
//        }
//        isMusicPlaying = !isMusicPlaying;
//    }

}
