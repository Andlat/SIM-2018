package daynight.daynnight.engine;

import android.opengl.GLES30;

import java.util.ArrayList;
import java.util.List;

import daynight.daynnight.engine.Model.Model;

/**
 * Created by andlat on 2018-05-04.
 */

class DrawingManager {
    private final int[] mVAO = new int[1];
    private final VBOManager mVBO;

    List<DrawGroup> mGroups = new ArrayList<>();

    private DrawingManager(){
        //One VertexArrayObject (VAO) per World
        GLES30.glGenVertexArrays(1, mVAO, 0);
        GLES30.glBindVertexArray(mVAO[0]);

        mVBO = new VBOManager(mVAO[0]);
    }

    private void addModel(Model model){
        for(DrawGroup group : mGroups){
            if(group.addModel(model))
                break;
        }
    }
    private void removeModel(Model model){
        
    }
}
