package daynight.daynnight;

import java.util.Date;

/**
 * Created by sebastien on 18-02-26.
 */

public class Badge
{
    //Variables
    String logo;
    String locationCapture;
    Date dateCapture;
    String type; //EX: Montagne, hopital, divertissement, etc...

    //Constructeurs
    Badge() {}
    Badge(String logo, String locationCapture, Date dateCapture, String type)
    {
        this.logo = logo;
        this.locationCapture = locationCapture;
        this.dateCapture = dateCapture;
        this.type = type;
    }

    //Getteurs & Setteurs
    public String getLogo()
    {
        return logo;
    }
    public String getLocationCapture()
    {
        return locationCapture;
    }
    public Date getDateCapture()
    {
        return dateCapture;
    }
    public String getType()
    {
        return type;
    }
}
