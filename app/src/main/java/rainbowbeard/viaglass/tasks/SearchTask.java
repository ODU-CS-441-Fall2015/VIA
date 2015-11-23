package rainbowbeard.viaglass.tasks;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 * Abstract base class for a JSoup search task.
 *
 * @author mchaney
 */
public abstract class SearchTask extends Thread {
    private static final String
            TAG = SearchTask.class.getCanonicalName(),
            USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0";

    protected Context context;
    protected String queryParam;

    public SearchTask(final Context context, final String queryParam) {
        this.context = context;
        this.queryParam = queryParam;
    }

    @Override
    public void run() {
        Log.d(TAG, "running task");

        final String encodedURL = encodeURL(queryParam);
        Log.d(TAG, "encoded url: " + encodedURL);

        Response response = null;
        try {
            response = Jsoup.connect(encodedURL)
                    .userAgent(USER_AGENT)
                    .followRedirects(true)
                    .referrer("http://www.google.com")
                    .ignoreContentType(true)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(context, "bad query param", Toast.LENGTH_SHORT);
            toast.show();
        }
        if(null == response || response.statusCode() != 200) {
            Log.e(TAG, "error in response" + (null != response ? ", status code: " + response.statusCode() : ""));
            return;
        }
        parseResponse(response);
    }

    public abstract void parseResponse(Response response);

    protected abstract String encodeURL(final String queryParam);
}
