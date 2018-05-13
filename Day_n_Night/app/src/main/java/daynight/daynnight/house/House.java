package daynight.daynnight.house;

import android.content.Context;

import java.io.IOException;

import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;

/**
 * Created by andlat on 2018-05-13.
 */

public class House {
    public static void Generate(Context context, World world) throws IOException{
        Room.Generate(context, world, 16, new Vec3(), Room.P_LFT| Room.P_RGHT| Room.P_UP| Room.P_DWN);

        Corridor.Generate(context, world, 4, new Vec3(-20, -1, 0), Corridor.Axis.X);
        Room.Generate(context, world, 8, new Vec3(-32, 0, 0), Room.P_RGHT);

        Corridor.Generate(context, world, 8, new Vec3(24, -1, 0), Corridor.Axis.X);
        Room.Generate(context, world, 10, new Vec3(40, 0, 0), Room.P_LFT);

        Corridor.Generate(context, world, 8, new Vec3(-1, 24, 0), Corridor.Axis.Y);
        Room.Generate(context, world, 4, new Vec3(0, 36, 0), Room.P_DWN);

        Corridor.Generate(context, world, 8, new Vec3(-1, -24, 0), Corridor.Axis.Y);
        Room.Generate(context, world, 6, new Vec3(0, -38, 0), Room.P_UP);
    }

    public static void GenRandom(Context context, World world) throws IOException{
        //TODO Random generation of the house
        Generate(context, world);
    }
}
