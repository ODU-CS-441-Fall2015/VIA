package rainbowbeard.viaglass.tasks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

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
 */

/**
 * Task to perform reverse image search given the filename of an uploaded image.
 *
 * Gives Google's best guess 'name' of subject of image.
 *
 * @author mchaney
 */
public class ImageSearchTask extends SearchTask {
    public static final String IMG_SEARCH_RESPONSE = "IMAGE_SEARCH_RESPONSE";

    private static final String
            TAG = ImageSearchTask.class.getCanonicalName(),
            URL_HEAD = "https://images.google.com/searchbyimage?image_url=http%3A%2F%2Fwww.cs.odu.edu%2F%7Emchaney%2F",
            URL_TAIL = "&image_content=&filename=&hl=en";

    public ImageSearchTask(Context context, String imageFile) {
        super(context, imageFile);
    }

    @Override
    protected String encodeURL(final String filename) {
        return URL_HEAD + Uri.encode(filename) + URL_TAIL;
    }

    @Override
    public void parseResponse(final Response response) {
        Document document = null;
        try {
            document = response.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Google Image Search "best guess" for subject of image
        final Elements nameElement = document.select("._gUb");
        final String name = nameElement.text();

        // broadcast results to rest of app
        Log.d(TAG, "ImageSearchTask Search results: " + name);
        final Intent resultIntent = new Intent(IMG_SEARCH_RESPONSE);
        resultIntent.putExtra(IMG_SEARCH_RESPONSE, name);
        LocalBroadcastManager.getInstance(context).sendBroadcast(resultIntent);
    }
}
