package daynight.daynnight.engine.physics;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.math.Vec3;

/**
 * Created by andlat on 2018-04-23.
 */

public class CollisionDetector {
    /**
     * Detects collisions between models. A MovingModel can collide with another MovingModel or a StaticModel of type BLOCK
     * @param movingModels
     * @param models
     */
    public static List<Pair<MovingModel, Model>> Detect(List<MovingModel> movingModels, List<Model> models){
        List<Pair<MovingModel, Model>> collisions = new ArrayList<>();


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
                        (mvgY >= wallY && mvgY <= (wallY + wallHeight)) ://Top wall. Between over bottom line of wall and height of wall
                        mvgY <= wallY && mvgY >= (wallY - wallHeight));//Bottom wall. Between under bottom line of wall and height bound. TODO problems when going under a bottom wall. Detecs a collision on an empty space the height of the wall and passes through the wall coming from the side
            }
        }

        return false;
    }

    private static boolean hasCollision(MovingModel moving, Model model){
        final List<Vec3> cornersMvg = moving.getCorners(), cornersMdl = model.getCorners();
        final Vec3 orgMvg = moving.CalculateOrigin(), orgMdl = model.CalculateOrigin();

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
}
