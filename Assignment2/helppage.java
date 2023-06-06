/*
 * Author: Lucas (Xidi Kuang)
 * ID:21008041
 *
 * Co-Author: Robert Tubman (Major refactoring to merge with team code)
 * ID: 11115713
 *
 * The helppage class
 *
 * This page is used to show some helpful tips about playing the game
 */

package Assignment2;

import java.awt.*;

public class helppage {
    private GameEngine engine;
    private Image helpbg;
    private int timeattacklinepos_x;
    private int timeattacklinepos_y;
    private int singleplaylinepos_x;
    private int singleplaylinepos_y;
    private int otherlinepos_x;
    private int otherlinepos_y;
    private Image title;
    private Image backicon;
    private double backIconXPos;
    private double backIconYPos;
    private double buttonSize;
    private int titleX;
    private int titleY;
    private int titleWidth;
    private int titleHeight;
    private int fontSize;
    private int displayOffset;
    private int windowWidth;
    private int windowHeight;
    private int cutoffLimit;
    private String singlePlayerHelp;
    private String timeAttackHelp;
    private String descriptionHelp;
    private Image whiteBackgroundImage;

    // Constructor
    public helppage(GameEngine engine,
                    Image helpbg,
                    Image title,
                    Image backicon,
                    int titleW,
                    int titleH,
                    int windowWidth,
                    int windowHeight) {
        this.engine = engine;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;

        cutoffLimit = 900;

        buttonSize = 75;
        backIconXPos = buttonSize;
        backIconYPos = windowHeight - (2 * buttonSize);

        this.helpbg = helpbg;
        this.title = title;
        this.backicon = backicon;
        fontSize = 15;
        displayOffset = 2;
        titleWidth = titleW;
        titleHeight = titleH;
        titleX = (windowWidth / 2) - (titleWidth / 2);
        titleY = windowHeight / 16;

        timeattacklinepos_x = 0;
        timeattacklinepos_y = (windowHeight / 8) * 5;
        singleplaylinepos_x = 0;
        singleplaylinepos_y = (windowHeight / 8) * 4;
        otherlinepos_x = 0;
        otherlinepos_y = (windowHeight / 8) * 3;

        // Time attack help
        timeAttackHelp = "TIME ATTACK: The player is tasked with getting as many points as possible until the timer runs out.";

        // Single player help
        singlePlayerHelp = "SINGLE PLAYER: the player is tasked with eating a predetermined number of enemy fish to complete the level shown in the growth bar";

        // Description help
        descriptionHelp = "Play as a little fish with a big appetite. Eat everything you come across, where the more you eat, the bigger you become!";
    }


    //**************************************************
    // Setter
    //**************************************************
    public void setWhiteBackgroundImage(Image whiteBackgroundImage) { this.whiteBackgroundImage = whiteBackgroundImage; }


