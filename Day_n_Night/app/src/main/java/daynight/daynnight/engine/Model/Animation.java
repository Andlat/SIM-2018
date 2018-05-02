package daynight.daynnight.engine.Model;

import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andlat on 2018-02-19.
 */

public class Animation {
    private List<Pair<Texture, Integer>> mFrames = new ArrayList<>();//Time in milliseconds
    private long mTimeInterval=0;
    private int mCurrentFrameIndex = 0;

    /**
     * Ajouter une "frame". Une valeur <= 0 équivaut à une "frame" qui ne changera pas
     * @param frame La texture à utiliser pour la frame et son temps d'affichage
     */
    public Animation addFrame(Pair<Texture, Integer> frame){
        Log.e("MILLIS 2", ""+frame.second);
        mFrames.add(frame);
        return this;
    }

    public void RemoveFrame(int i){
        mFrames.remove(i);
    }
    public void RemoveFrame(Pair<Texture, Integer> frame){
        mFrames.remove(frame);
    }

    public List<Pair<Texture, Integer>> getFrames(){ return mFrames; }

    @Nullable
    public Texture getCurrentTexture(long frameElapsedTime){
        final int size = mFrames.size();
        if(size == 0) return null;
        else {
            Pair<Texture, Integer> frame = mFrames.get(mCurrentFrameIndex);
            if (frame.second <= 0)//Stay on this animation
                return frame.first;
            else {
                mTimeInterval += frameElapsedTime;//Add the game frame time to the time interval for the current animation frame
                if (mTimeInterval >= mFrames.get(mCurrentFrameIndex).second) {//Check if the time interval has reached the time set for the animation frame

                    //Go to next animation frame
                    mCurrentFrameIndex = (mCurrentFrameIndex + 1) % size;
                    mTimeInterval = 0;
                }

                return mFrames.get(mCurrentFrameIndex).first;
            }
        }
    }

    void CloneTo(Animation clone){
        clone.getFrames().clear();
        for(Pair<Texture, Integer> frame : mFrames){
            clone.addFrame(new Pair<>(frame.first, frame.second));
        }
    }
}
