package daynight.daynnight.engine;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by andlat on 2018-02-03.
 */

class Shader {
    /**
     * Shader exception
     */
    static class Exception extends java.lang.Exception{
        /**
         * Default exception
         */
        Exception(){ super("Shader exception"); }

        /**
         * Exception with error message
         * @param msg Error message
         */
        Exception(String msg){ super("Shader exception: " + msg); }
    }

    private Context mContext;
    private String m_vert_shader_src, m_frag_shader_src;//Shader sources
    private int mProgram, mVertexShader, mFragmentShader;//IDs
    private boolean mIsLinked;

    enum Type {VERTEX, FRAGMENT};

    /**
     * Constructeur d'objets Shader
     * @param context Context de l'activité
     */
    Shader(Context context){
        mContext = context;
    }

    /**
     * Charge un vertex ou fragment shader
     * @param file Fichier à ouvrir qui se trouve dans le dossier "assets"
     * @param type Type de shader
     * @return this. Pour faire du "chaining" de fonctions
     * @throws IOException Si le fichier n'a pas pu être lu
     */
    Shader Load(String file, Type type) throws IOException{

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(file)));

            StringBuilder content = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null){
                content.append(line).append('\n');
            }

            if(type == Type.VERTEX)
                m_vert_shader_src = content.toString();
            else
                m_frag_shader_src = content.toString();

        }finally{
            if(reader != null)
                reader.close();
        }

        return this;
    }

    /**
     * Charger directement du code dans un shader
     * @param src Code du shader
     * @param type Type du shader
     * @return this. Pour faire du "chaining" de fonctions
     */
    Shader setSource(String src, Type type){
        if(type == Type.VERTEX)
            m_vert_shader_src = src;
        else
            m_frag_shader_src = src;

        return this;
    }

    /**
     * Compile les shaders
     * @return this. Pour faire du "chaining" de fonctions
     * @throws Exception Si le shader n'a pas pu être compilé
     */
    Shader Compile() throws Shader.Exception{
        int[] success_p = new int[1];

        //Compile the vertex shader
        mVertexShader = GLES30.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES30.glShaderSource(mVertexShader, m_vert_shader_src);
        GLES30.glCompileShader(mVertexShader);

        //Check if compiled successfully
        GLES30.glGetShaderiv(mVertexShader, GLES20.GL_COMPILE_STATUS, success_p, 0);
        if(success_p[0] == 0) throw new Shader.Exception("Vertex Shader couldn't compile: " + GLES30.glGetShaderInfoLog(mVertexShader));


        //Compile the fragment shader
        mFragmentShader = GLES30.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES30.glShaderSource(mFragmentShader, m_frag_shader_src);
        GLES30.glCompileShader(mFragmentShader);

        //Check if compiled successfully
        GLES30.glGetShaderiv(mFragmentShader, GLES20.GL_COMPILE_STATUS, success_p, 0);
        if(success_p[0] == 0)
            throw new Shader.Exception("Fragment Shader couldn't compile: " + GLES30.glGetShaderInfoLog(mFragmentShader));
        else
            mIsLinked = true;

        return this;
    }

    /**
     * Lier le programme du shader
     * @return this. Pour faire du "chaining" de fonctions
     * @throws Shader.Exception Si le programme n'a pas pu lier
     */
    Shader Link() throws Shader.Exception{
        mProgram = GLES30.glCreateProgram();

        GLES30.glAttachShader(mProgram, mVertexShader);
        GLES30.glAttachShader(mProgram, mFragmentShader);

        GLES30.glLinkProgram(mProgram);

        //Check if program successfully linked
        int[] link_success = new int[1];
        GLES30.glGetProgramiv(mProgram, GLES20.GL_LINK_STATUS, link_success, 0);
        if(link_success[0] == 0) throw new Shader.Exception("Program failed to link: " + GLES30.glGetProgramInfoLog(mProgram));

        return this;
    }

    /**
     * Supprimer les shaders (vertex & fragment)
     * @return this. Pour faire du "chaining" de fonctions
     */
    Shader DeleteShaders(){
        GLES30.glDeleteShader(mVertexShader);
        GLES30.glDeleteShader(mFragmentShader);

        return this;
    }

    /**
     * Supprimer le programme associé à cet objet Shader
     * @return this. Pour faire du "chaining" de fonctions
     */
    Shader DeleteProgram(){
        GLES30.glDeleteProgram(mProgram);

        return this;
    }
    Shader Use(){ GLES30.glUseProgram(mProgram); return this; }

    int getProgram(){ return mProgram; }
    boolean isLinked(){ return mIsLinked; }
}
