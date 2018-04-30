package daynight.daynnight;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import daynight.daynnight.engine.Model.Model;
import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.Model.ObjParser;
import daynight.daynnight.engine.Model.Texture;
import daynight.daynnight.engine.physics.PhysicsAttributes;
import daynight.daynnight.engine.util.Coord;

/**
 * Created by Galt Ouest Kingston on 2018-04-30.
 */

public class Toutou {
    private int mVie;
    private int mDMG;
    private Coord coord;
    private Long mID;
    private MovingModel movingModel;

    public Toutou(){
        this.mVie=25;
        this.mDMG=10;
        this.mID=null;
        this.coord = new Coord();
        this.movingModel = null;
    }

    public Toutou(int mVie, int mDMG, Coord coord, Long mID) {
        this.mVie = mVie;
        this.mDMG = mDMG;
        this.coord = coord;
        this.mID = mID;
    }

    public int getmVie() {
        return mVie;
    }

    public void setmVie(int mVie) {
        this.mVie = mVie;
    }

    public int getmDMG() {
        return mDMG;
    }

    public void setmDMG(int mDMG) {
        this.mDMG = mDMG;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public Long getmID() {
        return mID;
    }

    public void setmID(Long mID) {
        this.mID = mID;
    }

    public void DMGrecu(int DMGrecu){
        this.setmVie(this.getmVie()- DMGrecu);
    }

    public void objetBouge(float x, float y){
        this.coord.setX(this.coord.getX() - x);
        this.coord.setY(this.coord.getY() - y);
    }

    public MovingModel getModel(){ return movingModel; }

    public void setSkin(int skinResID, Context context) throws IOException{
        movingModel.setTextureSource(context.getResources().getResourceEntryName(skinResID));
        movingModel.setTexture(Texture.Load(context, skinResID));
    }
}
