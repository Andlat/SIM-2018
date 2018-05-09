package daynight.daynnight.engine;

import android.opengl.GLES30;

import java.util.ArrayList;
import java.util.List;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.Shader;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.util.Util;

/**
 * Created by andlat on 2018-05-04.
 */

class DrawingManager {
    private final static int mGroupMaxSize = 1024*1024/32;//32 groups per Mb of about 170 models of 192 bytes
    private final static int mGroupMaxModels = mGroupMaxSize/192;

    private final int[] mVAO = new int[1];
    private final VBOManager mVBO;

    private List<DrawGroup> mGroups = new ArrayList<>();

    private static DrawingManager mManager=null;

    private Model.onModelChangedListener mOnModelChangedListener = new Model.onModelChangedListener() {
        @Override
        public void onModelMatChanged(Model _this, Changed changed) {
            int groupID = _this.getDrawGroupID();
            if (groupID > 0) {
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
        int groupIndex = -1;

        for(DrawGroup group : mGroups){
            if(group.getQuantity() < mGroupMaxModels) {//If the group still has some space
                if(group.addModel(model, mVBO)) {

                    foundGroup = true;
                    groupIndex = mGroups.indexOf(group);

                    break;
                }
            }
        }

        //No DrawingGroup could contain this model. Create a new one just for it
        if(!foundGroup){
            DrawGroup n = new DrawGroup(model, mVBO, mGroups.size()*mGroupMaxSize/Util.FLOAT_SIZE);
            mGroups.add(n);
            groupIndex = mGroups.size()-1;
        }

        model.setDrawGroupID(groupIndex);
        model.setOnModelMatChangedListener(mOnModelChangedListener);
    }
    void removeModel(Model model){
        int index = model.getDrawGroupID();
        if(index > 0) {
            mGroups.get(index).removeModel(model, mVBO);
            model.setDrawGroupID(-1);
            model.setOnModelMatChangedListener(null);
        }

/*TODO If empty group, delete it
        if(mGroups.get(index).getQuantity() == 0) {
            //mGroups.remove(index);//Creates the problem that all the other groupID are shifted. Maybe shouldn't use groupID and go back to searching through the groups
            //Shift the VBO data to make space and substract group size to total size or keep track of empty pool
        }
*/
    }

    List<DrawGroup> getGroups(){ return mGroups; }

    void Draw(MVP mvp, long frameElapsedTime){
        GLES30.glBindVertexArray(mVAO[0]);//Not really necessary since it is never unbound, but yeah.

        //TODO Change this line if all models don't use the same shader
        //All models use the same shader, so it is OK to only bind it once
        Shader shader = mGroups.get(0).getModels().get(0).getShader();
        shader.Use();

        //Log.e("Size", "S: "+mGroups.size()+" ; F: "+frameElapsedTime);

        for(DrawGroup group : mGroups){
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
}
