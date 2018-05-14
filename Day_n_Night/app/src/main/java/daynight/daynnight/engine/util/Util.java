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
    public static final float PI = 3.141593f;

    public static float Clamp(float value, float min, float max){
        if(value < min){
            value = min;
        }else if(value > max){
            value = max;
        }

        return value;
    }

    public static float RadToDeg(float rad){
        return rad * 360.f / (2.f * PI);
    }
    public static float dirToDeg(float x, float y){
        float theta = Util.RadToDeg((float) Math.atan2(y, x));
        if(theta < 0) theta = 360+theta;//atan2 correction (atan2 returns negatives for [PI, 2PI]. (E.g. 3PI/4 = -PI/4)

        return theta;
    }

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

    /*TODO Remove these. The conversion takes too much time (tested for the long sparse array. Have to test for the int too)
    public static <T> List<T> SparseArrayToArrayList(SparseArray<T> array){
        List<T> list = new ArrayList<>();

        final int size = array.size();
        for(int i=0; i < size; ++i){
            list.add(array.valueAt(i));
        }
        return list;
    }
    public static <T> List<T> LongSparseArrayToArrayList(LongSparseArray<T> array){
        final List<T> list = new ArrayList<>();

        final int size = array.size();
        for(int i=0; i < size; ++i){
            list.add(array.valueAt(i));
        }
        return list;
    }
    */

    public static <T> int getKeyFromSparseArrayElmnt(SparseArray<T> array, T elmnt) {
        final int size = array.size();
        for (int i = 0; i < size; ++i) {
            if (array.valueAt(i) == elmnt)
                return array.keyAt(i);
        }
        return -1;
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
