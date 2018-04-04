package daynight.daynnight.engine.util;

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
}
