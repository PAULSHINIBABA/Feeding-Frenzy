/*
 * Author: Lucass (Xidi Kuang)
 * ID:
 *
 * Co-Author: Robert Tubman (Minor refactoring to merge with team code)
 * ID: 11115713
 *
 * Many of the getter/setter methods were refactored into Item Class because there were duplicate
 * methods in starfish.java, pearl.java, and boom.java
 */

package Assignment2;

import java.awt.*;
import java.util.Random;

public class starfish extends Item {

    // Constructor
    public starfish(GameEngine engine, Image starfishImage) { super(engine, starfishImage); }

    // Upodate the starfish parameters
    public boolean updatestarfish(double dt,
                                  double globalW,
                                  double globalH,
                                  double playerW,
                                  double playerH) {
        updateTimeVisible(dt);
        double delayTime = 5;

        double[] environmentBoundary = new double[4];
        Random nr = new Random();
        environmentBoundary[0] = getPlayAreaCOMX() + getWindowToGlobalCOMOffsetX(); // Left edge
        environmentBoundary[1] = getPlayAreaCOMX() + getWindowToGlobalCOMOffsetX() + globalW; // Right edge
        environmentBoundary[2] = getPlayAreaCOMY() + getWindowToGlobalCOMOffsetY(); // Top edge
        environmentBoundary[3] = getPlayAreaCOMY() + getWindowToGlobalCOMOffsetY() + globalH; // Bottom edge

        if(!getIsVisible() && getTimeVisible() >= delayTime) {
            setSavedRandomPos(nr.nextDouble((environmentBoundary[1] - environmentBoundary[0])), nr.nextDouble((environmentBoundary[3] - environmentBoundary[2])));

            randomSpeed();
            setPos(getSavedRandomPosX(), getSavedRandomPosY());

            setVisible(true);
            resetTimeVisible();
        } else { moveItem(dt, globalW, globalH); }

        // Create new collider for the player based on player parameters
        double px = (-getWindowToGlobalCOMOffsetX()) - (playerW / 2.0);
        double py = (-getWindowToGlobalCOMOffsetY()) - (playerH / 2.0);
        double stx = getXPos();
        double sty = getYPos();

        //check collision with fish
        Rectangle playerCollider = new Rectangle((int)px, (int)py, (int)playerW, (int)playerH);
        Rectangle starfishRect = new Rectangle((int)stx, (int)sty, (int)getWidth(), (int)getHeight());
        if (getIsVisible() && playerCollider.intersects(new Rectangle(starfishRect))) {
            setVisible(false);
            setTimesEaten(getTimesEaten() + 1);
            return true;
        }
        return false;
    }
}
