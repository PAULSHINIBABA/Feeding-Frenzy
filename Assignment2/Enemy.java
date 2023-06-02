/*
 * Author: Robert Tubman
 * ID: 11115713
 *
 * The Enemy Class
 *
 * Use this class to instantiate enemy "Fish" in the game.
 * Some fields are required on instantiation, but the rest can be set using
 * the Setters.
 */
package Assignment2;

import java.awt.*;
import java.util.Random;

// The enemy class
public class Enemy {

    // Final fields
    private final int MIN_SIZE = 0;
    private final int MAX_SIZE = 3;
    private final double EDGE_OFFSET = 1.0;
    private double colliderDefaultBodyLength = 64.0; // This needs to match the image dimension x
    private double colliderDefaultBodyHeight = 32.0; // This needs to match the image dimension y
    private final double SIZE_0 = 1.0;
    private final double SIZE_1 = 1.5;
    private final double SIZE_2 = 2.0;
    private final double SIZE_3 = 2.5;
    private final double SIZE_4 = 3.0;
    private final double SIZE_UNDEFINED = 0.5;
    private double defaultVelocity;

    // Variable fields
    private int size;
    private double xPos;
    private double yPos;
    private double velocity;
    private double velocityRange;
    private double headingXDirection;
    private double headingYDirection;
    private Image image;
    private final Random RANDOM_VALUE;
    private int leaveEnvironmentChance;

    // Use the head collider below when "eating" the player
    private double colliderHeadXOffset;
    private double colliderHeadYOffset;

    // Use the body collider below when being "eaten" by the player
    private double colliderBodyXOffset;
    private double colliderBodyYOffset;
    private double colliderBodyLength;
    private double colliderBodyHeight;

    // The image projection
    private double imageBodyXOffset;
    private double imageBodyYOffset;
    private double imageBodyLength;
    private double imageBodyHeight;


    // The Enemy class constructor
    // The Enemy requires:
    // - The enemy image
    // - The enemy size
    // - The frameWidth
    // - The frameHeight
    public Enemy(Image image, int size, double frameXOffset, double frameYOffset, double frameWidth, double frameHeight) throws IllegalArgumentException {
        this(image, size, 0.0, 0.0, 0.0, 0.0, 0.0, frameXOffset, frameYOffset, frameWidth, frameHeight);
    }
    public Enemy(Image image,
                 int size,
                 double velocity,
                 double xPos,
                 double yPos,
                 double xHeading,
                 double yHeading,
                 double frameXOffset,
                 double frameYOffset,
                 double frameWidth,
                 double frameHeight) throws IllegalArgumentException {
        // Initialize the randomizing variable
        RANDOM_VALUE = new Random();

        // Set the enemy image
        if (image == null) { throw new IllegalArgumentException("The enemy image cannot be null"); }
        this.image = image;

        // Set the enemy size
        if ((size < MIN_SIZE) || (size > MAX_SIZE)) { throw new IllegalArgumentException("The enemy size must be greater than " + MIN_SIZE + " and less than " + MAX_SIZE); }
        setSize(size);

        // Set the enemy velocity
        velocityRange = 50.0;
        defaultVelocity = 25.0;
        if (velocity == 0.0) { setRandomVelocity(defaultVelocity, velocityRange); }
        else { this.velocity = velocity; }

        // Set the enemy xPos and yPos
        // Check whether to randomly assign a position or not
        boolean randomPos = ((xPos == 0.0) && (yPos == 0.0));
        if (randomPos) {
            setRandomXPos(frameWidth, frameXOffset);
            setRandomYPos(frameHeight, frameYOffset);
        } else {
            // Assigning position based on input
            this.xPos = xPos;
            this.yPos = yPos;
        }

        // Set the enemy heading
        if (headingXDirection == 0.0) {
            setSidedXHeading(frameWidth);
            setRandomYHeading();
        } else {
            // A heading was provided, assign based on what was provided
            headingXDirection = xHeading;
            headingYDirection = yHeading;
        }

        // Set the head collider offset
        setHeadOffset((colliderBodyLength / 2.0), (colliderBodyHeight / 2.0), (colliderBodyHeight / 2.0));

        // Set chance to leave the environment
        leaveEnvironmentChance = 33; // The default value
    }

    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void setImage(Image image) throws IllegalArgumentException {
        if (image == null) { throw new IllegalArgumentException("The enemy image cannot be null"); }
        this.image = image;
    }
    public void setSize(int size) throws IllegalArgumentException {
        if ((size < MIN_SIZE) || (size > MAX_SIZE)) { throw new IllegalArgumentException("The enemy size must be greater than " + MIN_SIZE + " and less than " + MAX_SIZE); }
        this.size = size;

        // Define the enemy colliders based on the size
        switch (this.size) {
            case 0 -> setColliderOffsets(SIZE_0);
            case 1 -> setColliderOffsets(SIZE_1);
            case 2 -> setColliderOffsets(SIZE_2);
            case 3 -> setColliderOffsets(SIZE_4); // Set this to the shark
            default -> {
                setColliderOffsets(SIZE_UNDEFINED);
                System.out.println("Enemy.SetSize(); The size is outside of defined specifications. Please add an actual switch case for the size.");
            }
        }

        // Set up the image offset
        setImageOffsets(colliderBodyXOffset, colliderBodyYOffset, colliderBodyLength, colliderBodyHeight);
    }
    public void setXPos(double xPos) { this.xPos = xPos; }
    public void setYPos(double yPos) { this.yPos = yPos; }
    public void setRandomXPos(double frameWidth, double frameXOffset) {
        // Randomly assigning a position
        boolean enterLeft = RANDOM_VALUE.nextBoolean();
        if (enterLeft) { setXPos( frameXOffset - (colliderBodyLength + EDGE_OFFSET) ); }
        else { setXPos( frameXOffset + frameWidth + colliderBodyLength + EDGE_OFFSET ); }
    }
    public void setRandomYPos(double frameHeight, double frameYOffset) {
        setYPos(frameYOffset + EDGE_OFFSET + colliderBodyYOffset + RANDOM_VALUE.nextDouble(frameHeight - (2 * (EDGE_OFFSET + colliderBodyYOffset))));
    }
    public void setPos(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }
    public void setVelocity(double velocity) throws IllegalArgumentException {
        if (velocity < 0.0) { throw new IllegalArgumentException("The velocity cannot be less than 0.0"); }
        this.velocity = velocity;
    }
    public void setRandomVelocity(double value, double range) throws IllegalArgumentException {
        if (value < 0.0) { throw new IllegalArgumentException("The velocity cannot be less than 0.0"); }
        if (range < 0.0) { throw new IllegalArgumentException("The velocity range cannot be less than 0.0"); }
        velocity = value + RANDOM_VALUE.nextDouble(range);
    }
    public void setVelocityRange(double velocityRange) throws IllegalArgumentException {
        if (velocityRange < 0.0) { throw new IllegalArgumentException("The velocity range cannot be less than 0.0"); }
        this.velocityRange = velocityRange;
    }
    public void setXHeading(double xHeading) {
        headingXDirection = xHeading;
    }
    public void setYHeading(double yHeading) {
        headingYDirection = yHeading;
    }
    public void setSidedXHeading(double frameWidth) {
        // If the heading is undefined, then choose based on spawn edge
        if (xPos < (frameWidth / 2.0)) { setXHeading(1.0); } // Head right
        else { setXHeading(-1.0); } // Head left
    }
    public void setRandomXHeading() {
        if (RANDOM_VALUE.nextBoolean()) { setXHeading(1.0); } // Head right
        else { setXHeading(-1.0); } // Head left
    }
    public void setRandomYHeading() {
        double value = RANDOM_VALUE.nextDouble(2.0) - 1.0;
        setYHeading(value);
    }
    // After enemy creation, use this in combination with setSize() to change the
    // enemy dimensions (image and collision size.)
    public void setDefaultColliderBodyDimensions(int w, int h) {
        colliderDefaultBodyLength = w;
        colliderDefaultBodyHeight = h;
    }
    public void setColliderOffsets(double scale) {
        colliderBodyLength = colliderDefaultBodyLength * scale;
        colliderBodyHeight = colliderDefaultBodyHeight * scale;
        colliderBodyXOffset = colliderBodyLength / 2;
        colliderBodyYOffset = colliderBodyHeight / 2;
    }

