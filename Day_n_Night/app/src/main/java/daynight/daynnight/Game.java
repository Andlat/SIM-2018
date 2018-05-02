package daynight.daynnight;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import daynight.daynnight.engine.GameView;
import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;
import daynight.daynnight.engine.util.Coord;

import static daynight.daynnight.MainActivity.onPause;

/**
 * Created by andlat on 2018-02-17.
 */

class Game extends GameView{
    private Context mContext;

    private long mHeroID, mTileID, mSID;
    private float xPercentDirectionBalle = 0;
    private float yPercentDirectionBalle = 0;
    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimerReload;
    private int nbrBallesLancees = 0;
    private MovingModel perso;
    private Vec3 persoVec;
    private World world;
    private int round=0;
    private int nbrMonstreMauve69;
    private int nbrMonstreVert17;
    private int nbrMonstreBleu4;
    private int nbrMonstreJaune1;
    private int qttDifficulte;
    private int vieJoueur = 100;
    private ArrayList<Toutou> mListeToutou;
    private ArrayList<Projectile> mListeProjectile;
    private int i,j,h,k;

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


        mListeToutou = new ArrayList<Toutou>();
        mListeProjectile = new ArrayList<Projectile>();
    }

    @Override
    protected void onSurfaceChanged(int width, int height) {
    }

    float time=0;
    @Override
    protected void onDrawFrame(World world) throws IOException {

        int temp=0;
        int temp2=0;
        //world.getModel()
        for(Projectile projectile: mListeProjectile){
            projectile.setCoord(new Coord(this.getX()+ projectile.getmDirectionX(), this.getY()+ projectile.getmDirectionY()));
        }
        temp=0;
        for(Projectile projectile : mListeProjectile){
            world.Move(projectile.getmID(), new Vec3(projectile.getmDirectionX(), projectile.getmDirectionY(), 0.f), getElapsedFrameTime());
            for(Toutou monsieurMovingAmiID:mListeToutou){
                if(Math.sqrt(Math.pow(projectile.getCoord().getX() - mListeToutou.get(temp2).getCoord().getX(),2) + Math.pow(projectile.getCoord().getY() - mListeToutou.get(temp2).getCoord().getY(),2)) < 0.2){
                    mListeToutou.get(temp2).setmVie(mListeToutou.get(temp2).getmVie()-projectile.getmPuissance());
                    if(mListeToutou.get(temp2).getmVie()<=0){
                        mListeToutou.remove(temp2);
                        if(mListeToutou.isEmpty()){
                            updateLevel();
                        }
                    }
                    world.removeModel(projectile.getmID());
                    mListeProjectile.remove(temp);
                }
                temp2++;
            }
            temp++;
        }
        temp=0;
        float angle=0;
        for(Toutou monsieurMovingAmiID:mListeToutou){
            //TODO determiner si l'origine est le personnage ou alors un point dans la carte
            angle=(float)Math.atan2(-mListeToutou.get(temp2).getCoord().getY(), -mListeToutou.get(temp2).getCoord().getX());
            if(angle > 7*Math.PI/4 && angle < Math.PI/4){
                monsieurMovingAmiID.setDernierDeplacement('d');
            }else if(angle > Math.PI/4 && angle < 3*Math.PI/4){
                monsieurMovingAmiID.setDernierDeplacement('h');
            }else if(angle > 3*Math.PI/4 && angle < 5*Math.PI/4){
                monsieurMovingAmiID.setDernierDeplacement('g');
            }else if(angle > 5*Math.PI/4 && angle < 7*Math.PI/4){
                monsieurMovingAmiID.setDernierDeplacement('b');
            }
            world.Move(mListeToutou.get(temp).getmID(), new Vec3((float)Math.sin(angle), (float)Math.cos(angle), 0), getElapsedFrameTime());
            if(mListeToutou.get(temp2).getCoord().getX() < 0.5){
                world.Move(mListeToutou.get(temp).getmID(), new Vec3(-(float)Math.sin(angle)*20, -(float)Math.cos(angle)*20, 0), getElapsedFrameTime());
                this.vieJoueur -= mListeToutou.get(temp).getmDMG();
                if(this.vieJoueur <=0){
                    endGame();
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

    private void endGame() {
        for(Projectile projectile : mListeProjectile){
            world.removeModel(projectile.getmID());
        }
        mListeProjectile.clear();
        for(Toutou toutou: mListeToutou){
            world.removeModel(toutou.getmID());
        }
        mListeToutou.clear();
    }

    public void makeMrBalle() throws IOException {
        MovingModel bullet = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
        bullet.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
        mListeProjectile.add(new Projectile(15,this.xPercentDirectionBalle, this.yPercentDirectionBalle, new Coord(0,0), bullet, bullet.getID()));
        nbrBallesLancees++;
    }

    public void updateLevel() throws IOException {
        round++;
        qttDifficulte = (int)Math.floor((round*round+1)/2) + round*round;
        nbrMonstreMauve69 = (int)Math.floor(qttDifficulte/69) - (int)Math.floor(round/16)^2;
        nbrMonstreVert17 = (int)Math.floor((qttDifficulte-nbrMonstreMauve69*69)/17)+(int)Math.floor(round/12);
        nbrMonstreBleu4 = (int)Math.floor((qttDifficulte-nbrMonstreVert17*17-nbrMonstreMauve69*69)/4)+(int)Math.floor(round/7);
        nbrMonstreJaune1 = (int)Math.abs(qttDifficulte-nbrMonstreBleu4*4-nbrMonstreVert17*17-nbrMonstreMauve69*69+3+Math.floor(round/5));
        for(i=0; i<nbrMonstreMauve69;i++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mListeToutou.add(new Toutou(25*27+round*4, 34, new Coord(0,0), ami.getID()));
            mListeToutou.get(i).setMovingModel(getContext(), "toutou4");
            this.mListeToutou.get(i).getModel().setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(Model object) {
                    //mModel.setTranslation(mModel.getLastTranslation());
                    boolean estUnToutouOuUneBalle = false;
                    for(Toutou toutou:mListeToutou){
                        if(object.getID() == toutou.getmID()){
                            estUnToutouOuUneBalle = true;
                        }
                    }
                    for(Projectile projectile:mListeProjectile){
                        if(object.getID() == projectile.getmID()){
                            estUnToutouOuUneBalle = true;
                        }
                    }
                    if(!estUnToutouOuUneBalle){
                        //Contact avec mur
                        if(mListeToutou.get(i).getDernierDeplacement() == 'h'){
                            world.Move(mListeToutou.get(i).getmID(), new Vec3(0.f, -0.1f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(i).getDernierDeplacement() == 'd'){
                            world.Move(mListeToutou.get(i).getmID(), new Vec3(-0.1f, 0.0f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(i).getDernierDeplacement() == 'b'){
                            world.Move(mListeToutou.get(i).getmID(), new Vec3(0.f, 0.1f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(i).getDernierDeplacement() == 'g'){
                            world.Move(mListeToutou.get(i).getmID(), new Vec3(0.1f, 0.f, 0.f), getElapsedFrameTime());
                        }
                    }
                }
            });
        }
        i = mListeToutou.size();
        for(j=0; j<nbrMonstreVert17;j++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mListeToutou.add(new Toutou(25*9+round*3, 25, new Coord(0,0), ami.getID()));
            mListeToutou.get(j+i).setMovingModel(getContext(), "toutou3");
            this.mListeToutou.get(j+i).getModel().setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(Model object) {
                    //mModel.setTranslation(mModel.getLastTranslation());
                    boolean estUnToutouOuUneBalle = false;
                    for(Toutou toutou:mListeToutou){
                        if(object.getID() == toutou.getmID()){
                            estUnToutouOuUneBalle = true;
                        }
                    }
                    for(Projectile projectile:mListeProjectile){
                        if(object.getID() == projectile.getmID()){
                            estUnToutouOuUneBalle = true;
                        }
                    }
                    if(!estUnToutouOuUneBalle){
                        //Contact avec mur
                        if(mListeToutou.get(j+i).getDernierDeplacement() == 'h'){
                            world.Move(mListeToutou.get(j+i).getmID(), new Vec3(0.f, -0.1f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(j+i).getDernierDeplacement() == 'd'){
                            world.Move(mListeToutou.get(j+i).getmID(), new Vec3(-0.1f, 0.0f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(j+i).getDernierDeplacement() == 'b'){
                            world.Move(mListeToutou.get(j+i).getmID(), new Vec3(0.f, 0.1f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(j+i).getDernierDeplacement() == 'g'){
                            world.Move(mListeToutou.get(j+i).getmID(), new Vec3(0.1f, 0.f, 0.f), getElapsedFrameTime());
                        }
                    }
                }
            });
        }
        i=mListeToutou.size();
        for(h=0; h<nbrMonstreBleu4;h++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "toutou2.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mListeToutou.add(new Toutou(25*3+round*2, 20, new Coord(0,0), ami.getID()));
            mListeToutou.get(h+i).setMovingModel(getContext(), "toutou2");
            this.mListeToutou.get(h+i).getModel().setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(Model object) {
                    //mModel.setTranslation(mModel.getLastTranslation());
                    boolean estUnToutouOuUneBalle = false;
                    for(Toutou toutou:mListeToutou){
                        if(object.getID() == toutou.getmID()){
                            estUnToutouOuUneBalle = true;
                        }
                    }
                    for(Projectile projectile:mListeProjectile){
                        if(object.getID() == projectile.getmID()){
                            estUnToutouOuUneBalle = true;
                        }
                    }
                    if(!estUnToutouOuUneBalle){
                        //Contact avec mur
                        if(mListeToutou.get(h+i).getDernierDeplacement() == 'h'){
                            world.Move(mListeToutou.get(h+i).getmID(), new Vec3(0.f, -0.1f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(h+i).getDernierDeplacement() == 'd'){
                            world.Move(mListeToutou.get(h+i).getmID(), new Vec3(-0.1f, 0.0f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(h+i).getDernierDeplacement() == 'b'){
                            world.Move(mListeToutou.get(h+i).getmID(), new Vec3(0.f, 0.1f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(h+i).getDernierDeplacement() == 'g'){
                            world.Move(mListeToutou.get(h+i).getmID(), new Vec3(0.1f, 0.f, 0.f), getElapsedFrameTime());
                        }
                    }
                }
            });
        }
        i=mListeToutou.size();
        for(k=0; k<nbrMonstreVert17;k++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "toutou1.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mListeToutou.add(new Toutou(25+round, 17, new Coord(0,0), ami.getID()));
            mListeToutou.get(k+i).setMovingModel(getContext(), "toutou1");
            this.mListeToutou.get(k+i).getModel().setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(Model object) {
                    //mModel.setTranslation(mModel.getLastTranslation());
                    boolean estUnToutouOuUneBalle = false;
                    for(Toutou toutou:mListeToutou){
                        if(object.getID() == toutou.getmID()){
                            estUnToutouOuUneBalle = true;
                        }
                    }
                    for(Projectile projectile:mListeProjectile){
                        if(object.getID() == projectile.getmID()){
                            estUnToutouOuUneBalle = true;
                        }
                    }
                    if(!estUnToutouOuUneBalle){
                        //Contact avec mur
                        if(mListeToutou.get(k+i).getDernierDeplacement() == 'h'){
                            world.Move(mListeToutou.get(k+i).getmID(), new Vec3(0.f, -0.1f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(k+i).getDernierDeplacement() == 'd'){
                            world.Move(mListeToutou.get(k+i).getmID(), new Vec3(-0.1f, 0.0f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(k+i).getDernierDeplacement() == 'b'){
                            world.Move(mListeToutou.get(k+i).getmID(), new Vec3(0.f, 0.1f, 0.f), getElapsedFrameTime());
                        }else if(mListeToutou.get(k+i).getDernierDeplacement() == 'g'){
                            world.Move(mListeToutou.get(k+i).getmID(), new Vec3(0.1f, 0.f, 0.f), getElapsedFrameTime());
                        }
                    }
                }
            });
        }
    }

    public void movePerso(Vec3 vector){
        world.Move(perso.getID() ,persoVec,getElapsedFrameTime());
    }
}
