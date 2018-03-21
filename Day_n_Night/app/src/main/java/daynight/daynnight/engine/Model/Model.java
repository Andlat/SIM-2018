package daynight.daynnight.engine.Model;

import android.support.annotation.Nullable;

import java.nio.FloatBuffer;

/**
 * Created by zelovini on 2018-02-05.
 */

public class Model {
    private static long ModelID = 0;
    private long mWorldVBOOffset = -1;//TODO Check usage. How to create a big world VBO, and modify it at runtime if needed? (Like if I want to add or remove objects at runtime)

    private Shader mShader;

    private int mVerticesOffset=-1, mNormalsOffset=-1, mTexOffset=-1;
    private FloatBuffer mModelVBO = null;

    private String mTexture = null, mName="";

    private long mID;

    public Model(){
        mID = ModelID;
        ++ModelID;
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

    public String getTexture() {
        return mTexture;
    }

    public void setTexture(String Texture) {
        mTexture = mTexture;
    }

    public String getName() {
        return mName;
    }

    public void setName(String modelName) {
        mName = modelName;
    }

    public long getID(){ return mID; }

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
        clone.mTexture = this.mTexture;
    }

    /**
     * Clone this Model's attributes to another Model
     * @param model Model which will be cloned with this one's attributes
     */
    public void CloneFrom(Model model){
        model.CloneTo(this);
    }
}
