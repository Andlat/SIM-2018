package daynight.daynnight.engine.Model;

import android.support.annotation.Nullable;
import android.util.Log;

import java.nio.FloatBuffer;

import daynight.daynnight.engine.math.Mat4;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.util.Util;

/**
 * Created by zelovini on 2018-02-05.
 */

public class Model {
    private static long NxtModelID = 1;//Static variable incremented when a new Model is created
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
        mID = NxtModelID;
        ++NxtModelID;
    }

    public Model AssociateShader(Shader shader){
        mShader = shader;
        return this;
    }
    public Shader getShader(){ return mShader; }

    public int getVerticesOffset() {
        return mVerticesOffset;
    }

    public void setVerticesOffset(int verticesOffset) {
        mVerticesOffset = verticesOffset;
    }

    public int getNormalsOffset() {
        return mNormalsOffset;
    }

    public void setNormalsOffset(int normalsOffset) {
        mNormalsOffset = normalsOffset;
    }

    public int getTexOffset() {
        return mTexOffset;
    }

    public void setTexOffset(int texOffset) {
        mTexOffset = texOffset;
    }

    @Nullable
    public FloatBuffer getVBO() {
        return mModelVBO;
    }

    public void setVBO(FloatBuffer VBO) {
        mModelVBO = VBO;
    }

    public int getModelVBOSize(){ return mModelVBO.capacity() * Util.FLOAT_SIZE; }

    public String getTextureSource() {
        return mTextureSrc;
    }

    public void setTextureSource(String texture) { mTextureSrc = texture; }

    public Texture getTexture() { return mTexture; }

    public void setTexture(Texture texture) { mTexture = texture; }

    public String getName() {
        return mName;
    }

    public void setName(String modelName) {
        mName = modelName;
    }

    public long getID(){ return mID; }

    public long getVBOWorldOffset(){ return mWorldVBOOffset; }
    public void setVBOWorldOffset(long offset){ mWorldVBOOffset = offset; }

    public Mat4 getModelMatrix(){ return mModelMatrix; }
    public void setModelMatrix(Mat4 matrix){ mModelMatrix = matrix; }

    public Vec3 setTranslation(Vec3 translation){
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
    public MovingModel toMovingModel(){
        return new MovingModel(this);
    }

    /**
     * Clone model to a new StaticModel
     * @return The new StaticModel created from this Model
     */
    public StaticModel toStaticModel(){
        return new StaticModel(this);
    }

    /**
     * Clone a Model's attributes to this one. Deep
     * @param clone The model to clone
     */
    public void CloneTo(Model clone){
        clone.mShader = this.mShader;
        clone.mModelVBO = this.mModelVBO;

        clone.mVerticesOffset = this.mVerticesOffset;
        clone.mTexOffset = this.mTexOffset;
        clone.mNormalsOffset = this.mNormalsOffset;

        clone.mName = this.mName;
        clone.mTextureSrc = this.mTextureSrc;
    }

    /**
     * Clone this Model's attributes to another Model
     * @param model Model which will be cloned with this one's attributes
     */
    public void CloneFrom(Model model){
        model.CloneTo(this);
    }
}
