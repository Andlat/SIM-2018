package daynight.daynnight.engine.Model;

import android.opengl.Matrix;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import daynight.daynnight.engine.math.Mat4;
import daynight.daynnight.engine.math.Vec2;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.util.Util;

/**
 * Created by Nikola Zelovic on 2018-02-05.
 */

public class Model {
    private static long mNxtModelID = 1;//Static variable incremented when a new Model is created
    private long mWorldVBOOffset = -1;

    private Shader mShader;

    private int mVerticesOffset=-1, mNormalsOffset=-1, mTexOffset=-1;
    private FloatBuffer mModelVBO = null;

    private String mOrgTextureSrc = null;
    private Texture mOrgTexture = null;
    private Animation mAnimation = new Animation();

    private Vec3 mCurrentTranslation = new Vec3(), mLastTranslation = new Vec3();
    private float mCurrentRotation2D = 0;

    private Mat4 mModelMatrix = new Mat4();//Position of the model from its origin. Default is an identity matrix (it's origin)

    private Vec3 mOrigin = new Vec3();

    private ArrayList<Model> mAttached = new ArrayList<>();

    private long mID;

    private int mDrawGroupID;

    //On ModelMatrix or Animation changed listener
    public static abstract class onModelChangedListener {
        public enum Changed{ MODEL_MAT, ANIMATION }
        abstract public void onModelChanged(Model _this, Changed changed);
    }
    private onModelChangedListener mOnModelChangedListener;
    public void setOnModelChangedListener(onModelChangedListener listener){
        mOnModelChangedListener = listener;
    }

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

    public Vec3 getPosition(){
        return (Vec3)getOrigin().add(mCurrentTranslation);
    }

    //TODO Fonction CalculateOrigin indépendante à l'application
    /**
     * Calcul de l'origine du modèle. Basé sur les coordonnées statiques de l'objet (coors du fichier ou après une translation des coordonnées dans le vbo)
     * CETTE FONCTION EST DÉPENDANTE DE LA FAÇON DONT LES FICHIERS OBJ ONT ÉTÉS FAITS. ELLE NE FONCTIONNE QUE POUR DES RECTANGLES ET EST DÉPENDANTE À CETTE APPLICATION
     */
    private void CalculateOrigin(){
        Vec2 bottomRight, topLeft;

        bottomRight = new Vec2(mModelVBO.get(0), mModelVBO.get(1));
        topLeft = new Vec2(mModelVBO.get(8), mModelVBO.get(9));

        float midX = (topLeft.x() + bottomRight.x()) / 2;
        float midY = (bottomRight.y() + topLeft.y()) / 2;

        mOrigin = (Vec3)new Vec3(midX, midY, 0).add(mCurrentTranslation);
    }

    public Vec3 getOrigin(){ return mOrigin; }

    //TODO Fonction getBottomPolyline indépendante à l'application
    /**
     * Retourne les 2 coordonnées formant le bas du modèle rectangulaire.
     * CETTE FONCTION EST DÉPENDANTE DE LA FAÇON DONT LES FICHIERS OBJ ONT ÉTÉS FAITS. ELLE NE FONCTIONNE QUE POUR DES RECTANGLES ET EST DÉPENDANTE À CETTE APPLICATION
     * @return Les coordonnées de la ligne du bas du modèle
     */
    public Pair<Vec3, Vec3> getBottomPolyline(){
        Vec3 bottomLeft = (Vec3)new Vec3(mModelVBO.get(16), mModelVBO.get(17), 0).add(mCurrentTranslation),
                bottomRight = (Vec3)new Vec3(mModelVBO.get(0), mModelVBO.get(1), 0).add(mCurrentTranslation);

        return new Pair<>(bottomLeft, bottomRight);
    }


    //TODO Fonction getCorners dépendante à l'application
    /**
     * Retourne les coins du modèle rectangulaire à partir de haut-droit en sens anti-horaire
     * CETTE FONCTION EST DÉPENDANTE DE LA FAÇON DONT LES FICHIERS OBJ ONT ÉTÉS FAITS. ELLE NE FONCTIONNE QUE POUR DES RECTANGLES ET EST DÉPENDANTE À CETTE APPLICATION
     * @return Les coins du modèle
     */
    public List<Vec3> getCorners(){
        final List<Vec3> corners = new ArrayList<>();

        //Get spawn coordinates
        corners.add(new Vec3(mModelVBO.get(32), mModelVBO.get(33), mModelVBO.get(34)));
        corners.add(new Vec3(mModelVBO.get(8), mModelVBO.get(9), mModelVBO.get(10)));
        corners.add(new Vec3(mModelVBO.get(16), mModelVBO.get(17), mModelVBO.get(18)));
        corners.add(new Vec3(mModelVBO.get(0), mModelVBO.get(1), mModelVBO.get(2)));

        //Add the current translation
        for(Vec3 corner : corners)
            corner.add(mCurrentTranslation);

        return corners;
    }

    @Nullable
    public final FloatBuffer getVBO() {
        return mModelVBO;
    }

    public final void setVBO(FloatBuffer VBO) {
        mModelVBO = VBO;
    }

    public final int getModelVBOSize(){ return mModelVBO.capacity() * Util.FLOAT_SIZE; }

    public final String getOrgTextureSource() {
        return mOrgTextureSrc;
    }

    public final void setOrgTextureSource(String texture) { mOrgTextureSrc = texture; }

    public final Texture getOrgTexture() { return mOrgTexture; }

    public final void setOrgTexture(Texture texture) { mOrgTexture = texture; }

