package daynight.daynnight.engine.Model;

import android.support.annotation.Nullable;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andlat on 2018-02-19.
 */

public class Animation {
    private List<Pair<Texture, Integer>> mFrames = new ArrayList<>();//Time in milliseconds
    private long mTimeInterval;
    private int mCurrentFrameIndex = 0;

    public void addFrame(Pair<Texture, Integer> frame){
        mFrames.add(frame);
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
        if(mFrames.isEmpty()) return null;

        mTimeInterval += frameElapsedTime;//Add the game frame time to the time interval for the current animation frame
        if(mTimeInterval >= mFrames.get(mCurrentFrameIndex).second){//Check if the time interval has reached the time set for the animation frame

            //Go to next animation frame
            ++mCurrentFrameIndex;
            mTimeInterval = 0;
        }

        return mFrames.get(mCurrentFrameIndex).first;
    }
}
