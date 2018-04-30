package daynight.daynnight;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-04-30.
 */

public class Arthur{
    private MovingModel mModel = null;
    private final Context mContext;

    public Arthur(Context context){
        mContext = context;

        try {
            mModel = ObjParser.Parse(context, "models", "arthur.obj").get(0).toMovingModel();
            mModel.setPhysics(new PhysicsAttributes.MovingModelAttr(70000, 0, 0, 1.5f));

            mModel.setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(Model object) {
                    mModel.setTranslation(mModel.getLastTranslation());
                }
            });
        }catch(IOException ex){
            Log.e("Arthur Init", "Failed to load the Arthur model");
        }
    }

    public MovingModel getModel(){ return mModel; }

    public void setSkin(int skinResID) throws IOException{
        mModel.setTextureSource(mContext.getResources().getResourceEntryName(skinResID));
        mModel.setTexture(Texture.Load(mContext, skinResID));
    }
}
