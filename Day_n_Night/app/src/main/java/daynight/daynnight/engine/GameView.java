package daynight.daynnight.engine;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by andlat on 2018-02-17.
 */

public class GameView extends GLSurfaceView {
    private World mWorld;

    public GameView(Context context) {
        super(context);

        init();
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init(){
        setEGLContextClientVersion(3);

        Renderer renderer = new Renderer();
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);//TODO Remove RENDER_WHEN_DIRTY
    }

    public void UseWorld(World world){ mWorld = world; }

    protected void onCreate(){
    }

    protected void onDrawFrame(World world){
        world.DrawWorld();
    }

    private class Renderer implements GLSurfaceView.Renderer{
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GameView.this.onCreate();

            requestRender();//TODO Only usefull if rendering when dirty
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GameView.this.onDrawFrame(mWorld);
        }
    }
}
