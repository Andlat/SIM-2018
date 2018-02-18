package daynight.daynnight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import daynight.daynnight.engine.GameView;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.Shader;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-02-17.
 */

class Game extends GameView {
    private Context mContext;

    private long mHeroID;

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
        super.onCreate();

        World world = new World();
        world.setPhysics(new PhysicsAttributes.WorldAttr(9.81f));
        super.UseWorld(world);

        Shader basicShader = new Shader(mContext);
        try {//Load the mShader files
            basicShader.Load("basic_shader.vglsl", Shader.Type.VERTEX)
                    .Load("basic_shader.fglsl", Shader.Type.FRAGMENT);
        }catch(IOException ex){
            Log.e("SHADER IO", ex.getMessage());
        }
        try{
            basicShader.Compile()
                    .Link()
                    .DeleteShaders()
                    .Use();

        }catch(Shader.Exception ex){
            Log.e("SHADER EXCEPTION", ex.getMessage());
        }


        MovingModel hero = new MovingModel();
        hero.setPhysics(new PhysicsAttributes.MovingModelAttr(80, 0.5f, 0.4f, 1.f));
        hero.AssociateShader(basicShader);

        FloatBuffer buff = ByteBuffer.allocateDirect(9).order(ByteOrder.nativeOrder()).asFloatBuffer();
        buff.put(new float[]
                {-1, -1, 0,
                 1, -1, 0,
                 0.5f, 1.f, 0
                });
        hero.setVBO(buff);
        hero.setVerticesOffset(0);

        mHeroID = world.addModel(hero);
    }

    @Override
    protected void onDrawFrame(World world) {
        super.onDrawFrame(world);


    }
}
