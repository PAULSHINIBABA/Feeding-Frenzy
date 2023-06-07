/*
 * Author: Paul (Zeju Fan)
 * ID: 21019135
 *
 * Co-Author: Robert Tubman (Major refactoring to merge with team code)
 * ID: 11115713
 *
 * Team:
 * David, 22004319
 * Lucas (Xidi Kuang), 21008041
 * Paul (Zeju Fan), 21019135
 * Robert Tubman, 11115713
 *
 * The INGAMEMENU class
 *
 * This handles the settings page from the main menu as well as from in-game.
 * The game parameters for enabling the Shark Enemies (Lionfish), adjusting the growth threshold per level,
 * adjusting the duration available per level, as well as a toggle for the music are present.
 *
 * From the in-game settings page, only enable shark and toggle music are available.
 */

package Assignment2;

import java.awt.*;

public class INGAMEMENU{
    // Final fields
    final private boolean FROM_MAIN_MENU;
    final private GameEngine ENGINE;
    final private double POS_OFFSET_0 = 0;
    final private double POS_OFFSET_1 = 1;
    final private double POS_OFFSET_2 = 2;
    final private double POS_OFFSET_3 = 3;
    final private double POS_OFFSET_4 = 4;
    final private double POS_OFFSET_5 = 5;
    final private double POS_OFFSET_6 = 6;
    final private double POS_OFFSET_7 = 7;
    final private double POS_OFFSET_3_5 = 3.5;
    final private double POS_OFFSET_4_5 = 4.5;
    final private double SCREEN_SEGMENTS = 8.0;
    final private double TEXT_OFFSET = 1.25;
    final private double BUTTON_OFFSET = 1.5;

    // Non-final fields
    private double offset;
    private double buttonSize;
    private double buttonWidth;
    private double buttonHeight;
    private int fontSize;
    private Image background;
    private Image backicon;
    private Image MusicButton;
    private double backgroundXPos;
    private double backgroundYPos;
    private double backgroundWidth;
    private double backgroundHeight;
    private double backIconXPos;
    private double backIconYPos;
    private double musicButtonXPos;
    private double musicButtonYPos;

    Image hardModeIcon;
    private double hardModeButtonXPos;
    private double hardModeButtonYPos;
    boolean isHardMode;
    Image mainMenuIcon;
    private double mainMenuButtonXPos;
    private double mainMenuButtonYPos;
    Image timeAttackTimeIcon;
    private double timeAttackTimeButtonXPos;
    private double timeAttackTimeButtonYPos;
    private double timeAttackTimeValue;
    Image growthTargetIcon;
    private double growthTargetButtonXPos;
    private double growthTargetButtonYPos;
    private int growthTargetValue;


    // Constructor
    public INGAMEMENU(GameEngine engine, double width, double height, boolean fromMainMenu) {
        FROM_MAIN_MENU = fromMainMenu;
        this.ENGINE = engine;
        backgroundWidth = width;
        backgroundHeight = height;

        // Define some arbitrary sizes and dimensions
        offset = buttonSize = 75;
        buttonWidth = 140;
        buttonHeight = 50;
        fontSize = 28;

        // Set the offset to the screen dimensions
        double offsetX = ((width / 2.0) - (buttonWidth / 2.0));
        double offsetY = (height / SCREEN_SEGMENTS);
        int arbitraryOffset = -9999;

        // Set back and music toggle buttons based on screen dimensions
        backIconXPos = buttonSize;
        backIconYPos = backgroundHeight - buttonSize;
        musicButtonXPos = backgroundWidth - buttonSize;
        musicButtonYPos = backgroundHeight - buttonSize;

        if (FROM_MAIN_MENU) {
            // The settings were switched to from the main menu
            hardModeButtonXPos = offsetX;
            hardModeButtonYPos = offsetY * POS_OFFSET_3;
            growthTargetButtonXPos = offsetX;
            growthTargetButtonYPos = offsetY * POS_OFFSET_4;
            timeAttackTimeButtonXPos = offsetX;
            timeAttackTimeButtonYPos = offsetY * POS_OFFSET_5;

            // Won't use the button below when in menu settings (hide it with an arbitrary offset)
            mainMenuButtonXPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number
            mainMenuButtonYPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number

        } else {
            // The settings were switched to from in-game
            hardModeButtonXPos = offsetX;
            hardModeButtonYPos = offsetY * POS_OFFSET_3_5;
            mainMenuButtonXPos = offsetX;
            mainMenuButtonYPos = offsetY * POS_OFFSET_4_5;

            // Won't use the button below when in the in-game settings (hide it with an arbitrary offset)
            timeAttackTimeButtonXPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number
            timeAttackTimeButtonYPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number
            growthTargetButtonXPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number
            growthTargetButtonYPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number

        }
    }


