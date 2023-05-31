/*
 * Author: Lucass (Xidi Kuang)
 * ID:
 *
 * Co-Author: Robert Tubman (Refactored to merge with team code)
 * ID: 11115713
 */

package Assignment2;

import java.awt.*;

public class myfish {
    private final double SPEED_ACCELERATION_DEFAULT = 250.0;
    private final double MAX_SPEED_DEFAULT = 150.0;
    private double mposition_x,mposition_y;
    private double myfishspeed_x;
    private double myfishspeed_y;
    private double speedAcceleration;
    private double maxSpeed;
    private double myfish_w;
    private double myfish_h;
    private double fishImageX;
    private double fishImageY;
    private double fishImageWidth;
    private double fishImageHeight;
    private double fishHeadColliderXOffset;
    private double fishHeadColliderYOffset;
    private double fishHeadColliderRadius;
    private boolean facingLeft;
    private int playerSize;
    private boolean isAlive;

    // Constructor
    public myfish(double x, double y, double w, double h) {
        speedAcceleration = SPEED_ACCELERATION_DEFAULT;
        maxSpeed = MAX_SPEED_DEFAULT;

        myfish_w = w;
        myfish_h = h;
        mposition_x = x;
        mposition_y = y;
        myfishspeed_y = 0.0;
        myfishspeed_x = 0.0;

        fishImageX = x;
        fishImageY = y;
        fishImageWidth = w;
        fishImageHeight = h;

        playerSize = 0;
        isAlive = true;
        facingLeft = true;

        fishHeadColliderXOffset = myfish_w / 2.0;
        fishHeadColliderYOffset = myfish_h / 2.0;
        fishHeadColliderRadius = fishHeadColliderYOffset;
    }



    //**************************************************
    // Setters
    //**************************************************
    public void setFishParameters(double w, double l) {
        myfish_w = w;
        myfish_h = l;
    }
//    public void setFishLength(double length) { myfish_w = length; }
//    public void setFishHeight(double height) { myfish_h = height; }
    public void setFishHeadColliderParameters(double x, double y, double r) {
        fishHeadColliderXOffset = x;
        fishHeadColliderYOffset = y;
        fishHeadColliderRadius = r;
    }
//    public void setFishHeadColliderXOffset(double x) { fishHeadColliderXOffset = x; }
//    public void setFishHeadColliderYOffset(double y) { fishHeadColliderYOffset = y; }
//    public void setFishHeadColliderRadius(double r) { fishHeadColliderRadius = r; }
    public void setSize(int size) {
        playerSize = size;
    }
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    public void setXPos(double x) { mposition_x = x; }
    public void setYPos(double y) { mposition_y = y; }
    public void setXVel(double xv) { myfishspeed_x = xv; }
    public void setYVel(double yv) { myfishspeed_y = yv; }
    public void setFacingLeft(boolean facing) { facingLeft = facing; }
    public void setImageParameters(double x, double y, double w, double h) {
        fishImageX = x;
        fishImageY = y;
        fishImageWidth = w;
        fishImageHeight = h;
    }



    //**************************************************
    // Getters
    //**************************************************
    public double getFishHeadColliderXOffset() {
        return fishHeadColliderXOffset;
    }
    public double getFishHeadColliderYOffset() {
        return fishHeadColliderYOffset;
    }
    public double getFishHeadColliderRadius() {
        return fishHeadColliderRadius;
    }
    public int getSize() {
        return playerSize;
    }
    public boolean getIsAlive() {
        return isAlive;
    }
    public boolean getFacingLeft() { return facingLeft; }
    public double getXPos() { return mposition_x; }
    public double getYPos() { return mposition_y; }
    public double getWidth() { return myfish_w; }
    public double getHeight() { return myfish_h; }
    public double getXVel() { return myfishspeed_x; }
    public double getYVel() { return myfishspeed_y; }
    public double getImageX() { return fishImageX; }
    public double getImageY() { return fishImageY; }
    public double getImageWidth() { return fishImageWidth; }
    public double getImageHeight() { return fishImageHeight; }
    public double getMaxSpeed() { return maxSpeed; }



    //**************************************************
    // Other methods
    //**************************************************
    public void increaseSize() {
        increaseSize(1);
    }
    public void increaseSize(int size) {
        playerSize += size;
    }
    public Rectangle getmyfishRec() { return new Rectangle((int) mposition_x, (int) mposition_y, (int)myfish_w, (int)myfish_h); }
    public void updatemyfish(double dt,
                             boolean up,
                             boolean down,
                             boolean left,
                             boolean right,
                             double windowX,
                             double windowY,
                             double windowW,
                             double windowH) {

        if (up) {
            myfishspeed_y -= speedAcceleration * dt; // Update the speed
            if (myfishspeed_y < -maxSpeed) { myfishspeed_y = -maxSpeed; } // Limit the speed
        }
        if (down) {
            myfishspeed_y += speedAcceleration * dt; // Update the speed
            if (myfishspeed_y > maxSpeed) { myfishspeed_y = maxSpeed; } // Limit the speed
        }
        if (left) {
            myfishspeed_x -= speedAcceleration * dt; // Update the speed
            if (myfishspeed_x < -maxSpeed) { myfishspeed_x = -maxSpeed; } // Limit the speed
        }
        if (right) {
            myfishspeed_x += speedAcceleration * dt; // Update the speed
            if (myfishspeed_x > maxSpeed) { myfishspeed_x = maxSpeed; } // Limit the speed
        }

        // Normalize the speed
        double[] normalizedSpeed = normalizeSpeed(myfishspeed_x, myfishspeed_y);

        // Update the position based on the speed
        mposition_x += normalizedSpeed[0] * dt;
        mposition_y += normalizedSpeed[1] * dt;
        fishImageX = mposition_x + (myfish_w / 2.0) - (fishImageWidth / 2.0);
        fishImageY = mposition_y + (myfish_h / 2.0) - (fishImageHeight / 2.0);

        System.out.println("maxSpeed: " + maxSpeed + "\tspeedAcceleration:" + speedAcceleration);

        // Edge detection
        if (mposition_x < windowX) {
            mposition_x = windowX;
            myfishspeed_x = 0;
        } else if (mposition_x + myfish_w > (windowW + windowX)) {
            mposition_x = (windowW + windowX) - myfish_w;
            myfishspeed_x = 0;
        }

        if (mposition_y < windowY) {
            mposition_y = windowY;
            myfishspeed_y = 0;
        } else if (mposition_y + myfish_h > (windowY + windowH)) {
            mposition_y = (windowY + windowH) - myfish_h;
            myfishspeed_y = 0;
        }
    }

    public double[] normalizeSpeed(double x, double y) {
        // Calculate the directional vector
        double c = Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));

        // Normalise the vector as a scalar multiplier
        double alpha;
        if (c > 0) { alpha = c / maxSpeed; }
        else { alpha = 0; }

        // Normalize the x and y
        double[] normalized = new double[2];
        normalized[0] = x * alpha;
        normalized[1] = y * alpha;

        return normalized; // return the normalized values
    }
    public void incrementMaxSpeed(double maxSpeed) { this.maxSpeed += maxSpeed; }
    public void incrementAccelerationSpeed(double accelVal) { this.speedAcceleration += accelVal; }
    public void resetSpeed() {
        speedAcceleration = SPEED_ACCELERATION_DEFAULT;
        maxSpeed = MAX_SPEED_DEFAULT;
    }
}
