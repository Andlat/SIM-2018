package daynight.daynnight.engine.Model;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Nikola Zelovic on 2018-04-22.
 */

public class TShaderSingleton extends Shader {
    private static TShaderSingleton mShader = null;

    private TShaderSingleton(Context context){
        super();

        try{
            Load(context, "shaders/tex_shader.vglsl", Shader.Type.VERTEX)
                    .Load(context, "shaders/tex_shader.fglsl", Shader.Type.FRAGMENT)
                    .Compile().Link().DeleteShaders();

            mShader = this;
        }catch(IOException |Shader.Exception ex){
            Log.e("Creating TShader", ex.getMessage(), ex);
        }
    }

    public static TShaderSingleton getInstance(Context context){
        if(mShader == null)
            mShader = new TShaderSingleton(context);

        return mShader;
    }
}
