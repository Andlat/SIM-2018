package daynight.daynnight.engine;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.util.LongSparseArray;

import java.nio.FloatBuffer;
import java.util.List;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.math.Mat4;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;
import daynight.daynnight.engine.util.Util;

/**
 * Created by andlat on 2018-02-11.
 */

//TODO HIDDEN Models are not supported for now
public class World {
    private final int[] mVAO = new int[1];
    private final VBOManager mVBOMan;

    private static final int MVP_LOCATION = 4, TEXTURE_LOCATION = 5;

    private MVP mMVP = null;

    private final LongSparseArray<Model> mModels = new LongSparseArray<>(), mHiddenModels = new LongSparseArray<>();

    private PhysicsAttributes.WorldAttr mPhysicsAttr = null;
    private boolean mArePhysicsOn = false;

    public enum State {VISIBLE, HIDDEN}

    public World(){
        //One VertexArrayObject (VAO) per World
        GLES30.glGenVertexArrays(1, mVAO, 0);
        GLES30.glBindVertexArray(mVAO[0]);

        mVBOMan = new VBOManager();
    }

    public long addModel(Model model){
        final long modelID = model.getID();
        mModels.put(modelID, model);

        model.setVBOWorldOffset(mVBOMan.addData(model.getVBO()));

        return modelID;
    }
    public void removeModel(long id_model){
        Model toRemoveModel = mModels.get(id_model);


        FloatBuffer modelVBO = toRemoveModel.getVBO();
        int floatCount = (modelVBO != null ? modelVBO.capacity() : 0);
        mVBOMan.removeData((int)toRemoveModel.getVBOWorldOffset(), floatCount);

        mModels.remove(id_model);
    }

    public void hideModel(long id_model){
        mHiddenModels.put(id_model, mModels.get(id_model));
        mModels.remove(id_model);
    }
    public void showModel(long id_model){
        mModels.put(id_model, mHiddenModels.get(id_model));
        mHiddenModels.remove(id_model);
    }

    public Model getModel(long id_model, State state){
        return (state == State.VISIBLE ? mModels.get(id_model) : mHiddenModels.get(id_model));
    }

    @Nullable
    public State getState(long id_model){
        if(mModels.get(id_model) != null)
            return State.VISIBLE;
        else if(mHiddenModels.get(id_model) != null)
            return State.HIDDEN;
        else
            return null;
    }

    public int getModelsCount(){ return mModels.size() + mHiddenModels.size(); }
    public int getShownModelsCount(){ return mModels.size(); }
    public int getHiddenModelsCount(){ return mHiddenModels.size(); }


    public void setPhysics(PhysicsAttributes.WorldAttr physicsAttr){ mPhysicsAttr = physicsAttr; }
    public PhysicsAttributes.WorldAttr getPhysics(){ return mPhysicsAttr; }

    public void activatePhysics(){ mArePhysicsOn = true; }
    public void deactivatePhysics(){ mArePhysicsOn = false; }

    void setMVP(MVP mvp){ mMVP = mvp; }

    public boolean Move(long id_model, Vec3 dir, long frameElapsedTime){
        try {
            MovingModel model = (MovingModel) mModels.get(id_model);

            Vec3 translate = new Vec3(dir);
            translate.mult(frameElapsedTime/1000.f * model.getPhysics().getSpeed());//Translation to add to the current position

            this.Translate(model, translate);

        }catch(ClassCastException ex){
            return false;
        }

        return true;
    }

    public void Translate(long id_model, Vec3 translate){
        this.Translate(mModels.get(id_model), translate);
    }
    public void Translate(Model model, Vec3 translate){
        Vec3 position = model.setTranslation(translate);

        float[] translateBuffer = new Mat4().toArray();//Identity matrix
        Matrix.translateM(translateBuffer, 0, position.x(), position.y(), position.z());
        Mat4 translateMat4 = new Mat4(translateBuffer, 0);

        model.setModelMatrix(translateMat4);
    }

    //TODO Use the Specified shaders.
    //TODO Right now, it also draws hidden models. (but not removed ones). So I should remedy to that
    //TODO Bind textures. Group models based on the textures so I'd only need to bind the same image once.
    //TODO MVP uniform does not respect the location set in the shader. Because of that, I have to use glGetUniformLocation
    public void DrawWorld(){
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        //------------- OPTION 2. SLOW AS FUCK WITH MANY OBJECTS --------------\\
        GLES30.glBindVertexArray(mVAO[0]);//Not really necessary since it is never unbound, but yeah.

        final int size = mModels.size();
        for(int i=0; i < size; ++i){
            Model model = mModels.valueAt(i);

            model.getShader().Use();

            final int texUnit = model.getTexture().getUnit();
            Texture.ActivateUnit(texUnit);
            model.getTexture().Bind();
            GLES30.glUniform1i(TEXTURE_LOCATION, texUnit);


            GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(model.getShader().getProgram(), "MVP"), 1, false, mMVP.get(model.getModelMatrix()).toArray(), 0);

            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, (int)model.getVBOWorldOffset(), model.getModelVBOSize() / Util.FLOAT_SIZE);
        }
        //----------------------------------------------------------------------\\

        /*          OPTION 1. FASTEST, BUT CAN'T USE MULTIPLE MVPs
        final List<Pair<Integer, Integer>> drawOffsets = mVBOMan.getDrawOffsets();
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        //----------TEMPORARY----------------\\
        Model model = mModels.valueAt(0);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        model.getTexture().Bind();
        GLES30.glUniform1i(TEXTURE_LOCATION, 0);//Assign texture unit 0 to shader location 5

        if(mMVP != null) {
            Log.e("MVP", mMVP.get(model.getModelMatrix()).toString());

            int loc = GLES30.glGetUniformLocation(model.getShader().getProgram(), "MVP");

            GLES30.glUniformMatrix4fv(loc, 1, false, mMVP.get(model.getModelMatrix()).toArray(), 0);
        }
        //-----------------------------------\\

        GLES30.glBindVertexArray(mVAO[0]);

        for(Pair<Integer, Integer> offset : drawOffsets) {
            Log.e("DRAW WORLD PAIR", "Pair: " + offset.first + ", " + offset.second);
            GLES30.glDrawArrays(GLES20.GL_TRIANGLES, offset.first, offset.second/4);
        }
        */
    }
}
