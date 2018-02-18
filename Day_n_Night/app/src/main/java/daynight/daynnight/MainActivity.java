package daynight.daynnight;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import daynight.daynnight.engine.GLManager_v1;

public class MainActivity extends AppCompatActivity {
    private GLSurfaceView mGLSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Game game = new Game(this);
        setContentView(game);
    }
}
