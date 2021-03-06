package daynight.daynnight.engine;

import android.opengl.GLES30;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.Shader;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.util.Util;

/**
 * Created by andlat on 2018-05-04.
 */

class DrawingManager {
    private final static int mGroupMaxSize = 1024*1024/64;//64 groups per Mb of about 85 models of 192 bytes
    private final static int mGroupMaxModels = mGroupMaxSize/192;

    private final int[] mVAO = new int[1];
    private final VBOManager mVBO;

    private final SparseArray<DrawGroup> mGroups = new SparseArray<>();

    private final List<DrawGroup> mZOrderedGroups = new ArrayList<>();
    private boolean mGroupsZchanged = false;
    private ZIndex_QuickSort mZSorter = new ZIndex_QuickSort();

    private static DrawingManager mManager=null;

    private int mNxtVBOIndex = 0;
    private final List<Integer> mEmptyPools = new ArrayList<>();

    private Model.onModelChangedListener mOnModelChangedListener = new Model.onModelChangedListener() {
        @Override
        public void onModelChanged(Model _this, Changed changed) {
            int groupID = _this.getDrawGroupID();
            if (groupID > -1) {
                DrawGroup group = mGroups.get(_this.getDrawGroupID());

                if(changed == Changed.MODEL_MAT)
                    onModelMatChanged(_this, group);
                else
                    onAnimationChanged(_this, group);
            }
        }

        private void onModelMatChanged(Model _this, DrawGroup group){
            if (!_this.getModelMatrix().equals(group.getModelMatrix())) {
                //If the model is the only element in the group, only change the model mat of the group
                if (group.getQuantity() == 1) {
                    group.setModelMatrix(_this.getModelMatrix());
                } else {
                    CreateNewGroup(_this);
                }
            }
        }
        private void onAnimationChanged(Model _this, DrawGroup group){
            if(!_this.getAnimation().equals(group.getAnimation())){
                //If the model is the only element in the group, only change the animation of the group
                if (group.getQuantity() == 1) {
                    group.setAnimation(_this.getAnimation());
                }else{
                    CreateNewGroup(_this);
                }
            }
        }
        //Create a new group for the model
        private void CreateNewGroup(Model _this){
            DrawingManager.this.removeModel(_this);
            DrawingManager.this.addModel(_this);
        }
    };

    private DrawingManager(){
        //One VertexArrayObject (VAO) per World
        GLES30.glGenVertexArrays(1, mVAO, 0);
        GLES30.glBindVertexArray(mVAO[0]);

        mVBO = new VBOManager(mVAO[0]);
    }

    static DrawingManager getInstance(){
        if(mManager == null)
            mManager = new DrawingManager();

        return mManager;
    }

    void addModel(Model model){
        boolean foundGroup = false;
        int groupID = -1;

        final int size = mGroups.size();
        for(int i=0; i < size; ++i){
            DrawGroup group = mGroups.valueAt(i);

            if(group.getQuantity() < mGroupMaxModels) {//If the group still has some space
                if(group.addModel(model, mVBO)) {

                    foundGroup = true;
                    //groupID = group.getID();


                    break;
                }
            }
        }

        //No DrawingGroup could contain this model. Create a new one just for it
        if(!foundGroup){
            //groupID = newGroup(model);
            newGroup(model);
        }

        //model.setDrawGroupID(groupID);
        model.setOnModelChangedListener(mOnModelChangedListener);
    }
    void removeModel(Model model){
        int key = model.getDrawGroupID();
        if(key > -1) {
            mGroups.get(key).removeModel(model, mVBO);
            model.setDrawGroupID(-1);
            model.setOnModelChangedListener(null);

            //Delete group if it is empty
            if(mGroups.get(key).getQuantity() == 0) {
                deleteGroup(key);
            }
        }
    }

    /**
     * Create a new group
     * @param model First model of the group
     * @return Group ID. (Key in sparse array)
     */
    private int newGroup(Model model){
        //Get VBO offset. Use an empty pool if there is. If not, put at the end
        int vboOffset;
        if(mEmptyPools.size() > 0) {
            //Select the first empty pool and remove it from the list
            vboOffset = mEmptyPools.get(0);
            mEmptyPools.remove(0);
        }else{
            vboOffset = mNxtVBOIndex*mGroupMaxSize/Util.FLOAT_SIZE;
            ++mNxtVBOIndex;
        }

        DrawGroup n = new DrawGroup(model, mVBO, vboOffset);
        mGroups.put(n.getID(), n);
        mZOrderedGroups.add(n);

        mGroupsZchanged = true;

        return n.getID();
    }

