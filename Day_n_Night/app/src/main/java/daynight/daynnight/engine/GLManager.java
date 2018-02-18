package daynight.daynnight.engine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by zelovini on 2018-02-05.
 */

public class GLManager {
    private GLView mView;

    GLManager(Context context){
        mView = new GLView(context);
    }

    public static class GLView extends GLSurfaceView{
        private Context mContext;
        private GLRenderer mRenderer;

        GLView(Context context){
            super(context);
            mContext = context;

            setEGLContextClientVersion(3);//OpenGl ES version

            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            GLES30.glDepthFunc(GLES30.GL_LESS);

            GLES30.glEnable(GLES30.GL_CULL_FACE);//Backface culling
;
            mRenderer = new GLRenderer();
            setRenderer(mRenderer);
        }
    }

    public static class GLRenderer implements GLSurfaceView.Renderer {
        private MVP mMVP;

        @Override
        public void onSurfaceCreated(GL10 foo, EGLConfig config){

        }

        @Override
        public void onSurfaceChanged(GL10 foo, int width, int height){

        }

        //Game loop
        @Override
        public void onDrawFrame(GL10 foo){

        }
    }
}
