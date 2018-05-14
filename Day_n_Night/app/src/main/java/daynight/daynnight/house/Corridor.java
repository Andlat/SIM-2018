package daynight.daynnight.house;

import android.content.Context;
import android.util.Pair;

import java.io.IOException;

import daynight.daynnight.engine.Model.Animation;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;

import static daynight.daynnight.engine.Model.StaticModel.Type.WALL_BOTTOM;
import static daynight.daynnight.engine.Model.StaticModel.Type.WALL_TOP;
import static daynight.daynnight.properties.ZIndex.Z_WALL;
import static daynight.daynnight.properties.ZIndex.Z_WALL_BOTTOM;
import static daynight.daynnight.house.House.FLOOR_STYLE;

/**
 * Created by andlat on 2018-05-12.
 */

class Corridor {
    private static final int HEIGHT = 3;

    public enum Axis {X, Y}

    static void Generate(final Context context, final World world, final int LENGTH, final Vec3 orgInMap, Axis axis) throws IOException {

        GenFloor(context, world, LENGTH, orgInMap, axis);

        if(axis == Axis.X)
            GenWallsX(context, world, LENGTH, orgInMap);
        else
            GenWallsY(context, world, LENGTH, orgInMap);
    }

    private static void GenFloor(final Context context, final World world, final int LENGTH, final Vec3 orgInMap, Axis axis) throws IOException{
        StaticModel floor_org = ObjParser.Parse(context, "models", "plancher.obj").get(0).toStaticModel();
        floor_org.setAnimation(new Animation().addFrame(new Pair<>(Texture.Load(context, FLOOR_STYLE), 0)));

        Vec3 l = new Vec3(), r = new Vec3();

        for(int y=HEIGHT-1; y >= -HEIGHT+1; y -= 2){
            for(int x=LENGTH-1; x > 0; x -= 2){

                StaticModel floor_l = new StaticModel(floor_org);
                StaticModel floor_r = new StaticModel(floor_org);

                float lx = -x, ly = y, rx=x, ry=y;
                if(axis == Axis.Y){
                    float tmp = lx; lx = -ly;   ly = -tmp;

                    tmp = rx;   rx = ry;    ry = -tmp;
                }


                l.x(lx + orgInMap.x()); l.y(ly + orgInMap.y());
                r.x(rx + orgInMap.x()); r.y(ry + orgInMap.y());


                floor_l.StaticTranslate(l);
                floor_r.StaticTranslate(r);

                world.addModel(floor_l);
                world.addModel(floor_r);
            }
        }
    }

    private static void GenWallsX(final Context context, final World world, final int LENGTH, final Vec3 orgInMap) throws IOException{
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
            world.setGroupZIndex(wall_t, Z_WALL);

            world.addModel(wall_b);
            world.setGroupZIndex(wall_b, Z_WALL_BOTTOM);
        }
    }

    private static void GenWallsY(final Context context, final World world, final int LENGTH, final Vec3 orgInMap) throws IOException{
        StaticModel wall_org = ObjParser.Parse(context, "models", "mur.obj").get(0).toStaticModel();
        wall_org.setType(WALL_TOP);

        final int x = HEIGHT+1;

        StaticModel wall_corner_tl = ObjParser.Parse(context, "models", "plafond.obj").get(0).toStaticModel();
        StaticModel wall_corner_tr = new StaticModel(wall_corner_tl);

        wall_corner_tl.StaticTranslate(new Vec3(-x-1, LENGTH-1 + orgInMap.y(), 0));
        wall_corner_tr.StaticTranslate(new Vec3(x-1, LENGTH-1 + orgInMap.y(), 0));

        world.addModel(wall_corner_tl);
        world.addModel(wall_corner_tr);
        world.setGroupZIndex(wall_corner_tr, Z_WALL_BOTTOM);


        for(int y=LENGTH-2; y >= -LENGTH+2; y -= 2){
            //Left side
            StaticModel wall_l = new StaticModel(wall_org);
            wall_l.StaticTranslate(new Vec3(-x + orgInMap.x(),  y + orgInMap.y(), 0));
            world.addModel(wall_l);

            //right side
            StaticModel wall_r = new StaticModel(wall_org);
            wall_r.StaticTranslate(new Vec3(x + orgInMap.x(),  y + orgInMap.y(), 0));
            world.addModel(wall_r);

            world.setGroupZIndex(wall_r, Z_WALL);
        }
    }
}
