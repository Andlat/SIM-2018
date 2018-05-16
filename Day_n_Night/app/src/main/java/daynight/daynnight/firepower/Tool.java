package daynight.daynnight.firepower;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.util.List;

import daynight.daynnight.R;
import daynight.daynnight.engine.Model.Animation;
import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.util.Util;

import static daynight.daynnight.properties.ModelAttributes.ATTR_AMMO;
import static daynight.daynnight.properties.ModelAttributes.ATTR_TOOL;
import static daynight.daynnight.properties.ZIndex.Z_TOOL;

/**
 * Created by andlat on 2018-05-13.
 */

public class Tool {
    private Model mModel;
    private final Ammo mAmmoType;

    private float mLastTimeFired = SystemClock.elapsedRealtime();

    private Vec3 mDir = new Vec3();

    private Tool(Context context) throws IOException {
        mAmmoType = new Ammo(context, R.drawable.balle02);//TODO Aller chercher les projectiles reliées au fusil
    }

    public Tool(Context context, World world, Model model) throws IOException{
        this(context);

        mModel = model;

        init(world);
    }
    public Tool(Context context, World world, int resID) throws IOException{
        this(context);

        mModel = ObjParser.Parse(context, "models", "outil.obj").get(0);

        //TODO Vérifier si le fusil est une animation (s'il a plusieurs frames)
        this.setSkin(new Animation().addFrame(new Pair<>(Texture.Load(context, resID), 0)));

        init(world);
    }

    private void init(World world){
        mModel.setAttr(ATTR_TOOL);
        mModel.StaticTranslate(new Vec3(/*0.25f*/0, -0.5f, 0));

        world.addModel(mModel);
        world.setGroupZIndex(mModel, Z_TOOL);
    }

    public Model getModel(){ return mModel; }

    public void setDir(Vec3 dir){
        mDir = dir;

        float theta = Util.dirToDeg(dir.x(), dir.y());

        mModel.setRotation2D(theta);
    }

    public void setSkin(Animation skin){
        mModel.setAnimation(skin);
    }

    public void Fire(World world){
        long newTime = SystemClock.elapsedRealtime();
        if(!mDir.isEmpty() && (newTime-mLastTimeFired)/1000f >= mAmmoType.Interval){
            mLastTimeFired = newTime;

            Ammo bullet = new Ammo(mAmmoType);
            bullet.setDir(mDir);

            List<Vec3> toolCorners = mModel.getCorners();
            float x = toolCorners.get(0).x(), y = toolCorners.get(0).y() - ((toolCorners.get(0).y()-toolCorners.get(3).y())/2);

            Ammo.AmmoManager.add(world, bullet, new Vec3(x, y, 0));
        }
    }
}
