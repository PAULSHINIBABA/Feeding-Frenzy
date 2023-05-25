/*
 * Author: Lucass
 * ID:
 *
 * Co-Author: Robert Tubman (Tweaked to merge with team code)
 * ID: 11115713
 */

package Assignment2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class myfish {
    private double mposition_x,mposition_y;//myself fish position
    private double myfishspeed_x,myfishspeed_y;
    private double myfish_w = 90,myfish_h = 60;//myfish width and myfish high
    private double fishHeadColliderXOffset;
    private double fishHeadColliderYOffset;
    private double fishHeadColliderRadius;
    private boolean facingLeft;
    private int playerSize;
    private boolean isAlive;

    // Constructor
    public myfish(double x, double y){
        mposition_x = x;
        mposition_y = y;
        myfishspeed_y = 0;
        myfishspeed_x = 0;

        playerSize = 0;
        isAlive = true;
        facingLeft = true;
    }

    public Rectangle getmyfishRec(){
        return new Rectangle((int) mposition_x, (int) mposition_y, (int)myfish_w, (int)myfish_h);
    }
//    public void updatemyfish(double dt,boolean up, boolean down,boolean left,boolean right, int Window_w,int Window_h){
    public void updatemyfish(double dt,
                             boolean up,
                             boolean down,
                             boolean left,
                             boolean right,
                             double windowX,
                             double windowY,
                             double windowW,
                             double windowH) {

        if(up){ myfishspeed_y -= 250 * dt; }
        if (down){ myfishspeed_y += 250 * dt; }
        if (left){ myfishspeed_x -= 250 * dt; }
        if (right){ myfishspeed_x += 250 * dt; }

        mposition_x += myfishspeed_x * dt;
        mposition_y += myfishspeed_y * dt;

        // Edge detection
        if (mposition_x < windowX) {
            mposition_x = windowX;
            myfishspeed_x = 0;
        }
        else if (mposition_x + myfish_w > (windowW + windowX)) {
            mposition_x = (windowW + windowX) - myfish_w;
            myfishspeed_x = 0;
        }

        if (mposition_y < windowY) {
            mposition_y = windowY;
            myfishspeed_y = 0;
        }
        else if (mposition_y + myfish_h > (windowY + windowH)) {
            mposition_y = (windowY + windowH) - myfish_h;
            myfishspeed_y = 0;
        }

        fishHeadColliderXOffset = myfish_w / 2.0;
        fishHeadColliderYOffset = myfish_h / 2.0;
        fishHeadColliderRadius = fishHeadColliderYOffset;

    }

    //**************************************************
    // Setters
    //**************************************************
    public void setFishLength(double length) {
        myfish_w = length;
    }
    public void setFishHeight(double height) {
        myfish_h = height;
    }
    public void setFishHeadColliderXOffset(double x) {
        fishHeadColliderXOffset = x;
    }
    public void setFishHeadColliderYOffset(double y) {
        fishHeadColliderYOffset = y;
    }
    public void setFishHeadColliderRadius(double r) {
        fishHeadColliderRadius = r;
    }
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

    //**************************************************
    // Other methods
    //**************************************************
    public void increaseSize() {
        increaseSize(1);
    }
    public void increaseSize(int size) {
        playerSize += size;
    }
}
