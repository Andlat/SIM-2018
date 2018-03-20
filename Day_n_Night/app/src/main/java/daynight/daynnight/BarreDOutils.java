package daynight.daynnight;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class BarreDOutils extends Fragment
{
    //Variables
    ImageViewCarre outils[] = new ImageViewCarre[4];

    //constructeur
    public BarreDOutils() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_barre_doutils, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        outils[0] = (ImageViewCarre) getView().findViewById(R.id.outil0);
        outils[1] = (ImageViewCarre) getView().findViewById(R.id.outil1);
        outils[2] = (ImageViewCarre) getView().findViewById(R.id.outil2);
        outils[3] = (ImageViewCarre) getView().findViewById(R.id.outil3);

        for(int i = 0 ; i < 4 ; i++)
            outils[i].setImageResource(getResources().getIdentifier("", "drawable", getContext().getPackageName()));
    }
}
