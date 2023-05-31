/*
 * Author: Paul (Zeju Fan)
 * ID:
 *
 * Co-Author: Robert Tubman (Major refactoring to merge with team code)
 * ID: 11115713
 */

package Assignment2;

import java.awt.*;

public class INGAMEMENU{
    final private boolean FROM_MAIN_MENU;
    final private GameEngine ENGINE;
    final double POS_OFFSET_0 = 0;
    final double POS_OFFSET_1 = 1;
    final double POS_OFFSET_2 = 2;
    final double POS_OFFSET_3 = 3;
    final double POS_OFFSET_4 = 4;
    final double POS_OFFSET_5 = 5;
    final double POS_OFFSET_6 = 6;
    final double POS_OFFSET_7 = 7;
    final double POS_OFFSET_3_5 = 3.5;
    final double POS_OFFSET_4_5 = 4.5;
    final double SCREEN_SEGMENTS = 8.0;
    final double TEXT_OFFSET = 1.25;
    final double BUTTON_OFFSET = 1.5;
    double offset;
    double buttonSize;
    double buttonWidth;
    double buttonHeight;
    int fontSize;
    private Image background;
    private Image backicon;
    private Image MusicButton;
    private double backgroundXPos;
    private double backgroundYPos;
    private double backgroundWidth;
    private double backgroundHeight;
    private double backIconXPos;
    private double backIconYPos;
//    private double backIconWidth;
//    private double backIconHeight;
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


    public INGAMEMENU(GameEngine engine, double width, double height, boolean fromMainMenu) {
        FROM_MAIN_MENU = fromMainMenu;
        this.ENGINE = engine;
        backgroundWidth = width;
        backgroundHeight = height;

        offset = buttonSize = 75;
        buttonWidth = 140;
        buttonHeight = 50;
        fontSize = 28;
        double offsetX = ((width / 2.0) - (buttonWidth / 2.0));
        double offsetY = (height / SCREEN_SEGMENTS);
        int arbitraryOffset = -9999;

        backIconXPos = buttonSize;
        backIconYPos = backgroundHeight - buttonSize;
        musicButtonXPos = backgroundWidth - buttonSize;
        musicButtonYPos = backgroundHeight - buttonSize;

        if (FROM_MAIN_MENU) {
            hardModeButtonXPos = offsetX;
            hardModeButtonYPos = offsetY * POS_OFFSET_3;
            growthTargetButtonXPos = offsetX;
            growthTargetButtonYPos = offsetY * POS_OFFSET_4;
            timeAttackTimeButtonXPos = offsetX;
            timeAttackTimeButtonYPos = offsetY * POS_OFFSET_5;

            // Won't use the button below when in menu settings
            mainMenuButtonXPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number
            mainMenuButtonYPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number

        } else {
            hardModeButtonXPos = offsetX;
            hardModeButtonYPos = offsetY * POS_OFFSET_3_5;
            mainMenuButtonXPos = offsetX;
            mainMenuButtonYPos = offsetY * POS_OFFSET_4_5;

            // Won't use the button below when in the in-game settings
            timeAttackTimeButtonXPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number
            timeAttackTimeButtonYPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number
            growthTargetButtonXPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number
            growthTargetButtonYPos = arbitraryOffset; // Set it off the screen to some arbitrarily large number

        }
    }


    //**************************************************
    // Setters
    //**************************************************
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
    public void setButtonSize(int buttonSize) { this.buttonSize = buttonSize; }
    public void setButtonOffset(int offset) { this.offset = offset; }
    public void setBackgroundParameters(double x, double y, double w, double h) {
        backgroundXPos = x;
        backgroundYPos = y;
        backgroundWidth = w;
        backgroundHeight = h;
    }
    public void setBackIconParameters() {
        setBackIconParameters(backgroundXPos + offset,
                backgroundYPos + backgroundHeight - offset - buttonSize);
    }
    public void setBackIconParameters(double x, double y) {
        backIconXPos = x;
        backIconYPos = y;
    }
    public void setMusicButtonParameters() {
        setMusicButtonParameters(backgroundXPos + backgroundWidth - offset - buttonSize,
                backgroundYPos + backgroundHeight - offset - buttonSize);
    }
    public void setMusicButtonParameters(double x, double y) {
        musicButtonXPos = x;
        musicButtonYPos = y;
    }
    public void setHardModeParameters() {
        setHardModeParameters(backgroundXPos + (backgroundWidth / 2.0) - (buttonWidth / 2.0),
                backgroundYPos + (backgroundHeight / SCREEN_SEGMENTS) * POS_OFFSET_3_5);
    }
    public void setHardModeParameters(double x, double y) {
        hardModeButtonXPos = x;
        hardModeButtonYPos = y;
    }
    public void setTimeAttackTimeParameters() {
        setTimeAttackTimeParameters(backgroundXPos + (backgroundWidth / 2.0) - (buttonWidth / 2.0),
                backgroundYPos + (backgroundHeight / SCREEN_SEGMENTS) * POS_OFFSET_5);
    }
    public void setTimeAttackTimeParameters(double x, double y) {
        timeAttackTimeButtonXPos = x;
        timeAttackTimeButtonYPos = y;
    }
    public void setGrowthTargetParameters() {
        setGrowthTargetParameters(backgroundXPos + (backgroundWidth / 2.0) - (buttonWidth / 2.0),
                backgroundYPos + (backgroundHeight / SCREEN_SEGMENTS) * POS_OFFSET_4);
    }
    public void setGrowthTargetParameters(double x, double y) {
        growthTargetButtonXPos = x;
        growthTargetButtonYPos = y;
    }
    public void setToMainMenuParameters() {
        setToMainMenuParameters(backgroundXPos + (backgroundWidth / 2.0) - (buttonWidth / 2.0),
                backgroundYPos + (backgroundHeight / SCREEN_SEGMENTS) * POS_OFFSET_4_5);
    }
    public void setToMainMenuParameters(double x, double y) {
        mainMenuButtonXPos = x;
        mainMenuButtonYPos = y;
    }
    public void setTimeAttackTimeValue(double value) { timeAttackTimeValue = value; }
    public void setGrowthTargetValue(int value) { growthTargetValue = value; }
    public void setIsHardMode(boolean isHardMode) { this.isHardMode = isHardMode; }

