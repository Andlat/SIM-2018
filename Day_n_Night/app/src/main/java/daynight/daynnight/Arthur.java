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
import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by Nikola Zelovic on 2018-04-30.
 */

public class Arthur{
    private MovingModel mModel = null;
    private final Context mContext;
    private long mInWorldID;

    private final int FRAME_LENGTH = 200;

    public Arthur(Context context){
        mContext = context;

        try {
            mModel = ObjParser.Parse(context, "models", "arthur.obj", FRAME_LENGTH).get(0).toMovingModel();
            mModel.setPhysics(new PhysicsAttributes.MovingModelAttr(70000, 0, 0, 2.5f));
<<<<<<< HEAD
            this.setSkin(MainActivity.joueur.getSkin());
=======
            this.setSkin(SKIN);
            mModel.getAnimation().Start();
>>>>>>> 4c9d3614e69b5322c31bd561e93a79c98444dae7

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

    public void Walk(){ mModel.getAnimation().Start(); }
    public void Stay(){ mModel.getAnimation().Stop(); }

    public MovingModel getModel(){ return mModel; }

    public void setInWorldID(long id){
        mInWorldID = id;
    }
    public long getInWorldID(){ return mInWorldID; }

    public void setSkin(int firstFrameSkinResID) throws IOException{
        Animation skin = new Animation();
        for(byte i=0; i < 5; ++i)
            skin.addFrame(new Pair<>(Texture.Load(mContext, firstFrameSkinResID+i), FRAME_LENGTH));

        mModel.setAnimation(skin);
    }
}
