package daynight.daynnight.engine.Model;

import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-02-14.
 */

public class StaticModel extends Model {
    private PhysicsAttributes.StaticModelAttr mPhysics;

    public StaticModel(){
        super();
    }
    public StaticModel(Model model){
        this();
        this.CloneFrom(model);
    }

    public void setPhysics(PhysicsAttributes.StaticModelAttr attr){
        mPhysics = attr;
    }
}
