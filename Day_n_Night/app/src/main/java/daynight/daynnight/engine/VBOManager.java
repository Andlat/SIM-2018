package daynight.daynnight.engine;

import android.opengl.GLES30;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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

    private long mDataSizeInBytes = 0;
    private int mVBOSizeInMB = 1;

    VBOManager(){
        mVBOData = allocateDirect(1);
        CreateVBO();
    }

    int addData(FloatBuffer data){
        int count = data.remaining();
        long dataSize = count * 4;

        CheckSizeForIncrease(dataSize);

        mDataSizeInBytes += dataSize;

        mVBOData.put(data);

        return mVBOData.position() - count;
    }

    /**
     * Remove data from the buffer
     * @param offset Starting offset of the data in the FloatBuffer. Returned by {@link #addData}
     * @param floatCount Number of floats to be removed
     */
    void removeData(int offset, int floatCount){
        //TODO When drawing the VBO, skip the removed parts. For that, keep track of removed offsets and removed parts' size in bytes.
    }

    private void CreateVBO(){
        GLES30.glGenBuffers(1, mVBO, 0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBO[0]);

        mVBOData.position(0);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, mVBOData.capacity()*4, mVBOData, GLES30.GL_DYNAMIC_DRAW);
    }

    private void CheckSizeForIncrease(long newDataSizeInBytes){
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
        ++mVBOSizeInMB;
        try {
            AllocateTransferData(mVBOSizeInMB, 0);
        }catch(BufferOverflowException ex){
            IncreaseDataSize();//Recurse so mVBOSizeInMB will increase, and then try copying data again.
        }
    }

    private void AllocateTransferData(int allocateInMB, int dataOffset) throws BufferOverflowException{
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

    FloatBuffer allocateDirect(int sizeInMB){
        return ByteBuffer.allocateDirect(sizeInMB * 1024 * 1024).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }
}
