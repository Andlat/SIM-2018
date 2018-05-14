package daynight.daynnight.firepower;

import android.content.Context;
import android.util.Pair;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import daynight.daynnight.R;
import daynight.daynnight.engine.Model.Animation;
import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;
import daynight.daynnight.engine.util.Util;

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

        public static void add(Ammo ammo){
            mMovingAmmo.add(ammo);
        }

        public static void remove(Ammo ammo){
            mMovingAmmo.remove(ammo);
        }
    }

    private MovingModel mModel;
    private Vec3 mDir = new Vec3();

    public float Interval = 0.5f;//TODO Temporary time
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

        mModel.setPhysics(new PhysicsAttributes.MovingModelAttr(10, 0, 0, 5.f));//TODO Temporary speed

        mModel.setOnCollisionListener(new MovingModel.onCollisionListener() {
            @Override
            public void onCollision(World world, Model object) {
                //Delete the ammo when it hits something
                AmmoManager.remove(Ammo.this);
                world.removeModel(mModel);
            }
        });
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
}
