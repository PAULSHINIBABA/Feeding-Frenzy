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

        fishHeadColliderXOffset = this.myfish_w / 2.0;
        fishHeadColliderYOffset = this.myfish_h / 2.0;
        fishHeadColliderRadius = fishHeadColliderYOffset;

    }

    //**************************************************
    // Setters
    //**************************************************
    public void setFishLength(double length) {
        this.myfish_w = length;
    }
    public void setFishHeight(double height) {
        this.myfish_h = height;
    }
    public void setFishHeadColliderXOffset(double x) {
        this.fishHeadColliderXOffset = x;
    }
    public void setFishHeadColliderYOffset(double y) {
        this.fishHeadColliderYOffset = y;
    }
    public void setFishHeadColliderRadius(double r) {
        this.fishHeadColliderRadius = r;
    }
    public void setSize(int size) {
        this.playerSize = size;
    }
    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }
    public void setXPos(double x) { this.mposition_x = x; }
    public void setYPos(double y) { this.mposition_y = y; }
    public void setXVel(double xv) { this.myfishspeed_x = xv; }
    public void setYVel(double yv) { this.myfishspeed_y = yv; }
    public void setFacingLeft(boolean facing) { this.facingLeft = facing; }

    //**************************************************
    // Getters
    //**************************************************
    public double getFishHeadColliderXOffset() {
        return this.fishHeadColliderXOffset;
    }
    public double getFishHeadColliderYOffset() {
        return this.fishHeadColliderYOffset;
    }
    public double getFishHeadColliderRadius() {
        return this.fishHeadColliderRadius;
    }
    public int getSize() {
        return this.playerSize;
    }
    public boolean getIsAlive() {
        return this.isAlive;
    }
    public boolean getFacingLeft() { return this.facingLeft; }
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
        this.increaseSize(1);
    }
    public void increaseSize(int size) {
        this.playerSize += size;
    }
}
