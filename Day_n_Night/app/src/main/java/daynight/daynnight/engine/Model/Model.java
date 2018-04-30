package daynight.daynnight.engine.Model;

import android.opengl.Matrix;
import android.support.annotation.Nullable;
import android.util.Pair;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import daynight.daynnight.engine.math.Mat4;
import daynight.daynnight.engine.math.Vec2;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.math.Vector;
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

    private String mTextureSrc = null;
    private Texture mTexture = null;

    private Vec3 mCurrentTranslation = new Vec3(), mLastTranslation = new Vec3();

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

    public Vec3 getPosition(){
        return (Vec3)CalculateOrigin().add(mCurrentTranslation);
    }

    //TODO Fonction CalculateOrigin dépendante à l'application
    /**
     * Calcul de l'origine du modèle. Basé sur les coordonnées statiques de l'objet (coors du fichier ou après une translation des coordonnées dans le vbo)
     * CETTE FONCTION EST DÉPENDANTE DE LA FAÇON DONT LES FICHIERS OBJ ONT ÉTÉS FAITS. ELLE NE FONCTIONNE QUE POUR DES RECTANGLES ET EST DÉPENDANTE À CETTE APPLICATION
     */
    public Vec3 CalculateOrigin(){
        Vec2 bottomRight, topLeft;

        bottomRight = new Vec2(mModelVBO.get(0), mModelVBO.get(1));
        topLeft = new Vec2(mModelVBO.get(8), mModelVBO.get(9));

        float midX = (topLeft.x() + bottomRight.x()) / 2;
        float midY = (bottomRight.y() + topLeft.y()) / 2;

        return new Vec3(midX, midY, 0);
    }

    //TODO Fonction getBottomPolyline dépendante à l'application
    /**
     * Retourne les 2 coordonnées formant le bas du modèle rectangulaire.
     * CETTE FONCTION EST DÉPENDANTE DE LA FAÇON DONT LES FICHIERS OBJ ONT ÉTÉS FAITS. ELLE NE FONCTIONNE QUE POUR DES RECTANGLES ET EST DÉPENDANTE À CETTE APPLICATION
     * @return Les coordonnées de la ligne du bas du modèle
     */
    public Pair<Vec2, Vec2> getBottomPolyline(){
        Vec2 bottomLeft = new Vec2(mModelVBO.get(16), mModelVBO.get(17)),
                bottomRight = new Vec2(mModelVBO.get(0), mModelVBO.get(1));

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

        corners.add(new Vec3(mModelVBO.get(32), mModelVBO.get(33), mModelVBO.get(34)));
        corners.add(new Vec3(mModelVBO.get(8), mModelVBO.get(9), mModelVBO.get(10)));
        corners.add(new Vec3(mModelVBO.get(16), mModelVBO.get(17), mModelVBO.get(18)));
        corners.add(new Vec3(mModelVBO.get(0), mModelVBO.get(1), mModelVBO.get(2)));


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

    public final String getTextureSource() {
        return mTextureSrc;
    }

    public final void setTextureSource(String texture) { mTextureSrc = texture; }

    public final Texture getTexture() { return mTexture; }

    public final void setTexture(Texture texture) { mTexture = texture; }

    public final long getID(){ return mID; }

    public final long getVBOWorldOffset(){ return mWorldVBOOffset; }
    public final void setVBOWorldOffset(long offset){ mWorldVBOOffset = offset; }

    public final Mat4 getModelMatrix(){ return mModelMatrix; }
    public final void setModelMatrix(Mat4 matrix){ mModelMatrix = matrix; }

    public final void setTranslation(Vec3 position){
        mLastTranslation = mCurrentTranslation;
        mCurrentTranslation = position;
        this.CalculateModelMat();
    }
    public final void addTranslation(Vec3 translation){
        this.setTranslation((Vec3)mCurrentTranslation.add(translation));
    }
    public void ResetTranslation(){
        this.setTranslation(new Vec3());
    }

    public final Vec3 getTranslation(){ return mCurrentTranslation; }
    public final Vec3 getLastTranslation(){ return mLastTranslation; }

    private void CalculateModelMat(){
        //Calculate and set new ModelMatrix
        float[] translateBuffer = new Mat4().toArray();//Identity matrix
        Matrix.translateM(translateBuffer, 0, mCurrentTranslation.x(), mCurrentTranslation.y(), mCurrentTranslation.z());
        Mat4 translateMat4 = new Mat4(translateBuffer, 0);

        this.setModelMatrix(translateMat4);
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
        clone.mShader = this.mShader;

        clone.mTextureSrc = this.mTextureSrc;
        clone.mTexture = this.mTexture;

        clone.mCurrentTranslation = new Vec3(this.mCurrentTranslation);
        clone.mLastTranslation = new Vec3(this.mLastTranslation);
        clone.mModelMatrix = new Mat4(this.mModelMatrix.toArray(), 0);

        clone.mModelVBO = Util.CloneBuffer(this.mModelVBO);

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
