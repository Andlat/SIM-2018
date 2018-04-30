package daynight.daynnight;

import android.os.CountDownTimer;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.internal.zzp;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Antoine Mascolo on 2018-03-28.
 */

public class Poi extends Object {
    private String id;
    private String name;
    private JSONArray typesJson;
    private CountDownTimer timer;
    private long timeLeft;
    private boolean active;

    public Poi(String id, String name, JSONArray types) {
        this.id = id;
        this.name = name;
        this.typesJson = types;
        this.active = true;
        this.timeLeft = 0;
    }

    public String getPlaceID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getTypes() {

        ArrayList<String> typesString = new ArrayList<>();

        for(int k = 0; k<typesJson.length(); k++){

            try {
                typesString.add(typesJson.getString(k));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        typesString.trimToSize();
        return typesString;
    }

    public long getTimeLeft() {
        return timeLeft;
    }

    public void setTimer(long time) {
        this.timer = new CountDownTimer(time, 1000) {
            @Override
            public void onTick(long l) {
                //Stock le temps restant avant que le poi se réactive
                timeLeft = l/1000;
            }

            @Override
            public void onFinish() {
                active = true;
            }
        }.start();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
