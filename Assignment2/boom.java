/*
 * Author: Lucass
 * ID:
 *
 * Co-Author: Robert Tubman (Minor tweaking to merge with team code)
 * ID: 11115713
 */

package Assignment2;

import java.awt.*;

public class boom {
    private double boompos_x,boompos_y;
    private double boomspeed_x,boomspeed_y;
    private int boom_w,boom_h;
    private boolean is_visible;
    private double time_visible;
    public boom(){
        boom_w=30;
        boom_h=30;
        is_visible=false;
        time_visible=0;
    }
    public void randomboom(double randx, double randy,double randspeed){
        setposition(randx,randy);
        boomspeed_x=-50+randspeed;
        boomspeed_y=-50+randspeed;
    }
    public void boommove(double dt,double Window_w,double Window_h, double offsetX, double offsetY){
        boompos_x+=boomspeed_x*dt;
        boompos_y+=boomspeed_y*dt;
        if (boompos_x<offsetX || boompos_x + boom_w > (offsetX + Window_w)){
            boomspeed_x*=-1; // reverse direction
        }
        if (boompos_y<offsetY || boompos_y + boom_h > (offsetY + Window_h)){
            boomspeed_y*=-1; // reverse direction
        }
    }
//    public boolean updateboom(double dt, Rectangle myfishrec, boolean gamestart, double randx, double randy, double randspeed, double Window_w, double Window_h){
    public boolean updateboom(double dt,
                              Rectangle myfishrec,
                              double randx,
                              double randy,
                              double offsetX,
                              double offsetY,
                              double randspeed,
                              double Window_w,
                              double Window_h){
        updatetimevis(dt);
        if (!isvisible()&&gettimevis()>5){
            randomboom((offsetX + randx),(offsetY + randy),randspeed);
            setvisible(true);
            resettimevis();
        }
        boommove(dt,Window_w,Window_h, offsetX, offsetY);

        if (isvisible()&&myfishrec.intersects(new Rectangle(new Rectangle((int)getpositionx(),(int)getpositiony(),getwidth(),getheight())))){
            setvisible(false);
            return true;
        }
        return false;
    }
    public void setposition(double x,double y){
        boompos_x=x;
        boompos_y=y;
    }
    public double getpositionx(){
        return boompos_x;
    }
    public double getpositiony(){
        return boompos_y;
    }
    public void setvisible(boolean visible) {
        is_visible = visible;
    }
    public boolean isvisible() {
        return is_visible;
    }
    public int getwidth(){
        return boom_w;
    }
    public int getheight(){
        return boom_h;
    }
    public void updatetimevis(double dt){
        time_visible+=dt;
    }
    public void resettimevis(){
        time_visible=0;
    }
    public double gettimevis(){
        return time_visible;
    }
}
