/*
 * Author: Lucass
 * ID:
 *
 * Co-Author: Robert Tubman (Minor tweaking to merge with team code)
 * ID: 11115713
 */

package Assignment2;

import java.awt.*;

public class pearl {
    private double pearlposition_x,pearlposition_y;
    private int pearl_w,pearl_h;
    private boolean is_visible;
    private double time_visible;
    private int timesEaten;

    public pearl(){
        pearl_w = 30;
        pearl_h = 30;
        is_visible = false;
        time_visible = 0;
        timesEaten = 0;
    }
    public boolean updatepearl(double dt,
                            Rectangle myfishrec,
                            double fishspeed_x,
                            double fishspeed_y,
                            double randx,
                            double randy,
                            double offsetW,
                            double offsetH){
        updatetimevis(dt);

        if(!isvisible()&&gettimevis()>=10){
            setposition((offsetW + randx),(offsetH + randy));
            setvisible(true);
            resettimevis();
        }
        //check collision with fish
        if (isvisible() && myfishrec.intersects(new Rectangle(new Rectangle((int) getpositionx(), (int) getpositiony(), getwidth(), getheight()))))
        {
            // Both of these being set was redundant
//            fishspeed_x *= 1.5; // increase speed
//            fishspeed_y *= 1.5;
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
        pearlposition_x=x;
        pearlposition_y=y;
    }
    public void setvisible(boolean visible) {
        is_visible = visible;
    }
    public void setTimesEaten(int timesEaten) { this.timesEaten = timesEaten; }

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
}