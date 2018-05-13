package daynight.daynnight;

import android.content.Context;

import java.io.IOException;

import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;

import static daynight.daynnight.engine.Model.StaticModel.Type.WALL_BOTTOM;
import static daynight.daynnight.engine.Model.StaticModel.Type.WALL_TOP;

/**
 * Created by andlat on 2018-05-12.
 */

class Corridor {
    private static final int HEIGHT = 3;

    static void Generate(final Context context, final World world, final int LENGTH, final Vec3 orgInMap) throws IOException {
        GenFloor(context, world, LENGTH, orgInMap);
        GenWalls(context, world, LENGTH, orgInMap);
    }

    private static void GenFloor(final Context context, final World world, final int LENGTH, final Vec3 orgInMap) throws IOException{
        StaticModel floor_org = ObjParser.Parse(context, "models", "plancher.obj").get(0).toStaticModel();

        Vec3 l = new Vec3(), r = new Vec3();

        for(int y=HEIGHT-1; y >= -HEIGHT+1; y -= 2){
            for(int x=LENGTH-1; x > 0; x -= 2){

                StaticModel floor_l = new StaticModel(floor_org);
                StaticModel floor_r = new StaticModel(floor_org);

                l.x(-x + orgInMap.x()); l.y(y + orgInMap.y());
                r.x(x + orgInMap.x()); r.y(y + orgInMap.y());

                floor_l.StaticTranslate(l);
                floor_r.StaticTranslate(r);

                world.addModel(floor_l);
                world.addModel(floor_r);
            }
        }
    }

    private static void GenWalls(final Context context, final World world, final int LENGTH, final Vec3 orgInMap) throws IOException{
        StaticModel wall_t_org = ObjParser.Parse(context, "models", "mur.obj").get(0).toStaticModel();
        wall_t_org.setType(WALL_TOP);

        StaticModel wall_b_org = ObjParser.Parse(context, "models", "mur_bas.obj").get(0).toStaticModel();
        wall_b_org.setType(WALL_BOTTOM);

        for(int x=LENGTH-3; x > -LENGTH+2; x -= 2){//Don't draw the extremities
            StaticModel wall_t = new StaticModel(wall_t_org);
            wall_t.StaticTranslate(new Vec3(x+orgInMap.x(),  HEIGHT+2 + orgInMap.y(), 0));

            StaticModel wall_b = new StaticModel(wall_b_org);
            wall_b.StaticTranslate(new Vec3(x+orgInMap.x(),  -HEIGHT + orgInMap.y(), 0));

            world.addModel(wall_t);
            world.setGroupZIndex(wall_t, Room.Z_WALL);

            world.addModel(wall_b);
            world.setGroupZIndex(wall_b, Room.Z_WALL_BOTTOM);
        }
    }
}
