/*
 * Author: Robert Tubman
 * ID: 11115713
 *
 * Reference: Lucas (Xidi Kuang)
 * ID: 21008041
 *
 * Team:
 * David, 22004319
 * Lucas (Xidi Kuang), 21008041
 * Paul (Zeju Fan), 21019135
 * Robert Tubman, 11115713
 *
 * The Item Class
 *
 * Use this class to instantiate items in the game.
 * Some fields are required on instantiation, but the rest can be set using
 * the Setters.
 *
 * Many of the getter/setter methods from starfish.java, pearl.java, and boom.java were refactored
 * into this Item Class because there were duplicate methods.
 */

package Assignment2;

import java.awt.*;
import java.util.Random;

public class Item {
    // System fields
    private GameEngine engine;
    private double windowToGlobalCOMOffsetX;
    private double windowToGlobalCOMOffsetY;
    private double playAreaCOMX;
    private double playAreaCOMY;

    // Item fields
    private Image itemImage;
    private double width;
    private double height;
    private double xPos;
    private double yPos;
    private double xSpeed;
    private double ySpeed;
    private boolean directionIsLeft;
    private boolean directionIsUp;
    private double savedRandomPosX;
    private double savedRandomPosY;
    private boolean isVisible;
    private double timeVisible;
    private int timesEaten;

