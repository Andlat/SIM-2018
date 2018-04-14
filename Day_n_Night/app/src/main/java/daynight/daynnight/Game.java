package daynight.daynnight;

import android.content.Context;
import android.opengl.GLES30;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;

import daynight.daynnight.engine.GameView;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.Shader;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.ObjParser;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-02-17.
 */

class Game extends GameView {
    private Context mContext;

    private long mTileID, mTile2ID;

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
        //world.setPhysics(new PhysicsAttributes.WorldAttr(9.81f));
        super.UseWorld(world);
        Log.e("ERROR 1 GL", ""+ GLES30.glGetError());
        //TODO Generate the shader in the model
        Shader texShader = new Shader(mContext);
        try {//Load the shader files
            texShader.Load("shaders/tex_shader.vglsl", Shader.Type.VERTEX)
                    .Load("shaders/tex_shader.fglsl", Shader.Type.FRAGMENT);

            //Compile the shader
            try{ texShader.Compile().Link().DeleteShaders(); }catch(Shader.Exception ex){ Log.e("Shader Exception", ex.getMessage()); }
            Log.e("ERROR 2 GL", ""+ GLES30.glGetError());
            //Create a tile
            MovingModel tile = ObjParser.Parse(mContext, "models","tuile_cuisine.obj").get(0).toMovingModel();
            Log.e("ERROR 3 GL", ""+ GLES30.glGetError());
            tile.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 2));
            Log.e("ERROR 4 GL", ""+ GLES30.glGetError());
            tile.AssociateShader(texShader);
            Log.e("ERROR 5 GL", ""+ GLES30.glGetError());
            //MovingModel tile2 = ObjParser.Parse(mContext, "models","tuile_cuisine.obj").get(0).toMovingModel();//new MovingModel(tile);
            //tile2.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 2));
            //tile2.AssociateShader(texShader);

            mTileID = world.addModel(tile);
//            mTile2ID = world.addModel(tile2);

            world.Translate(tile, new Vec3(-3, -3, 0));
            //world.Translate(tile2, new Vec3(3, 3, 0));

        }catch(IOException ex){
            Log.e("Game Creation", ex.getMessage());
        }
    }

    @Override
    protected void onSurfaceChanged(int width, int height) {
    }

    @Override
    protected void onDrawFrame(World world) {
        world.Move(mTileID, new Vec3(0.1f, 0.8f, 0.f), getElapsedFrameTime());
        //world.Move(mTile2ID, new Vec3(-0.1f, -0.4f, 0.f), getElapsedFrameTime());
    }
}
