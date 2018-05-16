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
import daynight.daynnight.firepower.Tool;

import static daynight.daynnight.properties.ModelAttributes.ATTR_AMMO;
import static daynight.daynnight.properties.ModelAttributes.ATTR_ARTHUR;
import static daynight.daynnight.properties.ModelAttributes.ATTR_TOOL;
import static daynight.daynnight.properties.ZIndex.Z_ARTHUR;

/**
 * Created by Nikola Zelovic on 2018-04-30.
 */

public class Arthur{
    private MovingModel mModel = null;
    private final Context mContext;
    private long mInWorldID=-1;

    private Vec3 mDirection = new Vec3();

    private final int FRAME_LENGTH = 200;

    private Tool mTool;

    Arthur(Context context, World world){
        mContext = context;

        try {
            mModel = ObjParser.Parse(context, "models", "arthur.obj", FRAME_LENGTH).get(0).toMovingModel();
            mModel.setPhysics(new PhysicsAttributes.MovingModelAttr(70000, 0, 0, 7f));
            mModel.setAttr(ATTR_ARTHUR);
            this.setSkin(MainActivity.joueur.getSkin());

            mModel.setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(World world, Model object) {
                    Integer attr = object.getAttr();
                    if(attr != ATTR_TOOL && attr != ATTR_AMMO)//Ignore the collision for a tool or ammo
                        mModel.RewindTranslation();
                }
            });

            this.addToWorld(world);

            this.CreateTool(context, world);

        }catch(IOException ex){
            Log.e("Arthur Init", "Failed to load the Arthur model");
        }
    }

    private void addToWorld(World world){
        long wID = world.addModel(mModel);

        this.setInWorldID(wID);
        world.setGroupZIndex(wID, Z_ARTHUR);
    }

    void Walk(){ mModel.getAnimation().Start(); }
    void Stay(){ mModel.getAnimation().Stop(); }

    public MovingModel getModel(){ return mModel; }

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


    private void CreateTool(Context context, World world) throws IOException{
        //Temporary tool
        this.setTool(new Tool(context, world, R.drawable.outil02));//TODO Aller chercher l'outil sélectionner du joueur
    }

    void setTool(Tool tool){
        mTool = tool;
        mModel.Attach(tool.getModel());
    }

    Tool getTool(){ return mTool; }

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
        mTool.getModel().setSwitched(s);
    }
}
