package daynight.daynnight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

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

class Game extends GameView implements Joystick.JoystickListener{
    private Context mContext;

    private long mHeroID, mTileID;
    private ArrayList<Long> mBallesID;
    private ArrayList<MovingModel> mBalles;
    private ArrayList<Float> mDirectionsBallesX;
    private ArrayList<Float> mDirectionsBallesY;
    private Joystick joystickTir;

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

        joystickTir = findViewById(R.id.joystickTir);


        //TODO Generate the shader in the model
        Shader texShader = new Shader(mContext);
        try {//Load the shader files
            texShader.Load("shaders/tex_shader.vglsl", Shader.Type.VERTEX)
                    .Load("shaders/tex_shader.fglsl", Shader.Type.FRAGMENT);

            //Compile the shader
            try{ texShader.Compile().Link().DeleteShaders(); }catch(Shader.Exception ex){ Log.e("Shader Exception", ex.getMessage()); }

            //Create a tile
            MovingModel tile = ObjParser.Parse(mContext, "models","tuile_cuisine.obj").get(0).toMovingModel();
            tile.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 1));

            //TODO Use Model's texture source to load the texture, but check first if it wasn't already loaded. This here is temporary.
            //Load the kitchen texture
            Texture tex = Texture.Load(mContext, R.drawable.kitchen);

            tile.AssociateShader(texShader);
            tile.setTexture(tex);
            mTileID = world.addModel(tile);

            world.Translate(tile, new Vec3(-3, -3, 0));

            for(int i=0; i < 1000; ++i) {
                StaticModel tmp = ObjParser.Parse(mContext, "models", "tuile_cuisine.obj").get(0).toStaticModel();
                tmp.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 1));
                tmp.AssociateShader(texShader);
                tmp.setTexture(tex);
                world.addModel(tmp);
            }

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
        int temp=0;
        for(Long monsieurMovingModelID : mBallesID){
            world.Move(monsieurMovingModelID, new Vec3(mDirectionsBallesX.get(temp), mDirectionsBallesY.get(temp), 0.f), getElapsedFrameTime());
            temp++;
            //if(positionBalle == positionAutreShit) destroyBalle(temp);
        }
    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int source) throws IOException {
        if(source == 1){
            MovingModel bullet = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
            bullet.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mBalles.add(bullet);
            mDirectionsBallesX.add(xPercent);
            mDirectionsBallesY.add(yPercent);
        }
    }

    public void destroyBalle(int i){
        mBalles.remove(i);
        mDirectionsBallesX.remove(i);
        mDirectionsBallesY.remove(i);
        mBallesID.remove(i);
    }

    //private Shader createShader(){

    //}
}
