package daynight.daynnight.engine.math;

/**
 * Created by Nikola Zelovic on 2018-02-04.
 */

public class Vec2 extends Vector {

    public Vec2(){
        this(0.f, 0.f);
    }
    public Vec2(float x, float y){
        this.components = new float[2];

        this.x(x);
        this.y(y);
    }
}
