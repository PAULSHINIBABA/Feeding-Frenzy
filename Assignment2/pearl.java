/*
 * Author: Lucass (Xidi Kuang)
 * ID: 21008041
 *
 * Co-Author: Robert Tubman (Minor tweaking to merge with team code)
 * ID: 11115713
 *
 * Team:
 * David, 22004319
 * Lucas (Xidi Kuang), 21008041
 * Paul (Zeju Fan), 21019135
 * Robert Tubman, 11115713
 *
 * The pearl class
 * This class handles the updating of the pearl item. The parent class is the Item
 * class which handles the setting/getting and some processing
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

    // Update the pearl with this function
    // Handles the spawning, movement, and its state
    public boolean updatepearl(double dt,
                               double globalW,
                               double globalH,
                               double playerW,
                               double playerH) {
        // Update the visibility timer
        updateTimeVisible(dt);
        double delayTime = 10;

        // Retrieve the game boundaries
        double[] environmentBoundary = new double[4];
        Random nr = new Random();
        environmentBoundary[0] = getPlayAreaCOMX() + getWindowToGlobalCOMOffsetX(); // Left edge
        environmentBoundary[1] = getPlayAreaCOMX() + getWindowToGlobalCOMOffsetX() + globalW; // Right edge
        environmentBoundary[2] = getPlayAreaCOMY() + getWindowToGlobalCOMOffsetY(); // Top edge
        environmentBoundary[3] = getPlayAreaCOMY() + getWindowToGlobalCOMOffsetY() + globalH; // Bottom edge

        // Check whether the pearl should be processed
        if(!getIsVisible() && getTimeVisible() >= delayTime) {
            // Generate and save the random spawn location
            setSavedRandomPos(nr.nextDouble((environmentBoundary[1] - environmentBoundary[0])), nr.nextDouble((environmentBoundary[3] - environmentBoundary[2])));
            // Set the position of the pearl to spawn
            setPos(getSavedRandomPosX(), getSavedRandomPosY());
            // Set the pearl to visible
            setVisible(true);
            // Reset the visibility timer for when it can next be re-seen
            resetTimeVisible();

        } else {
            // Keep setting the position of the pearl (because the environment is "moving" in camera panning mode)
            setPos(getSavedRandomPosX(), getSavedRandomPosY());
        }

        // Define the collider parameters for the player based on player parameters
        double px = (-getWindowToGlobalCOMOffsetX()) - (playerW / 2.0);
        double py = (-getWindowToGlobalCOMOffsetY()) - (playerH / 2.0);
        // Define the collider parameters for the pearl
        double pex = getSavedRandomPosX();
        double pey = getSavedRandomPosY();

        // Create the rectangles to check for a collision
        Rectangle playerCollider = new Rectangle((int)px, (int)py, (int)playerW, (int)playerH);
        Rectangle pearlRect = new Rectangle((int)pex, (int)pey, (int)getWidth(), (int)getHeight());

        // Check for a collision with the player
        if (getIsVisible() && playerCollider.intersects(new Rectangle(pearlRect))) {
            // There was a collision, set the pearl to not be visible
            setVisible(false);
            // Increment the times the pearl was eaten
            setTimesEaten(getTimesEaten() + 1);
            // Return true for further collision with pearl handling
            return true;
        }
        // There was no collision with the pearl
        return false;
    }
}