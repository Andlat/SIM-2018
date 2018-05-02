package daynight.daynnight.engine;

import java.util.ArrayList;
import java.util.List;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.math.Mat4;

/**
 * Created by andlat on 2018-04-29.
 */

class DrawGroup {
    private List<Model> mList = new ArrayList<>();
    private Texture mTexture;
    private Mat4 mModelMatrix;

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
        mTexture = first.getOrgTexture();
        mModelMatrix = first.getModelMatrix();
    }

    void addModel(Model model) throws WrongGroupException {
        if(mList.isEmpty()){
            initGroup(model);
        }else{
            //Check if texture and model matrix is the same as the group's
            if(model.getOrgTexture() != mTexture || model.getModelMatrix() != mModelMatrix)
                throw new WrongGroupException();
            else
                mList.add(model);
        }
    }
}
