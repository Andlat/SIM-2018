package daynight.daynnight.engine;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

//import daynight.daynnight.engine.Model.Shader;
import daynight.daynnight.engine.math.Mat4;
import daynight.daynnight.engine.math.Vec3;

/**
 * Created by andlat on 2018-02-02.
 */

public class GLManager_v1 {
    public static class GLSurface extends GLSurfaceView {
        private Renderer mRenderer;

        public GLSurface(Context context) {
            super(context);

            setEGLContextClientVersion(3);//OpenGl ES version

            mRenderer = new GLRenderer(context);
            setRenderer(mRenderer);

            //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }

    public static class GLRenderer implements GLSurfaceView.Renderer{
        private int[] VAO = new int[1], VBO = new int[1];
        private Geometry.Triangle tri = new Geometry.Triangle();//Create the triangle
        private MVP mMVP;
        //private Shader mShader;

        private Mat4 mMat4Identity = new Mat4(1.f),
                    mMat4Projection = new Mat4(),
                    mMath4Camera;

        private Context mContext;
        public GLRenderer(Context context){
            mContext = context;
        }

        @Override
        public void onSurfaceCreated(GL10 unused, EGLConfig config){
            GLES30.glClearColor(50.f, 50.f, 0.f, 1.f);

          /*  //Create the shader
            mShader = new Shader(mContext);

            try {//Load the mShader files
                mShader.Load("basic_shader.vglsl", Shader.Type.VERTEX)
                        .Load("basic_shader.fglsl", Shader.Type.FRAGMENT);
            }catch(IOException ex){
                Log.e("SHADER IO", ex.getMessage());
            }
            try{
                mShader.Compile()
                        .Link()
                        .DeleteShaders()
                        .Use();

            }catch(Shader.Exception ex){
                Log.e("SHADER EXCEPTION", ex.getMessage());
            }
*/
            GLES30.glGenVertexArrays(1, VAO, 0);
            GLES30.glBindVertexArray(VAO[0]);

            GLES30.glGenBuffers(1, VBO, 0);
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, VBO[0]);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, tri.getVertexSize(), tri.getVertexBuffer(), GLES30.GL_STATIC_DRAW);

            //Attrib for the vertices
            GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 3*4, 0);
            GLES30.glEnableVertexAttribArray(0);
        }

        @Override
        public void onSurfaceChanged(GL10 unused, int width, int height){
            GLES30.glViewport(0, 0, width, height);

            //Set the mvp with a perspective of 45 degrees
            mMVP = new MVP((float)width  / (float)height);
            mMVP.getCamera().setEye(new Vec3(0, 0, -4))
                            .setCenter(new Vec3(0, 0, 0))
                            .setUp(new Vec3(0, 0, 1));

            //int mvp_shaderLoc = GLES30.glGetUniformLocation(mShader.getProgram(), "MVP");
            //GLES30.glUniformMatrix4fv(mvp_shaderLoc, 1, false, mMVP.get().toArray(), 0);
        }

        @Override
        public void onDrawFrame(GL10 unused){
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

            GLES30.glBindVertexArray(VAO[0]);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, tri.getVertexCount());
        }
    }
}