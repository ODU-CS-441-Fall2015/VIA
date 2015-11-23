package rainbowbeard.viaglass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 Outgoing data: (class)
     Receive image name
     Reverse image search Google API
     Retrieve search results
     Send search results => Search data
 Search data: (class)
     Receive search results
     Filter results for whitelist domains
     If no
     Send no data available
     Send available information to developer
     If yes
     Send filtered addresses => Site parser
 Site parser: (class)
     Receive filtered address
     Extract name
     Send name => Wiki retriever

     @author mchaney
 */
public class ImageSearchTask extends Thread {
    private static final String TAG = DataRetrieval.class.getCanonicalName();
    private static final String
            USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0",
            URI_HEAD = "https://images.google.com/searchbyimage?image_url=http%3A%2F%2Fwww.cs.odu.edu%2F%7Emchaney%2F",
            URI_TAIL = "&image_content=&filename=&hl=en",
            TEST_URL = "https://www.google.com/searchbyimage?&image_url=http://www.cs.odu.edu/~mchaney/uploads/upload-1.jpg";
    private Context context;
    private String imageFile;

    public ImageSearchTask(Context context, String imageFile) {
        this.context = context;
        this.imageFile = imageFile;
    }

    @Override
    public void run() {
        Log.d(TAG, "running task");
        // search for image
        final String encodedURL = encodeURL(imageFile);
        Log.d(TAG, "encoded url: " + encodedURL);
        String result = null;
        try {
            Connection.Response response = Jsoup.connect(encodedURL)
                    .userAgent(USER_AGENT)
                    .followRedirects(true)
                    .referrer("http://www.google.com")
                    .ignoreContentType(true)
                    .execute();
            Document document = response.parse();
            Elements elements = document.select("._gUb");
            result = elements.text();
        } catch (Exception e) {
            Log.e(TAG, (e.getMessage()==null) ? "message null???" : e.getMessage());
            e.printStackTrace();
        }
        if(null == result)
            result = "bad image/url";

        // send results
        Log.d(TAG, "Image Search results: " + result);
        final Intent resultIntent = new Intent(ImageSearchActivity.SEARCH_RESPONSE_EVENT);
        resultIntent.putExtra(ImageSearchActivity.SEARCH_RESPONSE_EVENT, result);
        LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent);
    }

    private static String encodeURL(final String filename) {
        return URI_HEAD + Uri.encode(filename) + URI_TAIL;
    }
}
