package daynight.daynnight;

import android.content.Context;

import java.io.IOException;

import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;

/**
 * Created by andlat on 2018-05-11.
 */

class Room {
    static int Z_WALL_BOTTOM = 100, Z_WALL =1;

    /**
     * Generate a new room
     * @param context Context of activity
     * @param world World of Game
     * @param FLOOR_SIZE Width and Height of a square room (number of 2x2 tiles). HAS TO BE PAR
     * @throws IOException Couldn't read the .obj file
     */
    static void Generate(final Context context, final World world, final int FLOOR_SIZE) throws IOException {

        GenFloor(context, world, FLOOR_SIZE);

        //Generate the walls
        GenYWalls(context, world, FLOOR_SIZE);
        GenSideWalls(context, world, FLOOR_SIZE);
    }

    private static void GenFloor(final Context context, final World world, final int FLOOR_SIZE) throws IOException{
        StaticModel tile_org = ObjParser.Parse(context, "models", "plancher.obj").get(0).toStaticModel();

        //Generate the floor
        for(int y=FLOOR_SIZE; y > -FLOOR_SIZE; y -= 2) {
            //Generate in X
            for (int i = 0; i < FLOOR_SIZE; i += 2) {
                StaticModel tile_l = new StaticModel(tile_org);
                StaticModel tile_r = new StaticModel(tile_org);

                tile_l.StaticTranslate(new Vec3(-i - 1, y, 0));
                tile_r.StaticTranslate(new Vec3(i + 1, y, 0));

                world.addModel(tile_l);
                world.addModel(tile_r);
            }
        }
    }

    /**
     * Generate Top and Bottom walls of the room
     * @param context Context of activity
     * @param world World of Game
     * @param FLOOR_SIZE Width and Height of a square room (number of 2x2 tiles). HAS TO BE PAR
     * @throws IOException Couldn't read the .obj file
     */
    private static void GenYWalls(final Context context, final World world, final int FLOOR_SIZE) throws IOException{
        //Generate the walls
        StaticModel wall_t_org = ObjParser.Parse(context, "models", "mur.obj").get(0).toStaticModel();
        wall_t_org.setType(StaticModel.Type.WALL_TOP);

        StaticModel wall_b_org = ObjParser.Parse(context, "models", "mur_bas.obj").get(0).toStaticModel();
        wall_b_org.setType(StaticModel.Type.WALL_BOTTOM);

        //Generate the top and bottom walls
        final int y_top = FLOOR_SIZE + 3;
        final int y_bot = -FLOOR_SIZE + 1;
        for(int i=0; i < FLOOR_SIZE+2; i += 2){//+2 for the corners, which are outside the width/height of the floor

            /*----------- Top row -----------*/
            //Left side
            StaticModel wall_top_l = new StaticModel(wall_t_org);
            wall_top_l.StaticTranslate(new Vec3(-i-1, y_top, 0));
            world.addModel(wall_top_l);

            //Right side
            StaticModel wall_top_r = new StaticModel(wall_t_org);
            wall_top_r.StaticTranslate(new Vec3(i+1, y_top, 0));
            world.addModel(wall_top_r);

            world.setGroupZIndex(wall_top_r, Z_WALL);
            /*---------------------------------/

            /*----------- Bottom row -----------*/
            //Left side
            StaticModel wall_bot_l = new StaticModel(wall_b_org);
            wall_bot_l.StaticTranslate(new Vec3(-i-1, y_bot, 0));
            world.addModel(wall_bot_l);

            //Right side
            StaticModel wall_bot_r = new StaticModel(wall_b_org);
            wall_bot_r.StaticTranslate(new Vec3(i+1, y_bot, 0));
            world.addModel(wall_bot_r);


            world.setGroupZIndex(wall_bot_r, Z_WALL_BOTTOM);
            /*------------------------------------*/
        }
    }

    /**
     * Generate the Side walls of the room
     * @param context Context of activity
     * @param world World of Game
     * @param FLOOR_SIZE Width and Height of a square room (number of 2x2 tiles). HAS TO BE PAR
     * @throws IOException Couldn't read the .obj file
     */
    private static void GenSideWalls(final Context context, final World world, final int FLOOR_SIZE) throws IOException{
        /*----------- Side columns -----------*/
        StaticModel wall_org = ObjParser.Parse(context, "models", "mur.obj").get(0).toStaticModel();
        wall_org.setType(StaticModel.Type.WALL_TOP);

        for(int y=FLOOR_SIZE; y > -FLOOR_SIZE; y -= 2) {
            //Left side
            StaticModel wall_side_l = new StaticModel(wall_org);
            wall_side_l.StaticTranslate(new Vec3(-FLOOR_SIZE-1, y+1, 0));
            world.addModel(wall_side_l);

            //Right Side
            StaticModel wall_side_r = new StaticModel(wall_org);
            wall_side_r.StaticTranslate(new Vec3(FLOOR_SIZE+1, y+1, 0));
            world.addModel(wall_side_r);
        }
        /*------------------------------------*/
    }
}
