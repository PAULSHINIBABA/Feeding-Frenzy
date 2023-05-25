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
    private final double COLLIDER_BODY_LENGTH = 32.0; // This needs to match the image dimension x
    private final double COLLIDER_BODY_HEIGHT = 32.0; // This needs to match the image dimension y
    private final double COLLIDER_BODY_X_SCALE = 1.0;
    private final double COLLIDER_BODY_Y_SCALE = 0.5;
    private final double SIZE_1 = 1.5;
    private final double SIZE_2 = 2.0;
    private final double SIZE_3 = 2.5;
    private final double SIZE_4 = 3.0;
    private final double SIZE_UNDEFINED = 0.5;
    private final double DEFAULT_VELOCITY = 25.0;

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
    private double colliderHeadRadius;

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
    public Enemy(Image image, int size, double frameXOffset, double frameYOffset, int frameWidth, int frameHeight) throws IllegalArgumentException {
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
                 int frameWidth,
                 int frameHeight) throws IllegalArgumentException {
        // Initialize the randomizing variable
        this.RANDOM_VALUE = new Random();

        // Set the enemy image
        if (image == null) { throw new IllegalArgumentException("The enemy image cannot be null"); }
        this.image = image;

        // Set the enemy size
        if ((size < this.MIN_SIZE) || (size > this.MAX_SIZE)) { throw new IllegalArgumentException("The enemy size must be greater than " + this.MIN_SIZE + " and less than " + this.MAX_SIZE); }
        this.setSize(size);

        // Set the enemy velocity
        this.velocityRange = 50.0;
        if (velocity == 0.0) { this.setRandomVelocity(this.DEFAULT_VELOCITY, velocityRange); }
        else { this.velocity = velocity; }

        // Set the enemy xPos and yPos
        // Check whether to randomly assign a position or not
        boolean randomPos = ((xPos == 0.0) && (yPos == 0.0));
        if (randomPos) {
            this.setRandomXPos(frameWidth, frameXOffset);
            this.setRandomYPos(frameHeight, frameYOffset);
        } else {
            // Assigning position based on input
            this.xPos = xPos;
            this.yPos = yPos;
        }

        // Set the enemy heading
        if (headingXDirection == 0.0) {
            this.setSidedXHeading(frameWidth);
            this.setRandomYHeading();
        } else {
            // A heading was provided, assign based on what was provided
            this.headingXDirection = xHeading;
            this.headingYDirection = yHeading;
        }

        // Set the head collider offset
        this.setHeadOffset();

        // Set chance to leave the environment
        this.leaveEnvironmentChance = 33; // The default value
    }

    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void setImage(Image image) throws IllegalArgumentException {
        if (image == null) { throw new IllegalArgumentException("The enemy image cannot be null"); }
        this.image = image;
    }

    public void setSize(int size) throws IllegalArgumentException {
        if ((size < this.MIN_SIZE) || (size > this.MAX_SIZE)) { throw new IllegalArgumentException("The enemy size must be greater than " + this.MIN_SIZE + " and less than " + this.MAX_SIZE); }
        this.size = size;

        // Define the enemy colliders based on the size
        switch (this.size) {
            case 0 -> this.setColliderOffsets(1.0);
            case 1 -> this.setColliderOffsets(this.SIZE_1);
            case 2 -> this.setColliderOffsets(this.SIZE_2);
            case 3 -> this.setColliderOffsets(this.SIZE_3);
            default -> {
                this.setColliderOffsets(this.SIZE_UNDEFINED);
                System.out.println("Enemy.SetSize(); The size is outside of defined specifications. Please add an actual switch case for the size.");
            }
        }

        // Set up the image offset
        this.setImageOffsets();
    }

    public void setXPos(double xPos) { this.xPos = xPos; }
    public void setYPos(double yPos) { this.yPos = yPos; }

    public void setRandomXPos(int frameWidth, double frameXOffset) {
        // Randomly assigning a position
        boolean enterLeft = this.RANDOM_VALUE.nextBoolean();
        if (enterLeft) { this.setXPos( frameXOffset - (this.colliderBodyLength + this.EDGE_OFFSET) ); }
        else { this.setXPos( frameXOffset + frameWidth + this.EDGE_OFFSET + this.colliderBodyLength ); }
    }

    public void setRandomYPos(int frameHeight, double frameYOffset) {
        this.setYPos(frameYOffset + this.EDGE_OFFSET + this.RANDOM_VALUE.nextDouble(frameHeight - (2 * this.EDGE_OFFSET)));
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
        this.velocity = value + this.RANDOM_VALUE.nextDouble(range);
    }

    public void setVelocityRange(double velocityRange) throws IllegalArgumentException {
        if (velocityRange < 0.0) { throw new IllegalArgumentException("The velocity range cannot be less than 0.0"); }
        this.velocityRange = velocityRange;
    }

    public void setXHeading(double xHeading) {
        this.headingXDirection = xHeading;
    }
    public void setYHeading(double yHeading) {
        this.headingYDirection = yHeading;
    }
    public void setSidedXHeading(int frameWidth) {
        // If the heading is undefined, then choose based on spawn edge
        if (this.xPos < (frameWidth / 2.0)) { this.setXHeading(1.0); } // Head right
        else { this.setXHeading(-1.0); } // Head left
    }
    public void setRandomXHeading() {
        if (this.RANDOM_VALUE.nextBoolean()) { this.setXHeading(1.0); } // Head right
        else { this.setXHeading(-1.0); } // Head left
    }
    public void setRandomYHeading() {
        double value = this.RANDOM_VALUE.nextDouble(2.0) - 1.0;
        this.setYHeading(value);
    }

    public void setColliderOffsets(double scale) {
        this.colliderBodyLength = this.COLLIDER_BODY_LENGTH * this.COLLIDER_BODY_X_SCALE * scale;
        this.colliderBodyHeight = this.COLLIDER_BODY_HEIGHT * this.COLLIDER_BODY_Y_SCALE * scale;
        this.colliderBodyXOffset = this.xPos - (this.colliderBodyLength / 2);
        this.colliderBodyYOffset = this.yPos - (this.colliderBodyHeight / 2);
    }

    // Set the image offsets based on the collider sizes.
    // If the offset is wrong, set them individually using:
    // - setImageBodyXOffset
    // - setImageBodyYOffset
    // - setImageBodyLength
    // - setImageBodyHeight
    // or to set them all at once use:
    // - setImageOffsets(double x, double y, double l, double h)
    public void setImageOffsets() {
        // Assuming the image and collider box are equal sizes, set the following
        this.imageBodyLength = this.colliderBodyLength;
        this.imageBodyHeight = this.colliderBodyHeight;
        this.imageBodyXOffset = this.colliderBodyXOffset;
        this.imageBodyYOffset = this.colliderBodyYOffset;

    }

    // This will set the offset of the image from the object origin
    // The x,y value provided should be negative to have the image center in the object center.
    // The imageBodyXOffset is the left side of the image.
    // The imageBodyYOffset is the top side of the image.
    public void setImageBodyXOffset(double x) { this.imageBodyXOffset = this.xPos + x; }
    public void setImageBodyYOffset(double y) { this.imageBodyYOffset = this.yPos + y; }
    public void setImageBodyLength(double l) { this.imageBodyLength = l; }
    public void setImageBodyHeight(double h) { this.imageBodyHeight = h; }

    public void setImageOffsets(double x, double y, double l, double h) {
        this.imageBodyXOffset = x;
        this.imageBodyYOffset = y;
        this.imageBodyLength = l;
        this.imageBodyHeight = h;
    }

    public void setChanceToLeaveEnvironment(int chance) throws IllegalArgumentException {
        if ((chance < 0) || (chance > 100)) { throw new IllegalArgumentException("The chance cannot be less than 0 or greater than 100;"); }
        this.leaveEnvironmentChance = chance;
    }

    public void setHeadXOffset(double x) {
        this.colliderHeadXOffset = x;
    }
    public void setHeadYOffset(double y) {
        this.colliderHeadYOffset = y;
    }
    public void setHeadRadius(double r) {
        this.colliderHeadRadius = r;
    }
    public void setHeadOffset() {
        if (this.headingXDirection == -1.0) {
            this.setHeadOffset(this.colliderBodyXOffset, 0, this.colliderBodyHeight/2);
        } else {
            this.setHeadOffset((this.colliderBodyXOffset + this.colliderBodyLength), 0, this.colliderBodyHeight/2);
        }
    }
    public void setHeadOffset(double x, double y, double r) {
        this.setHeadXOffset(x);
        this.setHeadYOffset(y);
        this.setHeadRadius(r);
    }

    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public double getXPos() { return this.xPos; }
    public double getYPos() { return this.yPos; }
    public Image getImage() { return this.image; }
    public int getSize() { return this.size; }
    public double getVelocity() { return this.velocity; }
    public double getHeadingX() { return this.headingXDirection; }
    public double getHeadingY() { return this.headingYDirection; }
    public double getLength() { return this.colliderBodyLength; }
    public double getHeight() { return this.colliderBodyHeight; }
    public int getChanceToLeaveEnvironment() { return this.leaveEnvironmentChance; }
    public double getColliderBodyXOffset() { return this.colliderBodyXOffset; }
    public double getColliderBodyYOffset() { return this.colliderBodyYOffset; }
    public double getColliderBodyLength() { return this.colliderBodyLength; }
    public double getColliderBodyHeight() { return this.colliderBodyHeight; }
    public double getColliderHeadXOffset() { return this.colliderHeadXOffset; }
    public double getColliderHeadYOffset() { return this.colliderHeadYOffset; }
    public double getColliderHeadRadius() { return this.colliderHeadRadius; }
    public double getImageXOffset() { return this.xPos + this.imageBodyXOffset; }
    public double getImageXOffsetNegative() { return this.xPos - this.imageBodyXOffset; }
    public double getImageYOffset() { return this.yPos + this.imageBodyYOffset; }
    public double getImageYOffsetNegative() { return this.yPos - this.imageBodyYOffset; }
    public double getImageLength() { return this.imageBodyLength; }
    public double getImageHeight() { return this.imageBodyHeight; }

    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
    public void updateColliderHeadXOffset() {
        if (this.headingXDirection == -1.0) { this.setHeadXOffset(this.colliderBodyXOffset); }
        else { this.setHeadXOffset((this.colliderBodyXOffset + this.colliderBodyLength)); }
    }

    // TODO: consider other enemy behaviours can be added once the main parts work.
}
