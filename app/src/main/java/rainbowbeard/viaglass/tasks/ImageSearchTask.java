package rainbowbeard.viaglass.tasks;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Task to perform reverse image search given the filename of an uploaded image.
 *
 * Gives Google's best guess 'queryParam' of subject of image.
 *
 * @author mchaney
 */
public class ImageSearchTask extends SearchTask {

    private static final String
            TAG = ImageSearchTask.class.getCanonicalName(),
            URL_HEAD = "https://images.google.com/searchbyimage?image_url=",
            URL_TAIL = "&image_content=&filename=&hl=en";

    public ImageSearchTask(Context context, String imageURL) {
        super(context, imageURL, URL_HEAD, URL_TAIL);
    }

    @Override
    public void parseResponse(final Document document) {
        // Google Image Search "best guess" for subject of image
        final Elements nameElement = document.select("._gUb");
        final String name = nameElement.text();

        // no intermediates, just kick off a new wiki search task
        Log.d(TAG, "ImageSearchTask Search results: " + name);
        new WikiSearchTask(context, name).start();
    }
}