    // Constructor
    public Item(GameEngine engine, Image itemImage) {
        this.engine = engine;
        this.itemImage = itemImage;

        // Set the playArea to a default value
        playAreaCOMX = 0.0;
        playAreaCOMY = 0.0;
        // Set the window to global centre of mass
        windowToGlobalCOMOffsetX = 0.0;
        windowToGlobalCOMOffsetY = 0.0;

        // Set the item dimensions
        width = 30;
        height = 30;

        // Set visibility
        isVisible = false;
        timeVisible = 0;

        // Set score field
        timesEaten = 0;

        // Set heading fields
        directionIsLeft = true;
        directionIsUp = true;
    }


    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    // Set the x, y positions of the item
    public void setPos(double x,double y) {
        xPos = x;
        yPos = y;
    }
    // Set the item dimensions
    public void setDimensions(double w, double h) {
        width = w;
        height = h;
    }
    // Set the speed and direction of the item
    public void setSpeed(double x, double y) {
        xSpeed = x;
        ySpeed = y;
        directionIsLeft = x < 0;
        directionIsUp = y < 0;
    }
    // Set the item visibility
    public void setVisible(boolean visible) { isVisible = visible; }
    public void setTimeVisible(double timeVisible) { this.timeVisible = timeVisible; }
    // Set the number of times the item was eaten
    public void setTimesEaten(int timesEaten) { this.timesEaten = timesEaten; }
    // Set the play area centre of mass
    // This is required to be set to the actual play area for "camera panning" to work.
    public void setPlayAreaCOM(double x, double y) {
        playAreaCOMX = x;
        playAreaCOMY = y;
    }
    // Set the display window to global play area centre of mass offsets
    public void setWindowToGlobalCOMOffset(double x, double y) {
        windowToGlobalCOMOffsetX = -x;
        windowToGlobalCOMOffsetY = -y;
    }
    // Set the saved randomised spawn position of the item
    public void setSavedRandomPos(double x, double y) {
        savedRandomPosX = x;
        savedRandomPosY = y;
    }


    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public double getXPos() { return xPos; }
    public double getYPos() { return yPos; }
    public double getXSpeed() { return xSpeed; }
    public double getYSpeed() { return ySpeed; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public boolean getIsVisible() { return isVisible; }
    public double getTimeVisible() { return timeVisible; }
    public int getTimesEaten() { return timesEaten; }
    public double getPlayAreaCOMX() { return playAreaCOMX; }
    public double getPlayAreaCOMY() { return playAreaCOMY; }
    public double getWindowToGlobalCOMOffsetX() { return windowToGlobalCOMOffsetX; }
    public double getWindowToGlobalCOMOffsetY() { return windowToGlobalCOMOffsetY; }
    public double getSavedRandomPosX() { return savedRandomPosX; }
    public double getSavedRandomPosY() { return savedRandomPosY; }


    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
    // Randomize the item speed
    public void randomSpeed() {
        Random randSpeed = new Random();
        xSpeed = randSpeed.nextDouble(100) - 50;
        ySpeed = randSpeed.nextDouble(100) - 50;
    }
    // Move the item in the environment (global space)
    public void moveItem(double dt, double globalW, double globalH) {
        // The game boundaries
        double[] envBound = new double[4];
        envBound[0] = playAreaCOMX + windowToGlobalCOMOffsetX; // Left edge
        envBound[1] = playAreaCOMX + windowToGlobalCOMOffsetX + globalW; // Right edge
        envBound[2] = playAreaCOMY + windowToGlobalCOMOffsetY; // Top edge
        envBound[3] = playAreaCOMY + windowToGlobalCOMOffsetY + globalH; // Bottom edge

        if (directionIsLeft && xSpeed > 0) { xSpeed *= -1.0; }
        else if (!directionIsLeft && xSpeed < 0) { xSpeed *= -1.0; }
        if (directionIsUp && ySpeed > 0) { ySpeed *= -1.0; }
        else if (!directionIsUp && ySpeed < 0) { ySpeed *= -1.0; }

        // Handle xSpeed
        xPos += xSpeed * dt;
        // The position in global space
        double itemGlobalXPos[] = {
                (xPos + playAreaCOMX + windowToGlobalCOMOffsetX), // Item left face
                (xPos + playAreaCOMX + windowToGlobalCOMOffsetX + width) // Item right face
        };
        // reverse direction
        if (itemGlobalXPos[0] < envBound[0]) { directionIsLeft = false; }
        else if (itemGlobalXPos[1] > envBound[1]) { directionIsLeft = true; }

        // Handle ySpeed
        yPos += ySpeed * dt;
        // The position in global space
        double itemGlobalYPos[] = {
                (yPos + playAreaCOMY + windowToGlobalCOMOffsetY), // Item top face
                (yPos + playAreaCOMY + windowToGlobalCOMOffsetY + height) // Item bottom face
        };
        // reverse direction
        if (itemGlobalYPos[0] < envBound[2]) { directionIsUp = false; }
        else if (itemGlobalYPos[1] > envBound[3]) { directionIsUp = true; }
    }
    // Update the item visibility
    public void updateTimeVisible(double dt) { timeVisible += dt; }
    public void resetTimeVisible() { timeVisible = 0; }
    // Draw the item
    public void drawItem() {
        double[] environmentBoundary = new double[2];
        environmentBoundary[0] = playAreaCOMX + windowToGlobalCOMOffsetX; // Left edge
        environmentBoundary[1] = playAreaCOMY + windowToGlobalCOMOffsetY; // Top edge
        engine.drawImage(itemImage, (environmentBoundary[0] + xPos), (environmentBoundary[1] + yPos), width, height);
    }
    // Draw the item colliders (debug)
    public void drawItemColliders() {
        double[] environmentBoundary = new double[2];
        environmentBoundary[0] = playAreaCOMX + windowToGlobalCOMOffsetX; // Left edge
        environmentBoundary[1] = playAreaCOMY + windowToGlobalCOMOffsetY; // Top edge
        double x1 = environmentBoundary[0] + xPos;
        double x2 = environmentBoundary[0] + xPos + width;
        double y1 = environmentBoundary[1] + yPos;
        double y2 = environmentBoundary[1] + yPos + height;

        engine.changeColor(255,0,0);
        engine.drawLine(x1, y1, x2, y1);
        engine.drawLine(x1, y2, x2, y2);
        engine.drawLine(x1, y1, x1, y2);
        engine.drawLine(x2, y1, x2, y2);
    }
    public void windowToGlobalCOMOffset(double x, double y) {
        windowToGlobalCOMOffsetX = -x;
        windowToGlobalCOMOffsetY = -y;
    }
}
