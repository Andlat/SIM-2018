package daynight.daynnight;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import daynight.daynnight.engine.GameView;
import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.StaticModel;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.World;
import daynight.daynnight.engine.math.Vec3;
import daynight.daynnight.engine.physics.PhysicsAttributes;
import daynight.daynnight.engine.util.Coord;

/**
 * Created by Nikola Zelovic on 2018-02-17.
 */

class Game extends GameView{
    private Context mContext;

    private float xPercentDirectionBalle = 0;
    private float yPercentDirectionBalle = 0;
    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimerReload;
    private int nbrBallesLancees = 0;
    private MovingModel perso;
    private Vec3 persoVec;
    private World mWorld;
    private int round=0;
    private int nbrMonstreMauve69;
    private int nbrMonstreVert17;
    private int nbrMonstreBleu4;
    private int nbrMonstreJaune1;
    private int qttDifficulte;
    private int vieJoueur = 100;
    private ArrayList<Toutou> mListeToutou = new ArrayList<>();;
    private ArrayList<Projectile> mListeProjectile = new ArrayList<>();;
    private ArrayList<StaticModel> mListePlancher = new ArrayList<>();
    private ArrayList<StaticModel> mListeMur = new ArrayList<>();
    private int i,j,h,k;
    private ArrayList<Integer> listeToutouDelete = new ArrayList<>();
    private ArrayList<Integer> listeBalleDelete = new ArrayList<>();
    private Toutou toutouTest;
    private boolean positionPositiveDuSpawn = false;

    private Arthur mArthur;
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

    Arthur getArthur(){ return mArthur; }
    MovingModel f;
    long fi;
    @Override
    protected void onCreate() {
        mWorld = new World();
        super.UseWorld(mWorld);

        mArthur = new Arthur(mContext);
        mArthur.getModel().StaticTranslate(new Vec3(-1, -2, 0));
        mArthur.setInWorldID(mWorld.addModel(mArthur.getModel()));

        mWorld.setCamFollowModel(mArthur.getInWorldID());

        try {
            StaticModel t1, t2, t3, b1, b2, b3;
            t1 = ObjParser.Parse(mContext, "models", "mur.obj").get(0).toStaticModel();
            t1.setType(StaticModel.Type.FLOOR);
            t1.StaticTranslate(new Vec3(0, 2, 0));

            t2 = new StaticModel(t1);
            t3 = new StaticModel(t1);

            t2.StaticTranslate(new Vec3(-2, 0, 0));
            t3.StaticTranslate(new Vec3(2, 0, 0));

//            mWorld.addModel(t1);
  //          mWorld.addModel(t2);
    //        mWorld.addModel(t3);

            f = ObjParser.Parse(mContext, "models", "plancher.obj").get(0).toMovingModel();
            f.setPhysics(new PhysicsAttributes.MovingModelAttr(0,0, 0, 1));
            //fi = mWorld.addModel(f);

        }catch(IOException ex){
            Log.e("Game Init", "Failed to lead game: " + ex.toString(), ex);
        }

/*
        try {
            MovingModel amiTest = ObjParser.Parse(mContext, "models", "lit.obj").get(0).toMovingModel();
            amiTest.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mWorld.addModel(amiTest);
            Vec3 vec3 = getCoordonnesMonstre();
            mWorld.Translate(amiTest, vec3);
            this.mListeToutou.add(new Toutou(25*27+round*4, 34, new Coord(vec3.x(),vec3.y()), amiTest.getID()));
            this.mListeToutou.get(i).setMovingModel(getContext(), "toutou4");
            this.toutouTest = new Toutou(25, 10, new Coord(0,0), amiTest.getID());
        }catch(IOException e){

        }

        try {
            StaticModel mur = ObjParser.Parse(mContext, "models", "plancher.obj").get(0).toStaticModel();
            mur.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            Vec3 vec3 = new Vec3((mur.getCorners().get(0).x()-mur.getCorners().get(1).x()), mur.getCorners().get(1).y()-mur.getCorners().get(2).y(), 0);
            for(int hauteur=0; hauteur<22;hauteur++){
                for(int largeur=0; largeur<22;largeur++){
                    if(hauteur==21){
                        placerMurBas(hauteur, largeur, vec3);
                    }else if(hauteur ==0){
                        placerMurHaut(hauteur, largeur, vec3);
                    }else if(largeur==0||largeur==21){
                        placerMurBlock(hauteur, largeur, vec3);
                    } else if((hauteur==1||hauteur==2||hauteur==5||hauteur==6||hauteur==8||hauteur==9||
                            hauteur==12||hauteur==13||hauteur==15||hauteur==16||hauteur==19||hauteur==20)
                            &&((largeur==0||largeur==7||largeur==14||largeur==21))){
                        placerMurBlock(hauteur, largeur, vec3);
                    }else if((hauteur==7||hauteur==14)&&(largeur<3||(largeur>4&&largeur<10)||(largeur>11&&largeur<17)||largeur>18)){
                        placerMurBlock(hauteur, largeur, vec3);
                    }else{
                        placerPlancher(hauteur, largeur, vec3);
                    }
                }
            }
        }catch(IOException e){

        }

*/
    }
    private void placerPlancher(int hauteur, int largeur, Vec3 vec3) {
        try {
            StaticModel mur = ObjParser.Parse(mContext, "models", "plancher.obj").get(0).toStaticModel();
            mur.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mur.setType(StaticModel.Type.FLOOR);
            mWorld.addModel(mur);
            mWorld.Translate(mur, new Vec3((largeur-10)*vec3.x(), (hauteur-11)*vec3.y(), 0.f));
            mListeMur.add(mur);
        }catch(IOException e){

        }
    }

