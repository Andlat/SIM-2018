package daynight.daynnight.engine.Model;

import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-02-14.
 */

public class MovingModel extends Model {
    private PhysicsAttributes.MovingModelAttr mPhysics;

    public MovingModel(){
        super();
    }
    public MovingModel(Model model){
        this();
        this.CloneFrom(model);
    }

    public void setPhysics(PhysicsAttributes.MovingModelAttr attr){
        mPhysics = attr;
    }
}
