package Assignment2;

import java.awt.*;

public class boom {
    double boompos_x,boompos_y;
    double boomspeed_x,boomspeed_y;
    int boom_w,boom_h;
    boolean is_visible;
    double time_visible;
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
    public void boommove(double dt,double Window_w,double Window_h){
        boompos_x+=boomspeed_x*dt;
        boompos_y+=boomspeed_y*dt;
        if (boompos_x<0 || boompos_x + boom_w > Window_w){
            boomspeed_x*=-1; // reverse direction
        }
        if (boompos_y<0 || boompos_y + boom_h > Window_h){
            boomspeed_y*=-1; // reverse direction
        }
    }
//    public boolean updateboom(double dt, Rectangle myfishrec, boolean gamestart, double randx, double randy, double randspeed, double Window_w, double Window_h){
    public boolean updateboom(double dt, Rectangle myfishrec, double randx, double randy, double randspeed, double Window_w, double Window_h){
        updatetimevis(dt);
        if (!isvisible()&&gettimevis()>5){
            randomboom(randx,randy,randspeed);
            setvisible(true);
            resettimevis();
        }
        boommove(dt,Window_w,Window_h);

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