    //**************************************************
    // Setters
    //**************************************************
    // Set the images used in the settings menu
    public void setImages(Image background,
                          Image backicon,
                          Image MusicButton,
                          Image hardModeIcon,
                          Image mainMenuIcon,
                          Image timeAttackTimeIcon,
                          Image growthTargetIcon) {
        this.background = background;
        this.backicon = backicon;
        this.MusicButton = MusicButton;
        this.hardModeIcon = hardModeIcon;
        this.mainMenuIcon = mainMenuIcon;
        this.timeAttackTimeIcon = timeAttackTimeIcon;
        this.growthTargetIcon = growthTargetIcon;
    }

    // Set the button size
    public void setButtonSize(int buttonSize) { this.buttonSize = buttonSize; }

    // Set the button offset fields
    public void setButtonOffset(int offset) { this.offset = offset; }

    // Set the background position and dimensions
    public void setBackgroundParameters(double x, double y, double w, double h) {
        backgroundXPos = x;
        backgroundYPos = y;
        backgroundWidth = w;
        backgroundHeight = h;
    }

    // Set the back to main menu button position (based on screen dimensions)
    public void setBackIconParameters() {
        setBackIconParameters(backgroundXPos + offset,
                backgroundYPos + backgroundHeight - offset - buttonSize);
    }

    // Set the back to main menu button position
    public void setBackIconParameters(double x, double y) {
        backIconXPos = x;
        backIconYPos = y;
    }

    // Set the music toggle button position (based on screen dimensions)
    public void setMusicButtonParameters() {
        setMusicButtonParameters(backgroundXPos + backgroundWidth - offset - buttonSize,
                backgroundYPos + backgroundHeight - offset - buttonSize);
    }

    // Set the music toggle button position
    public void setMusicButtonParameters(double x, double y) {
        musicButtonXPos = x;
        musicButtonYPos = y;
    }

    // Set the hard mode button position (based on screen dimensions)
    public void setHardModeParameters() {
        setHardModeParameters(backgroundXPos + (backgroundWidth / 2.0) - (buttonWidth / 2.0),
                backgroundYPos + (backgroundHeight / SCREEN_SEGMENTS) * POS_OFFSET_3_5);
    }

    // Set the hard mode button position
    public void setHardModeParameters(double x, double y) {
        hardModeButtonXPos = x;
        hardModeButtonYPos = y;
    }

    // Set the time attack threshold button position (based on screen dimensions)
    public void setTimeAttackTimeParameters() {
        setTimeAttackTimeParameters(backgroundXPos + (backgroundWidth / 2.0) - (buttonWidth / 2.0),
                backgroundYPos + (backgroundHeight / SCREEN_SEGMENTS) * POS_OFFSET_5);
    }

    // Set the time attack threshold button position
    public void setTimeAttackTimeParameters(double x, double y) {
        timeAttackTimeButtonXPos = x;
        timeAttackTimeButtonYPos = y;
    }

