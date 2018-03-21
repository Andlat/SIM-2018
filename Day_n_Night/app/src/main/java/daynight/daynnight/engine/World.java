package daynight.daynnight.engine;

import android.opengl.GLES20;
import android.opengl.GLES30;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.util.LongSparseArray;

import java.nio.FloatBuffer;
import java.util.List;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;

/**
 * Created by andlat on 2018-02-11.
 */

public class World {
    private float[] VAO;
    private VBOManager mVBOMan = new VBOManager();

    private final LongSparseArray<Model> mModels = new LongSparseArray<>(), mHiddenModels = new LongSparseArray<>();

    private PhysicsAttributes.WorldAttr mPhysicsAttr = null;
    private boolean mArePhysicsOn = false;

    public enum State {SHOWN, HIDDEN}

    public World(){

    }

    public long addModel(Model model){
        final long modelID = model.getID();
        mModels.put(modelID, model);

        mVBOMan.addData(model.getVBO());

        return modelID;
    }
    public void removeModel(long id_model){
        Model toRemoveModel = mModels.get(id_model);

        mVBOMan.removeData();

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
        return (state == State.SHOWN ? mModels.get(id_model) : mHiddenModels.get(id_model));
    }

    @Nullable
    public State getState(long id_model){
        if(mModels.get(id_model) != null)
            return State.SHOWN;
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

    //TODO Move models
    public void Move(long id_model, Vec3 dir){
        
    }

    //TODO Right now, it also draws hidden models. (but not removed ones). So I should remedy to that
    public void DrawWorld(){
        final List<Pair<Integer, Integer>> drawOffsets = mVBOMan.getDrawOffsets();
        for(Pair<Integer, Integer> offset : drawOffsets) {
            GLES30.glDrawArrays(GLES20.GL_TRIANGLES, offset.first, offset.second);
        }
    }
}
