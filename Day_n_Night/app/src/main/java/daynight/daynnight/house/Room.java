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

import static daynight.daynnight.properties.ZIndex.Z_WALL;
import static daynight.daynnight.properties.ZIndex.Z_WALL_BOTTOM;
import static daynight.daynnight.house.House.FLOOR_STYLE;

/**
 * Created by andlat on 2018-05-11.
 */

class Room {
    //Flags for passages. 1 passage per side for now.
    static int  P_NONE = 1,
                P_UP = 1 << 1,
                P_DWN = 1 << 2,
                P_LFT = 1 << 3,
                P_RGHT = 1 << 4;


    /**
     * Generate a new room
     * @param context Context of activity
     * @param world World of Game
     * @param FLOOR_SIZE Width and Height of a square room (number of 2x2 tiles). HAS TO BE PAR
     * @param orgInMap Center of the room in map coordinates
     * @throws IOException Couldn't read the .obj file
     */
    static void Generate(final Context context, final World world, final int FLOOR_SIZE, final Vec3 orgInMap) throws IOException {
        Generate(context, world, FLOOR_SIZE, orgInMap, P_NONE);
    }

    /**
     * Generate a new room
     * @param context Context of activity
     * @param world World of Game
     * @param FLOOR_SIZE Width and Height of a square room (number of 2x2 tiles). HAS TO BE PAR
     * @param orgInMap Center of the room in map coordinates
     * @param passages Where to put corridors
     * @throws IOException Couldn't read the .obj file
     */
    static void Generate(final Context context, final World world, final int FLOOR_SIZE, final Vec3 orgInMap, final int passages) throws IOException {
        GenFloor(context, world, FLOOR_SIZE, orgInMap);

        //Generate the walls
        GenYWalls(context, world, FLOOR_SIZE, orgInMap, passages);
        GenSideWalls(context, world, FLOOR_SIZE, orgInMap, passages);
    }

