/*
 * Author: Lucass (Xidi Kuang)
 * ID: 21008041
 *
 * Co-Author: Robert Tubman (Minor tweaking to merge with team code)
 * ID: 11115713
 *
 * The bomb class
 * This class handles the updating of the bomb item. The parent class is the Item
 * class which handles the setting/getting and some processing
 *
 * Many of the getter/setter methods were refactored into Item Class because there were duplicate
 * methods in starfish.java, pearl.java, and boom.java
 */

package Assignment2;

import java.awt.*;
import java.util.Random;

public class boom extends Item {

    // Constructor
    public boom(GameEngine engine, Image bombImage) { super(engine, bombImage); }

    // Update the bomb with this function
    // Handles the spawning, movement, and its state
    public boolean updateboom(double dt,
                              double globalW,
                              double globalH,
                              double playerW,
                              double playerH) {
        // Update the visibility timer
        updateTimeVisible(dt);
        double delayTime = 5;

        // Retrieve the game boundaries
        double[] environmentBoundary = new double[4];
        Random nr = new Random();
        environmentBoundary[0] = getPlayAreaCOMX() + getWindowToGlobalCOMOffsetX(); // Left edge
        environmentBoundary[1] = getPlayAreaCOMX() + getWindowToGlobalCOMOffsetX() + globalW; // Right edge
        environmentBoundary[2] = getPlayAreaCOMY() + getWindowToGlobalCOMOffsetY(); // Top edge
        environmentBoundary[3] = getPlayAreaCOMY() + getWindowToGlobalCOMOffsetY() + globalH; // Bottom edge

        // Check whether the bomb should be processed
        if(!getIsVisible() && getTimeVisible() >= delayTime) {
            // Generate and save the random spawn location
            setSavedRandomPos(nr.nextDouble((environmentBoundary[1] - environmentBoundary[0])), nr.nextDouble((environmentBoundary[3] - environmentBoundary[2])));
            // Randomize the speed of the bomb
            randomSpeed();
            // Set the position of the bomb to spawn
            setPos(getSavedRandomPosX(), getSavedRandomPosY());
            // Set the bomb to visible
            setVisible(true);
            // Reset the visibility timer for when it can next be re-seen
            resetTimeVisible();

        } else {
            // Update the movement of the bomb
            moveItem(dt, globalW, globalH);
        }

        // Define the collider parameters for the player based on player parameters
        double px = (-getWindowToGlobalCOMOffsetX()) - (playerW / 2.0);
        double py = (-getWindowToGlobalCOMOffsetY()) - (playerH / 2.0);
        // Define the collider parameters for the bomb
        double bmx = getXPos();
        double bmy = getYPos();

        // Create the rectangles to check for a collision
        Rectangle playerCollider = new Rectangle((int)px, (int)py, (int)playerW, (int)playerH);
        Rectangle starfishRect = new Rectangle((int)bmx, (int)bmy, (int)getWidth(), (int)getHeight());

        // Check for a collision with the player
        if (getIsVisible() && playerCollider.intersects(new Rectangle(starfishRect))) {
            // There was a collision, set the bomb to not be visible
            setVisible(false);
            // Return true for further collision with bomb handling
            return true;
        }
        // There was no collision with the bomb
        return false;
    }
}
