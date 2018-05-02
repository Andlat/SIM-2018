package daynight.daynnight.engine.Model;

import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-02-14.
 */

public class StaticModel extends Model {
    private PhysicsAttributes.StaticModelAttr mPhysics = new PhysicsAttributes.StaticModelAttr();
    public enum Type {FLOOR, BLOCK, WALL_BOTTOM, WALL_TOP}
    private Type mType = Type.FLOOR;

    /**
     * Constructor for a StaticModel.
     */
    public StaticModel(){
        super();
    }

    /**
     * Constructor for a StaticModel. Clones another model. No physics are cloned
     * @param model Model to clone
     */
    public StaticModel(Model model){
        this();
        this.CloneFrom(model);
    }

    /**
     * Constructor for a StaticModel. Clones another StaticModel (meaning that the physics of the model are also cloned)
     * @param model Model to clone
     */
    public StaticModel(StaticModel model){
        this();
        this.CloneFrom(model);
    }

    public void setPhysics(PhysicsAttributes.StaticModelAttr attr){
        mPhysics = attr;
    }
    public PhysicsAttributes.StaticModelAttr getPhysics(){ return mPhysics; }

    public void CloneTo(StaticModel clone) {
        super.CloneTo(clone);

        this.mPhysics.CloneTo(clone.mPhysics);
    }


    /**
     * Clone this Model's attributes to another Model
     * @param model Model which will be cloned with this one's attributes
     */
    public final void CloneFrom(StaticModel model){
        model.CloneTo(this);
    }


    public Type getType(){ return mType; }
    public void setType(Type type){ mType = type; }
}
