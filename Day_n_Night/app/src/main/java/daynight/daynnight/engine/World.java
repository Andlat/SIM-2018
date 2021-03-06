package daynight.daynnight.engine;

import android.opengl.GLES30;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.CollisionDetector;
import daynight.daynnight.engine.physics.PhysicsAttributes;
import daynight.daynnight.engine.util.Util;

/**
 * Created by Nikola Zelovic on 2018-02-11.
 */

//TODO HIDDEN Models are not supported for now
public class World {
/*
    private final int[] mVAO = new int[1];
    private final VBOManager mVBOMan;

    private static final int MVP_LOCATION = 4, TEXTURE_LOCATION = 5;
*/
    private MVP mMVP = null;
    private long mModelToFollow = -1;

    private final LongSparseArray<Model> mModels = new LongSparseArray<>(), mHiddenModels = new LongSparseArray<>();
    private final List<MovingModel> mMovingModels = new LinkedList<>();//Models in this list are also included in mModels
    private final List<Model> mModelsList = new LinkedList<>();

    private PhysicsAttributes.WorldAttr mPhysicsAttr = null;
    private boolean mArePhysicsOn = false;//TODO Take this in account?

    public enum State {VISIBLE, HIDDEN}

    private final DrawingManager mDrawMan = DrawingManager.getInstance();

    public World(){
        /*
        //One VertexArrayObject (VAO) per World
        GLES30.glGenVertexArrays(1, mVAO, 0);
        GLES30.glBindVertexArray(mVAO[0]);

        mVBOMan = new VBOManager(mVAO[0]);
        */
    }

    public long addModel(Model model){
        final long modelID = model.getID();
        mModels.put(modelID, model);
        mModelsList.add(model);

        //model.setVBOWorldOffset(mVBOMan.addData(model.getVBO()));

        //Add to movingModels list if necessary
        if(model instanceof MovingModel)
            mMovingModels.add((MovingModel)model);

        mDrawMan.addModel(model);

        return modelID;
    }
    public void removeModel(long id_model){
        Model toRemoveModel = mModels.get(id_model);
        removeModel(toRemoveModel);
    }
    public void removeModel(Model toRemoveModel){
        /*
        FloatBuffer modelVBO = toRemoveModel.getVBO();
        int floatCount = (modelVBO != null ? modelVBO.capacity() : 0);
        mVBOMan.removeData((int)toRemoveModel.getVBOWorldOffset(), floatCount);
        */

        mModels.remove(toRemoveModel.getID());
        mModelsList.remove(toRemoveModel);


        //Remove from mMovingModels if necessary
        if(toRemoveModel instanceof MovingModel)
            mMovingModels.remove(toRemoveModel);

        mDrawMan.removeModel(toRemoveModel);
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

            model.setLastDirection(dir);

        }catch(ClassCastException ex){
            return false;
        }

        return true;
    }

    public void Translate(long id_model, Vec3 translate){
        this.Translate(mModels.get(id_model), translate);
    }
    public void Translate(Model model, Vec3 translate){
        model.addTranslation(translate);
    }

    public final void setCamFollowModel(long id_model){
        mModelToFollow = id_model;
    }
    private void CamFollowModel(){
        if(mModelToFollow > -1) {
            MVP.Camera cam = mMVP.getCamera();

            Vec3 center = mModels.get(mModelToFollow).getRelOrigin();
            cam.setCenter(center);//set where to look

            cam.setEye(new Vec3(center.x(), center.y(), cam.getEye().z()));//Set where the camera is, so it is parallel to where it's looking
        }
    }

    public void setGroupZIndex(long id_model, int z){
        Model m = mModels.get(id_model);
        if(m != null)
            setGroupZIndex(m, z);
    }

    public void setGroupZIndex(Model model, int z){
        mDrawMan.setZindex(model.getDrawGroupID(), z);
    }

    //TODO Right now, it also draws hidden models. (but not removed ones ?). So I should remedy to that
    void DrawWorld(long frameElapsedTime){
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);


        //------------- OPTION 3. GROUPING MODELS BY MVPs AND ANIMATIONS --------------\\
        mDrawMan.Draw(mMVP, frameElapsedTime);
        //----------------------------------------------------------------------\\

        //Log.e("OP 1", "FINISHED DRAWING");
/*
        //------------- OPTION 2. SLOW AS FUCK WITH MANY OBJECTS --------------\\
        GLES30.glBindVertexArray(mVAO[0]);//Not really necessary since it is never unbound, but yeah.

        final int size = mModels.size();
        for(int i=0; i < size; ++i){
            Model model = mModels.valueAt(i);

            FloatBuffer g = ((ByteBuffer)GLES30.glMapBufferRange(GLES30.GL_ARRAY_BUFFER, (int)model.getVBOWorldOffset()*Util.FLOAT_SIZE, model.getModelVBOSize(), GLES30.GL_MAP_READ_BIT)).order(ByteOrder.nativeOrder()).asFloatBuffer();
            StringBuilder s = new StringBuilder(g.capacity()).append("{");
            for(int h=0; h < g.capacity(); ++h){
                s.append(g.get()).append(", ");
            }
            s.append("}");
            Log.e("GL DATA " + model.getModelVBOSize()/Util.FLOAT_SIZE, s.toString());
            GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER);

            model.getShader().Use();

            final Texture texture = model.getAnimation().getCurrentTexture(frameElapsedTime);
            final int texUnit = texture.getUnit();
            //final int texUnit = model.getOrgTexture().getUnit();

            Texture.ActivateUnit(texUnit);
            //model.getOrgTexture().Bind();
            texture.Bind();
            GLES30.glUniform1i(GLES30.glGetUniformLocation(model.getShader().getProgram(), "tex"), texUnit - GLES30.GL_TEXTURE0);

            GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(model.getShader().getProgram(), "MVP"), 1, false, mMVP.get(model.getModelMatrix()).toArray(), 0);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, (int)model.getVBOWorldOffset()/8,  model.getModelVBOSize()/Util.FLOAT_SIZE/8);
        }

        //----------------------------------------------------------------------\\
*/

        /*          OPTION 1. FASTEST, BUT CAN'T USE MULTIPLE MVPs
        final List<Pair<Integer, Integer>> drawOffsets = mVBOMan.getDrawOffsets();
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

        //----------TEMPORARY----------------\\
        Model model = mModels.valueAt(0);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        model.getOrgTexture().Bind();
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

        CamFollowModel();
        //Log.e("OP 2", "CAM FOLLOWED");

        //============ CollisionDetector.Detect IS FUCKING SLOW AND TAKES UP 4/5 OF THE FRAME TIME =========
        //Detect and execute collisions
        //Log.e("OP 3", "START COLLISIONS DETECTED");
        //Util.LongSparseArrayToArrayList(mModels);
        List<Pair<MovingModel, Model>> collisions = CollisionDetector.Detect2(mMovingModels, mModelsList);
        //Log.e("OP 3", "FINISHED COLLISIONS DETECTED");
        ExcecuteCollisions(collisions);
        //Log.e("OP 4", "FINISHED COLLISIONS EXECUTED");

    }

    private void ExcecuteCollisions(List<Pair<MovingModel, Model>> collisions){
        for(Pair<MovingModel, Model> collision : collisions){
            MovingModel.onCollisionListener listener = collision.first.getOnCollisionListener();
            if(listener != null){
                listener.onCollision(this, collision.second);
            }
        }
    }
}
