package daynight.daynnight;

import daynight.daynnight.engine.Model.MovingModel;
import daynight.daynnight.engine.util.Coord;

/**
 * Created by Galt Ouest Kingston on 2018-04-30.
 */

public class Projectile {
    private int mPuissance;
    private float mDirectionX;
    private float mDirectionY;
    private Coord coord;
    private MovingModel movingModel;
    private Long mID;

    public Projectile(){
        this.mPuissance = 10;
        this.mDirectionX = 1;
        this.mDirectionY = 0;
        this.coord = new Coord(0,0);
        this.movingModel = null;
        this.mID = null;
    }

    public Projectile(int mPuissance, float mDirectionX, float mDirectionY, Coord coord, MovingModel movingModel, Long mID) {
        this.mPuissance = mPuissance;
        this.mDirectionX = mDirectionX;
        this.mDirectionY = mDirectionY;
        this.coord = coord;
        this.movingModel = movingModel;
        this.mID = mID;
    }

    public int getmPuissance() {
        return mPuissance;
    }

    public void setmPuissance(int mPuissance) {
        this.mPuissance = mPuissance;
    }

    public float getmDirectionX() {
        return mDirectionX;
    }

    public void setmDirectionX(float mDirectionX) {
        this.mDirectionX = mDirectionX;
    }

    public float getmDirectionY() {
        return mDirectionY;
    }

    public void setmDirectionY(float mDirectionY) {
        this.mDirectionY = mDirectionY;
    }

    public Coord getCoord() {
        return coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public MovingModel getMovingModel() {
        return movingModel;
    }

    public void setMovingModel(MovingModel movingModel) {
        this.movingModel = movingModel;
    }

    public Long getmID() {
        return mID;
    }

    public void setmID(Long mID) {
        this.mID = mID;
    }
}
