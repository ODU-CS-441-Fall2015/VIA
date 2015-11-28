package rainbowbeard.viaglass.tasks;

import android.content.Context;
import android.net.Uri;
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

    private final String URL_HEAD, URL_TAIL;

    /**
     * Constructor for a search task. The encodeURL method will wrap the encoded query param with
     * the urlHead and urlTail parameters.
     * @param context the calling context
     * @param queryParam the query parameter
     * @param urlHead the first portion of the encoded query url
     * @param urlTail the last portion of the encoded query url
     */
    public SearchTask(final Context context, final String queryParam, final String urlHead, final String urlTail) {
        this.context = context;
        this.queryParam = queryParam;
        this.URL_HEAD = urlHead;
        this.URL_TAIL = urlTail;
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

    private String encodeURL(final String queryParam) {
        return URL_HEAD + Uri.encode(queryParam) + URL_TAIL;
    }

    /**
     * Abstract method for parsing the response for this search task
     *
     * @param response
     */
    protected abstract void parseResponse(final Response response);

}
