package daynight.daynnight;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import daynight.daynnight.engine.GLManager;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLSurface = new GLManager.GLSurface(this);

        setContentView(mGLSurface);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mGLSurface.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mGLSurface.onResume();
    }
}
