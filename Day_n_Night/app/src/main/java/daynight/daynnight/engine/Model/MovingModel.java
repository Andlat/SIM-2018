package daynight.daynnight.engine.Model;

import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-02-14.
 */

public class MovingModel extends Model {
    private PhysicsAttributes.MovingModelAttr mPhysics = new PhysicsAttributes.MovingModelAttr();

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

    public void setPhysics(PhysicsAttributes.MovingModelAttr attr){
        mPhysics = attr;
    }
    public PhysicsAttributes.MovingModelAttr getPhysics(){ return mPhysics; }

    public void CloneTo(MovingModel clone) {
        super.CloneTo(clone);

        this.mPhysics.CloneTo(clone.mPhysics);
    }


    /**
     * Clone this Model's attributes to another Model
     * @param model Model which will be cloned with this one's attributes
     */
    public final void CloneFrom(MovingModel model){
        model.CloneTo(this);
    }
}
