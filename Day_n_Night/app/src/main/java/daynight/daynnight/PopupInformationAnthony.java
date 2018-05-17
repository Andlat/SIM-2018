package daynight.daynnight;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static daynight.daynnight.MainActivity.SurChangementActivity;

/**
 * Created by Galt Ouest Kingston on 2018-04-16.
 */

public class PopupInformationAnthony {
    private int score;
    private int bestScore;
    private RelativeLayout buttons;
    private Button boutonRecommencer;
    private Button boutonMenu;
    private TextView textViewScore;
    private TextView textViewBestScore;

    public PopupInformationAnthony() {
        this(0,0);
    }

    public PopupInformationAnthony(int score, int bestScore) {
        this.score = score;
        this.bestScore = bestScore;

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }


}
