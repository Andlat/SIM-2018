package daynight.daynnight.engine.Model;

import android.support.annotation.Nullable;

import java.nio.FloatBuffer;

import daynight.daynnight.engine.math.Mat4;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.util.Util;

/**
 * Created by zelovini on 2018-02-05.
 */

public class Model {
    private static long mNxtModelID = 1;//Static variable incremented when a new Model is created
    private long mWorldVBOOffset = -1;

    private Shader mShader;

    private int mVerticesOffset=-1, mNormalsOffset=-1, mTexOffset=-1;
    private FloatBuffer mModelVBO = null;

    private String mName=""; //TODO Remove this ?

    private String mTextureSrc = null;
    private Texture mTexture = null;

    private Vec3 mCurrentTranslation = new Vec3();

    private Mat4 mModelMatrix = new Mat4();//Position of the model from its origin. Default is an identity matrix (it's origin)

    private long mID;

    public Model(){
        mID = mNxtModelID;
        ++mNxtModelID;
    }

    public Model AssociateShader(Shader shader){
        mShader = shader;
        return this;
    }
    public final Shader getShader(){ return mShader; }

    public final int getVerticesOffset() {
        return mVerticesOffset;
    }

    public void setVerticesOffset(int verticesOffset) {
        mVerticesOffset = verticesOffset;
    }

    public final int getNormalsOffset() {
        return mNormalsOffset;
    }

    public final void setNormalsOffset(int normalsOffset) {
        mNormalsOffset = normalsOffset;
    }

    public final int getTexOffset() {
        return mTexOffset;
    }

    public final void setTexOffset(int texOffset) {
        mTexOffset = texOffset;
    }

    @Nullable
    public final FloatBuffer getVBO() {
        return mModelVBO;
    }

    public final void setVBO(FloatBuffer VBO) {
        mModelVBO = VBO;
    }

    public final int getModelVBOSize(){ return mModelVBO.capacity() * Util.FLOAT_SIZE; }

    public final String getTextureSource() {
        return mTextureSrc;
    }

    public final void setTextureSource(String texture) { mTextureSrc = texture; }

    public final Texture getTexture() { return mTexture; }

    public final void setTexture(Texture texture) { mTexture = texture; }

    public final String getName() {
        return mName;
    }

    public final void setName(String modelName) {
        mName = modelName;
    }

    public final long getID(){ return mID; }

    public final long getVBOWorldOffset(){ return mWorldVBOOffset; }
    public final void setVBOWorldOffset(long offset){ mWorldVBOOffset = offset; }

    public final Mat4 getModelMatrix(){ return mModelMatrix; }
    public final void setModelMatrix(Mat4 matrix){ mModelMatrix = matrix; }

    public final Vec3 setTranslation(Vec3 translation){
        mCurrentTranslation = (Vec3)mCurrentTranslation.add(translation);
        return mCurrentTranslation;
    }
    public void ResetTranslation(){
        mCurrentTranslation.clear();
    }

    /**
     * Clone model to a new MovingModel
     * @return The new MovingModel created from this Model
     */
    public final MovingModel toMovingModel(){
        return new MovingModel(this);
    }

    /**
     * Clone model to a new StaticModel
     * @return The new StaticModel created from this Model
     */
    public final StaticModel toStaticModel(){
        return new StaticModel(this);
    }

    /**
     * Clone a Model's attributes to this one. Deep
     * @param clone The model to clone
     */
    public void CloneTo(Model clone){
        clone.mName = this.mName;

        clone.mShader = this.mShader;

        clone.mTextureSrc = this.mTextureSrc;
        clone.mTexture = this.mTexture;

        clone.mCurrentTranslation = this.mCurrentTranslation;
        clone.mModelMatrix = this.mModelMatrix;

        clone.mModelVBO = this.mModelVBO;

        clone.mVerticesOffset = this.mVerticesOffset;
        clone.mTexOffset = this.mTexOffset;
        clone.mNormalsOffset = this.mNormalsOffset;

        clone.mWorldVBOOffset = this.mWorldVBOOffset;
    }

    /**
     * Clone this Model's attributes to another Model
     * @param model Model which will be cloned with this one's attributes
     */
    public final void CloneFrom(Model model){
        model.CloneTo(this);
    }
}
