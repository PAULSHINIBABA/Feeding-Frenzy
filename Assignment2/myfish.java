package Assignment2;

import java.awt.*;

public class myfish {
    double mposition_x,mposition_y;//myself fish position
    double myfishspeed_x,myfishspeed_y;
    int myfish_w=90,myfish_h=60;//myfish width and myfish high
    public myfish(){
        mposition_x=250;
        mposition_y=250;
        myfishspeed_y=0;
        myfishspeed_x=0;
    }

    public Rectangle getmyfishRec(){
        return new Rectangle((int) mposition_x, (int) mposition_y,myfish_w,myfish_h);
    }
//    public void updatemyfish(double dt,boolean up, boolean down,boolean left,boolean right, int Window_w,int Window_h){
    public void updatemyfish(double dt,
                             boolean up,
                             boolean down,
                             boolean left,
                             boolean right,
                             double windowX,
                             double windowY,
                             double windowW,
                             double windowH){

        if(up){ myfishspeed_y -= 250 * dt; }
        if (down){ myfishspeed_y += 250 * dt; }
        if (left){ myfishspeed_x -= 250 * dt; }
        if (right){ myfishspeed_x += 250 * dt; }

        mposition_x += myfishspeed_x * dt;
        mposition_y += myfishspeed_y * dt;

        // Edge detection
        if (mposition_x < windowX) {
            mposition_x = windowX;
            myfishspeed_x = 0;
        }
        else if (mposition_x + myfish_w > (windowW + windowX)) {
            mposition_x = (windowW + windowX) - myfish_w;
            myfishspeed_x = 0;
        }

        if (mposition_y < windowY) {
            mposition_y = windowY;
            myfishspeed_y = 0;
        }
        else if (mposition_y + myfish_h > (windowY + windowH)) {
            mposition_y = (windowY + windowH) - myfish_h;
            myfishspeed_y = 0;
        }

    }
}
