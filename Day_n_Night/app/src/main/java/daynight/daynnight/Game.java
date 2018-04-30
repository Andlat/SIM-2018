package daynight.daynnight;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;

import daynight.daynnight.engine.GameView;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;
import daynight.daynnight.engine.util.Coord;

/**
 * Created by andlat on 2018-02-17.
 */

class Game extends GameView{
    private Context mContext;

    private long mHeroID, mTileID, mSID;
    private ArrayList<Long> mBallesID;
    private ArrayList<MovingModel> mBalles;
    private ArrayList<Integer> mBallesPuissance;
    private ArrayList<Float> mDirectionsBallesX;
    private ArrayList<Float> mDirectionsBallesY;
    private ArrayList<Coord> mCoordonnesBalles;
    private ArrayList<Long> mListeMonstresID;
    private ArrayList<MovingModel> mListeMonstres;
    private ArrayList<Integer> mListeMonstreVie;
    private ArrayList<Integer> mListeMonstreDMG;
    private ArrayList<Coord> mCoordonnesMonstres;
    private float xPercentDirectionBalle = 0;
    private float yPercentDirectionBalle = 0;
    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimerReload;
    private int nbrBallesLancees = 0;
    private Shader texShader;
    private Texture tex;
    private MovingModel perso;
    private Vec3 persoVec;
    private World world;
    private int round=0;
    private int nbrMonstreMauve69;
    private int vieMonstreMauve69=675;
    private int nbrMonstreVert17;
    private int vieMonstreVert17=225;
    private int nbrMonstreBleu4;
    private int vieMonstreBleu4=75;
    private int nbrMonstreJaune1;
    private int vieMonstreJaune1=25;
    private int qttDifficulte;
    private int vieJoueur = 100;

    public Game(Context context) {
        super(context);

        init(context);
    }

    public Game(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context){
        mContext = context;
    }


    @Override
    protected void onCreate() {
        World world = new World();
        super.UseWorld(world);

        mArthur = new Arthur(mContext);
        mArthurID = world.addModel(mArthur.getModel());
    }

    @Override
    protected void onSurfaceChanged(int width, int height) {
    }

    float time=0;
    @Override
    protected void onDrawFrame(World world) {

        int temp=0;
        int temp2=0;
        //world.getModel()
        for(Coord monsieurCoordonneBalle: mCoordonnesBalles){
            monsieurCoordonneBalle.setX(this.getX()+mDirectionsBallesX.get(temp));
            monsieurCoordonneBalle.setY(this.getY()+mDirectionsBallesY.get(temp));
        }
        temp=0;
        for(Long monsieurMovingModelID : mBallesID){
            world.Move(monsieurMovingModelID, new Vec3(mDirectionsBallesX.get(temp), mDirectionsBallesY.get(temp), 0.f), getElapsedFrameTime());
            for(Long monsieurMovingAmiID:mListeMonstresID){
                if(Math.sqrt(Math.pow(mCoordonnesBalles.get(temp).getX() - mCoordonnesMonstres.get(temp2).getX(),2) + Math.pow(mCoordonnesBalles.get(temp).getY() - mCoordonnesMonstres.get(temp2).getY(),2)) < 0.2){
                    destroyMrBalle(temp, world);
                    mListeMonstreVie.set(temp2, mListeMonstreVie.get(temp2) - mBallesPuissance.get(temp));
                    if(mListeMonstreVie.get(temp2)<=0){
                        mListeMonstreVie.remove(temp2);
                        mCoordonnesMonstres.remove(temp2);
                        mListeMonstres.remove(temp2);
                        mListeMonstresID.remove(temp2);
                        mListeMonstreDMG.remove(temp2);
                    }
                }
                temp2++;
            }
            temp++;
        }
        temp=0;
        float angle=0;
        for(Long monsieurMovingAmiID:mListeMonstresID){
            //TODO determiner si l'origine est le personnage ou alors un point dans la carte
            angle=(float)Math.atan2(-mCoordonnesMonstres.get(temp).getY(), -mCoordonnesMonstres.get(temp).getX());
            world.Move(monsieurMovingAmiID, new Vec3((float)Math.sin(angle), (float)Math.cos(angle), 0), getElapsedFrameTime());
            if(mCoordonnesMonstres.get(temp).getX() < 0.5){
                world.Move(monsieurMovingAmiID, new Vec3(-(float)Math.sin(angle)*20, -(float)Math.cos(angle)*20, 0), getElapsedFrameTime());
                if(mListeMonstreDMG.get(temp) != null){
                    this.vieJoueur -= mListeMonstreDMG.get(temp);
                }
            }
            temp++;
        }
/*
        world.Move(mTileID, new Vec3(0.1f, 0.8f, 0.f), getElapsedFrameTime());
        world.Move(mHeroID, new Vec3(0.1f, -0.8f, 0.f), getElapsedFrameTime());
        world.Move(mSID, new Vec3(-1, 0, 0), getElapsedFrameTime());
        */
    }