    // Set the growth threshold button position (based on screen dimensions)
    public void setGrowthTargetParameters() {
        setGrowthTargetParameters(backgroundXPos + (backgroundWidth / 2.0) - (buttonWidth / 2.0),
                backgroundYPos + (backgroundHeight / SCREEN_SEGMENTS) * POS_OFFSET_4);
    }

    // Set the growth threshold button position
    public void setGrowthTargetParameters(double x, double y) {
        growthTargetButtonXPos = x;
        growthTargetButtonYPos = y;
    }

    // Set the main menu button parameters to be based on the screen dimensions
    public void setToMainMenuParameters() {
        setToMainMenuParameters(backgroundXPos + (backgroundWidth / 2.0) - (buttonWidth / 2.0),
                backgroundYPos + (backgroundHeight / SCREEN_SEGMENTS) * POS_OFFSET_4_5);
    }

    // Set the main menu button position
    public void setToMainMenuParameters(double x, double y) {
        mainMenuButtonXPos = x;
        mainMenuButtonYPos = y;
    }

    // Set the time attack threshold toggle (the toggle selects a value in a list)
    public void setTimeAttackTimeValue(double value) { timeAttackTimeValue = value; }

    // Set the growth threshold toggle (the toggle selects a value in a list)
    public void setGrowthTargetValue(int value) { growthTargetValue = value; }

    // Set the hard mode
    public void setIsHardMode(boolean isHardMode) { this.isHardMode = isHardMode; }

    //**************************************************
    // Getters
    //**************************************************
    // Unused methods
    public double getBackgroundXPos() { return backgroundXPos; }
    public double getBackgroundYPos() { return backgroundYPos; }
    public double getBackgroundWidth() { return backgroundWidth; }
    public double getBackgroundHeight() { return backgroundHeight; }


    //**************************************************
    // Other methods
    //**************************************************
    // Initialize the class using this init (Obsolete)
    public void init() {}

    // Handle the mouse clicked event for the buttons here
    public String inGameMenuMouseClicked(double mouseX, double mouseY) {

        if (clickButton(mouseX,
                mouseY,
                backIconXPos + (buttonSize/2.0),
                backIconYPos + (buttonSize/2.0),
                buttonSize/2.0)) {

            // Change the response of the return button depending on the context
            // From the main menu = return to main menu
            // From the in-game menu = return to game
            if (FROM_MAIN_MENU) { return "main_menu"; } // If the back was pressed in the settings from the main page
            else { return "back_to_game"; } // If the back was pressed in the settings from in game

        } else if (clickButton(mouseX,
                mouseY,
                musicButtonXPos + (buttonSize/2.0),
                musicButtonYPos + (buttonSize/2.0),
                buttonSize/2.0)) {

            // The toggle music button was clicked
            return "toggle_music";

        } else if (clickButton(mouseX,
                mouseY,
                hardModeButtonXPos,
                hardModeButtonYPos,
                buttonWidth,
                buttonHeight)) {

            // The hard mode button was toggled
            return "hard_mode";
        }

        if (FROM_MAIN_MENU) {
            if (clickButton(mouseX,
                    mouseY,
                    growthTargetButtonXPos,
                    growthTargetButtonYPos,
                    buttonWidth,
                    buttonHeight)) {

                // The growth threshold button was toggled
                return "growth_target";

            } else if (clickButton(mouseX,
                    mouseY,
                    timeAttackTimeButtonXPos,
                    timeAttackTimeButtonYPos,
                    buttonWidth,
                    buttonHeight)) {

                // The time attack threshold button was toggled
                return "time_attack_time";

            }
        } else {

            // This is only used in from the in-game menu
            if (clickButton(mouseX,
                    mouseY,
                    mainMenuButtonXPos,
                    mainMenuButtonYPos,
                    buttonWidth,
                    buttonHeight)) {

                // The main menu button was clicked
                return "main_menu";
            }
        }

        // Nothing was clicked so return "nothing"
        return "nothing";
    }

