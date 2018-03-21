package daynight.daynnight.engine.math;

/**
 * Created by andlat on 2018-02-02.
 */

public class Vec3 extends Vector {
    public float z(){ return this.components[2];}
    public void z(float z){ this.components[2] = z; }

    public Vec3(){
        this(0.f, 0.f, 0.f);
    }
    public Vec3(float x, float y, float z){
        this.components = new float[3];

        this.x(x);
        this.y(y);
        this.z(z);
    }
    public Vec3(Vec2 v2) {
        this(v2.x(), v2.y(), 0.f);
    }
    public Vec3(Vec3 v3) {
        this(v3.x(), v3.y(), v3.z());
    }

    /**
     * Produit vectoriel
     * AxB = (AyBz − AzBy) î + (AzBx − AxBz) ĵ + (AxBy − AyBx) k̂
     *
     * @param vec vecteur à multiplier qui à le même nombre de dimensions ou moins.
     * @return Le vecteur résultant "this" après le produit vectoriel
     */
    public Vector cross(Vec3 vec){
        this.x( this.y() * vec.z() - this.z() * vec.y() );
        this.y( this.z() * vec.x() - this.x() * vec.z() );
        this.z( this.x() * vec.y() - this.y() * vec.x() );

        return this;
    }
}
