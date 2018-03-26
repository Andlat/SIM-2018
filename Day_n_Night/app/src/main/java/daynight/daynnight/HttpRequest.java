package daynight.daynnight;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

import static java.lang.System.in;


/**
 * Created by Antoine Mascolo on 2018-03-14.
 */

public class HttpRequest implements Callable<String> {
    private HttpsURLConnection con;
    private URL url;

    public HttpRequest(URL url) {
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        String response;

        try {
            con = (HttpsURLConnection) url.openConnection();
            con.setConnectTimeout(2000);
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.connect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //Redonne la réponse

        try {
            InputStream in = new BufferedInputStream(con.getInputStream());
            response = ConvertStreamToString(in);
        } finally {
            if (in != null) in.close();
            con.disconnect();
        }
        return response;
    }

    /* Function copied from StackOverflow */
    private static String ConvertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
