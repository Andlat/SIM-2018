package daynight.daynnight.firepower;

import android.content.Context;
import android.os.SystemClock;
import android.util.Pair;

import java.io.IOException;

import daynight.daynnight.R;
import daynight.daynnight.engine.Model.Animation;
import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.util.Util;

import static daynight.daynnight.ZIndex.Z_TOOL;

/**
 * Created by andlat on 2018-05-13.
 */

public class Tool {
    private Model mModel;
    private final Ammo mAmmoType;

    private float mLastTimeFired = SystemClock.elapsedRealtime();

    private Vec3 mDir = new Vec3();

    private Tool(Context context) throws IOException {
        mAmmoType = new Ammo(context, R.drawable.balle08);//TODO This is temporary ammo
    }

    public Tool(Context context, World world, Model model) throws IOException{
        this(context);

        mModel = model;

        init(world);
    }
    public Tool(Context context, World world, int resID) throws IOException{
        this(context);

        mModel = ObjParser.Parse(context, "models", "outil.obj").get(0);

        this.setSkin(new Animation().addFrame(new Pair<>(Texture.Load(context, resID), 0)));

        init(world);
    }

    private void init(World world){
        mModel.StaticTranslate(new Vec3(0.25f, -0.5f, 0));

        world.addModel(mModel);
        world.setGroupZIndex(mModel, Z_TOOL);
    }

    public Model getModel(){ return mModel; }

    public void setDir(Vec3 dir){
        mDir = dir;

        float theta = Util.dirToDeg(dir.x(), dir.y());

        mModel.setRotation2D(theta);
    }


    //TODO Load the animation auto. Like Arthur.setSkin. Have to check if tool has multiple frames or only one
    public void setSkin(Animation skin){
        mModel.setAnimation(skin);
    }

    public void Fire(){
        if(!mDir.isEmpty() && mLastTimeFired/1000f >= mAmmoType.Interval){
            mLastTimeFired = SystemClock.elapsedRealtime();

            Ammo bullet = new Ammo(mAmmoType);
            bullet.setDir(mDir);
            Ammo.AmmoManager.add(bullet);
        }
    }
}
