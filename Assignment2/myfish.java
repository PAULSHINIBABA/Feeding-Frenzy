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
    private final double SPEED_DECELERATION_DEFAULT = 1.0;
    private final double SIZE_0 = 1.0;
    private final double SIZE_1 = 1.15;
    private final double SIZE_2 = 1.3;
    private final double SIZE_3 = 1.45;
    private final double SIZE_4 = 1.6;
    private final double SIZE_UNDEFINED = 0.5;
    private double defaultWidth;
    private double defaultHeight;
    private double defaultImageWidth;
    private double defaultImageHeight;
    private double offsetX;
    private double offsetY;
    private double myfishspeed_x;
    private double myfishspeed_y;
    private double previousSpeedX;
    private double previousSpeedY;
    private double originX;
    private double originY;
    private double speedAcceleration;
    private double speedDeceleration;
    private double maxSpeed;
    private double myfish_w;
    private double myfish_h;
    private double fishImageOffsetX;
    private double fishImageOffsetY;
    private double fishImageWidth;
    private double fishImageHeight;
    private double fishHeadColliderXOffset;
    private double fishHeadColliderYOffset;
    private boolean facingLeft;
    private int playerSize;
    private boolean isAlive;

    // Constructor
    public myfish(double x, double y, double w, double h) {
        speedAcceleration = SPEED_ACCELERATION_DEFAULT;
        speedDeceleration = 1.0;
        maxSpeed = MAX_SPEED_DEFAULT;

        originX = x;
        originY = y;

        defaultWidth = w;
        defaultHeight = h;
        myfish_w = defaultWidth;
        myfish_h = defaultHeight;
        offsetX = myfish_w / 2.0;
        offsetY = myfish_h / 2.0;
        myfishspeed_y = 0.0;
        myfishspeed_x = 0.0;
        previousSpeedX = 0.0;
        previousSpeedY = 0.0;

        defaultImageWidth = w;
        defaultImageHeight = h;
        fishImageWidth = defaultImageWidth;
        fishImageHeight = defaultImageHeight;
        fishImageOffsetX = fishImageWidth / 2.0;
        fishImageOffsetY = fishImageHeight / 2.0;

        playerSize = 0;
        isAlive = true;
        facingLeft = true;

        fishHeadColliderXOffset = offsetX;
        fishHeadColliderYOffset = offsetY;
    }


    //**************************************************
    // Setters
    //**************************************************
    public void setFishParameters(double w, double l) {
        myfish_w = w;
        myfish_h = l;
    }
    public void setFishHeadColliderXOffset(double x) { fishHeadColliderXOffset = x; }
    public void setFishHeadColliderYOffset(double y) { fishHeadColliderYOffset = y; }
    public void setFishHeadColliderParameters(double x, double y, double r) {
        fishHeadColliderXOffset = x;
        fishHeadColliderYOffset = y;
    }
    public void setSize(int size) {
        playerSize = size;
    }
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    public void setXPos(double x) { originX = x; }
    public void setYPos(double y) { originY = y; }
    public void setXVel(double xv) { myfishspeed_x = xv; }
    public void setYVel(double yv) { myfishspeed_y = yv; }
    public void setFacingLeft(boolean facing) { facingLeft = facing; }
    public void setImageParameters(double x, double y, double w, double h) {
        defaultImageWidth = w;
        defaultImageHeight = h;

        fishImageOffsetX = x;
        fishImageOffsetY = y;
        fishImageWidth = defaultImageWidth;
        fishImageHeight = defaultImageHeight;
    }


    //**************************************************
    // Getters
    //**************************************************
    public double getFishHeadColliderXOffset() { return fishHeadColliderXOffset; }
    public double getFishHeadColliderYOffset() { return fishHeadColliderYOffset; }
    public int getSize() {
        return playerSize;
    }
    public boolean getIsAlive() {
        return isAlive;
    }
    public boolean getFacingLeft() { return facingLeft; }
    public double getXPos() { return originX; }
    public double getYPos() { return originY; }
    public double getWidth() { return myfish_w; }
    public double getHeight() { return myfish_h; }
    public double getXVel() { return myfishspeed_x; }
    public double getYVel() { return myfishspeed_y; }
    public double getImageOffsetX() { return fishImageOffsetX; }
    public double getImageOffsetY() { return fishImageOffsetY; }
    public double getImageWidth() { return fishImageWidth; }
    public double getImageHeight() { return fishImageHeight; }
    public double getMaxSpeed() { return maxSpeed; }
    public double getOffsetX() { return offsetX; }
    public double getOffsetY() { return offsetY; }


    //**************************************************
    // Other methods
    //**************************************************
    public Rectangle getmyfishRec() { return new Rectangle( (int)(originX - offsetX), (int)(originY - offsetY), (int)myfish_w, (int)myfish_h ); }
    public void updatemyfish(double dt,
                             boolean up,
                             boolean down,
                             boolean left,
                             boolean right,
                             double ox,
                             double oy,
                             double w,
                             double h) {

        boolean isMoving = false;
        if (up) {
            isMoving = true;
            myfishspeed_y -= speedAcceleration  * dt; // Update the speed
            if (myfishspeed_y < -maxSpeed) { myfishspeed_y = -maxSpeed; } // Limit the speed
        }
        if (down) {
            isMoving = true;
            myfishspeed_y += speedAcceleration  * dt; // Update the speed
            if (myfishspeed_y > maxSpeed) { myfishspeed_y = maxSpeed; } // Limit the speed
        }
        if (left) {
            isMoving = true;
            myfishspeed_x -= speedAcceleration  * dt; // Update the speed
            if (myfishspeed_x < -maxSpeed) { myfishspeed_x = -maxSpeed; } // Limit the speed
        }
        if (right) {
            isMoving = true;
            myfishspeed_x += speedAcceleration  * dt; // Update the speed
            if (myfishspeed_x > maxSpeed) { myfishspeed_x = maxSpeed; } // Limit the speed
        }

        // Decelerate the player
        if (!isMoving) {
            if (myfishspeed_x > SPEED_DECELERATION_DEFAULT) { myfishspeed_x -= SPEED_DECELERATION_DEFAULT; }
            else if (myfishspeed_x < -SPEED_DECELERATION_DEFAULT) { myfishspeed_x += SPEED_DECELERATION_DEFAULT; }

            if (myfishspeed_y > SPEED_DECELERATION_DEFAULT) { myfishspeed_y -= SPEED_DECELERATION_DEFAULT; }
            else if (myfishspeed_y < -SPEED_DECELERATION_DEFAULT) { myfishspeed_y += SPEED_DECELERATION_DEFAULT; }
        }

        // Normalize the speed
        // TODO: Not working, fix
        double[] normalizedSpeed = normalizeSpeed(myfishspeed_x, myfishspeed_y);

        // Update the position based on the speed normalized
        originX += normalizedSpeed[0] * dt;
        originY += normalizedSpeed[1] * dt;

        // Check for collision with the game environment boundaries
        if ((originX - offsetX) < -(ox - (w / 2.0))) {
            originX = -(ox - (w / 2.0)) + offsetX;
            myfishspeed_x = 0;
        } else if ((originX + offsetX) > (ox + (w / 2.0))) {
            originX = ox + (w / 2.0) - offsetX;
            myfishspeed_x = 0;
        }

        if ((originY - offsetY) < -(oy - (h / 2.0))) {
            originY = -(oy - (h / 2.0)) + offsetY;
            myfishspeed_y = 0;
        } else if ((originY + offsetY) > (oy + (h / 2.0))) {
            originY = (oy + (h / 2.0)) - offsetY;
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
    public void updateOffsets() {
        offsetX = myfish_w / 2.0;
        offsetY = myfish_h / 2.0;
        fishImageOffsetX = fishImageWidth / 2.0;
        fishImageOffsetY = fishImageHeight / 2.0;
    }
    public void updateSize() {
        switch (playerSize) {
            case 0 -> {
                myfish_w = defaultWidth * SIZE_0;
                myfish_h = defaultHeight * SIZE_0;
                fishImageWidth = defaultImageWidth * SIZE_0;
                fishImageHeight = defaultImageHeight * SIZE_0;
            }
            case 1 -> {
                myfish_w = defaultWidth * SIZE_1;
                myfish_h = defaultHeight * SIZE_1;
                fishImageWidth = defaultImageWidth * SIZE_1;
                fishImageHeight = defaultImageHeight * SIZE_1;
            }
            case 2 -> {
                myfish_w = defaultWidth * SIZE_2;
                myfish_h = defaultHeight * SIZE_2;
                fishImageWidth = defaultImageWidth * SIZE_2;
                fishImageHeight = defaultImageHeight * SIZE_2;
            }
            default -> {
                System.out.println("ERROR: Updating player fish size!");
            }
        }
        updateOffsets();
    }
}
