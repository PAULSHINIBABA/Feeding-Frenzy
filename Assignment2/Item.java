/*
 * Author: Robert Tubman
 * ID: 11115713
 *
 * Reference: Lucas (Xidi Kuang)
 * ID:
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
    private double savedRandomPosX;
    private double savedRandomPosY;
    private boolean isVisible;
    private double timeVisible;
    private int timesEaten;

    // Constructor
    public Item(GameEngine engine, Image itemImage) {
        this.engine = engine;
        this.itemImage = itemImage;

        playAreaCOMX = 0.0;
        playAreaCOMY = 0.0;
        windowToGlobalCOMOffsetX = 0.0;
        windowToGlobalCOMOffsetY = 0.0;

        width = 30;
        height = 30;
        isVisible = false;
        timeVisible = 0;
        timesEaten = 0;
    }


    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
//    public void setPos(double x,double y){
    public void setPos(double x,double y){
        xPos = x;
        yPos = y;
    }
    public void setDimensions(double w, double h) {
        width = w;
        height = h;
    }
    public void setSpeed(double x, double y) {
        xSpeed = x;
        ySpeed = y;
    }
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
    public void setTimeVisible(double timeVisible) { this.timeVisible = timeVisible; }
    public void setTimesEaten(int timesEaten) { this.timesEaten = timesEaten; }
    public void setPlayAreaCOM(double x, double y) {
        playAreaCOMX = x;
        playAreaCOMY = y;
    }
    public void setWindowToGlobalCOMOffset(double x, double y) {
        windowToGlobalCOMOffsetX = x;
        windowToGlobalCOMOffsetY = y;
    }
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
    public void randomSpeed() {
        Random randSpeed = new Random();
        xSpeed = randSpeed.nextDouble(100) - 50;
        ySpeed = randSpeed.nextDouble(100) - 50;
    }
    public void moveItem(double dt, double globalW, double globalH) {
        // The game boundaries
        double[] envBound = new double[4];
        envBound[0] = playAreaCOMX + windowToGlobalCOMOffsetX; // Left edge
        envBound[1] = playAreaCOMX + windowToGlobalCOMOffsetX + globalW; // Right edge
        envBound[2] = playAreaCOMY + windowToGlobalCOMOffsetY; // Top edge
        envBound[3] = playAreaCOMY + windowToGlobalCOMOffsetY + globalH; // Bottom edge

        // Handle xSpeed
        xPos += xSpeed * dt;
        // The position in global space
        double itemGlobalXPos[] = {
                (xPos + playAreaCOMX + windowToGlobalCOMOffsetX), // Item left face
                (xPos + playAreaCOMX + windowToGlobalCOMOffsetX + width) // Item right face
        };
        // reverse direction
        if (itemGlobalXPos[0] < envBound[0] || itemGlobalXPos[1] > envBound[1]) { xSpeed *= -1.0; }

        // Handle ySpeed
        yPos += ySpeed * dt;
        // The position in global space
        double itemGlobalYPos[] = {
                (yPos + playAreaCOMY + windowToGlobalCOMOffsetY), // Item top face
                (yPos + playAreaCOMY + windowToGlobalCOMOffsetY + height) // Item bottom face
        };
        // reverse direction
        if (itemGlobalYPos[0] < envBound[2] || itemGlobalYPos[1] > envBound[3]) { ySpeed *= -1.0; }
    }
    public void updateTimeVisible(double dt) { timeVisible += dt; }
    public void resetTimeVisible() { timeVisible = 0; }
    public void drawItem() {
        double[] environmentBoundary = new double[2];
        environmentBoundary[0] = playAreaCOMX + windowToGlobalCOMOffsetX; // Left edge
        environmentBoundary[1] = playAreaCOMY + windowToGlobalCOMOffsetY; // Top edge
        engine.drawImage(itemImage, (environmentBoundary[0] + xPos), (environmentBoundary[1] + yPos), width, height);
    }
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
