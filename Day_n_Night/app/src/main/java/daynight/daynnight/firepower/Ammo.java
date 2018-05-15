package daynight.daynnight.firepower;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import daynight.daynnight.properties.ZIndex;
import daynight.daynnight.engine.Model.Animation;
import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;
import daynight.daynnight.engine.util.Util;

import static daynight.daynnight.properties.ModelAttributes.ATTR_AMMO;
import static daynight.daynnight.properties.ModelAttributes.ATTR_ARTHUR;
import static daynight.daynnight.properties.ModelAttributes.ATTR_NONE;
import static daynight.daynnight.properties.ModelAttributes.ATTR_TOOL;

/**
 * Created by andlat on 2018-05-13.
 */

public class Ammo {
    public static class AmmoManager {
        private final static List<Ammo> mMovingAmmo = new LinkedList<>();

        public static void Move(World world){
            for(Ammo ammo : mMovingAmmo){
                world.Translate(ammo.mModel, ammo.mDir);
            }
        }

        public static void add(World world, Ammo ammo, Vec3 initPos){
            ammo.addToWorld(world, initPos);
            mMovingAmmo.add(ammo);
        }

        public static void remove(Ammo ammo){
            mMovingAmmo.remove(ammo);
        }
    }

    private MovingModel mModel = new MovingModel();
    private Vec3 mDir = new Vec3();

    public float Interval = 0.75f;//TODO Temporary time
    public int dmg = 10;//TODO Temp dammage

    Ammo(Ammo ammo){
        ammo.CloneTo(this);
    }

    Ammo(MovingModel model){
        initAmmo(model);
    }

    Ammo(Context context, int resID) throws IOException {
        initAmmo(ObjParser.Parse(context, "models", "outil.obj").get(0).toMovingModel());

        this.setSkin(new Animation().addFrame(new Pair<>(Texture.Load(context, resID), 0)));
    }

    private void initAmmo(MovingModel model){
        mModel = model;
        mModel.setPhysics(new PhysicsAttributes.MovingModelAttr(10, 0, 0, 0.5f));//TODO Temporary speed
        mModel.setAttr(ATTR_AMMO);
    }

    //TODO Load the animation auto. Like Arthur.setSkin. Have to check if tool has multiple frames or only one
    void setSkin(Animation skin){
        mModel.setAnimation(skin);
    }

    public void setDir(Vec3 dir){
        mDir = dir;
        mModel.setRotation2D(Util.dirToDeg(dir.x(), dir.y()));
    }
    public Vec3 getDir(){ return mDir; }

    void CloneTo(Ammo clone){
        clone.Interval = this.Interval;
        clone.dmg = this.dmg;

        this.mModel.CloneTo(clone.mModel);
    }

    void addToWorld(World world, Vec3 initPos){
        mModel.setOnCollisionListener(getNewAmmoOnCollisionListener(this));

        world.addModel(mModel);
        world.Translate(mModel, initPos);
        world.setGroupZIndex(mModel, ZIndex.Z_TOOL);
    }

    private static MovingModel.onCollisionListener getNewAmmoOnCollisionListener(final Ammo ammo){
        return new MovingModel.onCollisionListener() {
            @Override
            public void onCollision(World world, Model object) {
                boolean ignoreCollision = false;

                //Check if collided with Arthur, Ammo or Tool to know if should ignore
                int objAttr = object.getAttr();
                if(objAttr != ATTR_NONE) {
                    int cmp = (ATTR_ARTHUR | ATTR_TOOL | ATTR_AMMO) & objAttr;
                    if(cmp == ATTR_ARTHUR || cmp == ATTR_TOOL || cmp == ATTR_AMMO) {
                        ignoreCollision = true;
                    }
                }

                if(!ignoreCollision){
                    //Delete the ammo when it hits something
                    AmmoManager.remove(ammo);
                    world.removeModel(ammo.mModel);
                }
            }
        };
    }
}
