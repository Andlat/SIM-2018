package daynight.daynnight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;

import daynight.daynnight.engine.GameView;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.ObjParser;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-02-17.
 */

class Game extends GameView{
    private Context mContext;

    private long mHeroID, mTileID, mSID;

    public Game(Context context) {
        super(context);

        init(context);
    }

    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        mContext = context;
    }


    @Override
    protected void onCreate() {
        World world = new World();
        super.UseWorld(world);

        try{
            //Create a tile
            MovingModel tile = ObjParser.Parse(mContext, "models","tuile_cuisine.obj").get(0).toMovingModel();
            tile.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 2));

            MovingModel tile2 = new MovingModel(tile);
            tile2.getPhysics().setSpeed(1.5f);

            StaticModel tile3 = new StaticModel(tile2);

            mTileID = world.addModel(tile);
            mHeroID = world.addModel(tile2);
            world.addModel(tile3);

            MovingModel s = ObjParser.Parse(mContext, "models", "strange.obj").get(0).toMovingModel();
            s.setPhysics(new PhysicsAttributes.MovingModelAttr(50000, 0, 0, 3.5f));
            mSID = world.addModel(s);

            world.Translate(tile, new Vec3(-3, -3, 0));
            world.Translate(tile2, new Vec3(3, 3, 0));
            world.Translate(mSID, new Vec3(-3, 3, 0));

        }catch(IOException ex){
            Log.e("Models creation", ex.getMessage());
        }
    }

    @Override
    protected void onSurfaceChanged(int width, int height) {
    }

    @Override
    protected void onDrawFrame(World world) {
        world.Move(mTileID, new Vec3(0.1f, 0.8f, 0.f), getElapsedFrameTime());
        world.Move(mHeroID, new Vec3(0.1f, -0.8f, 0.f), getElapsedFrameTime());
        world.Move(mSID, new Vec3(-1, 0, 0), getElapsedFrameTime());
    }
}
