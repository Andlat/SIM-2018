package daynight.daynnight;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;

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
 * Created by Nikola Zelovic on 2018-04-30.
 */

class Arthur{
    private MovingModel mModel = null;
    private final Context mContext;
    private long mInWorldID=-1;

    private Vec3 mDirection = new Vec3();

    private final int FRAME_LENGTH = 200;

    private Model mTool;

    public static int Z_ARTHUR=75, Z_TOOL=80;

    Arthur(Context context){
        mContext = context;

        try {
            mModel = ObjParser.Parse(context, "models", "arthur.obj", FRAME_LENGTH).get(0).toMovingModel();
            mModel.setPhysics(new PhysicsAttributes.MovingModelAttr(70000, 0, 0, 10f));//TODO TEMP SPEED FOR TESTING
            this.setSkin(R.drawable.arthur1_1);//MainActivity.joueur.getSkin());

            mModel.setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(World world, Model object) {
                    mModel.RewindTranslation();
                }
            });
        }catch(IOException ex){
            Log.e("Arthur Init", "Failed to load the Arthur model");
        }
    }

    void Walk(){ mModel.getAnimation().Start(); }
    void Stay(){ mModel.getAnimation().Stop(); }

    MovingModel getModel(){ return mModel; }

    void setInWorldID(long id){
        mInWorldID = id;
    }
    long getInWorldID(){ return mInWorldID; }

    void setSkin(int firstFrameSkinResID) throws IOException{
        Animation skin = new Animation();
        for(byte i=0; i < 5; ++i)
            skin.addFrame(new Pair<>(Texture.Load(mContext, firstFrameSkinResID+i), FRAME_LENGTH));

        mModel.setAnimation(skin);
    }

    void setDirection(Vec3 dir){ mDirection=dir; }
    Vec3 getDirection(){ return mDirection; }

    void setTool(Model tool, World world){
        mTool = tool;
        mModel.Attach(tool);

        tool.StaticTranslate(new Vec3(0.25f, -0.5f, 0));

        world.addModel(tool);
        world.setGroupZIndex(tool, Z_TOOL);
    }

    Model getTool(){ return mTool; }

    void setToolDir(Vec3 dir){
        float theta = Util.RadToDeg((float) Math.atan2(dir.y(), dir.x()));

        if(theta < 0) theta = 360+theta;//atan2 correction (atan2 returns negatives for [PI, 2PI]. (E.g. 3PI/4 = -PI/4)

        mTool.setRotation2D(theta);
    }

    //Switch arthur to its opposite side
    void checkSwitch(float x){
        boolean s = x<0;
        mModel.setSwitched(s);

        /*TODO Translate tool x-axis to compensate the static translation when switching
        if(!mTool.isSwitched() && s){
            mTool.
        }else if(mTool.isSwitched() && !s){

        }
        */
        mTool.setSwitched(s);
    }

    //TODO
    void ChangeToolSkin(){

    }
}
