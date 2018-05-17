package daynight.daynnight.engine.Model;

import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.CollisionDetector;
import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by Nikola Zelovic on 2018-02-14.
 */

public class MovingModel extends Model {
    private PhysicsAttributes.MovingModelAttr mPhysics = new PhysicsAttributes.MovingModelAttr();

    private Vec3 mLastDirection = new Vec3();


    public static abstract class onCollisionListener{
        abstract public void onCollision(World world, Model object);
    }
    private onCollisionListener mOnCollisionListener = null;

    /**
     * Constructor for a MovingModel.
     */
    public MovingModel(){
        super();
    }

    /**
     * Constructor for a MovingModel. Clones another model. No physics are cloned
     * @param model Model to clone
     */
    public MovingModel(Model model){
        this();
        this.CloneFrom(model);
    }

    /**
     * Constructor for a MovingModel. Clones another MovingModel (meaning that the physics of the model are also cloned)
     * @param model Model to clone
     */
    public MovingModel(MovingModel model){
        this();
        this.CloneFrom(model);
    }

    public Vec3 getLastDirection(){ return mLastDirection; }
    public void setLastDirection(Vec3 dir){ mLastDirection = dir; }

    public void setPhysics(PhysicsAttributes.MovingModelAttr attr){
        mPhysics = attr;
    }
    public PhysicsAttributes.MovingModelAttr getPhysics(){ return mPhysics; }

    public void setOnCollisionListener(onCollisionListener listener){
        mOnCollisionListener = listener;
    }
    public onCollisionListener getOnCollisionListener(){
        return mOnCollisionListener;
    }

    public void CloneTo(MovingModel clone) {
        super.CloneTo(clone);

        this.mPhysics.CloneTo(clone.mPhysics);
        clone.mLastDirection = this.mLastDirection;
    }

    /**
     * Clone this Model's attributes to another Model
     * @param model Model which will be cloned with this one's attributes
     */
    public final void CloneFrom(MovingModel model){
        model.CloneTo(this);
    }
}
