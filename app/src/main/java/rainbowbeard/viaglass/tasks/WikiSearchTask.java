package rainbowbeard.viaglass.tasks;

import android.content.Context;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Performs a wikipedia search of the query param.
 *
 * @author mchaney
 */
public class WikiSearchTask extends SearchTask {
    public static final String WIKI_SEARCH_RESPONSE = "WIKI_SEARCH_RESPONSE";

    private static final String
            URL_HEAD = "https://en.wikipedia.org/w/api.php?action=opensearch&search=",
            URL_TAIL = "";

    public WikiSearchTask(final Context context, final String queryParam) {
        super(context, queryParam, URL_HEAD, URL_TAIL);
    }

    @Override
    public void parseResponse(Response response) {
        Document document = null;
        try {
            document = response.parse();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String json = document.select("body").text();

        // broadcast results
    
    }
}
