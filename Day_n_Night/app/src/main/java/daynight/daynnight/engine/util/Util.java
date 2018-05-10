package daynight.daynnight.engine.util;

import android.util.LongSparseArray;
import android.util.SparseArray;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nikola Zelovic on 2018-04-01.
 */

public final class Util {
    public static final int FLOAT_SIZE = 4;

    public static float[] FloatListToArray(final List<Float> list){
        final int size = list.size();
        float[] array = new float[size];

        for(int i=0; i < size; ++i){
            array[i] = list.get(i);
        }

        return array;
    }

    public static FloatBuffer CloneBuffer(FloatBuffer src){
        FloatBuffer clone = ByteBuffer.allocateDirect(src.capacity() * FLOAT_SIZE).order(ByteOrder.nativeOrder()).asFloatBuffer();

        src.position(0);
        clone.put(src);

        //Reset the positions
        src.position(0);
        clone.position(0);

        return clone;
    }

    public static <T> List<T> SparseArrayToArrayList(SparseArray<T> array){
        List<T> list = new ArrayList<>();

        final int size = array.size();
        for(int i=0; i < size; ++i){
            list.add(array.valueAt(i));
        }
        return list;
    }
    public static <T> List<T> LongSparseArrayToArrayList(LongSparseArray<T> array){
        List<T> list = new ArrayList<>();

        final int size = array.size();
        for(int i=0; i < size; ++i){
            list.add(array.valueAt(i));
        }
        return list;
    }

    private String getFloatBufferString(FloatBuffer buf){
        StringBuilder sb = new StringBuilder("Buffer{");
        buf.position(0);
        for(int i=0; i < buf.capacity(); ++i){
            sb.append(buf.get(i)).append(", ");
        }
        sb.append("}");
        return sb.toString();
    }
}