    public void makeMrBalle() throws IOException {
        MovingModel bullet = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
        bullet.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
        mBalles.add(bullet);
        mDirectionsBallesX.add(this.xPercentDirectionBalle);
        mDirectionsBallesY.add(this.yPercentDirectionBalle);
        mCoordonnesBalles.add(new Coord(0,0));
        //TODO get la puissance des balles
        mBallesPuissance.add(15);
        nbrBallesLancees++;
        bullet.AssociateShader(texShader);
        bullet.setTexture(tex);
        mBallesID.add(bullet.getID());
    }

    public void updateLevel() throws IOException {
        round++;
        qttDifficulte = (int)Math.floor((round*round+1)/2) + round*round;
        nbrMonstreMauve69 = (int)Math.floor(qttDifficulte/69) - (int)Math.floor(round/16)^2;
        nbrMonstreVert17 = (int)Math.floor((qttDifficulte-nbrMonstreMauve69*69)/17)+(int)Math.floor(round/12);
        nbrMonstreBleu4 = (int)Math.floor((qttDifficulte-nbrMonstreVert17*17-nbrMonstreMauve69*69)/4)+(int)Math.floor(round/7);
        nbrMonstreJaune1 = (int)Math.abs(qttDifficulte-nbrMonstreBleu4*4-nbrMonstreVert17*17-nbrMonstreMauve69*69+3+Math.floor(round/5));
        for(int i=0; i<nbrMonstreMauve69;i++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mListeMonstres.add(ami);
            //TODO associer les bonnes textures aux monstres
            ami.AssociateShader(texShader);
            ami.setTexture(tex);
            mListeMonstresID.add(ami.getID());
            mListeMonstreVie.add(25*27+round*4);
            mListeMonstreDMG.add(34);
            mCoordonnesMonstres.add(new Coord(0,0));
        }
        for(int i=0; i<nbrMonstreVert17;i++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mListeMonstres.add(ami);
            //TODO associer les bonnes textures aux monstres
            ami.AssociateShader(texShader);
            ami.setTexture(tex);
            mListeMonstresID.add(ami.getID());
            mListeMonstreVie.add(25*9+round*3);
            mListeMonstreDMG.add(25);
            mCoordonnesMonstres.add(new Coord(0,0));
        }
        for(int i=0; i<nbrMonstreBleu4;i++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mListeMonstres.add(ami);
            //TODO associer les bonnes textures aux monstres
            ami.AssociateShader(texShader);
            ami.setTexture(tex);
            mListeMonstresID.add(ami.getID());
            mListeMonstreVie.add(25*3+round*2);
            mListeMonstreDMG.add(20);
            mCoordonnesMonstres.add(new Coord(0,0));
        }
        for(int i=0; i<nbrMonstreVert17;i++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mListeMonstres.add(ami);
            //TODO associer les bonnes textures aux monstres
            ami.AssociateShader(texShader);
            ami.setTexture(tex);
            mListeMonstresID.add(ami.getID());
            mListeMonstreVie.add(25+round);
            mListeMonstreDMG.add(17);
            mCoordonnesMonstres.add(new Coord(0,0));
        }
    }

    public void destroyMrBalle(int i, World world){
        world.removeModel(mBallesID.get(i));
        mBalles.remove(i);
        mDirectionsBallesX.remove(i);
        mDirectionsBallesY.remove(i);
        mBallesID.remove(i);
    }

    public void movePerso(Vec3 vector){
        world.Move(perso.getID() ,persoVec,getElapsedFrameTime());
    }
}
