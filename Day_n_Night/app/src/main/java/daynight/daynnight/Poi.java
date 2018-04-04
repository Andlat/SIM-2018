package daynight.daynnight;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.internal.zzp;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Antoine Mascolo on 2018-03-28.
 */

public class Poi extends Object{
    private String id;
    private String name;
    private JSONArray typesJson;

    public Poi(String id, String name, JSONArray types) {
        this.id = id;
        this.name = name;
        this.typesJson = types;
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
}
