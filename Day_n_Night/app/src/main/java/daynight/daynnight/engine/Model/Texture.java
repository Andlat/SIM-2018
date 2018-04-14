package daynight.daynnight.engine.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;
import android.util.SparseArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by andlat on 2018-04-01.
 */

public class Texture {
    private final static SparseArray<Texture> mLoadedTextures = new SparseArray<>();

    private static Texture mBoundTexture=null;

    private static int mActivatedUnit=-1;

    private int mHandle;

    private int mUnit = GLES30.GL_TEXTURE0;
    private static byte mUnitCount = 0;

    private Texture(int textureHandle){
        mHandle = textureHandle;

        mUnit += mUnitCount;

        //Try to separate the textures in the units. Number of units is 32. The engine's user can also control the assigned unit with setUnit/getUnit
        if(mUnit == GLES30.GL_TEXTURE31)
            mUnit = GLES30.GL_TEXTURE0;
        else
            ++mUnitCount;
    }

    public void Bind(){
        if(mBoundTexture != this) {
            GLES30.glBindTexture(GLES20.GL_TEXTURE_2D, mHandle);
            mBoundTexture = this;
        }
    }
    public void UnBind(){
        if(mBoundTexture == this) {
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, 0);
            mBoundTexture = null;
        }
    }

    public boolean IsBound(){
        return (mBoundTexture == this);
    }

    public int getTextureHandle(){
        return mHandle;
    }

    public void setUnit(int unit){ mUnit = unit; }
    public int getUnit(){ return mUnit; }

    public static void ActivateUnit(int unit){
        if(unit != mActivatedUnit){
            GLES30.glActiveTexture(unit);
            mActivatedUnit = unit;
        }
    }

    /**
     * Charger une texture
     * @author http://www.learnopengles.com/android-lesson-four-introducing-basic-texturing/ modifié par Nikola Zelovic
     * @param context COntexte de l'application
     * @param resID resID de l'image
     * @return ID de la texture dans OpenGL
     */
    public static Texture Load(final Context context, final int resID) throws RuntimeException, IOException
    {
        Texture tex=null;

        int indexOfKey = mLoadedTextures.indexOfKey(resID);

        if(indexOfKey < 0) {//Texture is not already loaded
            final int[] textureHandle = new int[1];

            GLES30.glGenTextures(1, textureHandle, 0);

            if (textureHandle[0] != 0) {
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;   // No pre-scaling

                // Read in the resource
                final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resID, options);

                // Bind to the texture in OpenGL
                GLES30.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

                //Set filtering
                GLES30.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
                GLES30.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);

                // Load the bitmap into the bound texture.
                GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

                // Recycle the bitmap, since its data has been loaded into OpenGL.
                bitmap.recycle();

                //Generate mipmaps
                GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
            }

            if (textureHandle[0] == 0) {
                throw new RuntimeException("Error loading texture.");
            }

            tex = new Texture(textureHandle[0]);
            mLoadedTextures.put(resID, tex);

        }else{//Texture already exists
            tex = mLoadedTextures.valueAt(indexOfKey);
        }

        return tex;
    }
}
