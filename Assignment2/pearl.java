package Assignment2;

import java.awt.*;

public class pearl {
    double pearlposition_x,pearlposition_y;
    int pearl_w,pearl_h;
    boolean is_visible;
    double time_visible;

    public pearl(){
        pearl_w=30;
        pearl_h=30;
        is_visible=false;
        time_visible=0;
    }
    public void updatepearl(double dt,Rectangle myfishrec,double fishspeed_x,double fishspeed_y,double randx,double randy){
        updatetimevis(dt);

        if(!isvisible()&&gettimevis()>=10){
            setposition(randx,randy);
            setvisible(true);
            resettimevis();
        }
        //check collision with fish
        if (isvisible() && myfishrec.intersects(new Rectangle(new Rectangle((int) getpositionx(), (int) getpositiony(), getwidth(), getheight()))))
        {
            fishspeed_x*=1.5;//increase speed
            fishspeed_y*=1.5;
            setvisible(false);
        }
    }

    public void setposition(double x,double y){
        pearlposition_x=x;
        pearlposition_y=y;
    }
    public double getpositionx(){
        return pearlposition_x;
    }
    public double getpositiony(){
        return pearlposition_y;
    }
    public void setvisible(boolean visible) {
        is_visible = visible;
    }
    public boolean isvisible() {
        return is_visible;
    }
    public int getwidth(){
        return pearl_w;
    }
    public int getheight(){
        return pearl_h;
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