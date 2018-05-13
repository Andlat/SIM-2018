package daynight.daynnight.engine.util;

/**
 * Created by Nikola Zelovic on 2018-04-02.
 */

public class Coord {
    public float X=0, Y=0;

    public Coord(){}
    public Coord(float x, float y){
        this.X = x;
        this.Y = y;
    }
    public Coord(Coord copy){
        this.X = copy.X;
        this.Y = copy.Y;
    }

    public float distanceTo(Coord target){
        return distanceTo(this.X, this.Y, target.X, target.Y);
    }

    public static float distanceTo(float x1, float y1, float x2, float y2){
        return (float)Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public void setX(float x){
        this.X=x;
    }
    public void setY(float y){
        this.Y=y;
    }

    public float getX(){
        return this.X;
    }
    public float getY(){
        return this.Y;
    }
}
