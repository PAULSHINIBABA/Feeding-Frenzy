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
        this.SetSize(size);

        // Set the enemy velocity
        this.velocityRange = 50.0;
        if (velocity == 0.0) { this.SetRandomVelocity(this.DEFAULT_VELOCITY, velocityRange); }
        else { this.velocity = velocity; }

        // Set the enemy xPos and yPos
        // Check whether to randomly assign a position or not
        boolean randomPos = ((xPos == 0.0) && (yPos == 0.0));
        if (randomPos) {
            this.SetRandomXPos(frameWidth, frameXOffset);
            this.SetRandomYPos(frameHeight, frameYOffset);
        } else {
            // Assigning position based on input
            this.xPos = xPos;
            this.yPos = yPos;
        }

        // Set the enemy heading
        if (headingXDirection == 0.0) {
            this.SetSidedXHeading(frameWidth);
            this.SetRandomYHeading();
        } else {
            // A heading was provided, assign based on what was provided
            this.headingXDirection = xHeading;
            this.headingYDirection = yHeading;
        }

        // Set the head collider offset
        this.SetHeadOffset();

        // Set chance to leave the environment
        this.leaveEnvironmentChance = 33; // The default value
    }

    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void SetImage(Image image) throws IllegalArgumentException {
        if (image == null) { throw new IllegalArgumentException("The enemy image cannot be null"); }
        this.image = image;
    }

    public void SetSize(int size) throws IllegalArgumentException {
        if ((size < this.MIN_SIZE) || (size > this.MAX_SIZE)) { throw new IllegalArgumentException("The enemy size must be greater than " + this.MIN_SIZE + " and less than " + this.MAX_SIZE); }
        this.size = size;

        // Define the enemy colliders based on the size
        switch (this.size) {
            case 0:
                this.SetColliderOffsets(1.0);
                break;
            case 1:
                this.SetColliderOffsets(this.SIZE_1);
                break;
            case 2:
                this.SetColliderOffsets(this.SIZE_2);
                break;
            case 3:
                this.SetColliderOffsets(this.SIZE_3);
                break;
            default:
                this.SetColliderOffsets(this.SIZE_UNDEFINED);
                System.out.println("Enemy.SetSize(); The size is outside of defined specifications. Please add an actual switch case for the size.");
                break;
        }

        // Set up the image offset
        this.SetImageOffsets();
    }

    public void SetXPos(double xPos) { this.xPos = xPos; }
    public void SetYPos(double yPos) { this.yPos = yPos; }

    public void SetRandomXPos(int frameWidth, double frameXOffset) {
        // Randomly assigning a position
        boolean enterLeft = this.RANDOM_VALUE.nextBoolean();
        if (enterLeft) { this.SetXPos( frameXOffset - (this.colliderBodyLength + this.EDGE_OFFSET) ); }
        else { this.SetXPos( frameXOffset + frameWidth + this.EDGE_OFFSET + this.colliderBodyLength ); }
    }

    public void SetRandomYPos(int frameHeight, double frameYOffset) {
        this.SetYPos(frameYOffset + this.EDGE_OFFSET + this.RANDOM_VALUE.nextDouble(frameHeight - (2 * this.EDGE_OFFSET)));
    }

    public void SetPos(double xPos, double yPos) {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void SetVelocity(double velocity) throws IllegalArgumentException {
        if (velocity < 0.0) { throw new IllegalArgumentException("The velocity cannot be less than 0.0"); }
        this.velocity = velocity;
    }

    public void SetRandomVelocity(double value, double range) throws IllegalArgumentException {
        if (value < 0.0) { throw new IllegalArgumentException("The velocity cannot be less than 0.0"); }
        if (range < 0.0) { throw new IllegalArgumentException("The velocity range cannot be less than 0.0"); }
        this.velocity = value + this.RANDOM_VALUE.nextDouble(range);
    }

    public void SetVelocityRange(double velocityRange) throws IllegalArgumentException {
        if (velocityRange < 0.0) { throw new IllegalArgumentException("The velocity range cannot be less than 0.0"); }
        this.velocityRange = velocityRange;
    }

    public void SetXHeading(double xHeading) {
        this.headingXDirection = xHeading;
    }
    public void SetYHeading(double yHeading) {
        this.headingYDirection = yHeading;
    }
    public void SetSidedXHeading(int frameWidth) {
        // If the heading is undefined, then choose based on spawn edge
        if (this.xPos < (frameWidth / 2)) { this.SetXHeading(1.0); } // Head right
        else { this.SetXHeading(-1.0); } // Head left
    }
    public void SetRandomXHeading() {
        if (this.RANDOM_VALUE.nextBoolean()) { this.SetXHeading(1.0); } // Head right
        else { this.SetXHeading(-1.0); } // Head left
    }
    public void SetRandomYHeading() {
        double value = this.RANDOM_VALUE.nextDouble(2.0) - 1.0;
        this.SetYHeading(value);
    }

    public void SetColliderOffsets(double scale) {
        this.colliderBodyLength = this.COLLIDER_BODY_LENGTH * this.COLLIDER_BODY_X_SCALE * scale;
        this.colliderBodyHeight = this.COLLIDER_BODY_HEIGHT * this.COLLIDER_BODY_Y_SCALE * scale;
        this.colliderBodyXOffset = this.xPos - (this.colliderBodyLength / 2);
        this.colliderBodyYOffset = this.yPos - (this.colliderBodyHeight / 2);
    }

    // Set the image offsets based on the collider sizes.
    // If the offset is wrong, set them individually using:
    // - SetImageBodyXOffset
    // - SetImageBodyYOffset
    // - SetImageBodyLength
    // - SetImageBodyHeight
    // or to set them all at once use:
    // - SetImageOffsets(double x, double y, double l, double h)
    public void SetImageOffsets() {
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
    public void SetImageBodyXOffset(double x) { this.imageBodyXOffset = this.xPos + x; }
    public void SetImageBodyYOffset(double y) { this.imageBodyYOffset = this.yPos + y; }
    public void SetImageBodyLength(double l) { this.imageBodyLength = l; }
    public void SetImageBodyHeight(double h) { this.imageBodyHeight = h; }

    public void SetImageOffsets(double x, double y, double l, double h) {
        this.imageBodyXOffset = x;
        this.imageBodyYOffset = y;
        this.imageBodyLength = l;
        this.imageBodyHeight = h;
    }

    public void SetChanceToLeaveEnvironment(int chance) throws IllegalArgumentException {
        if ((chance < 0) || (chance > 100)) { throw new IllegalArgumentException("The chance cannot be less than 0 or greater than 100;"); }
        this.leaveEnvironmentChance = chance;
    }

    public void SetHeadXOffset(double x) {
        this.colliderHeadXOffset = x;
    }
    public void SetHeadYOffset(double y) {
        this.colliderHeadYOffset = y;
    }
    public void SetHeadRadius(double r) {
        this.colliderHeadRadius = r;
    }
    public void SetHeadOffset() {
        if (this.headingXDirection == -1.0) {
            this.SetHeadOffset(this.colliderBodyXOffset, 0, this.colliderBodyHeight/2);
        } else {
            this.SetHeadOffset((this.colliderBodyXOffset + this.colliderBodyLength), 0, this.colliderBodyHeight/2);
        }
    }
    public void SetHeadOffset(double x, double y, double r) {
        this.SetHeadXOffset(x);
        this.SetHeadYOffset(y);
        this.SetHeadRadius(r);
    }

    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public double GetXPos() { return this.xPos; }
    public double GetYPos() { return this.yPos; }
    public Image GetImage() { return this.image; }
    public int GetSize() { return this.size; }
    public double GetVelocity() { return this.velocity; }
    public double GetHeadingX() { return this.headingXDirection; }
    public double GetHeadingY() { return this.headingYDirection; }
    public double GetLength() { return this.colliderBodyLength; }
    public double GetHeight() { return this.colliderBodyHeight; }
    public int GetChanceToLeaveEnvironment() { return this.leaveEnvironmentChance; }
    public double GetColliderBodyXOffset() { return this.colliderBodyXOffset; }
    public double GetColliderBodyYOffset() { return this.colliderBodyYOffset; }
    public double GetColliderBodyLength() { return this.colliderBodyLength; }
    public double GetColliderBodyHeight() { return this.colliderBodyHeight; }
    public double GetColliderHeadXOffset() { return this.colliderHeadXOffset; }
    public double GetColliderHeadYOffset() { return this.colliderHeadYOffset; }
    public double GetColliderHeadRadius() { return this.colliderHeadRadius; }
    public double GetImageXOffset() { return this.xPos + this.imageBodyXOffset; }
    public double GetImageXOffsetNegative() { return this.xPos - this.imageBodyXOffset; }
    public double GetImageYOffset() { return this.yPos + this.imageBodyYOffset; }
    public double GetImageYOffsetNegative() { return this.yPos - this.imageBodyYOffset; }
    public double GetImageLength() { return this.imageBodyLength; }
    public double GetImageHeight() { return this.imageBodyHeight; }

    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
    public void UpdateColliderHeadXOffset() {
        if (this.headingXDirection == -1.0) { this.SetHeadXOffset(this.colliderBodyXOffset); }
        else { this.SetHeadXOffset((this.colliderBodyXOffset + this.colliderBodyLength)); }
    }

    // TODO: consider other enemy behaviours can be added once the main parts work.
}