    //**************************************************
    // Getters
    //**************************************************
//    public double getBackgroundXPos() { return backgroundXPos; }
//    public double getBackgroundYPos() { return backgroundYPos; }
//    public double getBackgroundWidth() { return backgroundWidth; }
//    public double getBackgroundHeight() { return backgroundHeight; }


    //**************************************************
    // Other methods
    //**************************************************
    public void init() {}
    public String inGameMenuMouseClicked(double mouseX, double mouseY) {

        if (clickButton(mouseX,
                mouseY,
                backIconXPos + (buttonSize/2.0),
                backIconYPos + (buttonSize/2.0),
                buttonSize/2.0)) {
            if (FROM_MAIN_MENU) { return "main_menu"; } // If the back was pressed in the settings from the main page
            else { return "back_to_game"; } // If the back was pressed in the settings from in game

        } else if (clickButton(mouseX,
                mouseY,
                musicButtonXPos + (buttonSize/2.0),
                musicButtonYPos + (buttonSize/2.0),
                buttonSize/2.0)) {
            return "toggle_music";

        } else if (clickButton(mouseX,
                mouseY,
                hardModeButtonXPos,
                hardModeButtonYPos,
                buttonWidth,
                buttonHeight)) {
            return "hard_mode";
        }

        if (FROM_MAIN_MENU) {
            if (clickButton(mouseX,
                    mouseY,
                    growthTargetButtonXPos,
                    growthTargetButtonYPos,
                    buttonWidth,
                    buttonHeight)) {
                return "growth_target";

            } else if (clickButton(mouseX,
                    mouseY,
                    timeAttackTimeButtonXPos,
                    timeAttackTimeButtonYPos,
                    buttonWidth,
                    buttonHeight)) {
                return "time_attack_time";

            }
        } else {
            if (clickButton(mouseX,
                    mouseY,
                    mainMenuButtonXPos,
                    mainMenuButtonYPos,
                    buttonWidth,
                    buttonHeight)) {
                return "main_menu";
            }
        }

        return "nothing";
    }
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
//        drawCollisionLines();
    }
    public void drawbackground() { this.ENGINE.drawImage(background, backgroundXPos, backgroundYPos, backgroundWidth, backgroundHeight); }
    public void drawbackicon() { this.ENGINE.drawImage(backicon, backIconXPos, backIconYPos, buttonSize, buttonSize); }
    public void drawMusicButton() { this.ENGINE.drawImage(MusicButton, musicButtonXPos, musicButtonYPos, buttonSize, buttonSize); }
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
    public void drawGrowthToggleButton() {
        this.ENGINE.drawImage(growthTargetIcon, growthTargetButtonXPos, growthTargetButtonYPos, buttonWidth, buttonHeight);
        this.ENGINE.drawText(growthTargetButtonXPos + (buttonWidth * BUTTON_OFFSET),
                growthTargetButtonYPos + (fontSize * TEXT_OFFSET),
                Integer.toString(growthTargetValue),
                "Sans serif",
                fontSize);
    }
    public void drawTimeAttackTimeToggleButton() {
        this.ENGINE.drawImage(timeAttackTimeIcon, timeAttackTimeButtonXPos, timeAttackTimeButtonYPos, buttonWidth, buttonHeight);
        this.ENGINE.drawText(timeAttackTimeButtonXPos + (buttonWidth * BUTTON_OFFSET),
                timeAttackTimeButtonYPos + (fontSize * TEXT_OFFSET),
                Double.toString(timeAttackTimeValue),
                "Sans serif",
                fontSize);
    }
    public void drawBackToMainMenuButton() { this.ENGINE.drawImage(mainMenuIcon, mainMenuButtonXPos, mainMenuButtonYPos, buttonWidth, buttonHeight); }
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