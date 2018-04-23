package daynight.daynnight.engine.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

/**
 * Created by andlat on 2018-04-01.
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
}
