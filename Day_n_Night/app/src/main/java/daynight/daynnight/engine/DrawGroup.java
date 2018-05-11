package daynight.daynnight.engine;

import android.util.Log;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import daynight.daynnight.engine.Model.Animation;
import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.math.Mat4;

/**
 * Created by Nikola Zelovic on 2018-04-29.
 */

class DrawGroup {
    private List<Model> mList = new ArrayList<>();
    private Animation mAnimation;
    private Mat4 mModelMatrix;

    private int mVBOOffsetFloat=-1, mSizeFloat =0;

    private static int mNxtID = 0;
    private final int mID;

    static class WrongGroupException extends Exception{
        WrongGroupException(){ super("Model has a model matrix or a Texture that is not the same as this group's"); }
        WrongGroupException(String msg){ super(msg); }
    }

    DrawGroup(){
        mID = mNxtID;
        ++mNxtID;
    }
    DrawGroup(Model first, VBOManager vbo, int vboOffset){
        this();
        this.setVBOOffset(vboOffset);
        initGroup(first, vbo);
    }
    DrawGroup(List<Model> models, VBOManager vbo, int vboOffset) throws WrongGroupException{
        this();
        this.setVBOOffset(vboOffset);
        initGroup(models.get(0), vbo);

        final int size = models.size();
        for(int i=1; i < size; ++i ){
            this.addModel(models.get(i), vbo);
        }
    }

    private void initGroup(Model first, VBOManager vbo){
        this.addVBO(first, vbo);

        mAnimation = first.getAnimation();
        mModelMatrix = first.getModelMatrix();
    }

    boolean addModel(Model model, VBOManager vbo) {
        if(mList.isEmpty()){
            initGroup(model, vbo);
        }else{
            //Check if texture and model matrix is the same as the group's
            if(isGroup(model)) {
                this.addVBO(model, vbo);
            }else{
                return false;
            }
        }
        return true;
    }
    private void addVBO(Model model, VBOManager vbo){
        mList.add(model);

        //The order of the 2 following calls is important because of the variable mSizeFloat
        int offset = mVBOOffsetFloat + mSizeFloat;
        vbo.addData(model.getVBO(), offset);
        mSizeFloat += model.getVBO().capacity();

        model.setVBOWorldOffset(offset);
    }

    //TODO Verify this function.
    boolean removeModel(Model model, VBOManager vbo){
        int index = mList.indexOf(model);
        if(index == -1) return false;

        if(index < mList.size() -1){//If not last element in the list. If it is, it is not necessary to shift the data.
            //Shift all the data of this group in the VBO. Now, the data is sequential and can all be drawn with only 1 draw call.
            int vboOffset = (int)model.getVBOWorldOffset();

            for(int i = index+1; i < mList.size(); ++i){
                final FloatBuffer data = mList.get(i).getVBO();

                vbo.addData(data, vboOffset);
                vboOffset += data.capacity();
            }
        }

        mSizeFloat -= model.getVBO().capacity();
        mList.remove(model);

        return true;
    }

    boolean isGroup(Model model){
        return model.getAnimation().equals(mAnimation) && model.getModelMatrix().equals(mModelMatrix);
    }

    List<Model> getModels(){ return mList; }
    int getQuantity(){return mList.size();}

    int getSizeInFloat(){ return mSizeFloat; }

    void setVBOOffset(int offset){ mVBOOffsetFloat = offset; }
    int getVBOOffset(){ return mVBOOffsetFloat; }

    Animation getAnimation(){ return mAnimation; }
    void setAnimation(Animation animation){ mAnimation = animation; }

    Mat4 getModelMatrix(){ return mModelMatrix; }
    void setModelMatrix(Mat4 modelMat){ mModelMatrix = modelMat; }

    int getID(){ return mID; }
}
