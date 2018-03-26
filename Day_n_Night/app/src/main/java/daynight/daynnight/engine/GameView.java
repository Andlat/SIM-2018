package daynight.daynnight.engine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import daynight.daynnight.engine.Model.Shader;

/**
 * Created by andlat on 2018-02-17.
 */

public abstract class GameView extends GLSurfaceView {
    private Context mContext;

    private World mWorld;

    public GameView(Context context) {
        super(context);

        init(context);
    }
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        mContext = context;

        setEGLContextClientVersion(3);

        Renderer renderer = new Renderer();
        setRenderer(renderer);

        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);//TODO Remove RENDER_WHEN_DIRTY
    }

    public void UseWorld(World world){ Log.e("EEE", "WORLD");mWorld = world; }

    abstract protected void onCreate();

    /**
     * Function qui est appelée juste avant que la frame se fasse dessiner
     * @param world World qui est utilisé
     */
    abstract protected void onDrawFrame(World world);

    private class Renderer implements GLSurfaceView.Renderer{
        //int[] vao=new int[1], vbo=new int[1];

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GameView.this.onCreate();
            GLES30.glClearColor(0.f,0.f, 0.f, 1.f);//Black background

            /*
            GLES30.glClearColor(50.f, 50.f, 0.f, 1.f);

            //Create the shader
            Shader shader = new Shader(mContext);

            try {//Load the mShader files
                shader.Load("basic_shader.vglsl", Shader.Type.VERTEX)
                        .Load("basic_shader.fglsl", Shader.Type.FRAGMENT);
            }catch(IOException ex){
                Log.e("SHADER IO", ex.getMessage());
            }
            try{
                shader.Compile()
                        .Link()
                        .DeleteShaders();

            }catch(Shader.Exception ex){
                Log.e("SHADER EXCEPTION", ex.getMessage());
            }


            GLES30.glGenVertexArrays(1, vao, 0);
            GLES30.glBindVertexArray(vao[0]);

            GLES30.glGenBuffers(1, vbo, 0);
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo[0]);

            FloatBuffer buff = ByteBuffer.allocateDirect(9*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
            buff.put(new float[]
                    {.5f, 0.f, 0.f, //Top middle
                            -.5f, .5f, 0.f, //Bottom right
                            -.5f, -.5f, 0.f //Bottom Left
                    });

            buff.position(0);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, 9*4, buff, GLES30.GL_STATIC_DRAW);
            GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 3*4, 0);
            GLES30.glEnableVertexAttribArray(0);

            shader.Use();
            */
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            try {
                GameView.this.onDrawFrame(mWorld);
                mWorld.DrawWorld();
            }catch(NullPointerException ex){
                Log.e("World is null", "World is null: " + mWorld); //TODO Handle this exception. World to use wasn't set
            }
            /*
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

            GLES30.glBindVertexArray(vao[0]);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 9);
            */
        }
    }
}
