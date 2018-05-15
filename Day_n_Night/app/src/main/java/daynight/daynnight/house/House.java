package daynight.daynnight.house;

import android.content.Context;
import android.util.Pair;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import daynight.daynnight.R;
import daynight.daynnight.engine.Model.Animation;
import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;

import static daynight.daynnight.properties.ModelAttributes.ATTR_TOUTOU;
import static daynight.daynnight.properties.ZIndex.Z_ARTHUR;
import static daynight.daynnight.properties.ZIndex.Z_DECO;
import static daynight.daynnight.properties.ZIndex.Z_TOUTOU;

/**
 * Created by andlat on 2018-05-13.
 */

public class House {
    static final int FLOOR_STYLE = R.drawable.plancher5;

    public static List<MovingModel> toutous = new LinkedList<>();//TODO TOUTOUS: THIS IS TEMPORARY. REMOVE IT

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

        toutous = new GenToutouTEMP(context, world).toutous;//TODO TOUTOUS: THIS IS TEMPORARY. REMOVE IT

        GenDecoTemp(context, world);
    }

    public static void GenRandom(Context context, World world) throws IOException{
        //TODO Random generation of the house
        Generate(context, world);
    }

    //TODO DECO: THIS IS TEMPORARY. REMOVE IT
    private static void GenDecoTemp(Context context, World world) throws IOException{
        StaticModel deco = ObjParser.Parse(context, "models", "cadre.obj").get(0).toStaticModel();

        StaticModel deco2 = new StaticModel(deco);
        deco2.setAnimation(new Animation().addFrame(new Pair<>(Texture.Load(context, R.drawable.cadre2), 0)));

        StaticModel deco3 = new StaticModel(deco);
        deco3.setAnimation(new Animation().addFrame(new Pair<>(Texture.Load(context, R.drawable.cadre3), 0)));

        StaticModel deco4 = new StaticModel(deco);
        deco4.setAnimation(new Animation().addFrame(new Pair<>(Texture.Load(context, R.drawable.cadre4), 0)));

        StaticModel deco4_1 = new StaticModel(deco4);

        deco.StaticTranslate(new Vec3(-11, 17, 0));
        world.addModel(deco);
        world.setGroupZIndex(deco, Z_DECO);

        deco2.StaticTranslate(new Vec3(11, 17, 0));
        world.addModel(deco2);
        world.setGroupZIndex(deco2, Z_DECO);

        deco3.StaticTranslate(new Vec3(-37, 9, 0));
        world.addModel(deco3);
        world.setGroupZIndex(deco3, Z_DECO);

        deco4.StaticTranslate(new Vec3(40, 11, 0));
        world.addModel(deco4);
        world.setGroupZIndex(deco4, Z_DECO);

        deco4_1.StaticTranslate(new Vec3(15, 17, 0));
        world.addModel(deco4_1);
        world.setGroupZIndex(deco4_1, Z_DECO);

        StaticModel couch = ObjParser.Parse(context, "models", "divan.obj").get(0).toStaticModel();
        couch.setType(StaticModel.Type.BLOCK);
        world.addModel(couch);
        world.setGroupZIndex(couch, Z_DECO);

        StaticModel bed = ObjParser.Parse(context, "models", "lit.obj").get(0).toStaticModel();
        bed.setType(StaticModel.Type.BLOCK);
        bed.StaticTranslate(new Vec3(-32, 0, 0));
        world.addModel(bed);
        world.setGroupZIndex(bed, Z_DECO);
    }

    //TODO TOUTOUS: THIS IS TEMPORARY. REMOVE IT
    static class GenToutouTEMP {
        final List<MovingModel> toutous = new LinkedList<>();

        private final Context mContext;
        private final World mWorld;
        private static final int FRAME_LENGTH = 200;

        GenToutouTEMP(Context context, World world) throws IOException {
            mContext = context;
            mWorld = world;

            MovingModel toutou1 = ObjParser.Parse(context, "models", "toutou1.obj").get(0).toMovingModel();
            toutou1.setAttr(ATTR_TOUTOU);
            toutou1.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 2f));
            setSkin(toutou1, R.drawable.toutou1_1);

            MovingModel toutou2 = ObjParser.Parse(context, "models", "toutou2.obj").get(0).toMovingModel();
            toutou2.setAttr(ATTR_TOUTOU);
            toutou2.setPhysics(new PhysicsAttributes.MovingModelAttr(2500, 0, 0, 1.25f));
            MovingModel toutou3 = new MovingModel(toutou2);

            setSkin(toutou2, R.drawable.toutou2_1);
            setSkin(toutou3, R.drawable.toutou3_1);

            MovingModel toutou4 = ObjParser.Parse(context, "models", "toutou3.obj").get(0).toMovingModel();
            toutou4.setAttr(ATTR_TOUTOU);
            toutou4.setPhysics(new PhysicsAttributes.MovingModelAttr(3500, 0, 0, 0.75f));
            setSkin(toutou4, R.drawable.toutou4_1);


            for(int i = 0; i < 4; ++i) {
                float x=0, y=0;
                switch(i){
                    case 0: x = -15; y = 12; break;
                    case 1: x = -15; y = -12; break;
                    case 2: x = 15; y = 12; break;
                    case 3: x = 15; y = -12; break;
                }

                MovingModel m = new MovingModel(toutou1);
                m.setOnCollisionListener(getListener(m));
                world.addModel(m);
                world.Translate(m, new Vec3(x, y, 0));
                world.setGroupZIndex(m, Z_TOUTOU);

                toutous.add(m);
            }
            for(int i = 0; i < 3; ++i) {
                float x=0, y=0;
                switch(i){
                    case 0: x = 0; y = 15; break;
                    case 1: x = -15; y = 6; break;
                    case 2: x = 15; y = 6; break;
                }

                MovingModel m = new MovingModel(toutou2);
                m.setOnCollisionListener(getListener(m));
                world.addModel(m);
                world.Translate(m, new Vec3(x, y, 0));
                world.setGroupZIndex(m, Z_TOUTOU);

                toutous.add(m);
            }
            for(int i = 0; i < 2; ++i) {
                float x=0, y=0;
                switch(i){
                    case 0: x = -15; y = -6; break;
                    case 1: x = 15; y = -6; break;
                }

                MovingModel m = new MovingModel(toutou3);
                m.setOnCollisionListener(getListener(m));
                world.addModel(m);
                world.Translate(m, new Vec3(x, y, 0));
                world.setGroupZIndex(m, Z_TOUTOU);

                toutous.add(m);
            }

            world.addModel(toutou4);
            toutou4.setOnCollisionListener(getListener(toutou4));
            world.Translate(toutou4, new Vec3(4, 7, 0));
            world.setGroupZIndex(toutou4, Z_TOUTOU);

            toutous.add(toutou4);
        }

        void setSkin(Model model, int firstFrameSkinResID) throws IOException{
            Animation skin = new Animation();
            for(byte i=0; i < 5; ++i)
                skin.addFrame(new Pair<>(Texture.Load(mContext, firstFrameSkinResID+i), FRAME_LENGTH));

            model.setAnimation(skin);
        }

        MovingModel.onCollisionListener getListener(final MovingModel m){
            return new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(World world, Model object) {
                    m.RewindTranslation();
                }
            };
        }
    }
}
