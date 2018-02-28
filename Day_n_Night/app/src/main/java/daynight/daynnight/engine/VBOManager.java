package daynight.daynnight.engine;

import android.opengl.GLES30;
import android.util.Pair;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by andlat on 2018-02-18.
 */

/**
 * S'occuper de gérer les données dans la mémoire du GPU. N'utilise qu'un VBO à la fois.
 * Cela peut être plus lent pour afficher les graphiques s'il y a beaucoup de changement de modèles,
 * mais ça utilise moins de mémoire que s'il procédait à 2 VBO
 * (VBO swap: 1 modifie les données pendant que l'autre affiche et puis s'inversent à la prochaine frame).
 */
class VBOManager {
    private FloatBuffer mVBOData;

    private int[] mVBO = new int[1];

    private int mDataSizeInBytes = 0;
    private float mVBOSizeInMB = .5f;
    private final static float VBO_SIZE_JUMP_MB = .5f;

    private final List<Pair<Integer, Integer>> mEmptyVBOPools = new ArrayList<>();//Pair<Offset in VBO, Data size in bytes>

    VBOManager(){
        mVBOData = allocateDirect(1);
        CreateVBO();
    }

    private void WriteToVBO(int floatBufferOffset, int dataSize){
        //Bind VBO in case it was unbound. (Maybe another vbo was bound outside of the engine)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBO[0]);

        mVBOData.position(floatBufferOffset);
        GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, floatBufferOffset*4, dataSize, mVBOData);
    }

    int addData(FloatBuffer data){
        int count = data.remaining();
        int dataSize = count * 4;

        CheckSizeForIncrease(dataSize);
        mDataSizeInBytes += dataSize;

        mVBOData.put(data);
        int offset = mVBOData.position() - count;

        WriteToVBO(offset, dataSize);

        return offset;
    }

    /**
     * Remove data from the buffer
     * @param floatBufferOffset Starting offset of the data in the FloatBuffer. Returned by {@link #addData}
     * @param floatCount Number of floats to be removed
     */
    void removeData(int floatBufferOffset, int floatCount){
        //TODO When drawing the VBO, skip the removed parts. For that, keep track of removed offsets and removed parts' size in bytes.
        int dataSize = floatCount * 4, VBOOffset = floatBufferOffset * 4;

        mEmptyVBOPools.add(new Pair<>(VBOOffset, dataSize));
    }


    //Shift data as to fill the empty pools. Shouldn't really be used because it is kinda slow, unless memory is too high.
    private void ShiftDataGPU(){
        this.SortOrderEmptyPoolsArray();

        for(int i = mEmptyVBOPools.size(); i > 0; --i){
            mVBOData.position(mEmptyVBOPools.get(mEmptyVBOPools.size()).first);
            for(int j=0; j < mEmptyVBOPools.get(i).second; ++j){

            }
        }
    }

    private void CreateVBO(){
        GLES30.glGenBuffers(1, mVBO, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBO[0]);

        mVBOData.position(0);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, mVBOData.capacity()*4, mVBOData, GLES30.GL_DYNAMIC_DRAW);
    }

    private void CheckSizeForIncrease(int newDataSizeInBytes){
        if(mDataSizeInBytes + newDataSizeInBytes > mVBOData.capacity()*4) {
            IncreaseVBO();
        }
    }

    private void IncreaseVBO(){
        GLES30.glDeleteBuffers(1, mVBO, 0);

        IncreaseDataSize();

        CreateVBO();
    }

    private void IncreaseDataSize(){
        mVBOSizeInMB += VBO_SIZE_JUMP_MB;
        try {
            AllocateTransferData(mVBOSizeInMB, 0);
        }catch(BufferOverflowException ex){
            IncreaseDataSize();//Recurse so mVBOSizeInMB will increase, and then try copying data again.
        }
    }

    private void AllocateTransferData(float allocateInMB, int dataOffset) throws BufferOverflowException{
        FloatBuffer buff = this.allocateDirect(allocateInMB);
        mVBOData.position(dataOffset);

        //Throw exception if new buffer doesn't have enough space for the old data.
        if(buff.capacity() < mVBOData.remaining())
            throw new BufferOverflowException();
        else{
            buff.put(mVBOData);
            mVBOData = buff;
        }
    }

    FloatBuffer allocateDirect(float sizeInMB){
        return ByteBuffer.allocateDirect((int)(sizeInMB * 1024 * 1024)).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    /**
     * Order empty pools of memory by index
     */
    void SortOrderEmptyPoolsArray(){
        final int size = mEmptyVBOPools.size();
        Pair<Integer, Integer> h, k;
        for(int i=0; i < size; ++i){
            h = mEmptyVBOPools.get(i);
            for(int j=0; j < size; ++j){
                k = mEmptyVBOPools.get(j);

                if(k.first > h.first)
                    Collections.swap(mEmptyVBOPools, i, j);
            }
        }
    }
}