    private void placerMurBlock(int hauteur, int largeur, Vec3 vec3) {
        try {
            StaticModel mur = ObjParser.Parse(mContext, "models", "mur.obj").get(0).toStaticModel();
            mur.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mur.setType(StaticModel.Type.BLOCK);
            mWorld.addModel(mur);
            mWorld.Translate(mur, new Vec3((largeur-10)*vec3.x(), (hauteur-11)*vec3.y(), 0.f));
            mListeMur.add(mur);
        }catch(IOException e){

        }
    }

    private void placerMurBas(int hauteur, int largeur, Vec3 vec3) {
        try {
            StaticModel mur = ObjParser.Parse(mContext, "models", "mur.obj").get(0).toStaticModel();
            mur.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mur.setType(StaticModel.Type.WALL_BOTTOM);
            mWorld.addModel(mur);
            mWorld.Translate(mur, new Vec3((largeur-10)*vec3.x(), (hauteur-11)*vec3.y(), 0.f));
            mListeMur.add(mur);
        }catch(IOException e){

        }
    }

    private void placerMurHaut(int hauteur, int largeur, Vec3 vec3) {
        try {
            StaticModel mur = ObjParser.Parse(mContext, "models", "mur.obj").get(0).toStaticModel();
            mur.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mur.setType(StaticModel.Type.WALL_TOP);
            mWorld.addModel(mur);
            mWorld.Translate(mur, new Vec3((largeur-10)*vec3.x(), (hauteur-11)*vec3.y(), 0.f));
            mListeMur.add(mur);
        }catch(IOException e){

        }
    }

    @Override
    protected void onSurfaceChanged(int width, int height) {
    }

