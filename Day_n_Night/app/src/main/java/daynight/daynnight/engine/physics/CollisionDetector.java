package daynight.daynnight.engine.physics;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.util.Coord;

/**
 * Created by Nikola Zelovic on 2018-04-23.
 */

public class CollisionDetector {
    /**
     * Detects collisions between models. A MovingModel can collide with another MovingModel or a StaticModel of type BLOCK
     * Deprecated. Use {@link CollisionDetector#Detect2(List, List) }. This one is subject to removal
     * @param movingModels List of moving models to verify for
     * @param models All models to verify against
     */
    public static List<Pair<MovingModel, Model>> Detect(List<MovingModel> movingModels, List<Model> models){
        List<Pair<MovingModel, Model>> collisions = new ArrayList<>();

        Log.e("CD Q", "QMM: " + movingModels.size() + ", QMO: " + models.size());

        for(MovingModel moving : movingModels){
            for(Model model : models){
                if(moving == model)//Skip if checking collision against themselves
                    continue;

                boolean checkCollision = false;
                boolean checkYWallCollision = false;

                //Verify if need to check for collision against another model
                if(movingModels.contains(model)){//Check against another moving model
                    checkCollision = true;
                }else if(model instanceof StaticModel){//Check against a static model
                    StaticModel.Type t = ((StaticModel)model).getType();
                    if(t == StaticModel.Type.BLOCK){//If the static model is of normal type
                        checkCollision = true;
                    }else if(t == StaticModel.Type.WALL_BOTTOM || t == StaticModel.Type.WALL_TOP){//If the static model is of a top or bottom wall type, then check for a collision using their specific function
                        checkYWallCollision = true;
                    }
                }


                boolean hasCollision = false;
                if(checkCollision){
                    if(hasCollision(moving, model))
                        hasCollision = true;
                }else if(checkYWallCollision){
                    if(hasYWallCollision(moving, (StaticModel)model))
                        hasCollision = true;
                }
                if(hasCollision)
                    collisions.add(new Pair<>(moving, model));
            }
        }
        return collisions;
    }

    private static boolean hasYWallCollision(MovingModel moving, StaticModel wall){
        final List<Vec3> cornersWall = wall.getCorners(), cornersMvg = moving.getCorners();
        final StaticModel.Type wallType = wall.getType();

        float wallHeight = cornersWall.get(1).y() - cornersWall.get(2).y();//taking the wall's height as Y bound. Subject to change

        final Vec3 wallCornerBottomLeft = cornersWall.get(2), wallCornerBottomRight = cornersWall.get(3);

        //Only check for bottom of moving model
        for(byte i=2; i < 4; ++i){
            Vec3 cornerMvg = cornersMvg.get(i);

            //Check if x coord is in bound of wall
            if(cornerMvg.x() >= wallCornerBottomLeft.x() && cornerMvg.x() <= wallCornerBottomRight.x()){

                //Check if y coord in in bound (taking the wall's height as bound. Subject to change)
                final float mvgY = cornerMvg.y(),
                        wallY = wallCornerBottomRight.y();

                return (wallType == StaticModel.Type.WALL_TOP ?
                        (mvgY >= wallY && mvgY <= (wallY + wallHeight + 0.1)) ://Top wall. Between over bottom line of wall and height of wall. Adding a little margin on top, because of the corridors' opening. Without it, we can see the feet
                        mvgY <= (wallY + wallHeight/2) && mvgY >= (wallY - wallHeight/2));//Bottom wall. Between a height same as the wall with the bottom of the wall as the middle.
            }
        }

        return false;
    }

    private static boolean hasCollision(MovingModel moving, Model model){
        final List<Vec3> cornersMvg = moving.getCorners(), cornersMdl = model.getCorners();
        final Vec3 orgMdl = model.getRelOrigin();

        for(Vec3 cornerMvg : cornersMvg){
            for(Vec3 cornerMdl : cornersMdl){
                //Is X coordinate inside the other model X coordinates. Same for Y
                boolean isColX = (cornerMvg.x() >= cornerMdl.x() && cornerMvg.x() <= orgMdl.x()) || (cornerMvg.x() <= cornerMdl.x() && cornerMvg.x() >= orgMdl.x());
                boolean isColY = (cornerMvg.y() >= cornerMdl.y() && cornerMvg.y() <= orgMdl.y()) || (cornerMvg.y() <= cornerMdl.y() && cornerMvg.y() >= orgMdl.y());

                if(isColX && isColY)
                    return true;
            }
        }
        return false;
    }

    /**
     * Detects collisions between models. A MovingModel can collide with another MovingModel or a StaticModel of type BLOCK
     * Optimised version of {@link CollisionDetector#Detect(List, List) }
     * @param movingModels List of moving models to verify for
     * @param models All models to verify against
     */
    public static List<Pair<MovingModel, Model>> Detect2(List<MovingModel> movingModels, List<Model> models) {
        final int BOX_RADIUS = 4;//The radius around the moving model in which to check for collisions.
        // The origins of the models have to be in it.
        //Meaning that this radius has to be able to contain the biggest object Worst case: (biggest object/2) * 2. 2 biggest model collide together

        List<Pair<MovingModel, Model>> collisions = new ArrayList<>();

        //Log.e("CD Q", "QMM: " + movingModels.size() + ", QMO: " + models.size());

        for (MovingModel moving : movingModels) {
            final Vec3 orgMvg = moving.getRelOrigin();

            for (Model model : models) {
                boolean isStaticModel = false;

                //If it is a floor tile, can discard it immediately
                if(model instanceof StaticModel) {
                    isStaticModel = true;

                    if (((StaticModel) model).getType() == StaticModel.Type.FLOOR)
                        continue;
                }

                final Vec3 orgMdl = model.getRelOrigin();

                if (Coord.distanceTo(orgMvg.x(), orgMvg.y(), orgMdl.x(), orgMdl.y()) <= BOX_RADIUS) {//Check if other model is in radius
                    //Log.e("IN BOUNDS", "IS IN BOUNDS");
                    if (moving == model)//Skip if checking collision against themselves
                        continue;

                    //If the other model is in bound, check for collisions

                    boolean checkCollision = false;
                    boolean checkYWallCollision = false;

                    //Verify if need to check for collision against another model
                    if(isStaticModel){//Check against a static model
                        StaticModel.Type t = ((StaticModel)model).getType();
                        if(t == StaticModel.Type.BLOCK){//If the static model is of normal type
                            checkCollision = true;
                        }else if(t == StaticModel.Type.WALL_BOTTOM || t == StaticModel.Type.WALL_TOP){//If the static model is of a top or bottom wall type, then check for a collision using their specific function
                            checkYWallCollision = true;
                        }
                    }else if(movingModels.contains(model)){//Check against another moving model
                        checkCollision = true;
                    }


                    boolean hasCollision = false;
                    if(checkCollision){
                        if(hasCollision(moving, model))
                            hasCollision = true;
                    }else if(checkYWallCollision){
                        if(hasYWallCollision(moving, (StaticModel)model))
                            hasCollision = true;
                    }
                    if(hasCollision)
                        collisions.add(new Pair<>(moving, model));

                }
            }
        }
        return collisions;
    }
}
