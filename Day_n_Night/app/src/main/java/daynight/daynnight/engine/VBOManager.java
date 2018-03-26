package daynight.daynnight.engine;

import android.opengl.GLES30;
import android.util.Log;
import android.util.Pair;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by andlat on 2018-02-18.
 */

/**
 * VBO: Vertex Buffer Object
 *
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

    //TODO Look for the offsets in the models and set them to the attribs. For now, this is done manually. Potential problem: All models in a VBOManager must have the same offsets.
    private final static int VERTEX_ATTRIB = 0, UV_ATTRIB = 1, NORMAL_ATTRIB = 2;

    /**
     * List<Pair<Offset in VBO, Data size in bytes>>
     */
    private final List<Pair<Integer, Integer>> mEmptyVBOPools = new ArrayList<>();

    VBOManager(){
        mVBOData = allocateDirect(mVBOSizeInMB);

        CreateVBO();
    }

    private void WriteToVBO(int floatBufferOffset, int dataSize){
        //Bind VBO in case it was unbound. (Maybe another vbo was bound outside of the engine)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBO[0]);

        mVBOData.position(floatBufferOffset);
        GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, floatBufferOffset*4, dataSize, mVBOData);
    }

    /**
     * Ajouter un modèle dans le VBO
     * @param data Données du modèle
     * @return Offset des données du modèle dans le VBO global
     */
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

    //TODO Use mVBOData instead and write to the GPU buffer only once.
    /**
     * Do not use this function for now.
     *
     * Shift data in the gpu as to fill the empty pools. Shouldn't really be used because it is kinda slow, unless memory usage is too high.
     */
    private void ShiftDataGPU(){
        this.SortOrderEmptyPoolsArray();

        int subIndex = 0;//glSubDataIndex
        int dataIndex = 0;//mVBOData index

        for(Pair<Integer, Integer> pool : mEmptyVBOPools){//Pair<Offset in VBO, Data size in bytes>
            final int poolIndex = pool.first, poolSize = pool.second;
            if(dataIndex < poolIndex) {
                final int indexDiff = poolIndex - dataIndex;
                final int writeDataSize = indexDiff*4;

                mVBOData.position(dataIndex);
                GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, subIndex, writeDataSize, mVBOData);

                subIndex += writeDataSize;

            }

            dataIndex = poolIndex + poolSize/4;
        }

        mEmptyVBOPools.clear();
    }

    private void CreateVBO(){
        GLES30.glGenBuffers(1, mVBO, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBO[0]);

        mVBOData.position(0);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, mVBOData.capacity()*4, mVBOData, GLES30.GL_DYNAMIC_DRAW);

        CreateAttribs();
    }

    //TODO Look for the offsets in the models and set them to the attribs. For now, this is done manually. Potential problem: All models in a VBOManager must have the same offsets.
    private void CreateAttribs(){
        GLES30.glVertexAttribPointer(VERTEX_ATTRIB, 3, GLES30.GL_FLOAT, false, 3*4, 0);
        ActivateAttribs();//TODO Remove this and put it somewhere where it makes sense. This is only temporary for debugging.
    }

    void ActivateAttribs(){
        GLES30.glEnableVertexAttribArray(VERTEX_ATTRIB);
    }
    //TODO Make a DisableAttribs function with glDisableVertexAttrib calls.

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

    private FloatBuffer allocateDirect(float sizeInMB){
        return ByteBuffer.allocateDirect((int)(sizeInMB * 1024 * 1024)).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    /**
     * Order empty pools of memory by index
     */
    private void SortOrderEmptyPoolsArray(){
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

    /**
     * Get a list containing the offsets and size of vertices to draw (visible vertices)
     * @return A list containing pairs of the offset in the VBO and size of sequential vertices (a model). List<Pair<Offset in VBO, Size in bytes>>
     */
    List<Pair<Integer, Integer>> getDrawOffsets(){

        Log.e("TRIANGLE VBO", mVBOData.get(0) + ", " + mVBOData.get(1) + ", " +  mVBOData.get(2) + ", " +  mVBOData.get(3) + ", " +  mVBOData.get(4) + ", " +  mVBOData.get(5) + ", " +  mVBOData.get(6) + ", " +  mVBOData.get(7) + ", " +  mVBOData.get(8));
        Log.e("TRIANGLE VBO", mVBOData.get(9) + ", " + mVBOData.get(10) + ", " +  mVBOData.get(11) + ", " +  mVBOData.get(12) + ", " +  mVBOData.get(13));
        List<Pair<Integer, Integer>> drawOffsets = new ArrayList<>();

        //TODO Repair this loop. Will not work if there is no empty pools
        int offset=0, sizeInBytes;
        for(Pair<Integer, Integer> empty : mEmptyVBOPools){
            sizeInBytes = empty.first - offset;

            drawOffsets.add(new Pair<>(offset, sizeInBytes));

            offset = empty.first + empty.second;
        }

        Log.e("LIST DRAW OFFSETS", drawOffsets.toString());
        drawOffsets.add(new Pair<>(0, 36));//TODO Remove this. This is for testing
        return drawOffsets;
    }
}
