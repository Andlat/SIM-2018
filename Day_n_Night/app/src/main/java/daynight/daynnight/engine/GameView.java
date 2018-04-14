package daynight.daynnight.engine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import daynight.daynnight.engine.Model.Shader;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.util.Coord;

/**
 * Created by andlat on 2018-02-17.
 */

public abstract class GameView extends GLSurfaceView {
    private World mWorld;

    private long mCurrentTime=0, mLastFrameTime=0;

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

    final public void UseWorld(World world){ mWorld = world; }

    final public long getElapsedFrameTime(){
        return mCurrentTime - mLastFrameTime;
    }

    abstract protected void onCreate();
    abstract protected void onSurfaceChanged(int width, int height);

    /**
     * Function qui est appelée juste avant que la frame se fasse dessiner
     * @param world World qui est utilisé
     */
    abstract protected void onDrawFrame(World world);


    private class Renderer implements GLSurfaceView.Renderer{
        private void glInit(){
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            GLES30.glDepthFunc(GLES30.GL_LESS);
            //GLES30.glEnable(GLES30.GL_CULL_FACE);//Backface culling. Not necessary, because the game is in 2d

            GLES30.glClearColor(0.f,0.f, 0.f, 1.f);//Black background by default
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glInit();

            GameView.this.onCreate();

            mCurrentTime = SystemClock.elapsedRealtime();//The initial time
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);


            MVP mvp = new MVP((float)width/(float)height);
            mvp.getCamera().setCenter(new Vec3(0,0,0))
                           .setEye(new Vec3(0,0,30.f))
                            .setUp(new Vec3(0, 1, 0));
            mWorld.setMVP(mvp);

            GameView.this.onSurfaceChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            mLastFrameTime = mCurrentTime;
            mCurrentTime = SystemClock.elapsedRealtime();//Keep track of frame time

            GameView.this.onDrawFrame(mWorld);

            mWorld.DrawWorld();
        }
    }
/*
    private Coord p1=new Coord(), p2=new Coord(), tmp;
    private float distanceP1=0, distanceP2=0, distanceBetween=0, lastDistanceBetween=0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Check for a pinch for zooming
        if(event.getPointerCount() == 2){
            switch(event.getAction()){
                case MotionEvent.ACTION_MOVE:
                    tmp = new Coord(p1);
                    p1.X = event.getX(0); p1.Y = event.getY(0);
                    distanceP1 = p1.distanceTo(tmp);

                    tmp = new Coord(p2);
                    p2.X = event.getX(0); p2.Y = event.getY(0);
                    distanceP2 = p2.distanceTo(tmp);

                    //if(dista)
                    lastDistanceBetween = distanceBetween;
                    distanceBetween = p1.distanceTo(p2);



                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    p1.X = event.getX(0); p1.Y = event.getY(0);
                    p2.X = event.getX(1); p2.Y = event.getY(1);

                    distanceBetween = p1.distanceTo(p2);
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    p1 = null;
                    p2 = null;
                    break;
            }
        }

        return super.onTouchEvent(event);
    }
    */
}
