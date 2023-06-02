/*
 * Author: Lucass
 * ID:
 *
 * Co-Author: Robert Tubman (Minor tweaking to merge with team code)
 * ID: 11115713
 */

package Assignment2;

import java.awt.*;
import java.util.Random;

public class pearl {
    private double pearlposition_x,pearlposition_y;
    private double pearlRandSaveX;
    private double pearlRandSaveY;
    private double diffXPos;
    private double diffYPos;
    private int pearl_w,pearl_h;
    private boolean is_visible;
    private double time_visible;
    private int timesEaten;
    private double windowToGlobalCOMOffsetX;
    private double windowToGlobalCOMOffsetY;
    private double playAreaCOMX;
    private double playAreaCOMY;
    private GameEngine engine;
    private Image pearlImage;

    public pearl(GameEngine engine, Image pearlImage){
        this.engine = engine;
        this.pearlImage = pearlImage;
        pearl_w = 30;
        pearl_h = 30;
        is_visible = false;
        time_visible = 0;
        timesEaten = 0;
        windowToGlobalCOMOffsetX = 0.0;
        windowToGlobalCOMOffsetY = 0.0;
        playAreaCOMX = 0.0;
        playAreaCOMY = 0.0;
    }
    public boolean updatepearl(double dt,
                               double globalOriginX,
                               double globalOriginY,
                               double playerX,
                               double playerY,
                               double globalW,
                               double globalH,
                               double playerW,
                               double playerH) {
        updatetimevis(dt);
        double delayTime = 2;

        double[] environmentBoundary = new double[4];
        Random nr = new Random();
        environmentBoundary[0] = playAreaCOMX + windowToGlobalCOMOffsetX; // Left edge
        environmentBoundary[1] = playAreaCOMX + windowToGlobalCOMOffsetX + globalW; // Right edge
        environmentBoundary[2] = playAreaCOMY + windowToGlobalCOMOffsetY; // Top edge
        environmentBoundary[3] = playAreaCOMY + windowToGlobalCOMOffsetY + globalH; // Bottom edge

        diffXPos = globalOriginX - playerX;
        diffYPos = globalOriginY - playerY;
        if(!isvisible() && gettimevis() >= delayTime) {
            pearlRandSaveX = nr.nextDouble((environmentBoundary[1] - environmentBoundary[0]));
            pearlRandSaveY = nr.nextDouble((environmentBoundary[3] - environmentBoundary[2]));
            setposition(pearlRandSaveX, pearlRandSaveY);
            setvisible(true);
            resettimevis();
        } else { setposition(pearlRandSaveX, pearlRandSaveY); }

        // Create new collider for the player based on player parameters
        double px = (-windowToGlobalCOMOffsetX) - (playerW / 2.0);
        double py = (-windowToGlobalCOMOffsetY) - (playerH / 2.0);
        double pex = pearlRandSaveX;
        double pey = pearlRandSaveY;

        //check collision with fish
        Rectangle playerCollider = new Rectangle((int)px, (int)py, (int)playerW, (int)playerH);
        Rectangle pearlRect = new Rectangle((int)pex, (int)pey, getwidth(), getheight());
        if (isvisible() && playerCollider.intersects(new Rectangle(pearlRect))) {
            setvisible(false);
            timesEaten += 1;
            return true;
        }
        return false;
    }

    //-------------------------------------------------------
    // Setters
    //-------------------------------------------------------
    public void setposition(double x,double y){
        pearlposition_x = x;
        pearlposition_y = y;
    }
    public void setvisible(boolean visible) {
        is_visible = visible;
    }
    public void setTimesEaten(int timesEaten) { this.timesEaten = timesEaten; }
    public void setPlayAreaCOM(double x, double y) {
        playAreaCOMX = x;
        playAreaCOMY = y;
    }

    //-------------------------------------------------------
    // Getters
    //-------------------------------------------------------
    public double getpositionx(){
        return pearlposition_x;
    }
    public double getpositiony(){
        return pearlposition_y;
    }
    public int getwidth(){
        return pearl_w;
    }
    public int getheight(){
        return pearl_h;
    }
    public double gettimevis(){
        return time_visible;
    }
    public int getTimesEaten() { return timesEaten; }