    // This will set the offset of the image from the object origin
    // The x,y value provided should be negative to have the image center in the object center.
    // The imageBodyXOffset is the left side of the image.
    // The imageBodyYOffset is the top side of the image.
    public void setImageBodyXOffset(double x) { imageBodyXOffset = x; }
    public void setImageBodyYOffset(double y) { imageBodyYOffset = y; }
    public void setImageBodyLength(double l) { imageBodyLength = l; }
    public void setImageBodyHeight(double h) { imageBodyHeight = h; }

    // Set the image offsets based on the collider sizes.
    // If the offset is wrong, set them individually using:
    // - imageBodyXOffset
    // - imageBodyYOffset
    // - imageBodyLength
    // - imageBodyHeight
    // or to set them all at once use:
    // - setImageOffsets(double x, double y, double l, double h)
    public void setImageOffsets(double x, double y, double l, double h) {
        imageBodyXOffset = x;
        imageBodyYOffset = y;
        imageBodyLength = l;
        imageBodyHeight = h;
    }

    public void setChanceToLeaveEnvironment(int chance) throws IllegalArgumentException {
        if ((chance < 0) || (chance > 100)) { throw new IllegalArgumentException("The chance cannot be less than 0 or greater than 100;"); }
        leaveEnvironmentChance = chance;
    }

    public void setHeadXOffset(double x) {
        colliderHeadXOffset = x;
    }
    public void setHeadYOffset(double y) { colliderHeadYOffset = y; }
    public void setHeadOffset(double x, double y, double r) {
        setHeadXOffset(x);
        setHeadYOffset(y);
    }


    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public double getXPos() { return xPos; }
    public double getYPos() { return yPos; }
    public Image getImage() { return image; }
    public int getSize() { return size; }
    public double getVelocity() { return velocity; }
    public double getHeadingX() { return headingXDirection; }
    public double getHeadingY() { return headingYDirection; }
    public double getLength() { return colliderBodyLength; }
    public double getHeight() { return colliderBodyHeight; }
    public int getChanceToLeaveEnvironment() { return leaveEnvironmentChance; }
    public double getColliderBodyXOffset() { return colliderBodyXOffset; }
    public double getColliderBodyYOffset() { return colliderBodyYOffset; }
    public double getColliderBodyLength() { return colliderBodyLength; }
    public double getColliderBodyHeight() { return colliderBodyHeight; }
    public double getColliderHeadXOffset() { return colliderHeadXOffset; }
    public double getColliderHeadYOffset() { return colliderHeadYOffset; }
    public double getImageXOffset() { return imageBodyXOffset; }
    public double getImageYOffset() { return imageBodyYOffset; }
    public double getImageLength() { return imageBodyLength; }
    public double getImageHeight() { return imageBodyHeight; }
    public double getDefaultVelocity() { return defaultVelocity; }
    public double getDefaultVelocityRange() { return velocityRange; }

    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------

    // TODO: consider other enemy behaviours can be added once the main parts work.
}