    /**
     * Delete a group
     * @param key Group ID. (Key in sparse array)
     */
    private void deleteGroup(int key){
        DrawGroup group = mGroups.get(key);

        //Keep track of empty pool index
        mEmptyPools.add(group.getVBOOffset());

        mGroups.remove(key);
        mZOrderedGroups.remove(group);

        mGroupsZchanged = true;
    }

    List<DrawGroup> getGroups(){ return mZOrderedGroups; }//return Util.SparseArrayToArrayList(mGroups); }TODO Remove this line. The conversion takes too much time

    void setZindex(int groupID, int z){
        mGroups.get(groupID).setZ(z);
    }

    void Draw(MVP mvp, long frameElapsedTime){
        GLES30.glBindVertexArray(mVAO[0]);//Not really necessary since it is never unbound, but yeah.

        //TODO Change this line if all models don't use the same shader
        //All models use the same shader, so it is OK to only bind it once
        Shader shader = mGroups.get(0).getModels().get(0).getShader();
        shader.Use();

        //Log.e("Groups Count: ", ""+mGroups.size());

        //Log.e("Size", "S: "+mGroups.size()+" ; F: "+frameElapsedTime);

        //FOR Z-INDEX: Sort the groups depending on their Z-index
        mZSorter.sort();

        for(DrawGroup group : mZOrderedGroups){
            final Texture texture = group.getAnimation().getCurrentTexture(frameElapsedTime);
            final int texUnit = texture.getUnit();
            Texture.ActivateUnit(texUnit);
            texture.Bind();
            GLES30.glUniform1i(GLES30.glGetUniformLocation(shader.getProgram(), "tex"), texUnit - GLES30.GL_TEXTURE0);

            //Log.e("Offset", "Off8: " + group.getVBOOffset()/8 + " ; size8: " + group.getSizeInFloat()/8);
/*
            FloatBuffer g = ((ByteBuffer)GLES30.glMapBufferRange(GLES30.GL_ARRAY_BUFFER, group.getVBOOffset()*Util.FLOAT_SIZE, group.getSizeInFloat()*Util.FLOAT_SIZE, GLES30.GL_MAP_READ_BIT)).order(ByteOrder.nativeOrder()).asFloatBuffer();
            StringBuilder s = new StringBuilder(g.capacity()).append("{");
            for(int h=0; h < g.capacity(); ++h){
                s.append(g.get()).append(", ");
            }
            s.append("}");
            Log.e("GL DATA " + group.getSizeInFloat()/Util.FLOAT_SIZE, s.toString());
            GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER);
*/

            GLES30.glUniformMatrix4fv(GLES30.glGetUniformLocation(shader.getProgram(), "MVP"), 1, false, mvp.get(group.getModelMatrix()).toArray(), 0);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, group.getVBOOffset()/8,  group.getSizeInFloat()/8);//Strides of 8
        }
    }


/*========== Implementation of the Lomuto partition scheme Quick Sort to order the groups by their Z-index ==========*/
    private class ZIndex_QuickSort{
        private void quickSort(final int low, final int hi){
            if(low < hi) {
                final int p = partition(low, hi);

                quickSort(low, p-1);//Sort before partition
                quickSort(p+1, hi);//Sort after
            }
        }
        private int partition(final int low, final int hi){
            final int pivot = mZOrderedGroups.get(hi).getZ();
            int i = low - 1;
            for(int j = low; j <= hi-1; ++j){
                if(mZOrderedGroups.get(j).getZ() < pivot){
                    ++i;
                    Collections.swap(mZOrderedGroups, i, j);
                }
            }

            Collections.swap(mZOrderedGroups, ++i, hi);

            return i;
        }

        void sort(){
            if(mGroupsZchanged){
                Log.e("SORT", "SORT");
                //mZOrderedGroups = Util.SparseArrayToArrayList(mGroups);TODO Remove this line. The conversion takes too much time

                final int hi = mZOrderedGroups.size()-1;
                quickSort(0, hi);

                mGroupsZchanged = false;
            }
        }
    }
/*================================================================================================================*/
}
