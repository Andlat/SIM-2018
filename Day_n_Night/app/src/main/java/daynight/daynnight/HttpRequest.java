package daynight.daynnight;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Antoine Mascolo on 2018-03-14.
 */

public class HttpRequest implements Callable<String> {
    HttpsURLConnection con;

    @Override
    public String call() throws Exception {
        String response;

        URL url = null;
        try {
            url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=45.404476,-71.888351&types=point_of_interest&radius=50000&key=AIzaSyCkJvT6IguUIXVbBAe8-0l2vO1RWbxW4Tk");

            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Get response
        InputStream in = null;

        try {
            in = new BufferedInputStream(con.getInputStream());
            response = ConvertStreamToString(in);
        } finally{
            if(in != null) in.close();
        }
        return response;
    }

    /* Function copied from StackOverflow */
    private static String ConvertStreamToString(InputStream is)
    {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}