package daynight.daynnight.engine;

import java.util.ArrayList;
import java.util.List;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.math.Mat4;

/**
 * Created by Nikola Zelovic on 2018-04-29.
 */

class DrawGroup {
    private List<Model> mList = new ArrayList<>();
    private Texture mTexture;
    private Mat4 mModelMatrix;

    private int mVBOOffset=-1, mSize=0;

    static class WrongGroupException extends Exception{
        WrongGroupException(){ super("Model has a model matrix or a Texture that is not the same as this group's"); }
        WrongGroupException(String msg){ super(msg); }
    }

    DrawGroup(){}
    DrawGroup(Model first){
        initGroup(first);
    }
    DrawGroup(List<Model> models) throws WrongGroupException{
        initGroup(models.get(0));

        final int size = models.size();
        for(int i=1; i < size; ++i ){
            this.addModel(models.get(i));
        }
    }

    private void initGroup(Model first){
        mList.add(first);
        mSize += first.getVBO().capacity();

        mTexture = first.getOrgTexture();
        mModelMatrix = first.getModelMatrix();
    }

    boolean addModel(Model model) {
        if(mList.isEmpty()){
            initGroup(model);
        }else{
            //Check if texture and model matrix is the same as the group's
            if(isGroup(model)) {
                mList.add(model);
                mSize += model.getVBO().capacity();
            }else{
                return false;
            }
        }
        return true;
    }

    boolean isGroup(Model model){
        return model.getOrgTexture() == mTexture && model.getModelMatrix() == mModelMatrix;
    }

    int getSize(){ return mSize; }
    void setVBOOffset(int offset){ mVBOOffset = offset; }
    int getVBOOffset(){ return mVBOOffset; }
}