    //-------------------------------------------------------
    // Other methods
    //-------------------------------------------------------
    public boolean isvisible() {
        return is_visible;
    }
    public void updatetimevis(double dt){
        time_visible+=dt;
    }
    public void resettimevis(){
        time_visible=0;
    }
    public boolean checkPosition() {
        return true;
    }
    public void windowToGlobalCOMOffsetX(double x, double y) {
        windowToGlobalCOMOffsetX = -x;
        windowToGlobalCOMOffsetY = -y;
    }
    public double[] updateGlobalOffset(double globalWidth, double globalHeight) {
        double[] environmentBoundary = new double[4];
        // Left edge
        environmentBoundary[0] = playAreaCOMX + windowToGlobalCOMOffsetX;
        // Right edge
        environmentBoundary[1] = playAreaCOMX + windowToGlobalCOMOffsetX + globalWidth;
        // Top edge
        environmentBoundary[2] = playAreaCOMY + windowToGlobalCOMOffsetY;
        // Bottom edge
        environmentBoundary[3] = playAreaCOMY + windowToGlobalCOMOffsetY + globalHeight;

//        double size = 10;
//        engine.changeColor(255,0,0);
//        engine.drawCircle(environmentBoundary[0], environmentBoundary[2], size);
//        engine.drawCircle(environmentBoundary[0], environmentBoundary[3], size);
//        engine.drawCircle(environmentBoundary[1], environmentBoundary[2], size);
//        engine.drawCircle(environmentBoundary[1], environmentBoundary[3], size);

//        System.out.println("x1:" + environmentBoundary[0] +
//                "\tx2:" + environmentBoundary[1] +
//                "\ty1:" + environmentBoundary[2] +
//                "\ty2:" + environmentBoundary[3]);

        return environmentBoundary;
    }
    public double[] randomSpawnPos(double[] envBounds) {
//        // Top left corner or global
        Random randP = new Random();
        double xDiff = envBounds[1] - envBounds[0];
        double yDiff = envBounds[3] - envBounds[2];
        double[] randSpawn = new double[2];

        randSpawn[0] = randP.nextDouble((xDiff / 2.0)) + envBounds[0];
        randSpawn[1] = randP.nextDouble((yDiff / 2.0)) + envBounds[2];
        return randSpawn;
    }

//    private double newLocX = 0.0;
//    private double newLocY = 0.0;
//    private boolean fake = true;
//public void drawCornersFromPearl(double w, double h) {
    public void drawPearl() {
        double[] environmentBoundary = new double[2];
        // Left edge
        environmentBoundary[0] = playAreaCOMX + windowToGlobalCOMOffsetX;
        // Top edge
        environmentBoundary[1] = playAreaCOMY + windowToGlobalCOMOffsetY;

        engine.drawImage(pearlImage,
                environmentBoundary[0] + pearlposition_x,
                environmentBoundary[1] + pearlposition_y,
                pearl_w,
                pearl_h);
    }

    public void drawPearlColliders() {
        double[] environmentBoundary = new double[2];
        // Left edge
        environmentBoundary[0] = playAreaCOMX + windowToGlobalCOMOffsetX;
        // Top edge
        environmentBoundary[1] = playAreaCOMY + windowToGlobalCOMOffsetY;
        double x1 = environmentBoundary[0] + pearlposition_x;
        double x2 = environmentBoundary[0] + pearlposition_x + pearl_w;
        double y1 = environmentBoundary[1] + pearlposition_y;
        double y2 = environmentBoundary[1] + pearlposition_y + pearl_h;

        engine.changeColor(255,0,0);
        engine.drawLine(x1, y1, x2, y1);
        engine.drawLine(x1, y2, x2, y2);
        engine.drawLine(x1, y1, x1, y2);
        engine.drawLine(x2, y1, x2, y2);

//        // TODO: remove this, it is used only for testing
//        drawGameBoundaryColliderTest();
    }

}