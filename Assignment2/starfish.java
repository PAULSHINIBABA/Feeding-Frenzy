package Assignment2;

import java.awt.*;

public class starfish {
    double starfishpos_x,starfishpos_y;
    double starspeed_x,starfishspeed_y;
    int starfish_w,starfish_h;
    boolean is_visible;
    double time_visible;
    public starfish(){
        starfish_w=30;
        starfish_h=30;
        is_visible=false;
        time_visible=0;
    }
    public void randomstarfish(double randx, double randy,double randspeed){
        setposition(randx,randy);
        starspeed_x=-50+randspeed;
        starfishspeed_y=-50+randspeed;
    }
    public void starfishmove(double dt,double Window_w,double Window_h){
        starfishpos_x+=starspeed_x*dt;
        starfishpos_y+=starfishspeed_y*dt;
        if (starfishpos_x<0 || starfishpos_x + starfish_w > Window_w){
            starspeed_x*=-1; // reverse direction
        }
        if (starfishpos_y<0 || starfishpos_y + starfish_h > Window_h){
            starfishspeed_y*=-1; // reverse direction
        }
    }
    public boolean updatestarfish(double dt, Rectangle myfishrec, double score,double randx, double randy,double randspeed,double Window_w,double Window_h){
        updatetimevis(dt);
        if (!isvisible()&&gettimevis()>5){
            randomstarfish(randx,randy,randspeed);
            setvisible(true);
            resettimevis();
        }
        starfishmove(dt,Window_w,Window_h);

        if (isvisible()&&myfishrec.intersects(new Rectangle(new Rectangle((int)getpositionx(),(int)getpositiony(),getwidth(),getheight())))){
            setvisible(false);
            return true;
        }
        return false;
    }
    public void setposition(double x,double y){
        starfishpos_x=x;
        starfishpos_y=y;
    }
    public double getpositionx(){
        return starfishpos_x;
    }
    public double getpositiony(){
        return starfishpos_y;
    }
    public void setvisible(boolean visible) {
        is_visible = visible;
    }
    public boolean isvisible() {
        return is_visible;
    }
    public int getwidth(){
        return starfish_w;
    }
    public int getheight(){
        return starfish_h;
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