    /**
     * Generate a the floor of the room
     * @param context Context of activity
     * @param world World of Game
     * @param FLOOR_SIZE Width and Height of a square room (number of 2x2 tiles). HAS TO BE PAR
     * @param orgInMap Center of the room in map coordinates
     * @throws IOException Couldn't read the .obj file
     */
    private static void GenFloor(final Context context, final World world, final int FLOOR_SIZE, final Vec3 orgInMap) throws IOException{
        StaticModel tile_org = ObjParser.Parse(context, "models", "plancher.obj").get(0).toStaticModel();
        tile_org.setAnimation(new Animation().addFrame(new Pair<>(Texture.Load(context, FLOOR_STYLE), 0)));

        Vec3 l = new Vec3(), r = new Vec3();
        //Generate the floor
        for(int y=FLOOR_SIZE-1; y >= -FLOOR_SIZE+1; y -= 2) {
            //Generate in X
            for (int i = 0; i < FLOOR_SIZE; i += 2) {
                StaticModel tile_l = new StaticModel(tile_org);
                StaticModel tile_r = new StaticModel(tile_org);

                //Set the positions
                l.x(-i-1 + orgInMap.x());  l.y(y + orgInMap.y());
                r.x(i+1 + orgInMap.x()); r.y(y + orgInMap.y());

                //Translate the tiles
                tile_l.StaticTranslate(l);
                tile_r.StaticTranslate(r);

                //Add the tiles to the world
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
     * @param orgInMap Center of the room in map coordinates
     * @param passages Where to put corridors
     * @throws IOException Couldn't read the .obj file
     */
    private static void GenYWalls(final Context context, final World world, final int FLOOR_SIZE, final Vec3 orgInMap, final int passages) throws IOException{
        //Generate the walls
        StaticModel wall_t_org = ObjParser.Parse(context, "models", "mur.obj").get(0).toStaticModel();
        wall_t_org.setType(StaticModel.Type.WALL_TOP);

        StaticModel wall_b_org = ObjParser.Parse(context, "models", "mur_bas.obj").get(0).toStaticModel();
        wall_b_org.setType(StaticModel.Type.WALL_BOTTOM);

        boolean p_up = (passages & P_UP) == P_UP;
        boolean p_dwn = (passages & P_DWN) == P_DWN;

        Vec3 l = new Vec3(), r = new Vec3();

        //Generate the top and bottom walls
        final int y_top = FLOOR_SIZE + 2;
        final int y_bot = -FLOOR_SIZE;
        for(int i=0; i < FLOOR_SIZE+2; i += 2){//+2 for the corners, which are outside the width/height of the floor

            /*----------- Top row -----------*/
            //Left side
            l.x(-i-1 + orgInMap.x());  l.y(y_top + orgInMap.y());

            if(!p_up || l.x() < -3 || l.x() > -1) {//Skip the wall generation if needed a hole for a passage
                StaticModel wall_top_l = new StaticModel(wall_t_org);
                wall_top_l.StaticTranslate(l);
                world.addModel(wall_top_l);
            }

            //Right side
            r.x(i+1 + orgInMap.x()); r.y(y_top + orgInMap.y());

            if(!p_up || r.x() != 1) {//Skip the wall generation if needed a hole for a passage
                StaticModel wall_top_r = new StaticModel(wall_t_org);
                wall_top_r.StaticTranslate(r);
                world.addModel(wall_top_r);

                world.setGroupZIndex(wall_top_r, Z_WALL);
            }
            /*---------------------------------/

            /*----------- Bottom row -----------*/
            //Left side
            l.x(-i-1 + orgInMap.x());  l.y(y_bot + orgInMap.y());

            if(!p_dwn || l.x() < -3 || l.x() > -1) {//Skip the wall generation if needed a hole for a passage
                StaticModel wall_bot_l = new StaticModel(wall_b_org);
                wall_bot_l.StaticTranslate(l);
                world.addModel(wall_bot_l);
            }

            //Right side
            r.x(i+1 + orgInMap.x()); r.y(y_bot + orgInMap.y());

            if(!p_dwn || r.x() != 1) {//Skip the wall generation if needed a hole for a passage
                StaticModel wall_bot_r = new StaticModel(wall_b_org);
                wall_bot_r.StaticTranslate(r);
                world.addModel(wall_bot_r);

                world.setGroupZIndex(wall_bot_r, Z_WALL_BOTTOM);
            }
            /*------------------------------------*/
        }
    }

    /**
     * Generate the Side walls of the room
     * @param context Context of activity
     * @param world World of Game
     * @param FLOOR_SIZE Width and Height of a square room (number of 2x2 tiles). HAS TO BE PAR
     * @param orgInMap Center of the room in map coordinates
     * @param passages Where to put corridors
     * @throws IOException Couldn't read the .obj file
     */
    private static void GenSideWalls(final Context context, final World world, final int FLOOR_SIZE, final Vec3 orgInMap, final int passages) throws IOException{
        /*----------- Side columns -----------*/
        StaticModel wall_org = ObjParser.Parse(context, "models", "mur.obj").get(0).toStaticModel();
        wall_org.setType(StaticModel.Type.WALL_TOP);

        StaticModel wall_pb_org = ObjParser.Parse(context, "models", "plafond.obj").get(0).toStaticModel();

        final boolean p_lft = (passages & P_LFT) == P_LFT;
        final boolean p_rght = (passages & P_RGHT) == P_RGHT;

        final int top_range = 2, low_range = -3;//Set up the passage hole in the middle of the wall

        Vec3 l = new Vec3(), r = new Vec3();
        for(int y=FLOOR_SIZE; y >= -FLOOR_SIZE+2; y -= 2) {//Shift the y coord by +1, so the side walls will link to the top/bottom walls

            //----- Left side -----\\
            if(!p_lft || y > top_range || y < low_range) {//Skip when needed to make an opening of 3 tiles

                l.x(-FLOOR_SIZE - 1 + orgInMap.x());    l.y(y + orgInMap.y());

                //Create the wall
                StaticModel wall_side_l;

                if(p_lft && y == low_range-1) {//Bottom wall of the passage hole. (Needs to act like a bottom wall but top wall have to be linked to hit. This causes a problems with the z-index, so this solution is used.
                    wall_side_l = new StaticModel(wall_pb_org);
                    l.y(l.y()+1);
                }else {
                    wall_side_l = new StaticModel(wall_org);
                }

                //Translate the wall to the appropriate place and add it to the world
                wall_side_l.StaticTranslate(l);
                world.addModel(wall_side_l);

                //Set the Z-Index for the bottom wall of the opening
                if(p_lft && y==low_range-1)
                    world.setGroupZIndex(wall_side_l, Z_WALL_BOTTOM);
            }

            //----- Right Side -----\\
            if(!p_rght || y > top_range || y < low_range) {//Skip when needed to make an opening of 3 tiles

                r.x(FLOOR_SIZE + 1 + orgInMap.x()); r.y(y + orgInMap.y());

                //Create the wall
                StaticModel wall_side_r;
                if(p_rght && y == low_range-1) {//Bottom wall of the passage hole. (Needs to act like a bottom wall but top wall have to be linked to hit. This causes a problems with the z-index, so this solution is used.
                    wall_side_r = new StaticModel(wall_pb_org);
                    r.y(r.y()+1);
                }else{
                    wall_side_r = new StaticModel(wall_org);
                }

                wall_side_r.StaticTranslate(r);
                world.addModel(wall_side_r);

                //Set the Z-Index for the bottom wall of the opening
                if(p_rght && y==low_range-1)
                    world.setGroupZIndex(wall_side_r, Z_WALL_BOTTOM);
            }
        }
        /*------------------------------------*/
    }
}