    public final Animation addFrame(Texture texture, int milliseconds){
        return mAnimation.addFrame(new Pair<>(texture, milliseconds));
    }
    public final Animation getAnimation(){ return mAnimation; }
    public final void setAnimation(Animation animation){
        mAnimation = animation;

        if(mOnModelChangedListener != null)
            mOnModelChangedListener.onModelChanged(this, onModelChangedListener.Changed.ANIMATION);
    }


    public final long getID(){ return mID; }

    public final long getVBOWorldOffset(){ return mWorldVBOOffset; }
    public final void setVBOWorldOffset(long offset){ mWorldVBOOffset = offset; }

    public final Mat4 getModelMatrix(){ return mModelMatrix; }
    public final void setModelMatrix(Mat4 matrix){
        mModelMatrix = matrix;

        if(mOnModelChangedListener != null) {
            mOnModelChangedListener.onModelChanged(this, onModelChangedListener.Changed.MODEL_MAT);
        }

        RunAttached();
    }

    public int getDrawGroupID(){ return mDrawGroupID; }
    public void setDrawGroupID(int groupID){ mDrawGroupID = groupID; }

    public final void RewindTranslation(){
        mCurrentTranslation = mLastTranslation;
    }

    public final void setTranslation(Vec3 position){
        mLastTranslation = mCurrentTranslation;
        mCurrentTranslation = position;
        this.CalculateModelMat();
    }
    public final void addTranslation(Vec3 translation){
        this.setTranslation((Vec3)new Vec3(mCurrentTranslation).add(translation));
    }
    public void ResetTranslation(){
        this.setTranslation(new Vec3());
    }

    public final Vec3 getTranslation(){ return mCurrentTranslation; }
    public final Vec3 getLastTranslation(){ return mLastTranslation; }

    /**
     * Set the rotation in degrees
     * @param degrees degrees to rotate
     */
    public final void setRotation2D(float degrees){
        //Clamp the degrees to [0, 360]
        degrees = Util.Clamp(degrees, 0, 360);

        mCurrentRotation2D = degrees;
        this.CalculateModelMat();
    }
    public final void addRotation2D(float degrees){
        this.setRotation2D(mCurrentRotation2D +degrees);
    }

    public final float getRotation2D(){ return mCurrentRotation2D; }
    public final void ResetRotation2D(){ this.setRotation2D(0);}

    private void CalculateModelMat(){
        //Calculate and set new ModelMatrix
        float[] modelBuffer = new Mat4().toArray();

        //Translate according to the relative position and the absolute position (the latter is used for the rotation on itself)
        Matrix.translateM(modelBuffer, 0, mOrigin.x() + mCurrentTranslation.x(), mOrigin.y() + mCurrentTranslation.y(), mCurrentTranslation.z());

        //Rotate on itself around the z-axis
        Matrix.rotateM(modelBuffer, 0, mCurrentRotation2D, 0, 0, 1f);

        //Translate back to the relative position (discards the absolute position)
        Matrix.translateM(modelBuffer, 0, -mOrigin.x(), -mOrigin.y(), 0);

        this.setModelMatrix(new Mat4(modelBuffer, 0));
    }

    /**
     * Translation des coordonnées absolue du modèle et non de la matrice modèle. Doit être appelé AVANT d'ajouter le modèle à un monde, si elle est utilisée.
     * @param translation Translation à faire
     */
    public void StaticTranslate(Vec3 translation){
        final int vboSize = mModelVBO.capacity();
        for(int i=0; i < vboSize; i+=6){
            mModelVBO.put(i, mModelVBO.get(i) + translation.x());
            mModelVBO.put(++i, mModelVBO.get(i) + translation.y());
            mModelVBO.put(++i, mModelVBO.get(i) + translation.z());
        }

        CalculateOrigin();
    }


    /**
     * Attach a model to another. When the model matrix is changed, the attached model's model matrix is also changed to be the same. Meaning that any non-static translation of that object will be deleted. (Subject to change)
     * @param model Model to attach to this
     */
    public void Attach(Model model){ mAttached.add(model); }
    public void RemoveAttached(Model model){ mAttached.remove(model); }
    public List<Model> getAttached(){ return mAttached; }
    public boolean isAttached(Model model){ return mAttached.contains(model); }
    public int getAttachedQuantity(){ return mAttached.size(); }

    private void RunAttached(){
        for(Model attached : mAttached){
            attached.setModelMatrix(new Mat4(this.mModelMatrix.toArray(), 0));
        }
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
    @SuppressWarnings("unchecked")
    public void CloneTo(Model clone){
        clone.mShader = this.mShader;

        clone.mOrgTextureSrc = this.mOrgTextureSrc;
        clone.mOrgTexture = this.mOrgTexture;
        this.mAnimation.CloneTo(clone.mAnimation);

        clone.mCurrentTranslation = new Vec3(this.mCurrentTranslation);
        clone.mLastTranslation = new Vec3(this.mLastTranslation);
        clone.mCurrentRotation2D = this.mCurrentRotation2D;
        clone.mModelMatrix = new Mat4(this.mModelMatrix.toArray(), 0);

        clone.mOrigin = this.mOrigin;

        clone.mModelVBO = Util.CloneBuffer(this.mModelVBO);

        clone.mVerticesOffset = this.mVerticesOffset;
        clone.mTexOffset = this.mTexOffset;
        clone.mNormalsOffset = this.mNormalsOffset;

        clone.mWorldVBOOffset = this.mWorldVBOOffset;

        clone.mAttached = (ArrayList<Model>)mAttached.clone();
    }

    /**
     * Clone this Model's attributes to another Model
     * @param model Model which will be cloned with this one's attributes
     */
    public final void CloneFrom(Model model){
        model.CloneTo(this);
    }
}