    //**************************************************
    // Other methods
    //**************************************************
    public void drawhelpimg() {
        engine.drawImage(helpbg,0,0,engine.width(),engine.height());
    }
    public void drawtitleimage(){
        engine.drawImage(title, titleX, titleY, titleWidth, titleHeight);
    }
    public void drawtimeline() {
        // TODO: Refine this to only calculate on init
        engine.changeColor(0,0,0);
        int factor = (windowWidth / 50);
        if (timeAttackHelp.length() <= factor) {
            engine.drawImage(whiteBackgroundImage,
                    fontSize + timeattacklinepos_x - displayOffset,
                    timeattacklinepos_y - fontSize - displayOffset,
                    (int)(windowWidth - ((fontSize + timeattacklinepos_x) * 2)),
                    fontSize + (3 * displayOffset));
            engine.drawText(fontSize + timeattacklinepos_x,
                    timeattacklinepos_y,
                    timeAttackHelp,
                    "a",
                    fontSize);
        } else {
            StringBuilder temp = new StringBuilder();
            int spaceCount = 0;
            int offset = 0;
            engine.drawImage(whiteBackgroundImage,
                    fontSize + timeattacklinepos_x - displayOffset,
                    timeattacklinepos_y - fontSize - displayOffset,
                    (int)(windowWidth - ((fontSize + timeattacklinepos_x) * 2)),
                    (2 * fontSize) + (4 * displayOffset));
            for (int i = 0; i < timeAttackHelp.length(); i++) {
                if (timeAttackHelp.charAt(i) == ' ') {
                    temp.append(timeAttackHelp.charAt(i));
                    if (spaceCount >= factor) {
                        engine.drawText(fontSize + timeattacklinepos_x,
                                timeattacklinepos_y + offset,
                                temp.toString(),
                                "a",
                                fontSize);
                        temp = new StringBuilder();
                        spaceCount = 0;
                        offset += fontSize + displayOffset;
                    }
                    spaceCount += 1;

                } else {
                    temp.append(timeAttackHelp.charAt(i));
                }

            }
            engine.drawText(fontSize + timeattacklinepos_x,
                    timeattacklinepos_y + offset,
                    temp.toString(),
                    "a",
                    fontSize);
        }
    }
    public void drawsingleline() {
        // TODO: Refine this to only calculate on init
        engine.changeColor(0,0,0);
        int factor = (windowWidth / 50);
        if (singlePlayerHelp.length() <= factor) {
            engine.drawImage(whiteBackgroundImage,
                    fontSize + singleplaylinepos_x - displayOffset,
                    singleplaylinepos_y - fontSize - displayOffset,
                    (int)(windowWidth - ((fontSize + singleplaylinepos_x) * 2)),
                    fontSize + (3 * displayOffset));
            engine.drawText(fontSize + singleplaylinepos_x,
                    singleplaylinepos_y,
                    singlePlayerHelp,
                    "a",
                    fontSize);
        } else {
            engine.drawImage(whiteBackgroundImage,
                    fontSize + singleplaylinepos_x - displayOffset,
                    singleplaylinepos_y - fontSize - displayOffset,
                    (int)(windowWidth - ((fontSize + singleplaylinepos_x) * 2)),
                    (2 * fontSize) + (4 * displayOffset));
            StringBuilder temp = new StringBuilder();
            int spaceCount = 0;
            int offset = 0;
            for (int i = 0; i < singlePlayerHelp.length(); i++) {
                if (singlePlayerHelp.charAt(i) == ' ') {
                    temp.append(singlePlayerHelp.charAt(i));
                    if (spaceCount >= factor) {
                        engine.drawText(fontSize + singleplaylinepos_x,
                                singleplaylinepos_y + offset,
                                temp.toString(),
                                "a",
                                fontSize);
                        temp = new StringBuilder();
                        spaceCount = 0;
                        offset += fontSize + displayOffset;
                    }
                    spaceCount += 1;

                } else {
                    temp.append(singlePlayerHelp.charAt(i));
                }

            }
            engine.drawText(fontSize + singleplaylinepos_x,
                    singleplaylinepos_y + offset,
                    temp.toString(),
                    "a",
                    fontSize);
        }
    }
    public void drawother() {
        // TODO: Refine this to only calculate on init
        engine.changeColor(0,0,0);
        int factor = (windowWidth / 50);
        if (descriptionHelp.length() <= factor) {
            engine.drawImage(whiteBackgroundImage,
                    fontSize + otherlinepos_x - displayOffset,
                    otherlinepos_y - fontSize - displayOffset,
                    (int)(windowWidth - ((fontSize + otherlinepos_x) * 2)),
                    fontSize + (3 * displayOffset));
            engine.drawText(fontSize + otherlinepos_x,
                    otherlinepos_y,
                    descriptionHelp,
                    "a",
                    fontSize);
        } else {
            engine.drawImage(whiteBackgroundImage,
                    fontSize + otherlinepos_x - displayOffset,
                    otherlinepos_y - fontSize - displayOffset,
                    (int)(windowWidth - ((fontSize + otherlinepos_x) * 2)),
                    (2 * fontSize) + (4 * displayOffset));
            StringBuilder temp = new StringBuilder();
            int spaceCount = 0;
            int offset = 0;
            for (int i = 0; i < descriptionHelp.length(); i++) {
                if (descriptionHelp.charAt(i) == ' ') {
                    temp.append(descriptionHelp.charAt(i));
                    if (spaceCount >= factor) {
                        engine.drawText(fontSize + otherlinepos_x,
                                otherlinepos_y + offset,
                                temp.toString(),
                                "a",
                                fontSize);
                        temp = new StringBuilder();
                        spaceCount = 0;
                        offset += fontSize + displayOffset;
                    }
                    spaceCount += 1;

                } else {
                    temp.append(descriptionHelp.charAt(i));
                }

            }
            engine.drawText(fontSize + otherlinepos_x,
                    otherlinepos_y + offset,
                    temp.toString(),
                    "a",
                    fontSize);
        }
    }
    public void drawbackicon() { engine.drawImage(backicon, backIconXPos, backIconYPos, buttonSize, buttonSize); }
    public void drawall() {
        drawhelpimg();
        drawother();
        drawsingleline();
        drawtitleimage();
        drawtimeline();
        drawbackicon();
    }
    public String inGameMenuMouseClicked(double mouseX, double mouseY) {

        if (clickButton(mouseX,
                mouseY,
                backIconXPos + (buttonSize/2.0),
                backIconYPos + (buttonSize/2.0),
                buttonSize/2.0)) {
            return "main_menu"; // Go back to main menu

        }
        return "nothing";
    }
    public boolean clickButton(double mouseX, double mouseY, double buttonX, double buttonY, double radius) {
        double dx = mouseX - buttonX;
        double dy = mouseY - buttonY;
        return (dx * dx + dy * dy) <= (radius * radius);
    }
}