    // Check if the mouse clicked a collider radius
    public boolean clickButton(double mouseX, double mouseY, double buttonX, double buttonY, double radius) {
        double dx = mouseX - buttonX;
        double dy = mouseY - buttonY;
        return (dx * dx + dy * dy) <= (radius * radius);
    }

    // Check if the mouse clicked a collider box
    public boolean clickButton(double mouseX, double mouseY, double buttonX, double buttonY, double buttonW, double buttonH) {
        // Check if the mouse is in the box
        if ((mouseX > buttonX) && (mouseX < buttonX + buttonW) && (mouseY > buttonY) && (mouseY < buttonY + buttonH)) { return true; }
        else { return false; }
    }

    // a draw method that encapsulates all the drawing methods of this class
    public void drawInGameMenu() {
        drawbackground();
        drawbackicon();
        drawMusicButton();

        drawHardModeButton();
        if (FROM_MAIN_MENU) {
            // Only visible from main menu settings
            drawGrowthToggleButton();
            drawTimeAttackTimeToggleButton();
        } else {
            // Only visible from the in game settings
            drawBackToMainMenuButton();
        }

        // Debug dev only
//        drawCollisionLines();
    }

    // Draw the menu background
    public void drawbackground() { this.ENGINE.drawImage(background, backgroundXPos, backgroundYPos, backgroundWidth, backgroundHeight); }

    // Draw the menu navigation buttons
    public void drawbackicon() { this.ENGINE.drawImage(backicon, backIconXPos, backIconYPos, buttonSize, buttonSize); }

    // Draw the button to toggle the music on/off
    public void drawMusicButton() { this.ENGINE.drawImage(MusicButton, musicButtonXPos, musicButtonYPos, buttonSize, buttonSize); }

    // Draw the toggle button to select "hard" mode
    // Hard mode will spawn fast and dangerous enemies that will keep spawning once a user is 33% from completing
    // the current level.
    public void drawHardModeButton() {
        this.ENGINE.drawImage(hardModeIcon, hardModeButtonXPos, hardModeButtonYPos, buttonWidth, buttonHeight);
        if (isHardMode) {
            this.ENGINE.drawText(hardModeButtonXPos + (buttonWidth * BUTTON_OFFSET),
                    hardModeButtonYPos + (fontSize * TEXT_OFFSET),
                    "On",
                    "Sans serif",
                    fontSize);
        } else {
            this.ENGINE.drawText(hardModeButtonXPos + (buttonWidth * BUTTON_OFFSET),
                    hardModeButtonYPos + (fontSize * TEXT_OFFSET),
                    "Off",
                    "Sans serif",
                    fontSize);
        }

    }

    // Draw the toggle for the "growth threshold" goals
    // The growth threshold determines the maximum level that can be reached
    public void drawGrowthToggleButton() {
        this.ENGINE.drawImage(growthTargetIcon, growthTargetButtonXPos, growthTargetButtonYPos, buttonWidth, buttonHeight);
        this.ENGINE.drawText(growthTargetButtonXPos + (buttonWidth * BUTTON_OFFSET),
                growthTargetButtonYPos + (fontSize * TEXT_OFFSET),
                Integer.toString(growthTargetValue),
                "Sans serif",
                fontSize);
    }

    // Draw the toggle button for the "time attack" goals
    // This time attack toggle selects the amount of time given for the user in time attack mode
    public void drawTimeAttackTimeToggleButton() {
        this.ENGINE.drawImage(timeAttackTimeIcon, timeAttackTimeButtonXPos, timeAttackTimeButtonYPos, buttonWidth, buttonHeight);
        this.ENGINE.drawText(timeAttackTimeButtonXPos + (buttonWidth * BUTTON_OFFSET),
                timeAttackTimeButtonYPos + (fontSize * TEXT_OFFSET),
                Double.toString(timeAttackTimeValue),
                "Sans serif",
                fontSize);
    }

    // Draw the "back to main menu" button
    public void drawBackToMainMenuButton() { this.ENGINE.drawImage(mainMenuIcon, mainMenuButtonXPos, mainMenuButtonYPos, buttonWidth, buttonHeight); }

