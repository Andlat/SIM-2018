package daynight.daynnight.engine.math;

/**
 * Created by andlat on 2018-02-04.
 */

public class Vec4 extends Vector {
    public float z(){ return this.components[2];}
    public void z(float z){ this.components[2] = z; }

    public float w(){ return this.components[3];}
    public void w(float z){ this.components[3] = z; }

    public Vec4(){
        this(0.f, 0.f, 0.f, 0.f);
    }
    public Vec4(float x, float y, float z, float w){
        this.components = new float[4];

        this.x(x);
        this.y(y);
        this.z(z);
        this.w(w);
    };
    public Vec4(Vec2 v2) {
        this(v2.x(), v2.y(), 0.f, 0.f);
    }
    public Vec4(Vec3 v3){
        this(v3.x(), v3.y(), v3.z(), 0.f);
    }
    public Vec4(Vec4 v4){
        this(v4.x(), v4.y(), v4.z(), v4.w());
    }
}
