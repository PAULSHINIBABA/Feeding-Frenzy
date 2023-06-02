/*
 * Author: Lucass (Xidi Kuang)
 * ID:
 *
 * Co-Author: Robert Tubman (Minor tweaking to merge with team code)
 * ID: 11115713
 *
 * Many of the getter/setter methods were refactored into Item Class because there were duplicate
 * methods in starfish.java, pearl.java, and boom.java
 */

package Assignment2;

import java.awt.*;
import java.util.Random;

public class pearl extends Item {

    // Constructor
    public pearl(GameEngine engine, Image pearlImage) { super(engine, pearlImage); }
    public boolean updatepearl(double dt,
                               double globalW,
                               double globalH,
                               double playerW,
                               double playerH) {
        updateTimeVisible(dt);
        double delayTime = 10;

        double[] environmentBoundary = new double[4];
        Random nr = new Random();
        environmentBoundary[0] = getPlayAreaCOMX() + getWindowToGlobalCOMOffsetX(); // Left edge
        environmentBoundary[1] = getPlayAreaCOMX() + getWindowToGlobalCOMOffsetX() + globalW; // Right edge
        environmentBoundary[2] = getPlayAreaCOMY() + getWindowToGlobalCOMOffsetY(); // Top edge
        environmentBoundary[3] = getPlayAreaCOMY() + getWindowToGlobalCOMOffsetY() + globalH; // Bottom edge

        if(!getIsVisible() && getTimeVisible() >= delayTime) {
            setSavedRandomPos(nr.nextDouble((environmentBoundary[1] - environmentBoundary[0])), nr.nextDouble((environmentBoundary[3] - environmentBoundary[2])));
            setPos(getSavedRandomPosX(), getSavedRandomPosY());
            setVisible(true);
            resetTimeVisible();

        } else {
            setPos(getSavedRandomPosX(), getSavedRandomPosY());
        }

        // Create new collider for the player based on player parameters
        double px = (-getWindowToGlobalCOMOffsetX()) - (playerW / 2.0);
        double py = (-getWindowToGlobalCOMOffsetY()) - (playerH / 2.0);
        double pex = getSavedRandomPosX();
        double pey = getSavedRandomPosY();

        //check collision with fish
        Rectangle playerCollider = new Rectangle((int)px, (int)py, (int)playerW, (int)playerH);
        Rectangle pearlRect = new Rectangle((int)pex, (int)pey, (int)getWidth(), (int)getHeight());
        if (getIsVisible() && playerCollider.intersects(new Rectangle(pearlRect))) {
            setVisible(false);
            setTimesEaten(getTimesEaten() + 1);
            return true;
        }
        return false;
    }
}