    // Draw the collision boxes of the buttons in the page.
    public void drawCollisionLines() {
        this.ENGINE.changeColor(255,0,0);
        // Draw box colliders for hardMode
        this.ENGINE.drawLine(hardModeButtonXPos,
                hardModeButtonYPos,
                hardModeButtonXPos + buttonWidth,
                hardModeButtonYPos);
        this.ENGINE.drawLine(hardModeButtonXPos,
                hardModeButtonYPos + buttonHeight,
                hardModeButtonXPos + buttonWidth,
                hardModeButtonYPos + buttonHeight);
        this.ENGINE.drawLine(hardModeButtonXPos,
                hardModeButtonYPos,
                hardModeButtonXPos,
                hardModeButtonYPos + buttonHeight);
        this.ENGINE.drawLine(hardModeButtonXPos + buttonWidth,
                hardModeButtonYPos,
                hardModeButtonXPos + buttonWidth,
                hardModeButtonYPos + buttonHeight);

        if (FROM_MAIN_MENU) {
            // Draw box colliders for growth button
            this.ENGINE.drawLine(growthTargetButtonXPos,
                    growthTargetButtonYPos,
                    growthTargetButtonXPos + buttonWidth,
                    growthTargetButtonYPos);
            this.ENGINE.drawLine(growthTargetButtonXPos,
                    growthTargetButtonYPos + buttonHeight,
                    growthTargetButtonXPos + buttonWidth,
                    growthTargetButtonYPos + buttonHeight);
            this.ENGINE.drawLine(growthTargetButtonXPos,
                    growthTargetButtonYPos,
                    growthTargetButtonXPos,
                    growthTargetButtonYPos + buttonHeight);
            this.ENGINE.drawLine(growthTargetButtonXPos + buttonWidth,
                    growthTargetButtonYPos,
                    growthTargetButtonXPos + buttonWidth,
                    growthTargetButtonYPos + buttonHeight);

            // Draw box colliders for time attack time button
            this.ENGINE.drawLine(timeAttackTimeButtonXPos,
                    timeAttackTimeButtonYPos,
                    timeAttackTimeButtonXPos + buttonWidth,
                    timeAttackTimeButtonYPos);
            this.ENGINE.drawLine(timeAttackTimeButtonXPos,
                    timeAttackTimeButtonYPos + buttonHeight,
                    timeAttackTimeButtonXPos + buttonWidth,
                    timeAttackTimeButtonYPos + buttonHeight);
            this.ENGINE.drawLine(timeAttackTimeButtonXPos,
                    timeAttackTimeButtonYPos,
                    timeAttackTimeButtonXPos,
                    timeAttackTimeButtonYPos + buttonHeight);
            this.ENGINE.drawLine(timeAttackTimeButtonXPos + buttonWidth,
                    timeAttackTimeButtonYPos,
                    timeAttackTimeButtonXPos + buttonWidth,
                    timeAttackTimeButtonYPos + buttonHeight);

        } else {
            // Draw box colliders for main menu button
            this.ENGINE.drawLine(mainMenuButtonXPos,
                    mainMenuButtonYPos,
                    mainMenuButtonXPos + buttonWidth,
                    mainMenuButtonYPos);
            this.ENGINE.drawLine(mainMenuButtonXPos,
                    mainMenuButtonYPos + buttonHeight,
                    mainMenuButtonXPos + buttonWidth,
                    mainMenuButtonYPos + buttonHeight);
            this.ENGINE.drawLine(mainMenuButtonXPos,
                    mainMenuButtonYPos,
                    mainMenuButtonXPos,
                    mainMenuButtonYPos + buttonHeight);
            this.ENGINE.drawLine(mainMenuButtonXPos + buttonWidth,
                    mainMenuButtonYPos,
                    mainMenuButtonXPos + buttonWidth,
                    mainMenuButtonYPos + buttonHeight);
        }
    }
}