    @Override
    protected void onDrawFrame(World world) {
<<<<<<< HEAD
        if(mArthur != null)
            world.Move(mArthur.getInWorldID(), mArthur.getDirection(), getElapsedFrameTime());
/*
=======
        world.Move(mArthur.getInWorldID(), mArthur.getDirection(), getElapsedFrameTime());

>>>>>>> master
        this.listeToutouDelete.clear();
        this.listeBalleDelete.clear();
        int temp=0;
        int temp2=0;
        //mWorld.getModel()
        for(Projectile projectile: mListeProjectile){
            projectile.setCoord(new Coord(this.getX()+ projectile.getmDirectionX(), this.getY()+ projectile.getmDirectionY()));
        }
        for(Projectile projectile : mListeProjectile){
            world.Move(projectile.getmID(), new Vec3(projectile.getmDirectionX(), projectile.getmDirectionY(), 0.f), getElapsedFrameTime());
            for(Toutou monsieurMovingAmiID:mListeToutou){
                if(Math.sqrt(Math.pow(projectile.getCoord().getX() - mListeToutou.get(temp2).getCoord().getX(),2) + Math.pow(projectile.getCoord().getY() - mListeToutou.get(temp2).getCoord().getY(),2)) < 0.2){
                    mListeToutou.get(temp2).setmVie(mListeToutou.get(temp2).getmVie()-projectile.getmPuissance());
                    if(mListeToutou.get(temp2).getmVie()<=0){
                        this.listeToutouDelete.add(temp2);
                        if(mListeToutou.isEmpty()){
                            try{
                                updateLevel();
                            }catch(IOException e){

                            }
                        }
                    }
                    world.removeModel(projectile.getmID());
                    this.listeBalleDelete.add(temp);
                }
                temp2++;
            }
            for(Integer positionsToutousDelete: this.listeToutouDelete){
                this.mListeToutou.remove(positionsToutousDelete);
            }
            for(Integer positionBallesDelete: this.listeBalleDelete){
                this.mListeProjectile.remove(positionBallesDelete);
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
    }

    private void endGame() {
        for(Projectile projectile : mListeProjectile){
            mWorld.removeModel(projectile.getmID());
        }
        mListeProjectile.clear();
        for(Toutou toutou: mListeToutou){
            mWorld.removeModel(toutou.getmID());
        }
        mListeToutou.clear();
    }

    public void makeMrBalle() throws IOException {
        MovingModel bullet = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
        bullet.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
        mWorld.addModel(bullet);
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
        //Création de toutous mauves
        for(i=0; i<nbrMonstreMauve69;i++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mWorld.addModel(ami);
            Vec3 vec3 = new Vec3();
            do{
                vec3 = getCoordonnesMonstre();
                for(StaticModel plancher:mListePlancher){
                    mWorld.Translate(this.toutouTest.getmID(), vec3);
                    if((plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).x()
                            && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).x()
                            && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).y()
                            && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                            && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                            && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                            && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                            && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                            && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                            && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                            && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                            && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                            && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ){
                        this.positionPositiveDuSpawn = true;
                    }else{
                        mWorld.Translate(this.toutouTest.getmID(), new Vec3(-vec3.x(), -vec3.y(),0f));
                    }
                }
            }while(!this.positionPositiveDuSpawn);
            this.positionPositiveDuSpawn = false;
            mWorld.Translate(ami, vec3);
            mListeToutou.add(new Toutou(25*27+round*4, 34, new Coord(vec3.x(),vec3.y()), ami.getID()));
            mListeToutou.get(i).setMovingModel(getContext(), "toutou4");
            this.mListeToutou.get(i).getModel().setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(World world, Model object) {
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
        //Création de toutous verts
        for(j=0; j<nbrMonstreVert17;j++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "cube.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mWorld.addModel(ami);
            Vec3 vec3 = new Vec3();
            do{
                vec3 = getCoordonnesMonstre();
                for(StaticModel plancher:mListePlancher){
                    mWorld.Translate(this.toutouTest.getmID(), vec3);
                    if((plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).x()
                            && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).x()
                            && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).y()
                            && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                                    && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                                    && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                                    && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ){
                        this.positionPositiveDuSpawn = true;
                    }else{
                        mWorld.Translate(this.toutouTest.getmID(), new Vec3(-vec3.x(), -vec3.y(),0f));
                    }
                }
            }while(!this.positionPositiveDuSpawn);
            this.positionPositiveDuSpawn = false;
            mWorld.Translate(ami, vec3);
            mListeToutou.add(new Toutou(25*9+round*3, 25, new Coord(vec3.x(),vec3.y()), ami.getID()));
            mListeToutou.get(j+i).setMovingModel(getContext(), "toutou3");
            this.mListeToutou.get(j+i).getModel().setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(World world, Model object) {
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
        //Céation de toutous bleu
        for(h=0; h<nbrMonstreBleu4;h++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "toutou2.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mWorld.addModel(ami);
            Vec3 vec3 = new Vec3();
            do{
                vec3 = getCoordonnesMonstre();
                for(StaticModel plancher:mListePlancher){
                    mWorld.Translate(this.toutouTest.getmID(), vec3);
                    if((plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).x()
                            && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).x()
                            && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).y()
                            && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                                    && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                                    && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                                    && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ){
                        this.positionPositiveDuSpawn = true;
                    }else{
                        mWorld.Translate(this.toutouTest.getmID(), new Vec3(-vec3.x(), -vec3.y(),0f));
                    }
                }
            }while(!this.positionPositiveDuSpawn);
            this.positionPositiveDuSpawn = false;
            mWorld.Translate(ami, vec3);
            mListeToutou.add(new Toutou(25*3+round*2, 20, new Coord(vec3.x(), vec3.y()), ami.getID()));
            mListeToutou.get(h+i).setMovingModel(getContext(), "toutou2");
            this.mListeToutou.get(h+i).getModel().setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(World world, Model object) {
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
        //Création des Clémentines
        for(k=0; k<nbrMonstreJaune1;k++){
            MovingModel ami = ObjParser.Parse(mContext, "models", "toutou1.obj").get(0).toMovingModel();
            ami.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            mWorld.addModel(ami);
            Vec3 vec3 = new Vec3();
            do{
                vec3 = getCoordonnesMonstre();
                for(StaticModel plancher:mListePlancher){
                    mWorld.Translate(this.toutouTest.getmID(), vec3);
                    if((plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).x()
                            && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).x()
                            && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).y()
                            && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(1).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                                    && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                                    && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ||
                            (plancher.getCorners().get(1).x() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).x() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).x()
                                    && plancher.getCorners().get(0).y() < mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y()
                                    && plancher.getCorners().get(2).y() > mWorld.getModel(this.toutouTest.getmID(), World.State.HIDDEN).getCorners().get(0).y())
                            ){
                        this.positionPositiveDuSpawn = true;
                    }else{
                        mWorld.Translate(this.toutouTest.getmID(), new Vec3(-vec3.x(), -vec3.y(),0f));
                    }
                }
            }while(!this.positionPositiveDuSpawn);
            this.positionPositiveDuSpawn = false;
            mWorld.Translate(ami, vec3);
            mListeToutou.add(new Toutou(25+round, 17, new Coord(vec3.x(),vec3.y()), ami.getID()));
            mListeToutou.get(k+i).setMovingModel(getContext(), "toutou1");
            this.mListeToutou.get(k+i).getModel().setOnCollisionListener(new MovingModel.onCollisionListener() {
                @Override
                public void onCollision(World world, Model object) {
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
*/
    }

    private Vec3 getCoordonnesMonstre() {
        try {
            StaticModel mur = ObjParser.Parse(mContext, "models", "plancher.obj").get(0).toStaticModel();
            mur.setPhysics(new PhysicsAttributes.MovingModelAttr(1000, 0, 0, 3));
            Vec3 vec3 = new Vec3((mur.getCorners().get(0).x()-mur.getCorners().get(1).x()), mur.getCorners().get(1).y()-mur.getCorners().get(2).y(), 0);
            Vec3 vec3retour = new Vec3((float)Math.random()*19 +1, (float)Math.random()*19 +1, 0);
            return vec3retour;
        }catch(IOException e){

        }
        return new Vec3(0,0,0);
    }

    public void movePerso(Vec3 vector){
        mWorld.Move(perso.getID() ,persoVec,getElapsedFrameTime());
    }
}
